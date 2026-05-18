package com.gasparian.rob.feature.experience.ui.navigation

import com.gasparian.rob.core.navigation.RcvRoute
import kotlinx.serialization.Serializable

@Serializable
data object RcvExperienceRoute : RcvRoute

@Serializable
data class RcvExperienceDetailRoute(
    val experienceId: String,
) : RcvRoute
