package com.gasparian.rob.feature.home.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RcvHomeMilestonesResponseDto(
    val currentFocus: RcvHomeCurrentFocusDto,
    val recentMilestones: List<RcvHomeMilestoneDto>,
)

@Serializable
data class RcvHomeCurrentFocusDto(
    val summary: String,
    val topics: List<String>,
)

@Serializable
data class RcvHomeMilestoneDto(
    val id: String,
    val title: String,
    val description: String,
    val completedAt: String,
    val topics: List<String>,
)
