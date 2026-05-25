package com.gasparian.rob.feature.experience.domain.usecase

import com.gasparian.rob.feature.experience.domain.repository.ExperienceRepository

class ClearExperienceCacheUseCase(
    private val experienceRepository: ExperienceRepository,
) {
    suspend operator fun invoke() {
        experienceRepository.clearCache()
    }
}
