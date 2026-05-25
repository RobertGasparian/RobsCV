package com.gasparian.rob.feature.experience.domain.usecase

import com.gasparian.rob.feature.experience.domain.model.Experience
import com.gasparian.rob.feature.experience.domain.repository.ExperienceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetExperienceUseCaseTest {
    @Test
    fun `returns experience from repository`() = runTest {
        val expected = Experience(roles = emptyList())
        val useCase = GetExperienceUseCase(FakeExperienceRepository(Result.success(expected)))

        assertEquals(Result.success(expected), useCase().first())
    }

    @Test
    fun `clear experience cache delegates to repository`() = runTest {
        val repository = FakeExperienceRepository(Result.failure(IllegalStateException("Unused")))
        val useCase = ClearExperienceCacheUseCase(repository)

        useCase()

        assertEquals(1, repository.clearCacheCallCount)
    }

    @Test
    fun `sync experience delegates to repository`() = runTest {
        val repository = FakeExperienceRepository(Result.failure(IllegalStateException("Unused")))
        val useCase = SyncExperienceUseCase(repository)

        useCase()

        assertEquals(1, repository.syncCallCount)
    }
}

private class FakeExperienceRepository(
    result: Result<Experience>,
) : ExperienceRepository {
    override val experience: Flow<Result<Experience>> = flowOf(result)
    var clearCacheCallCount = 0
    var syncCallCount = 0

    override suspend fun clearCache() {
        clearCacheCallCount++
    }

    override suspend fun sync() {
        syncCallCount++
    }
}
