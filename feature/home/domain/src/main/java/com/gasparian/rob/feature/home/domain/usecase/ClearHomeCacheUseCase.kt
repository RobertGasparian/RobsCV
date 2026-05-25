package com.gasparian.rob.feature.home.domain.usecase

import com.gasparian.rob.feature.home.domain.repository.HomeRepository

class ClearHomeCacheUseCase(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke() {
        homeRepository.clearCache()
    }
}
