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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
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

    @Test
    fun `clearCache delegates to every composed feature repository`() = runTest {
        val profileRepository = FakeProfileRepository(Result.success(profile))
        val skillsRepository = FakeSkillsRepository(Result.success(skills))
        val experienceRepository = FakeExperienceRepository(Result.success(experience))
        val educationRepository = FakeEducationRepository(Result.success(education))
        val milestonesRepository = FakeMilestonesRepository(Result.success(milestones))
        val repository = CompositeRcvHomeRepository(
            profileRepository = profileRepository,
            skillsRepository = skillsRepository,
            experienceRepository = experienceRepository,
            educationRepository = educationRepository,
            milestonesRepository = milestonesRepository,
        )

        repository.clearCache()

        assertEquals(1, profileRepository.clearCacheCallCount)
        assertEquals(1, skillsRepository.clearCacheCallCount)
        assertEquals(1, experienceRepository.clearCacheCallCount)
        assertEquals(1, educationRepository.clearCacheCallCount)
        assertEquals(1, milestonesRepository.clearCacheCallCount)
    }

    @Test
    fun `clearCache runs feature cache clears concurrently and waits for all to finish`() = runTest {
        val startedClears = mutableListOf<String>()
        val finishedClears = mutableListOf<String>()
        val allClearsStarted = CompletableDeferred<Unit>()
        val releaseClears = CompletableDeferred<Unit>()

        fun onClear(name: String): suspend () -> Unit = {
            startedClears += name
            if (startedClears.size == 5) {
                allClearsStarted.complete(Unit)
            }
            releaseClears.await()
            finishedClears += name
        }

        val repository = CompositeRcvHomeRepository(
            profileRepository = FakeProfileRepository(Result.success(profile), onClear("profile")),
            skillsRepository = FakeSkillsRepository(Result.success(skills), onClear("skills")),
            experienceRepository = FakeExperienceRepository(Result.success(experience), onClear("experience")),
            educationRepository = FakeEducationRepository(Result.success(education), onClear("education")),
            milestonesRepository = FakeMilestonesRepository(Result.success(milestones), onClear("milestones")),
        )
        val clearFinished = CompletableDeferred<Unit>()

        launch {
            repository.clearCache()
            clearFinished.complete(Unit)
        }

        allClearsStarted.await()
        assertEquals(listOf("profile", "skills", "experience", "education", "milestones"), startedClears)
        assertEquals(emptyList<String>(), finishedClears)
        assertFalse(clearFinished.isCompleted)

        releaseClears.complete(Unit)
        clearFinished.await()

        assertEquals(listOf("profile", "skills", "experience", "education", "milestones"), finishedClears)
    }

    @Test
    fun `sync delegates to every composed feature repository`() = runTest {
        val profileRepository = FakeProfileRepository(Result.success(profile))
        val skillsRepository = FakeSkillsRepository(Result.success(skills))
        val experienceRepository = FakeExperienceRepository(Result.success(experience))
        val educationRepository = FakeEducationRepository(Result.success(education))
        val milestonesRepository = FakeMilestonesRepository(Result.success(milestones))
        val repository = CompositeRcvHomeRepository(
            profileRepository = profileRepository,
            skillsRepository = skillsRepository,
            experienceRepository = experienceRepository,
            educationRepository = educationRepository,
            milestonesRepository = milestonesRepository,
        )

        repository.sync()

        assertEquals(1, profileRepository.syncCallCount)
        assertEquals(1, skillsRepository.syncCallCount)
        assertEquals(1, experienceRepository.syncCallCount)
        assertEquals(1, educationRepository.syncCallCount)
        assertEquals(1, milestonesRepository.syncCallCount)
    }
}

private class FakeProfileRepository(
    result: Result<Profile>,
    private val onClear: suspend () -> Unit = {},
) : ProfileRepository {
    override val profile: Flow<Result<Profile>> = MutableStateFlow(result)
    var clearCacheCallCount = 0
    var syncCallCount = 0

    override suspend fun clearCache() {
        clearCacheCallCount++
        onClear()
    }

    override suspend fun sync() {
        syncCallCount++
    }
}

private class FakeSkillsRepository(
    result: Result<Skills>,
    private val onClear: suspend () -> Unit = {},
) : SkillsRepository {
    override val skills: Flow<Result<Skills>> = MutableStateFlow(result)
    var clearCacheCallCount = 0
    var syncCallCount = 0

    override suspend fun clearCache() {
        clearCacheCallCount++
        onClear()
    }

    override suspend fun sync() {
        syncCallCount++
    }
}

private class FakeExperienceRepository(
    result: Result<Experience>,
    private val onClear: suspend () -> Unit = {},
) : ExperienceRepository {
    override val experience: Flow<Result<Experience>> = MutableStateFlow(result)
    var clearCacheCallCount = 0
    var syncCallCount = 0

    override suspend fun clearCache() {
        clearCacheCallCount++
        onClear()
    }

    override suspend fun sync() {
        syncCallCount++
    }
}

private class FakeEducationRepository(
    result: Result<Education>,
    private val onClear: suspend () -> Unit = {},
) : EducationRepository {
    override val education: Flow<Result<Education>> = MutableStateFlow(result)
    var clearCacheCallCount = 0
    var syncCallCount = 0

    override suspend fun clearCache() {
        clearCacheCallCount++
        onClear()
    }

    override suspend fun sync() {
        syncCallCount++
    }
}

private class FakeMilestonesRepository(
    result: Result<Milestones>,
    private val onClear: suspend () -> Unit = {},
) : MilestonesRepository {
    override val milestones: Flow<Result<Milestones>> = MutableStateFlow(result)
    var clearCacheCallCount = 0
    var syncCallCount = 0

    override suspend fun clearCache() {
        clearCacheCallCount++
        onClear()
    }

    override suspend fun sync() {
        syncCallCount++
    }
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
