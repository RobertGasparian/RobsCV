package com.gasparian.rob.feature.milestones.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class RcvMilestonesResponseDto(
    val currentFocus: RcvCurrentFocusDto,
    val recentMilestones: List<RcvMilestoneDto>,
)

@Serializable
data class RcvCurrentFocusDto(
    val summary: String,
    val topics: List<String>,
)

@Serializable
data class RcvMilestoneDto(
    val id: String,
    val title: String,
    val description: String,
    val completedAt: String,
    val topics: List<String>,
)
