package com.gasparian.rob.feature.milestones.data.repository

import app.cash.turbine.test
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.milestones.data.local.CurrentFocusEntity
import com.gasparian.rob.feature.milestones.data.local.CurrentFocusTopicEntity
import com.gasparian.rob.feature.milestones.data.local.MilestoneEntity
import com.gasparian.rob.feature.milestones.data.local.MilestoneTopicEntity
import com.gasparian.rob.feature.milestones.data.local.MilestonesDao
import com.gasparian.rob.feature.milestones.data.local.MilestonesEntityGraph
import com.gasparian.rob.feature.milestones.data.mapper.toEntityGraph
import com.gasparian.rob.feature.milestones.data.remote.CurrentFocusDto
import com.gasparian.rob.feature.milestones.data.remote.MilestoneDto
import com.gasparian.rob.feature.milestones.data.remote.MilestonesRemoteDataSource
import com.gasparian.rob.feature.milestones.data.remote.MilestonesResponseDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NetworkBackedRcvMilestonesRepositoryTest {
    @Test
    fun `milestones syncs remote data into dao before emitting`() = runTest {
        val dao = FakeRcvMilestonesDao()
        val remoteDataSource = mockk<MilestonesRemoteDataSource>()
        coEvery { remoteDataSource.getMilestones() } returns RcvNetworkResult.Success(milestonesResponse)
        val repository = NetworkBackedRcvMilestonesRepository(
            remoteDataSource = remoteDataSource,
            milestonesDao = dao,
            currentTimeMillis = { 456L },
        )

        repository.sync()

        repository.milestones.test {
            val item = awaitItem()

            assertEquals("KMP migration", item.getOrThrow().currentFocus.summary)
            assertEquals(LocalDate.parse("2026-05-18"), item.getOrThrow().recentMilestones.single().completedAt)
            assertEquals(456L, dao.lastReplacedGraph?.currentFocus?.updatedAtMillis)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `milestones emits cached data when remote sync fails`() = runTest {
        val dao = FakeRcvMilestonesDao(initialGraph = milestonesResponse.toEntityGraph(updatedAtMillis = 111L))
        val remoteDataSource = mockk<MilestonesRemoteDataSource>()
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
        val remoteDataSource = mockk<MilestonesRemoteDataSource>()
        coEvery { remoteDataSource.getMilestones() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvMilestonesRepository(remoteDataSource = remoteDataSource, milestonesDao = dao)

        repository.milestones.test {
            val item = awaitItem()

            assertEquals(true, item.isFailure)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clearCache clears dao cache without syncing remote data`() = runTest {
        val dao = FakeRcvMilestonesDao(initialGraph = milestonesResponse.toEntityGraph(updatedAtMillis = 111L))
        val remoteDataSource = mockk<MilestonesRemoteDataSource>()
        val repository = NetworkBackedRcvMilestonesRepository(
            remoteDataSource = remoteDataSource,
            milestonesDao = dao,
            currentTimeMillis = { 222L },
        )

        repository.clearCache()

        assertEquals(1, dao.clearMilestonesCacheCallCount)
        assertEquals(null, dao.lastReplacedGraph)
    }
}

private class FakeRcvMilestonesDao(
    initialGraph: MilestonesEntityGraph? = null,
) : MilestonesDao {
    private val graphFlow = MutableStateFlow(initialGraph)
    var lastReplacedGraph: MilestonesEntityGraph? = null
    var clearMilestonesCacheCallCount = 0

    override fun milestonesGraphFlow(): Flow<MilestonesEntityGraph?> = graphFlow

    override suspend fun replaceMilestones(graph: MilestonesEntityGraph) {
        lastReplacedGraph = graph
        graphFlow.value = graph
    }

    override suspend fun getMilestonesGraph(): MilestonesEntityGraph? = graphFlow.value
    override suspend fun clearMilestonesCache() {
        clearMilestonesCacheCallCount++
        graphFlow.value = null
    }

    override suspend fun getCurrentFocus(id: String): CurrentFocusEntity? = error("Unused")
    override fun currentFocusFlow(id: String): Flow<CurrentFocusEntity?> = error("Unused")
    override suspend fun getCurrentFocusTopics(): List<CurrentFocusTopicEntity> = error("Unused")
    override fun currentFocusTopicsFlow(): Flow<List<CurrentFocusTopicEntity>> = error("Unused")
    override suspend fun getMilestones(): List<MilestoneEntity> = error("Unused")
    override fun milestonesFlow(): Flow<List<MilestoneEntity>> = error("Unused")
    override suspend fun getMilestoneTopics(): List<MilestoneTopicEntity> = error("Unused")
    override fun milestoneTopicsFlow(): Flow<List<MilestoneTopicEntity>> = error("Unused")
    override suspend fun upsertCurrentFocus(currentFocus: CurrentFocusEntity) = error("Unused")
    override suspend fun upsertCurrentFocusTopics(topics: List<CurrentFocusTopicEntity>) = error("Unused")
    override suspend fun upsertMilestones(milestones: List<MilestoneEntity>) = error("Unused")
    override suspend fun upsertMilestoneTopics(topics: List<MilestoneTopicEntity>) = error("Unused")
    override suspend fun clearCurrentFocus() = error("Unused")
    override suspend fun clearCurrentFocusTopics() = error("Unused")
    override suspend fun clearMilestones() = error("Unused")
    override suspend fun clearMilestoneTopics() = error("Unused")
}

private val milestonesResponse = MilestonesResponseDto(
    currentFocus = CurrentFocusDto(
        summary = "KMP migration",
        topics = listOf("Room"),
    ),
    recentMilestones = listOf(
        MilestoneDto(
            id = "nav3",
            title = "Navigation skeleton",
            description = "Added Navigation 3 skeleton.",
            completedAt = "2026-05-18",
            topics = emptyList(),
        ),
    ),
)
