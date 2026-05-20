package com.gasparian.rob.feature.milestones.data.remote

import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResponse
import com.gasparian.rob.core.network.RcvNetworkResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MilestonesRemoteDataSourceTest {
    @Test
    fun `getMilestones requests milestones endpoint`() = runTest {
        val networkClient = CapturingNetworkClient(RcvNetworkResult.Success(milestonesResponse))
        val dataSource = MilestonesRemoteDataSource(networkClient)

        val result = dataSource.getMilestones()

        assertEquals("milestones", networkClient.capturedPath)
        assertEquals(RcvNetworkResult.Success(milestonesResponse), result)
    }

    @Test
    fun `getMilestones returns network failure from client`() = runTest {
        val failure = RcvNetworkResult.Failure(RcvNetworkError.Unknown("Nope"))
        val networkClient = CapturingNetworkClient(failure)
        val dataSource = MilestonesRemoteDataSource(networkClient)

        val result = dataSource.getMilestones()

        assertEquals("milestones", networkClient.capturedPath)
        assertEquals(failure, result)
    }
}

private class CapturingNetworkClient(
    private val result: RcvNetworkResult<MilestonesResponseDto>,
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

private val milestonesResponse = MilestonesResponseDto(
    currentFocus = CurrentFocusDto(summary = "KMP migration", topics = emptyList()),
    recentMilestones = emptyList(),
)
