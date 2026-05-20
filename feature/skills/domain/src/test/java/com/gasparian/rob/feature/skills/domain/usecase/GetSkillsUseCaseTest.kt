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
}

private class FakeSkillsRepository(
    result: Result<Skills>,
) : SkillsRepository {
    override val skills: Flow<Result<Skills>> = flowOf(result)
}
