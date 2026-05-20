package com.gasparian.rob.feature.experience.data.repository

import app.cash.turbine.test
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.experience.data.local.RcvExperienceDao
import com.gasparian.rob.feature.experience.data.local.RcvExperienceEntityGraph
import com.gasparian.rob.feature.experience.data.local.RcvExperienceHighlightEntity
import com.gasparian.rob.feature.experience.data.local.RcvExperienceRoleEntity
import com.gasparian.rob.feature.experience.data.mapper.toEntityGraph
import com.gasparian.rob.feature.experience.data.remote.RcvExperienceRemoteDataSource
import com.gasparian.rob.feature.experience.data.remote.RcvExperienceResponseDto
import com.gasparian.rob.feature.experience.data.remote.RcvExperienceRoleDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NetworkBackedRcvExperienceRepositoryTest {
    @Test
    fun `experience syncs remote data into dao before emitting`() = runTest {
        val dao = FakeRcvExperienceDao()
        val remoteDataSource = mockk<RcvExperienceRemoteDataSource>()
        coEvery { remoteDataSource.getExperience() } returns RcvNetworkResult.Success(experienceResponse)
        val repository = NetworkBackedRcvExperienceRepository(remoteDataSource = remoteDataSource, experienceDao = dao)

        repository.experience.test {
            val item = awaitItem()

            assertEquals("Priceline", item.getOrThrow().roles.single().company)
            assertEquals("priceline-android-engineer-2025-11-01", dao.lastReplacedGraph?.roles?.single()?.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `experience emits cached data when remote sync fails`() = runTest {
        val dao = FakeRcvExperienceDao(initialGraph = experienceResponse.toEntityGraph())
        val remoteDataSource = mockk<RcvExperienceRemoteDataSource>()
        coEvery { remoteDataSource.getExperience() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvExperienceRepository(remoteDataSource = remoteDataSource, experienceDao = dao)

        repository.experience.test {
            val item = awaitItem()

            assertEquals("Priceline", item.getOrThrow().roles.single().company)
            assertEquals(null, dao.lastReplacedGraph)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `experience emits failure when remote sync fails and cache is empty`() = runTest {
        val dao = FakeRcvExperienceDao()
        val remoteDataSource = mockk<RcvExperienceRemoteDataSource>()
        coEvery { remoteDataSource.getExperience() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvExperienceRepository(remoteDataSource = remoteDataSource, experienceDao = dao)

        repository.experience.test {
            val item = awaitItem()

            assertEquals(true, item.isFailure)
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private class FakeRcvExperienceDao(
    initialGraph: RcvExperienceEntityGraph = RcvExperienceEntityGraph(emptyList(), emptyList()),
) : RcvExperienceDao {
    private val graphFlow = MutableStateFlow(initialGraph)
    var lastReplacedGraph: RcvExperienceEntityGraph? = null

    override fun experienceGraphFlow(): Flow<RcvExperienceEntityGraph> = graphFlow

    override suspend fun replaceExperience(graph: RcvExperienceEntityGraph) {
        lastReplacedGraph = graph
        graphFlow.value = graph
    }

    override suspend fun getExperienceGraph(): RcvExperienceEntityGraph = graphFlow.value
    override suspend fun getRoles(): List<RcvExperienceRoleEntity> = error("Unused")
    override fun rolesFlow(): Flow<List<RcvExperienceRoleEntity>> = error("Unused")
    override suspend fun getHighlights(): List<RcvExperienceHighlightEntity> = error("Unused")
    override fun highlightsFlow(): Flow<List<RcvExperienceHighlightEntity>> = error("Unused")
    override suspend fun upsertRoles(roles: List<RcvExperienceRoleEntity>) = error("Unused")
    override suspend fun upsertHighlights(highlights: List<RcvExperienceHighlightEntity>) = error("Unused")
    override suspend fun clearRoles() = error("Unused")
    override suspend fun clearHighlights() = error("Unused")
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
            highlights = listOf("Built KMP foundation"),
        ),
    ),
)
