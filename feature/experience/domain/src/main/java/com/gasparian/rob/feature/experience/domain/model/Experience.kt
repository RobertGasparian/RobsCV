package com.gasparian.rob.feature.experience.domain.model

import kotlinx.datetime.LocalDate

data class Experience(
    val roles: List<ExperienceRole>,
)

data class ExperienceRole(
    val title: String,
    val company: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val location: String,
    val workArrangement: WorkArrangement,
    val summary: String,
    val highlights: List<String>,
)

sealed interface WorkArrangement {
    data object Remote : WorkArrangement

    data object OnSite : WorkArrangement

    data object Hybrid : WorkArrangement

    data class Unknown(
        val rawValue: String,
    ) : WorkArrangement
}
