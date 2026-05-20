package com.gasparian.rob.feature.milestones.data.remote

import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkResult

private const val RCV_MILESTONES_ENDPOINT = "milestones"

class MilestonesRemoteDataSource(
    private val networkClient: RcvNetworkClient,
) {
    suspend fun getMilestones(): RcvNetworkResult<MilestonesResponseDto> = networkClient.get(RCV_MILESTONES_ENDPOINT) { body() }
}
