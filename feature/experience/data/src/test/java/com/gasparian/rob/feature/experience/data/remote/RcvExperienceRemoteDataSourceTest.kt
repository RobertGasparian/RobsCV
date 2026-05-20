package com.gasparian.rob.feature.experience.data.remote

import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResponse
import com.gasparian.rob.core.network.RcvNetworkResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RcvExperienceRemoteDataSourceTest {
    @Test
    fun `getExperience requests experience endpoint`() = runTest {
        val networkClient = CapturingNetworkClient(RcvNetworkResult.Success(experienceResponse))
        val dataSource = RcvExperienceRemoteDataSource(networkClient)

        val result = dataSource.getExperience()

        assertEquals("experience", networkClient.capturedPath)
        assertEquals(RcvNetworkResult.Success(experienceResponse), result)
    }

    @Test
    fun `getExperience returns network failure from client`() = runTest {
        val failure = RcvNetworkResult.Failure(RcvNetworkError.Serialization)
        val networkClient = CapturingNetworkClient(failure)
        val dataSource = RcvExperienceRemoteDataSource(networkClient)

        val result = dataSource.getExperience()

        assertEquals("experience", networkClient.capturedPath)
        assertEquals(failure, result)
    }
}

private class CapturingNetworkClient(
    private val result: RcvNetworkResult<RcvExperienceResponseDto>,
) : RcvNetworkClient {
    var capturedPath: String? = null

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> get(
        path: String,
        responseMapper: suspend RcvNetworkResponse.() -> T,
    ): RcvNetworkResult<T> {
        capturedPath = path
        return result as RcvNetworkResult<T>
    }
}

private val experienceResponse = RcvExperienceResponseDto(
    roles = listOf(
        RcvExperienceRoleDto(
            title = "Android Engineer",
            company = "Priceline",
            startDate = "2025-11-01",
            location = "Toronto, Canada",
            workArrangement = "hybrid",
            summary = "Travel technology platform.",
            highlights = emptyList(),
        ),
    ),
)
