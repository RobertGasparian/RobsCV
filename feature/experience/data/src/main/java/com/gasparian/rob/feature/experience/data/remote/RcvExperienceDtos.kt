package com.gasparian.rob.feature.experience.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class RcvExperienceResponseDto(
    val roles: List<RcvExperienceRoleDto>,
)

@Serializable
data class RcvExperienceRoleDto(
    val title: String,
    val company: String,
    val startDate: String,
    val endDate: String? = null,
    val location: String,
    val workArrangement: String,
    val summary: String,
    val highlights: List<String>,
)
