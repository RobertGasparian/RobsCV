package com.gasparian.rob.feature.experience.domain.repository

import com.gasparian.rob.feature.experience.domain.model.RcvExperience
import kotlinx.coroutines.flow.Flow

interface RcvExperienceRepository {
    val experience: Flow<Result<RcvExperience>>
}
