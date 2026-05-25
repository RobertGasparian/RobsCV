package com.gasparian.rob.feature.milestones.domain.usecase

import com.gasparian.rob.feature.milestones.domain.model.CurrentFocus
import com.gasparian.rob.feature.milestones.domain.model.Milestones
import com.gasparian.rob.feature.milestones.domain.repository.MilestonesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetMilestonesUseCaseTest {
    @Test
    fun `returns milestones from repository`() = runTest {
        val expected = Milestones(
            currentFocus = CurrentFocus(
                summary = "KMP migration",
                topics = emptyList(),
            ),
            recentMilestones = emptyList(),
        )
        val useCase = GetMilestonesUseCase(FakeMilestonesRepository(Result.success(expected)))

        assertEquals(Result.success(expected), useCase().first())
    }

    @Test
    fun `clear milestones cache delegates to repository`() = runTest {
        val repository = FakeMilestonesRepository(Result.failure(IllegalStateException("Unused")))
        val useCase = ClearMilestonesCacheUseCase(repository)

        useCase()

        assertEquals(1, repository.clearCacheCallCount)
    }

    @Test
    fun `sync milestones delegates to repository`() = runTest {
        val repository = FakeMilestonesRepository(Result.failure(IllegalStateException("Unused")))
        val useCase = SyncMilestonesUseCase(repository)

        useCase()

        assertEquals(1, repository.syncCallCount)
    }
}

private class FakeMilestonesRepository(
    result: Result<Milestones>,
) : MilestonesRepository {
    override val milestones: Flow<Result<Milestones>> = flowOf(result)
    var clearCacheCallCount = 0
    var syncCallCount = 0

    override suspend fun clearCache() {
        clearCacheCallCount++
    }

    override suspend fun sync() {
        syncCallCount++
    }
}
