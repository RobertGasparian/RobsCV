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
}

private class FakeProfileRepository(
    result: Result<Profile>,
) : ProfileRepository {
    override val profile: Flow<Result<Profile>> = flowOf(result)
}
