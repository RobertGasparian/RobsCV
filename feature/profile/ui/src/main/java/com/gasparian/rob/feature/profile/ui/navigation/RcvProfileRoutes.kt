package com.gasparian.rob.feature.profile.ui.navigation

import com.gasparian.rob.core.navigation.RcvRoute
import kotlinx.serialization.Serializable

@Serializable
data object RcvProfileRoute : RcvRoute

@Serializable
data class RcvProfileDetailRoute(
    val sectionId: String,
) : RcvRoute
