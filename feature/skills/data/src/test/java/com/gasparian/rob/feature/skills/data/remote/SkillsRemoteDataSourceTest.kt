package com.gasparian.rob.feature.skills.data.remote

import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResponse
import com.gasparian.rob.core.network.RcvNetworkResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SkillsRemoteDataSourceTest {
    @Test
    fun `getSkills requests skills endpoint`() = runTest {
        val networkClient = CapturingNetworkClient(RcvNetworkResult.Success(skillsResponse))
        val dataSource = SkillsRemoteDataSource(networkClient)

        val result = dataSource.getSkills()

        assertEquals("skills", networkClient.capturedPath)
        assertEquals(RcvNetworkResult.Success(skillsResponse), result)
    }

    @Test
    fun `getSkills returns network failure from client`() = runTest {
        val failure = RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val networkClient = CapturingNetworkClient(failure)
        val dataSource = SkillsRemoteDataSource(networkClient)

        val result = dataSource.getSkills()

        assertEquals("skills", networkClient.capturedPath)
        assertEquals(failure, result)
    }
}

private class CapturingNetworkClient(
    private val result: RcvNetworkResult<SkillsResponseDto>,
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

private val skillsResponse = SkillsResponseDto(
    categories = listOf(SkillCategoryDto(id = "android", name = "Android")),
    skills = emptyList(),
)
