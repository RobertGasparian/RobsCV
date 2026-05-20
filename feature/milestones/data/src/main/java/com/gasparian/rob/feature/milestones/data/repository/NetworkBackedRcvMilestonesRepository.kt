package com.gasparian.rob.feature.milestones.data.repository

import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.milestones.data.local.MilestonesDao
import com.gasparian.rob.feature.milestones.data.mapper.toDomain
import com.gasparian.rob.feature.milestones.data.mapper.toEntityGraph
import com.gasparian.rob.feature.milestones.data.remote.MilestonesRemoteDataSource
import com.gasparian.rob.feature.milestones.domain.model.Milestones
import com.gasparian.rob.feature.milestones.domain.repository.MilestonesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class NetworkBackedRcvMilestonesRepository(
    private val remoteDataSource: MilestonesRemoteDataSource,
    private val milestonesDao: MilestonesDao,
    private val currentTimeMillis: () -> Long = { System.currentTimeMillis() },
) : MilestonesRepository {
    override val milestones: Flow<Result<Milestones>> = milestonesDao
        .milestonesGraphFlow()
        .map { graph ->
            graph
                ?.toDomain()
                ?.let(Result.Companion::success)
                ?: Result.failure(IllegalStateException("Milestones cache is empty"))
        }
        .onStart {
            syncMilestonesFromRemote()
        }

    private suspend fun syncMilestonesFromRemote() {
        when (val remoteResult = remoteDataSource.getMilestones()) {
            is RcvNetworkResult.Failure -> Unit

            is RcvNetworkResult.Success ->
                milestonesDao.replaceMilestones(
                    remoteResult.data.toEntityGraph(
                        updatedAtMillis = currentTimeMillis(),
                    ),
                )
        }
    }
}
