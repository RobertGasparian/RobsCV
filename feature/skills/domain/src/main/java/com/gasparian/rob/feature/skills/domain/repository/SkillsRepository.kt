package com.gasparian.rob.feature.skills.domain.repository

import com.gasparian.rob.feature.skills.domain.model.Skills
import kotlinx.coroutines.flow.Flow

interface SkillsRepository {
    val skills: Flow<Result<Skills>>
}
