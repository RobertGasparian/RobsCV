package com.gasparian.rob.feature.profile.domain.repository

import com.gasparian.rob.feature.profile.domain.model.RcvProfile
import kotlinx.coroutines.flow.Flow

interface RcvProfileRepository {
    val profile: Flow<Result<RcvProfile>>
}
