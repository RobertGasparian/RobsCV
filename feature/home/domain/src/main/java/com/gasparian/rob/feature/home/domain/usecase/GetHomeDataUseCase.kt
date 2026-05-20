package com.gasparian.rob.feature.home.domain.usecase

import com.gasparian.rob.feature.home.domain.model.HomeData
import com.gasparian.rob.feature.home.domain.model.HomeResult
import com.gasparian.rob.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetHomeDataUseCase(
    private val homeRepository: HomeRepository,
) {
    operator fun invoke(): Flow<HomeResult<HomeData>> = homeRepository.homeData
}
