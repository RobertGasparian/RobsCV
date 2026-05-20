package com.gasparian.rob.feature.milestones.domain.model

import kotlinx.datetime.LocalDate

data class Milestones(
    val currentFocus: CurrentFocus,
    val recentMilestones: List<Milestone>,
)

data class CurrentFocus(
    val summary: String,
    val topics: List<String>,
)

data class Milestone(
    val id: String,
    val title: String,
    val description: String,
    val completedAt: LocalDate,
    val topics: List<String>,
)
