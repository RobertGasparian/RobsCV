package com.gasparian.rob.core.dsm.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class RcvSpacing(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    val section: Dp = 40.dp,
    val screenHorizontal: Dp = 20.dp,
    val screenHorizontalExpanded: Dp = 32.dp,
)

val LocalRcvSpacing = staticCompositionLocalOf { RcvSpacing() }
