package com.gasparian.rob.feature.skills.domain.usecase

import com.gasparian.rob.feature.skills.domain.model.Skills
import com.gasparian.rob.feature.skills.domain.repository.SkillsRepository
import kotlinx.coroutines.flow.Flow

class GetSkillsUseCase(
    private val skillsRepository: SkillsRepository,
) {
    operator fun invoke(): Flow<Result<Skills>> = skillsRepository.skills
}
