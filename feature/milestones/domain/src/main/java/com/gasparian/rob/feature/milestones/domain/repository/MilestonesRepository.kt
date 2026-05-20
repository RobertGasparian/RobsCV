package com.gasparian.rob.feature.milestones.domain.repository

import com.gasparian.rob.feature.milestones.domain.model.Milestones
import kotlinx.coroutines.flow.Flow

interface MilestonesRepository {
    val milestones: Flow<Result<Milestones>>
}
