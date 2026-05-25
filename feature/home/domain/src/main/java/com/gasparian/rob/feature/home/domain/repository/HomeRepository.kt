package com.gasparian.rob.feature.home.domain.repository

import com.gasparian.rob.feature.home.domain.model.HomeData
import com.gasparian.rob.feature.home.domain.model.HomeResult
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    val homeData: Flow<HomeResult<HomeData>>

    suspend fun sync()

    suspend fun clearCache()
}
