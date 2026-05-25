package com.gasparian.rob.feature.skills.domain.usecase

import com.gasparian.rob.feature.skills.domain.model.Skills
import com.gasparian.rob.feature.skills.domain.repository.SkillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetSkillsUseCaseTest {
    @Test
    fun `returns skills from repository`() = runTest {
        val expected = Skills(categories = emptyList(), skills = emptyList())
        val useCase = GetSkillsUseCase(FakeSkillsRepository(Result.success(expected)))

        assertEquals(Result.success(expected), useCase().first())
    }

    @Test
    fun `clear skills cache delegates to repository`() = runTest {
        val repository = FakeSkillsRepository(Result.failure(IllegalStateException("Unused")))
        val useCase = ClearSkillsCacheUseCase(repository)

        useCase()

        assertEquals(1, repository.clearCacheCallCount)
    }

    @Test
    fun `sync skills delegates to repository`() = runTest {
        val repository = FakeSkillsRepository(Result.failure(IllegalStateException("Unused")))
        val useCase = SyncSkillsUseCase(repository)

        useCase()

        assertEquals(1, repository.syncCallCount)
    }
}

private class FakeSkillsRepository(
    result: Result<Skills>,
) : SkillsRepository {
    override val skills: Flow<Result<Skills>> = flowOf(result)
    var clearCacheCallCount = 0
    var syncCallCount = 0

    override suspend fun clearCache() {
        clearCacheCallCount++
    }

    override suspend fun sync() {
        syncCallCount++
    }
}
