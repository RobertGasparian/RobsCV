package com.gasparian.rob.navigation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gasparian.rob.core.navigation.RcvTopLevelDestination
import com.gasparian.rob.navigation.route.RcvTopLevelDestinations

@Composable
fun RcvNavigationBar(
    selectedDestination: RcvTopLevelDestination,
    onRouteClick: (RcvTopLevelDestination) -> Unit,
) {
    NavigationBar {
        RcvTopLevelDestinations.forEach { destination ->
            NavigationBarItem(
                selected = destination == selectedDestination,
                onClick = { onRouteClick(destination) },
                icon = {
                    RcvRouteSymbol(
                        destination = destination,
                    )
                },
                label = {
                    Text(destination.label)
                },
            )
        }
    }
}

@Composable
fun RcvNavigationRail(
    selectedDestination: RcvTopLevelDestination,
    onRouteClick: (RcvTopLevelDestination) -> Unit,
) {
    NavigationRail {
        Spacer(modifier = Modifier.width(80.dp))
        RcvTopLevelDestinations.forEach { destination ->
            NavigationRailItem(
                selected = destination == selectedDestination,
                onClick = { onRouteClick(destination) },
                icon = {
                    RcvRouteSymbol(
                        destination = destination,
                    )
                },
                label = {
                    Text(destination.label)
                },
            )
        }
    }
}

@Composable
private fun RcvRouteSymbol(
    destination: RcvTopLevelDestination,
) {
    Box(
        modifier =
        Modifier
            .clip(CircleShape)
            .background(destination.color.copy(alpha = 0.22f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = destination.symbol,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}
