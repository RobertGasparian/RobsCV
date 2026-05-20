package com.gasparian.rob.feature.profile.data.remote

import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResponse
import com.gasparian.rob.core.network.RcvNetworkResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ProfileRemoteDataSourceTest {
    @Test
    fun `getProfile requests profile endpoint`() = runTest {
        val networkClient = CapturingNetworkClient(RcvNetworkResult.Success(profileResponse))
        val dataSource = ProfileRemoteDataSource(networkClient)

        val result = dataSource.getProfile()

        assertEquals("profile", networkClient.capturedPath)
        assertEquals(RcvNetworkResult.Success(profileResponse), result)
    }

    @Test
    fun `getProfile returns network failure from client`() = runTest {
        val failure = RcvNetworkResult.Failure(RcvNetworkError.NetworkUnavailable)
        val networkClient = CapturingNetworkClient(failure)
        val dataSource = ProfileRemoteDataSource(networkClient)

        val result = dataSource.getProfile()

        assertEquals("profile", networkClient.capturedPath)
        assertEquals(failure, result)
    }
}

private class CapturingNetworkClient(
    private val result: RcvNetworkResult<ProfileResponseDto>,
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

private val profileResponse = ProfileResponseDto(
    id = "rob",
    displayName = "Robert Gasparyan",
    headline = "Android Engineer",
    shortBio = "Senior Android engineer.",
    location = ProfileLocationDto(city = "Toronto", region = "ON", country = "Canada"),
    contact = ProfileContactDto(
        email = "rob.gasparian@gmail.com",
        phone = "+1 437-551-9859",
        linkedin = "linkedin.com/in/rob-gasparian/",
    ),
    professionalProfile = "Professional profile",
    summaryOfQualifications = emptyList(),
)
