package com.gasparian.rob.feature.education.domain.usecase

import com.gasparian.rob.feature.education.domain.repository.EducationRepository

class SyncEducationUseCase(
    private val educationRepository: EducationRepository,
) {
    suspend operator fun invoke() {
        educationRepository.sync()
    }
}
