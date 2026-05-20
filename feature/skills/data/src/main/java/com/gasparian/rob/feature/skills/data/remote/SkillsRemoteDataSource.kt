package com.gasparian.rob.feature.skills.data.remote

import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkResult

private const val RCV_SKILLS_ENDPOINT = "skills"

class SkillsRemoteDataSource(
    private val networkClient: RcvNetworkClient,
) {
    suspend fun getSkills(): RcvNetworkResult<SkillsResponseDto> = networkClient.get(RCV_SKILLS_ENDPOINT) { body() }
}
