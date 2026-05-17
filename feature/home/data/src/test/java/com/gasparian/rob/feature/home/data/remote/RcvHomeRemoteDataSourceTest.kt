package com.gasparian.rob.feature.home.data.remote

import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkResponse
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeProfileResponseDto
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

class RcvHomeRemoteDataSourceTest {
    @Test
    fun `getProfile delegates to module-owned profile endpoint`() = runTest {
        val dataSource =
            RcvHomeRemoteDataSource(
                networkClient =
                FakeRcvNetworkClient(
                    expectedPath = RcvHomeEndpoints.PROFILE,
                    result = RcvNetworkResult.Success(profileDto),
                ),
            )

        val result = dataSource.getProfile()

        when (result) {
            is RcvNetworkResult.Success -> assertEquals("Robert Gasparyan", result.data.displayName)
            is RcvNetworkResult.Failure -> fail("Expected success but got ${result.error}")
        }
    }
}

private class FakeRcvNetworkClient<T>(
    private val expectedPath: String,
    private val result: RcvNetworkResult<T>,
) : RcvNetworkClient {
    override suspend fun <T> get(
        path: String,
        responseMapper: suspend RcvNetworkResponse.() -> T,
    ): RcvNetworkResult<T> {
        assertEquals(expectedPath, path)
        @Suppress("UNCHECKED_CAST")
        return result as RcvNetworkResult<T>
    }
}

private val profileDto =
    RcvHomeProfileResponseDto(
        id = "robert-gasparyan",
        displayName = "Robert Gasparyan",
        headline = "Android Engineer",
        shortBio = "Senior Android Engineer.",
        location =
        com.gasparian.rob.feature.home.data.remote.dto.RcvHomeLocationDto(
            city = "Toronto",
            region = "ON",
            country = "Canada",
        ),
        contact =
        com.gasparian.rob.feature.home.data.remote.dto.RcvHomeContactDto(
            email = "rob.gasparian@gmail.com",
            phone = "+1 437-551-9859",
            linkedin = "https://linkedin.com/in/rob-gasparian/",
        ),
        professionalProfile = "Profile.",
        summaryOfQualifications = emptyList(),
    )
