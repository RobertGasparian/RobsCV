package com.gasparian.rob.feature.experience.domain.usecase

import com.gasparian.rob.feature.experience.domain.model.Experience
import com.gasparian.rob.feature.experience.domain.repository.ExperienceRepository
import kotlinx.coroutines.flow.Flow

class GetExperienceUseCase(
    private val experienceRepository: ExperienceRepository,
) {
    operator fun invoke(): Flow<Result<Experience>> = experienceRepository.experience
}
