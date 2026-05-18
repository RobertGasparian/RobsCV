package com.gasparian.rob.navigation.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.NavDisplay
import com.gasparian.rob.navigation.state.RcvNavigationState
import com.gasparian.rob.navigation.state.rememberRcvNavigationState

@Composable
fun RcvAppNavigation(
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
    navigationState: RcvNavigationState = rememberRcvNavigationState(),
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        val useNavigationRail = maxWidth >= 720.dp

        if (useNavigationRail) {
            Row(
                modifier = Modifier.fillMaxSize(),
            ) {
                RcvNavigationRail(
                    selectedDestination = navigationState.selectedTopLevelDestination,
                    onRouteClick = navigationState::navigateToTopLevel,
                )
                RcvNavDisplay(
                    navigationState = navigationState,
                    contentPadding = PaddingValues(),
                    onExit = onExit,
                    modifier = Modifier.weight(1f),
                )
            }
        } else {
            Scaffold(
                bottomBar = {
                    RcvNavigationBar(
                        selectedDestination = navigationState.selectedTopLevelDestination,
                        onRouteClick = navigationState::navigateToTopLevel,
                    )
                },
            ) { innerPadding ->
                RcvNavDisplay(
                    navigationState = navigationState,
                    contentPadding = innerPadding,
                    onExit = onExit,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun RcvNavDisplay(
    navigationState: RcvNavigationState,
    contentPadding: PaddingValues,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        backStack = navigationState.backStack,
        modifier =
        modifier
            .fillMaxSize()
            .padding(contentPadding),
        onBack = {
            if (!navigationState.navigateBack()) {
                onExit()
            }
        },
        entryProvider = { key ->
            rcvNavigationEntry(
                route = key,
                navigationState = navigationState,
            )
        },
    )
}
