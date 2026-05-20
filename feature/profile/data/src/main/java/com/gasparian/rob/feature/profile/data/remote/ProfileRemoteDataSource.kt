package com.gasparian.rob.feature.profile.data.remote

import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkResult

private const val RCV_PROFILE_ENDPOINT = "profile"

class ProfileRemoteDataSource(
    private val networkClient: RcvNetworkClient,
) {
    suspend fun getProfile(): RcvNetworkResult<ProfileResponseDto> = networkClient.get(RCV_PROFILE_ENDPOINT) { body() }
}
