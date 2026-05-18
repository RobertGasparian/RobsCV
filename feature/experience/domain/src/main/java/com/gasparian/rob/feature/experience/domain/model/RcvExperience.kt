package com.gasparian.rob.feature.experience.domain.model

data class RcvExperience(
    val roles: List<RcvExperienceRole>,
)

data class RcvExperienceRole(
    val title: String,
    val company: String,
    val startDate: String,
    val endDate: String?,
    val location: String,
    val workArrangement: String,
    val summary: String,
    val highlights: List<String>,
)
