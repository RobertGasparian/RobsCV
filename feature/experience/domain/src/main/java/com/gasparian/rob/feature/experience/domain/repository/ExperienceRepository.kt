package com.gasparian.rob.feature.experience.domain.repository

import com.gasparian.rob.feature.experience.domain.model.Experience
import kotlinx.coroutines.flow.Flow

interface ExperienceRepository {
    val experience: Flow<Result<Experience>>
}
