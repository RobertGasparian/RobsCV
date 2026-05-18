package com.gasparian.rob.feature.experience.data.repository

import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.experience.data.local.RcvExperienceDao
import com.gasparian.rob.feature.experience.data.mapper.toDomain
import com.gasparian.rob.feature.experience.data.mapper.toEntityGraph
import com.gasparian.rob.feature.experience.data.remote.RcvExperienceRemoteDataSource
import com.gasparian.rob.feature.experience.domain.model.RcvExperience
import com.gasparian.rob.feature.experience.domain.repository.RcvExperienceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class NetworkBackedRcvExperienceRepository(
    private val remoteDataSource: RcvExperienceRemoteDataSource,
    private val experienceDao: RcvExperienceDao,
) : RcvExperienceRepository {
    override val experience: Flow<Result<RcvExperience>> = experienceDao
        .experienceGraphFlow()
        .map { graph ->
            graph
                .takeUnless { it.isEmpty() }
                ?.toDomain()
                ?.let(Result.Companion::success)
                ?: Result.failure(IllegalStateException("Experience cache is empty"))
        }
        .onStart {
            syncExperienceFromRemote()
        }

    private suspend fun syncExperienceFromRemote() {
        when (val remoteResult = remoteDataSource.getExperience()) {
            is RcvNetworkResult.Failure -> Unit
            is RcvNetworkResult.Success -> experienceDao.replaceExperience(remoteResult.data.toEntityGraph())
        }
    }
}
