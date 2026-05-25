package com.gasparian.rob.feature.skills.domain.usecase

import com.gasparian.rob.feature.skills.domain.repository.SkillsRepository

class SyncSkillsUseCase(
    private val skillsRepository: SkillsRepository,
) {
    suspend operator fun invoke() {
        skillsRepository.sync()
    }
}
