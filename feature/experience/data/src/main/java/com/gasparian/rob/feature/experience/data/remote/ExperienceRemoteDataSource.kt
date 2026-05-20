package com.gasparian.rob.feature.experience.data.remote

import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkResult

private const val RCV_EXPERIENCE_ENDPOINT = "experience"

class ExperienceRemoteDataSource(
    private val networkClient: RcvNetworkClient,
) {
    suspend fun getExperience(): RcvNetworkResult<ExperienceResponseDto> = networkClient.get(RCV_EXPERIENCE_ENDPOINT) { body() }
}
