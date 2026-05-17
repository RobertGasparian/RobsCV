package com.gasparian.rob.feature.home.data.remote

import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeEducationResponseDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeExperienceResponseDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeMilestonesResponseDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeProfileResponseDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeSkillsResponseDto

class RcvHomeRemoteDataSource(
    private val networkClient: RcvNetworkClient,
) {
    suspend fun getProfile(): RcvNetworkResult<RcvHomeProfileResponseDto> = networkClient.get(RcvHomeEndpoints.PROFILE) { body() }

    suspend fun getSkills(): RcvNetworkResult<RcvHomeSkillsResponseDto> = networkClient.get(RcvHomeEndpoints.SKILLS) { body() }

    suspend fun getExperience(): RcvNetworkResult<RcvHomeExperienceResponseDto> = networkClient.get(RcvHomeEndpoints.EXPERIENCE) { body() }

    suspend fun getEducation(): RcvNetworkResult<RcvHomeEducationResponseDto> = networkClient.get(RcvHomeEndpoints.EDUCATION) { body() }

    suspend fun getMilestones(): RcvNetworkResult<RcvHomeMilestonesResponseDto> = networkClient.get(RcvHomeEndpoints.MILESTONES) { body() }
}
