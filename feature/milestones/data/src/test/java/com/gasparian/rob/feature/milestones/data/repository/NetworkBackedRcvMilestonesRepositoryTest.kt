package com.gasparian.rob.feature.milestones.data.repository

import app.cash.turbine.test
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.milestones.data.local.RcvCurrentFocusEntity
import com.gasparian.rob.feature.milestones.data.local.RcvCurrentFocusTopicEntity
import com.gasparian.rob.feature.milestones.data.local.RcvMilestoneEntity
import com.gasparian.rob.feature.milestones.data.local.RcvMilestoneTopicEntity
import com.gasparian.rob.feature.milestones.data.local.RcvMilestonesDao
import com.gasparian.rob.feature.milestones.data.local.RcvMilestonesEntityGraph
import com.gasparian.rob.feature.milestones.data.mapper.toEntityGraph
import com.gasparian.rob.feature.milestones.data.remote.RcvCurrentFocusDto
import com.gasparian.rob.feature.milestones.data.remote.RcvMilestoneDto
import com.gasparian.rob.feature.milestones.data.remote.RcvMilestonesRemoteDataSource
import com.gasparian.rob.feature.milestones.data.remote.RcvMilestonesResponseDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NetworkBackedRcvMilestonesRepositoryTest {
    @Test
    fun `milestones syncs remote data into dao before emitting`() = runTest {
        val dao = FakeRcvMilestonesDao()
        val remoteDataSource = mockk<RcvMilestonesRemoteDataSource>()
        coEvery { remoteDataSource.getMilestones() } returns RcvNetworkResult.Success(milestonesResponse)
        val repository = NetworkBackedRcvMilestonesRepository(
            remoteDataSource = remoteDataSource,
            milestonesDao = dao,
            currentTimeMillis = { 456L },
        )

        repository.milestones.test {
            val item = awaitItem()

            assertEquals("KMP migration", item.getOrThrow().currentFocus.summary)
            assertEquals(456L, dao.lastReplacedGraph?.currentFocus?.updatedAtMillis)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `milestones emits cached data when remote sync fails`() = runTest {
        val dao = FakeRcvMilestonesDao(initialGraph = milestonesResponse.toEntityGraph(updatedAtMillis = 111L))
        val remoteDataSource = mockk<RcvMilestonesRemoteDataSource>()
        coEvery { remoteDataSource.getMilestones() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvMilestonesRepository(remoteDataSource = remoteDataSource, milestonesDao = dao)

        repository.milestones.test {
            val item = awaitItem()

            assertEquals("KMP migration", item.getOrThrow().currentFocus.summary)
            assertEquals(null, dao.lastReplacedGraph)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `milestones emits failure when remote sync fails and cache is empty`() = runTest {
        val dao = FakeRcvMilestonesDao()
        val remoteDataSource = mockk<RcvMilestonesRemoteDataSource>()
        coEvery { remoteDataSource.getMilestones() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvMilestonesRepository(remoteDataSource = remoteDataSource, milestonesDao = dao)

        repository.milestones.test {
            val item = awaitItem()

            assertEquals(true, item.isFailure)
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private class FakeRcvMilestonesDao(
    initialGraph: RcvMilestonesEntityGraph? = null,
) : RcvMilestonesDao {
    private val graphFlow = MutableStateFlow(initialGraph)
    var lastReplacedGraph: RcvMilestonesEntityGraph? = null

    override fun milestonesGraphFlow(): Flow<RcvMilestonesEntityGraph?> = graphFlow

    override suspend fun replaceMilestones(graph: RcvMilestonesEntityGraph) {
        lastReplacedGraph = graph
        graphFlow.value = graph
    }

    override suspend fun getMilestonesGraph(): RcvMilestonesEntityGraph? = graphFlow.value
    override suspend fun getCurrentFocus(id: String): RcvCurrentFocusEntity? = error("Unused")
    override fun currentFocusFlow(id: String): Flow<RcvCurrentFocusEntity?> = error("Unused")
    override suspend fun getCurrentFocusTopics(): List<RcvCurrentFocusTopicEntity> = error("Unused")
    override fun currentFocusTopicsFlow(): Flow<List<RcvCurrentFocusTopicEntity>> = error("Unused")
    override suspend fun getMilestones(): List<RcvMilestoneEntity> = error("Unused")
    override fun milestonesFlow(): Flow<List<RcvMilestoneEntity>> = error("Unused")
    override suspend fun getMilestoneTopics(): List<RcvMilestoneTopicEntity> = error("Unused")
    override fun milestoneTopicsFlow(): Flow<List<RcvMilestoneTopicEntity>> = error("Unused")
    override suspend fun upsertCurrentFocus(currentFocus: RcvCurrentFocusEntity) = error("Unused")
    override suspend fun upsertCurrentFocusTopics(topics: List<RcvCurrentFocusTopicEntity>) = error("Unused")
    override suspend fun upsertMilestones(milestones: List<RcvMilestoneEntity>) = error("Unused")
    override suspend fun upsertMilestoneTopics(topics: List<RcvMilestoneTopicEntity>) = error("Unused")
    override suspend fun clearCurrentFocus() = error("Unused")
    override suspend fun clearCurrentFocusTopics() = error("Unused")
    override suspend fun clearMilestones() = error("Unused")
    override suspend fun clearMilestoneTopics() = error("Unused")
}

private val milestonesResponse = RcvMilestonesResponseDto(
    currentFocus = RcvCurrentFocusDto(
        summary = "KMP migration",
        topics = listOf("Room"),
    ),
    recentMilestones = listOf(
        RcvMilestoneDto(
            id = "nav3",
            title = "Navigation skeleton",
            description = "Added Navigation 3 skeleton.",
            completedAt = "2026-05-18",
            topics = emptyList(),
        ),
    ),
)
