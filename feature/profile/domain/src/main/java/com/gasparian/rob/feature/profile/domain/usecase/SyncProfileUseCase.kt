package com.gasparian.rob.feature.profile.domain.usecase

import com.gasparian.rob.feature.profile.domain.repository.ProfileRepository

class SyncProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() {
        profileRepository.sync()
    }
}
