package com.gasparian.rob.feature.home.domain.repository

import com.gasparian.rob.feature.home.domain.model.RcvHomeData
import com.gasparian.rob.feature.home.domain.model.RcvHomeResult
import kotlinx.coroutines.flow.Flow

interface RcvHomeRepository {
    fun observeHomeData(): Flow<RcvHomeResult<RcvHomeData>>
}
