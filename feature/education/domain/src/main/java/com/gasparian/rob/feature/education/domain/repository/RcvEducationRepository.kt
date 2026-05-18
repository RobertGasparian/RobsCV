package com.gasparian.rob.feature.education.domain.repository

import com.gasparian.rob.feature.education.domain.model.RcvEducation
import kotlinx.coroutines.flow.Flow

interface RcvEducationRepository {
    val education: Flow<Result<RcvEducation>>
}
