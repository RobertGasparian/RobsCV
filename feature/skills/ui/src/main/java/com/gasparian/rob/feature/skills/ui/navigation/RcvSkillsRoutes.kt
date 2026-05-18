package com.gasparian.rob.feature.skills.ui.navigation

import com.gasparian.rob.core.navigation.RcvRoute
import kotlinx.serialization.Serializable

@Serializable
data object RcvSkillsRoute : RcvRoute

@Serializable
data class RcvSkillsDetailRoute(
    val skillId: String,
) : RcvRoute
