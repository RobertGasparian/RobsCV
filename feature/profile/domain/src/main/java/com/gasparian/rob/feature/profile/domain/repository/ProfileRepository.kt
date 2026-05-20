package com.gasparian.rob.feature.profile.domain.repository

import com.gasparian.rob.feature.profile.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Result<Profile>>
}
