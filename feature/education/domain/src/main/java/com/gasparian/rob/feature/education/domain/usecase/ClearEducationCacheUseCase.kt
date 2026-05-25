package com.gasparian.rob.feature.education.domain.usecase

import com.gasparian.rob.feature.education.domain.repository.EducationRepository

class ClearEducationCacheUseCase(
    private val educationRepository: EducationRepository,
) {
    suspend operator fun invoke() {
        educationRepository.clearCache()
    }
}
