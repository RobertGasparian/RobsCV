package com.gasparian.rob.core.navigation

import androidx.compose.ui.graphics.Color

data class RcvTopLevelDestination(
    /**
     * Route pushed when the user selects this top-level destination.
     */
    val route: RcvRoute,

    /**
     * Human-readable section name shown in navigation chrome such as the bottom bar or rail.
     */
    val label: String,

    /**
     * Temporary compact marker for the skeleton UI icon slot until proper icons are introduced.
     */
    val symbol: String,

    /**
     * Temporary accent color used by the skeleton route marker.
     */
    val color: Color,
)
