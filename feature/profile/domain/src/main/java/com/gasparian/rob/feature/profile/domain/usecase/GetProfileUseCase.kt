package com.gasparian.rob.feature.profile.domain.usecase

import com.gasparian.rob.feature.profile.domain.model.Profile
import com.gasparian.rob.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<Result<Profile>> = profileRepository.profile
}
