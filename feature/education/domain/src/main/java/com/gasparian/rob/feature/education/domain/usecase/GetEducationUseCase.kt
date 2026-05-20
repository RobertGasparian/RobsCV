package com.gasparian.rob.feature.education.domain.usecase

import com.gasparian.rob.feature.education.domain.model.Education
import com.gasparian.rob.feature.education.domain.repository.EducationRepository
import kotlinx.coroutines.flow.Flow

class GetEducationUseCase(
    private val educationRepository: EducationRepository,
) {
    operator fun invoke(): Flow<Result<Education>> = educationRepository.education
}
