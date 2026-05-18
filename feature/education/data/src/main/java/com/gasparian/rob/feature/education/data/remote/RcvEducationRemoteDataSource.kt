package com.gasparian.rob.feature.education.data.remote

import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkResult

private const val RCV_EDUCATION_ENDPOINT = "education"

class RcvEducationRemoteDataSource(
    private val networkClient: RcvNetworkClient,
) {
    suspend fun getEducation(): RcvNetworkResult<RcvEducationResponseDto> = networkClient.get(RCV_EDUCATION_ENDPOINT) { body() }
}
