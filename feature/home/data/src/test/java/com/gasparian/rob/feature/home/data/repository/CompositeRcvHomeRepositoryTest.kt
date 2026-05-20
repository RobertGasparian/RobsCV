package com.gasparian.rob.feature.home.data.repository

import app.cash.turbine.test
import com.gasparian.rob.feature.education.domain.model.RcvEducation
import com.gasparian.rob.feature.education.domain.repository.RcvEducationRepository
import com.gasparian.rob.feature.experience.domain.model.RcvExperience
import com.gasparian.rob.feature.experience.domain.repository.RcvExperienceRepository
import com.gasparian.rob.feature.home.domain.model.RcvHomeError
import com.gasparian.rob.feature.home.domain.model.RcvHomeResult
import com.gasparian.rob.feature.milestones.domain.model.RcvCurrentFocus
import com.gasparian.rob.feature.milestones.domain.model.RcvMilestones
import com.gasparian.rob.feature.milestones.domain.repository.RcvMilestonesRepository
import com.gasparian.rob.feature.profile.domain.model.RcvProfile
import com.gasparian.rob.feature.profile.domain.model.RcvProfileContact
import com.gasparian.rob.feature.profile.domain.model.RcvProfileLocation
import com.gasparian.rob.feature.profile.domain.repository.RcvProfileRepository
import com.gasparian.rob.feature.skills.domain.model.RcvSkills
import com.gasparian.rob.feature.skills.domain.repository.RcvSkillsRepository
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

            assertInstanceOf(RcvHomeResult.Success::class.java, item)
            assertEquals(profile, (item as RcvHomeResult.Success).data.profile)
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

            assertInstanceOf(RcvHomeResult.Failure::class.java, item)
            val error = (item as RcvHomeResult.Failure).error as RcvHomeError.Unknown
            assertEquals("Profile missing", error.message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private class FakeProfileRepository(
    result: Result<RcvProfile>,
) : RcvProfileRepository {
    override val profile: Flow<Result<RcvProfile>> = MutableStateFlow(result)
}

private class FakeSkillsRepository(
    result: Result<RcvSkills>,
) : RcvSkillsRepository {
    override val skills: Flow<Result<RcvSkills>> = MutableStateFlow(result)
}

private class FakeExperienceRepository(
    result: Result<RcvExperience>,
) : RcvExperienceRepository {
    override val experience: Flow<Result<RcvExperience>> = MutableStateFlow(result)
}

private class FakeEducationRepository(
    result: Result<RcvEducation>,
) : RcvEducationRepository {
    override val education: Flow<Result<RcvEducation>> = MutableStateFlow(result)
}

private class FakeMilestonesRepository(
    result: Result<RcvMilestones>,
) : RcvMilestonesRepository {
    override val milestones: Flow<Result<RcvMilestones>> = MutableStateFlow(result)
}

private val profile = RcvProfile(
    id = "rob",
    displayName = "Robert Gasparyan",
    headline = "Android Engineer",
    shortBio = "Senior Android engineer.",
    location = RcvProfileLocation(city = "Toronto", region = "ON", country = "Canada", addressLine = null),
    contact = RcvProfileContact(
        email = "rob.gasparian@gmail.com",
        phone = "+1 437-551-9859",
        linkedin = "linkedin.com/in/rob-gasparian/",
    ),
    professionalProfile = "Professional profile",
    summaryOfQualifications = emptyList(),
)

private val skills = RcvSkills(categories = emptyList(), skills = emptyList())
private val experience = RcvExperience(roles = emptyList())
private val education = RcvEducation(institutions = emptyList(), items = emptyList())
private val milestones = RcvMilestones(
    currentFocus = RcvCurrentFocus(summary = "KMP migration", topics = emptyList()),
    recentMilestones = emptyList(),
)
