package com.gasparian.rob.feature.profile.domain.usecase

import com.gasparian.rob.feature.profile.domain.model.Profile
import com.gasparian.rob.feature.profile.domain.model.ProfileContact
import com.gasparian.rob.feature.profile.domain.model.ProfileLocation
import com.gasparian.rob.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetProfileUseCaseTest {
    @Test
    fun `returns profile from repository`() = runTest {
        val expected = Profile(
            id = "rob",
            displayName = "Robert Gasparyan",
            headline = "Android Engineer",
            shortBio = "Senior Android engineer",
            location = ProfileLocation(
                city = "Toronto",
                region = "ON",
                country = "Canada",
                addressLine = null,
            ),
            contact = ProfileContact(
                email = "rob.gasparian@gmail.com",
                phone = "+1 437-551-9859",
                linkedin = "linkedin.com/in/rob-gasparian/",
            ),
            professionalProfile = "Android engineering profile",
            summaryOfQualifications = emptyList(),
        )
        val useCase = GetProfileUseCase(FakeProfileRepository(Result.success(expected)))

        assertEquals(Result.success(expected), useCase().first())
    }

    @Test
    fun `clear profile cache delegates to repository`() = runTest {
        val repository = FakeProfileRepository(Result.failure(IllegalStateException("Unused")))
        val useCase = ClearProfileCacheUseCase(repository)

        useCase()

        assertEquals(1, repository.clearCacheCallCount)
    }

    @Test
    fun `sync profile delegates to repository`() = runTest {
        val repository = FakeProfileRepository(Result.failure(IllegalStateException("Unused")))
        val useCase = SyncProfileUseCase(repository)

        useCase()

        assertEquals(1, repository.syncCallCount)
    }
}

private class FakeProfileRepository(
    result: Result<Profile>,
) : ProfileRepository {
    override val profile: Flow<Result<Profile>> = flowOf(result)
    var clearCacheCallCount = 0
    var syncCallCount = 0

    override suspend fun clearCache() {
        clearCacheCallCount++
    }

    override suspend fun sync() {
        syncCallCount++
    }
}
