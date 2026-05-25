package com.gasparian.rob.feature.home.domain.usecase

import com.gasparian.rob.feature.education.domain.model.Education
import com.gasparian.rob.feature.experience.domain.model.Experience
import com.gasparian.rob.feature.home.domain.model.HomeData
import com.gasparian.rob.feature.home.domain.model.HomeResult
import com.gasparian.rob.feature.home.domain.repository.HomeRepository
import com.gasparian.rob.feature.milestones.domain.model.CurrentFocus
import com.gasparian.rob.feature.milestones.domain.model.Milestones
import com.gasparian.rob.feature.profile.domain.model.Profile
import com.gasparian.rob.feature.profile.domain.model.ProfileContact
import com.gasparian.rob.feature.profile.domain.model.ProfileLocation
import com.gasparian.rob.feature.skills.domain.model.Skills
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetHomeDataUseCaseTest {
    @Test
    fun `returns repository home data flow`() = runTest {
        val expected = HomeResult.Success(homeData())
        val useCase = GetHomeDataUseCase(FakeHomeRepository(flowOf(expected)))

        assertEquals(expected, useCase().first())
    }

    @Test
    fun `clear home cache delegates to repository`() = runTest {
        val repository = FakeHomeRepository(flowOf(HomeResult.Success(homeData())))
        val useCase = ClearHomeCacheUseCase(repository)

        useCase()

        assertEquals(1, repository.clearCacheCallCount)
    }

    @Test
    fun `sync home delegates to repository`() = runTest {
        val repository = FakeHomeRepository(flowOf(HomeResult.Success(homeData())))
        val useCase = SyncHomeUseCase(repository)

        useCase()

        assertEquals(1, repository.syncCallCount)
    }

    private class FakeHomeRepository(
        override val homeData: Flow<HomeResult<HomeData>>,
    ) : HomeRepository {
        var clearCacheCallCount = 0
        var syncCallCount = 0

        override suspend fun clearCache() {
            clearCacheCallCount++
        }

        override suspend fun sync() {
            syncCallCount++
        }
    }

    private fun homeData() = HomeData(
        profile =
        Profile(
            id = "profile",
            displayName = "Robert Gasparyan",
            headline = "Android Engineer",
            shortBio = "Short bio",
            location =
            ProfileLocation(
                city = "Toronto",
                region = "ON",
                country = "Canada",
                addressLine = null,
            ),
            contact =
            ProfileContact(
                email = "rob.gasparian@gmail.com",
                phone = "+1 437-551-9859",
                linkedin = "linkedin.com/in/rob-gasparian/",
            ),
            professionalProfile = "Professional profile",
            summaryOfQualifications = emptyList(),
        ),
        skills = Skills(categories = emptyList(), skills = emptyList()),
        experience = Experience(roles = emptyList()),
        education = Education(institutions = emptyList(), items = emptyList()),
        milestones =
        Milestones(
            currentFocus =
            CurrentFocus(
                summary = "Focus",
                topics = emptyList(),
            ),
            recentMilestones = emptyList(),
        ),
    )
}
