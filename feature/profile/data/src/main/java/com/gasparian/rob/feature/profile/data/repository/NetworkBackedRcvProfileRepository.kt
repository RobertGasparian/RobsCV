package com.gasparian.rob.feature.profile.data.repository

import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.profile.data.local.RcvProfileDao
import com.gasparian.rob.feature.profile.data.mapper.toDomain
import com.gasparian.rob.feature.profile.data.mapper.toEntityGraph
import com.gasparian.rob.feature.profile.data.remote.RcvProfileRemoteDataSource
import com.gasparian.rob.feature.profile.domain.model.RcvProfile
import com.gasparian.rob.feature.profile.domain.repository.RcvProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class NetworkBackedRcvProfileRepository(
    private val remoteDataSource: RcvProfileRemoteDataSource,
    private val profileDao: RcvProfileDao,
    private val currentTimeMillis: () -> Long = { System.currentTimeMillis() },
) : RcvProfileRepository {
    override val profile: Flow<Result<RcvProfile>> = profileDao
        .profileGraphFlow()
        .map { graph ->
            graph
                ?.toDomain()
                ?.let(Result.Companion::success)
                ?: Result.failure(IllegalStateException("Profile cache is empty"))
        }
        .onStart {
            syncProfileFromRemote()
        }

    private suspend fun syncProfileFromRemote() {
        when (val remoteResult = remoteDataSource.getProfile()) {
            is RcvNetworkResult.Failure -> Unit

            is RcvNetworkResult.Success ->
                profileDao.replaceProfile(
                    remoteResult.data.toEntityGraph(
                        updatedAtMillis = currentTimeMillis(),
                    ),
                )
        }
    }
}
