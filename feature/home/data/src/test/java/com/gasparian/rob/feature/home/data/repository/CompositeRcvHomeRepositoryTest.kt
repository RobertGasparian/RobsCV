package com.gasparian.rob.feature.home.data.repository

import app.cash.turbine.test
import com.gasparian.rob.feature.education.domain.model.Education
import com.gasparian.rob.feature.education.domain.repository.EducationRepository
import com.gasparian.rob.feature.experience.domain.model.Experience
import com.gasparian.rob.feature.experience.domain.repository.ExperienceRepository
import com.gasparian.rob.feature.home.domain.model.HomeError
import com.gasparian.rob.feature.home.domain.model.HomeResult
import com.gasparian.rob.feature.milestones.domain.model.CurrentFocus
import com.gasparian.rob.feature.milestones.domain.model.Milestones
import com.gasparian.rob.feature.milestones.domain.repository.MilestonesRepository
import com.gasparian.rob.feature.profile.domain.model.Profile
import com.gasparian.rob.feature.profile.domain.model.ProfileContact
import com.gasparian.rob.feature.profile.domain.model.ProfileLocation
import com.gasparian.rob.feature.profile.domain.repository.ProfileRepository
import com.gasparian.rob.feature.skills.domain.model.Skills
import com.gasparian.rob.feature.skills.domain.repository.SkillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class CompositeRcvHomeRepositoryTest {
    @Test
    fun `homeData combines successful feature streams`() = runTest {
        val repository = CompositeRcvHomeRepository(
            profileRepository = FakeProfileRepository(Result.success(profile)),
            skillsRepository = FakeSkillsRepository(Result.success(skills)),
            experienceRepository = FakeExperienceRepository(Result.success(experience)),
            educationRepository = FakeEducationRepository(Result.success(education)),
            milestonesRepository = FakeMilestonesRepository(Result.success(milestones)),
        )

        repository.homeData.test {
            val item = awaitItem()

            assertInstanceOf(HomeResult.Success::class.java, item)
            assertEquals(profile, (item as HomeResult.Success).data.profile)
            assertEquals(skills, item.data.skills)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `homeData emits failure when any feature stream fails`() = runTest {
        val repository = CompositeRcvHomeRepository(
            profileRepository = FakeProfileRepository(Result.failure(IllegalStateException("Profile missing"))),
            skillsRepository = FakeSkillsRepository(Result.success(skills)),
            experienceRepository = FakeExperienceRepository(Result.success(experience)),
            educationRepository = FakeEducationRepository(Result.success(education)),
            milestonesRepository = FakeMilestonesRepository(Result.success(milestones)),
        )

        repository.homeData.test {
            val item = awaitItem()

            assertInstanceOf(HomeResult.Failure::class.java, item)
            val error = (item as HomeResult.Failure).error as HomeError.Unknown
            assertEquals("Profile missing", error.message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private class FakeProfileRepository(
    result: Result<Profile>,
) : ProfileRepository {
    override val profile: Flow<Result<Profile>> = MutableStateFlow(result)
}

private class FakeSkillsRepository(
    result: Result<Skills>,
) : SkillsRepository {
    override val skills: Flow<Result<Skills>> = MutableStateFlow(result)
}

private class FakeExperienceRepository(
    result: Result<Experience>,
) : ExperienceRepository {
    override val experience: Flow<Result<Experience>> = MutableStateFlow(result)
}

private class FakeEducationRepository(
    result: Result<Education>,
) : EducationRepository {
    override val education: Flow<Result<Education>> = MutableStateFlow(result)
}

private class FakeMilestonesRepository(
    result: Result<Milestones>,
) : MilestonesRepository {
    override val milestones: Flow<Result<Milestones>> = MutableStateFlow(result)
}

private val profile = Profile(
    id = "rob",
    displayName = "Robert Gasparyan",
    headline = "Android Engineer",
    shortBio = "Senior Android engineer.",
    location = ProfileLocation(city = "Toronto", region = "ON", country = "Canada", addressLine = null),
    contact = ProfileContact(
        email = "rob.gasparian@gmail.com",
        phone = "+1 437-551-9859",
        linkedin = "linkedin.com/in/rob-gasparian/",
    ),
    professionalProfile = "Professional profile",
    summaryOfQualifications = emptyList(),
)

private val skills = Skills(categories = emptyList(), skills = emptyList())
private val experience = Experience(roles = emptyList())
private val education = Education(institutions = emptyList(), items = emptyList())
private val milestones = Milestones(
    currentFocus = CurrentFocus(summary = "KMP migration", topics = emptyList()),
    recentMilestones = emptyList(),
)
