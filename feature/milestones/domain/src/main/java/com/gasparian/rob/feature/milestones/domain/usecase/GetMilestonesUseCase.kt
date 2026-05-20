package com.gasparian.rob.feature.milestones.domain.usecase

import com.gasparian.rob.feature.milestones.domain.model.Milestones
import com.gasparian.rob.feature.milestones.domain.repository.MilestonesRepository
import kotlinx.coroutines.flow.Flow

class GetMilestonesUseCase(
    private val milestonesRepository: MilestonesRepository,
) {
    operator fun invoke(): Flow<Result<Milestones>> = milestonesRepository.milestones
}
