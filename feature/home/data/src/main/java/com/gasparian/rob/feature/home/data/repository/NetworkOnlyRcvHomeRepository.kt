package com.gasparian.rob.feature.home.data.repository

import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.home.data.mapper.toDomain
import com.gasparian.rob.feature.home.data.remote.RcvHomeRemoteDataSource
import com.gasparian.rob.feature.home.domain.model.RcvHomeData
import com.gasparian.rob.feature.home.domain.model.RcvHomeResult
import com.gasparian.rob.feature.home.domain.repository.RcvHomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkOnlyRcvHomeRepository(
    private val remoteDataSource: RcvHomeRemoteDataSource,
) : RcvHomeRepository {
    override fun observeHomeData(): Flow<RcvHomeResult<RcvHomeData>> = flow {
        emit(fetchHomeData())
    }

    private suspend fun fetchHomeData(): RcvHomeResult<RcvHomeData> {
        val profileResult = remoteDataSource.getProfile()
        if (profileResult is RcvNetworkResult.Failure) return profileResult.toDomain()

        val skillsResult = remoteDataSource.getSkills()
        if (skillsResult is RcvNetworkResult.Failure) return skillsResult.toDomain()

        val experienceResult = remoteDataSource.getExperience()
        if (experienceResult is RcvNetworkResult.Failure) return experienceResult.toDomain()

        val educationResult = remoteDataSource.getEducation()
        if (educationResult is RcvNetworkResult.Failure) return educationResult.toDomain()

        val milestonesResult = remoteDataSource.getMilestones()
        if (milestonesResult is RcvNetworkResult.Failure) return milestonesResult.toDomain()

        return RcvHomeResult.Success(
            RcvHomeData(
                profile = (profileResult as RcvNetworkResult.Success).data.toDomain(),
                skills = (skillsResult as RcvNetworkResult.Success).data.toDomain(),
                experience = (experienceResult as RcvNetworkResult.Success).data.toDomain(),
                education = (educationResult as RcvNetworkResult.Success).data.toDomain(),
                milestones = (milestonesResult as RcvNetworkResult.Success).data.toDomain(),
            ),
        )
    }
}
