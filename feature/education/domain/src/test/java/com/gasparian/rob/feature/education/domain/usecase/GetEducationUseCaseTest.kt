package com.gasparian.rob.feature.education.domain.usecase

import com.gasparian.rob.feature.education.domain.model.Education
import com.gasparian.rob.feature.education.domain.repository.EducationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetEducationUseCaseTest {
    @Test
    fun `returns education from repository`() = runTest {
        val expected = Education(institutions = emptyList(), items = emptyList())
        val useCase = GetEducationUseCase(FakeEducationRepository(Result.success(expected)))

        assertEquals(Result.success(expected), useCase().first())
    }
}

private class FakeEducationRepository(
    result: Result<Education>,
) : EducationRepository {
    override val education: Flow<Result<Education>> = flowOf(result)
}
