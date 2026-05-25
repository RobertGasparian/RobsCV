package com.gasparian.rob.feature.skills.data.repository

import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.skills.data.local.SkillsDao
import com.gasparian.rob.feature.skills.data.mapper.toDomain
import com.gasparian.rob.feature.skills.data.mapper.toEntityGraph
import com.gasparian.rob.feature.skills.data.remote.SkillsRemoteDataSource
import com.gasparian.rob.feature.skills.domain.model.Skills
import com.gasparian.rob.feature.skills.domain.repository.SkillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NetworkBackedRcvSkillsRepository(
    private val remoteDataSource: SkillsRemoteDataSource,
    private val skillsDao: SkillsDao,
) : SkillsRepository {
    override val skills: Flow<Result<Skills>> = skillsDao
        .skillsGraphFlow()
        .map { graph ->
            graph
                .takeUnless { it.isEmpty() }
                ?.toDomain()
                ?.let(Result.Companion::success)
                ?: Result.failure(IllegalStateException("Skills cache is empty"))
        }

    override suspend fun clearCache() {
        skillsDao.clearSkillsCache()
    }

    override suspend fun sync() {
        when (val remoteResult = remoteDataSource.getSkills()) {
            is RcvNetworkResult.Failure -> Unit
            is RcvNetworkResult.Success -> skillsDao.replaceSkills(remoteResult.data.toEntityGraph())
        }
    }
}
