package com.gasparian.rob.feature.education.domain.repository

import com.gasparian.rob.feature.education.domain.model.Education
import kotlinx.coroutines.flow.Flow

interface EducationRepository {
    val education: Flow<Result<Education>>

    suspend fun sync()

    suspend fun clearCache()
}
