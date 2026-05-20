package com.gasparian.rob.feature.experience.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ExperienceResponseDto(
    val roles: List<ExperienceRoleDto>,
)

@Serializable
data class ExperienceRoleDto(
    val title: String,
    val company: String,
    val startDate: String,
    val endDate: String? = null,
    val location: String,
    val workArrangement: String,
    val summary: String,
    val highlights: List<String>,
)
