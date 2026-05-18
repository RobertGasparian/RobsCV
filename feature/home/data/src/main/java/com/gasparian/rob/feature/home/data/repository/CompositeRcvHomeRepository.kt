package com.gasparian.rob.feature.home.data.repository

import com.gasparian.rob.feature.education.domain.repository.RcvEducationRepository
import com.gasparian.rob.feature.experience.domain.repository.RcvExperienceRepository
import com.gasparian.rob.feature.home.domain.model.RcvHomeData
import com.gasparian.rob.feature.home.domain.model.RcvHomeError
import com.gasparian.rob.feature.home.domain.model.RcvHomeResult
import com.gasparian.rob.feature.home.domain.repository.RcvHomeRepository
import com.gasparian.rob.feature.milestones.domain.repository.RcvMilestonesRepository
import com.gasparian.rob.feature.profile.domain.repository.RcvProfileRepository
import com.gasparian.rob.feature.skills.domain.repository.RcvSkillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class CompositeRcvHomeRepository(
    private val profileRepository: RcvProfileRepository,
    private val skillsRepository: RcvSkillsRepository,
    private val experienceRepository: RcvExperienceRepository,
    private val educationRepository: RcvEducationRepository,
    private val milestonesRepository: RcvMilestonesRepository,
) : RcvHomeRepository {
    override val homeData: Flow<RcvHomeResult<RcvHomeData>> = combine(
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

        RcvHomeResult.Success(
            RcvHomeData(
                profile = profile,
                skills = skills,
                experience = experience,
                education = education,
                milestones = milestones,
            ),
        )
    }
}

private fun Throwable.toHomeFailure(): RcvHomeResult.Failure = RcvHomeResult.Failure(
    error = RcvHomeError.Unknown(message = message ?: "Unable to load home data"),
)
