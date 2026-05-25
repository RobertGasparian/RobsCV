package com.gasparian.rob.feature.profile.data.repository

import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.profile.data.local.ProfileDao
import com.gasparian.rob.feature.profile.data.mapper.toDomain
import com.gasparian.rob.feature.profile.data.mapper.toEntityGraph
import com.gasparian.rob.feature.profile.data.remote.ProfileRemoteDataSource
import com.gasparian.rob.feature.profile.domain.model.Profile
import com.gasparian.rob.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NetworkBackedRcvProfileRepository(
    private val remoteDataSource: ProfileRemoteDataSource,
    private val profileDao: ProfileDao,
    private val currentTimeMillis: () -> Long = { System.currentTimeMillis() },
) : ProfileRepository {
    override val profile: Flow<Result<Profile>> = profileDao
        .profileGraphFlow()
        .map { graph ->
            graph
                ?.toDomain()
                ?.let(Result.Companion::success)
                ?: Result.failure(IllegalStateException("Profile cache is empty"))
        }

    override suspend fun clearCache() {
        profileDao.clearProfileCache()
    }

    override suspend fun sync() {
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
