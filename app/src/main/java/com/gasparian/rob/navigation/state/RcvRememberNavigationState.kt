package com.gasparian.rob.navigation.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.gasparian.rob.navigation.route.RcvTopLevelDestinations

@Composable
fun rememberRcvNavigationState(): RcvNavigationState = remember {
    RcvNavigationState(
        startDestination = RcvTopLevelDestinations.first(),
    )
}
