package com.gasparian.rob.feature.home.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RcvHomeExperienceResponseDto(
    val roles: List<RcvHomeExperienceRoleDto>,
)

@Serializable
data class RcvHomeExperienceRoleDto(
    val title: String,
    val company: String,
    val startDate: String,
    val endDate: String? = null,
    val location: String,
    val workArrangement: String,
    val summary: String,
    val highlights: List<String>,
)
