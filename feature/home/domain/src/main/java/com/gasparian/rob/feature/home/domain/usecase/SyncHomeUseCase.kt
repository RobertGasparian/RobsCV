package com.gasparian.rob.feature.home.domain.usecase

import com.gasparian.rob.feature.home.domain.repository.HomeRepository

class SyncHomeUseCase(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke() {
        homeRepository.sync()
    }
}
