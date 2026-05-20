package com.gasparian.rob.feature.milestones.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class MilestonesResponseDto(
    val currentFocus: CurrentFocusDto,
    val recentMilestones: List<MilestoneDto>,
)

@Serializable
data class CurrentFocusDto(
    val summary: String,
    val topics: List<String>,
)

@Serializable
data class MilestoneDto(
    val id: String,
    val title: String,
    val description: String,
    val completedAt: String,
    val topics: List<String>,
)
