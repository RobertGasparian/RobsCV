package com.gasparian.rob.feature.skills.domain.repository

import com.gasparian.rob.feature.skills.domain.model.RcvSkills
import kotlinx.coroutines.flow.Flow

interface RcvSkillsRepository {
    val skills: Flow<Result<RcvSkills>>
}
