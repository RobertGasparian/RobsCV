package com.gasparian.rob.feature.milestones.domain.model

data class RcvMilestones(
    val currentFocus: RcvCurrentFocus,
    val recentMilestones: List<RcvMilestone>,
)

data class RcvCurrentFocus(
    val summary: String,
    val topics: List<String>,
)

data class RcvMilestone(
    val id: String,
    val title: String,
    val description: String,
    val completedAt: String,
    val topics: List<String>,
)
