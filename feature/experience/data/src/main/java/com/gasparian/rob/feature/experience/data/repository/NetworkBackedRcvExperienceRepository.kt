package com.gasparian.rob.feature.experience.data.repository

import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.experience.data.local.ExperienceDao
import com.gasparian.rob.feature.experience.data.mapper.toDomain
import com.gasparian.rob.feature.experience.data.mapper.toEntityGraph
import com.gasparian.rob.feature.experience.data.remote.ExperienceRemoteDataSource
import com.gasparian.rob.feature.experience.domain.model.Experience
import com.gasparian.rob.feature.experience.domain.repository.ExperienceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NetworkBackedRcvExperienceRepository(
    private val remoteDataSource: ExperienceRemoteDataSource,
    private val experienceDao: ExperienceDao,
) : ExperienceRepository {
    override val experience: Flow<Result<Experience>> = experienceDao
        .experienceGraphFlow()
        .map { graph ->
            graph
                .takeUnless { it.isEmpty() }
                ?.toDomain()
                ?.let(Result.Companion::success)
                ?: Result.failure(IllegalStateException("Experience cache is empty"))
        }

    override suspend fun clearCache() {
        experienceDao.clearExperienceCache()
    }

    override suspend fun sync() {
        when (val remoteResult = remoteDataSource.getExperience()) {
            is RcvNetworkResult.Failure -> Unit
            is RcvNetworkResult.Success -> experienceDao.replaceExperience(remoteResult.data.toEntityGraph())
        }
    }
}
