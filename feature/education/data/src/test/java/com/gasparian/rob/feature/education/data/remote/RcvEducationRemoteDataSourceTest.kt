package com.gasparian.rob.feature.education.data.remote

import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResponse
import com.gasparian.rob.core.network.RcvNetworkResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RcvEducationRemoteDataSourceTest {
    @Test
    fun `getEducation requests education endpoint`() = runTest {
        val networkClient = CapturingNetworkClient(RcvNetworkResult.Success(educationResponse))
        val dataSource = RcvEducationRemoteDataSource(networkClient)

        val result = dataSource.getEducation()

        assertEquals("education", networkClient.capturedPath)
        assertEquals(RcvNetworkResult.Success(educationResponse), result)
    }

    @Test
    fun `getEducation returns network failure from client`() = runTest {
        val failure = RcvNetworkResult.Failure(RcvNetworkError.Http(code = 500, message = "Server error"))
        val networkClient = CapturingNetworkClient(failure)
        val dataSource = RcvEducationRemoteDataSource(networkClient)

        val result = dataSource.getEducation()

        assertEquals("education", networkClient.capturedPath)
        assertEquals(failure, result)
    }
}

private class CapturingNetworkClient(
    private val result: RcvNetworkResult<RcvEducationResponseDto>,
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

private val educationResponse = RcvEducationResponseDto(
    institutions = emptyList(),
    items = emptyList(),
)
