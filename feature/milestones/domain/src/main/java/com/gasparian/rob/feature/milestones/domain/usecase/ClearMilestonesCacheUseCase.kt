package com.gasparian.rob.feature.milestones.domain.usecase

import com.gasparian.rob.feature.milestones.domain.repository.MilestonesRepository

class ClearMilestonesCacheUseCase(
    private val milestonesRepository: MilestonesRepository,
) {
    suspend operator fun invoke() {
        milestonesRepository.clearCache()
    }
}
