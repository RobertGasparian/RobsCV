package com.gasparian.rob.feature.home.data.repository

import com.gasparian.rob.feature.education.domain.repository.EducationRepository
import com.gasparian.rob.feature.experience.domain.repository.ExperienceRepository
import com.gasparian.rob.feature.home.domain.model.HomeData
import com.gasparian.rob.feature.home.domain.model.HomeError
import com.gasparian.rob.feature.home.domain.model.HomeResult
import com.gasparian.rob.feature.home.domain.repository.HomeRepository
import com.gasparian.rob.feature.milestones.domain.repository.MilestonesRepository
import com.gasparian.rob.feature.profile.domain.repository.ProfileRepository
import com.gasparian.rob.feature.skills.domain.repository.SkillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class CompositeRcvHomeRepository(
    private val profileRepository: ProfileRepository,
    private val skillsRepository: SkillsRepository,
    private val experienceRepository: ExperienceRepository,
    private val educationRepository: EducationRepository,
    private val milestonesRepository: MilestonesRepository,
) : HomeRepository {
    override val homeData: Flow<HomeResult<HomeData>> = combine(
        profileRepository.profile,
        skillsRepository.skills,
        experienceRepository.experience,
        educationRepository.education,
        milestonesRepository.milestones,
    ) { profileResult, skillsResult, experienceResult, educationResult, milestonesResult ->
        val profile = profileResult.getOrElse { return@combine it.toHomeFailure() }
        val skills = skillsResult.getOrElse { return@combine it.toHomeFailure() }
        val experience = experienceResult.getOrElse { return@combine it.toHomeFailure() }
        val education = educationResult.getOrElse { return@combine it.toHomeFailure() }
        val milestones = milestonesResult.getOrElse { return@combine it.toHomeFailure() }

        HomeResult.Success(
            HomeData(
                profile = profile,
                skills = skills,
                experience = experience,
                education = education,
                milestones = milestones,
            ),
        )
    }
}

private fun Throwable.toHomeFailure(): HomeResult.Failure = HomeResult.Failure(
    error = HomeError.Unknown(message = message ?: "Unable to load home data"),
)
