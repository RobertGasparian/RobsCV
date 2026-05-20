package com.gasparian.rob.feature.education.data.repository

import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.education.data.local.EducationDao
import com.gasparian.rob.feature.education.data.mapper.toDomain
import com.gasparian.rob.feature.education.data.mapper.toEntityGraph
import com.gasparian.rob.feature.education.data.remote.EducationRemoteDataSource
import com.gasparian.rob.feature.education.domain.model.Education
import com.gasparian.rob.feature.education.domain.repository.EducationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class NetworkBackedRcvEducationRepository(
    private val remoteDataSource: EducationRemoteDataSource,
    private val educationDao: EducationDao,
) : EducationRepository {
    override val education: Flow<Result<Education>> = educationDao
        .educationGraphFlow()
        .map { graph ->
            graph
                .takeUnless { it.isEmpty() }
                ?.toDomain()
                ?.let(Result.Companion::success)
                ?: Result.failure(IllegalStateException("Education cache is empty"))
        }
        .onStart {
            syncEducationFromRemote()
        }

    private suspend fun syncEducationFromRemote() {
        when (val remoteResult = remoteDataSource.getEducation()) {
            is RcvNetworkResult.Failure -> Unit
            is RcvNetworkResult.Success -> educationDao.replaceEducation(remoteResult.data.toEntityGraph())
        }
    }
}
