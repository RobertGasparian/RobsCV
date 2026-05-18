package com.gasparian.rob.feature.milestones.domain.repository

import com.gasparian.rob.feature.milestones.domain.model.RcvMilestones
import kotlinx.coroutines.flow.Flow

interface RcvMilestonesRepository {
    val milestones: Flow<Result<RcvMilestones>>
}
