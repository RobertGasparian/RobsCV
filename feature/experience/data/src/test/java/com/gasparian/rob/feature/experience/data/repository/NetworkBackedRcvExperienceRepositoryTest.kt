package com.gasparian.rob.feature.experience.data.repository

import app.cash.turbine.test
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.experience.data.local.ExperienceDao
import com.gasparian.rob.feature.experience.data.local.ExperienceEntityGraph
import com.gasparian.rob.feature.experience.data.local.ExperienceHighlightEntity
import com.gasparian.rob.feature.experience.data.local.ExperienceRoleEntity
import com.gasparian.rob.feature.experience.data.mapper.toEntityGraph
import com.gasparian.rob.feature.experience.data.remote.ExperienceRemoteDataSource
import com.gasparian.rob.feature.experience.data.remote.ExperienceResponseDto
import com.gasparian.rob.feature.experience.data.remote.ExperienceRoleDto
import com.gasparian.rob.feature.experience.domain.model.WorkArrangement
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
        val remoteDataSource = mockk<ExperienceRemoteDataSource>()
        coEvery { remoteDataSource.getExperience() } returns RcvNetworkResult.Success(experienceResponse)
        val repository = NetworkBackedRcvExperienceRepository(remoteDataSource = remoteDataSource, experienceDao = dao)

        repository.experience.test {
            val item = awaitItem()

            assertEquals("Priceline", item.getOrThrow().roles.single().company)
            assertEquals(WorkArrangement.Hybrid, item.getOrThrow().roles.single().workArrangement)
            assertEquals("priceline-android-engineer-2025-11-01", dao.lastReplacedGraph?.roles?.single()?.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `experience emits cached data when remote sync fails`() = runTest {
        val dao = FakeRcvExperienceDao(initialGraph = experienceResponse.toEntityGraph())
        val remoteDataSource = mockk<ExperienceRemoteDataSource>()
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
        val remoteDataSource = mockk<ExperienceRemoteDataSource>()
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
    initialGraph: ExperienceEntityGraph = ExperienceEntityGraph(emptyList(), emptyList()),
) : ExperienceDao {
    private val graphFlow = MutableStateFlow(initialGraph)
    var lastReplacedGraph: ExperienceEntityGraph? = null

    override fun experienceGraphFlow(): Flow<ExperienceEntityGraph> = graphFlow

    override suspend fun replaceExperience(graph: ExperienceEntityGraph) {
        lastReplacedGraph = graph
        graphFlow.value = graph
    }

    override suspend fun getExperienceGraph(): ExperienceEntityGraph = graphFlow.value
    override suspend fun getRoles(): List<ExperienceRoleEntity> = error("Unused")
    override fun rolesFlow(): Flow<List<ExperienceRoleEntity>> = error("Unused")
    override suspend fun getHighlights(): List<ExperienceHighlightEntity> = error("Unused")
    override fun highlightsFlow(): Flow<List<ExperienceHighlightEntity>> = error("Unused")
    override suspend fun upsertRoles(roles: List<ExperienceRoleEntity>) = error("Unused")
    override suspend fun upsertHighlights(highlights: List<ExperienceHighlightEntity>) = error("Unused")
    override suspend fun clearRoles() = error("Unused")
    override suspend fun clearHighlights() = error("Unused")
}

private val experienceResponse = ExperienceResponseDto(
    roles = listOf(
        ExperienceRoleDto(
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
