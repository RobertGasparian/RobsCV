package com.gasparian.rob.navigation.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.gasparian.rob.core.navigation.RcvRoute
import com.gasparian.rob.core.navigation.RcvTopLevelDestination
import com.gasparian.rob.navigation.route.RcvTopLevelDestinations

class RcvNavigationState(
    startDestination: RcvTopLevelDestination,
) {
    private val topLevelStacks: LinkedHashMap<RcvRoute, SnapshotStateList<RcvRoute>> =
        linkedMapOf(startDestination.route to mutableStateListOf(startDestination.route))

    var selectedTopLevelDestination: RcvTopLevelDestination by mutableStateOf(startDestination)
        private set

    val backStack: SnapshotStateList<RcvRoute> = mutableStateListOf(startDestination.route)

    fun navigateToTopLevel(destination: RcvTopLevelDestination) {
        val stack =
            topLevelStacks.remove(destination.route)
                ?: mutableStateListOf<RcvRoute>(destination.route)
        topLevelStacks[destination.route] = stack
        selectedTopLevelDestination = destination
        updateBackStack()
    }

    fun navigateTo(route: RcvRoute) {
        topLevelStacks[selectedTopLevelDestination.route]?.add(route)
        updateBackStack()
    }

    fun navigateBack(): Boolean {
        if (backStack.size <= 1) return false

        val currentStack = topLevelStacks[selectedTopLevelDestination.route] ?: return false
        val removedRoute = currentStack.removeLastOrNull()

        if (removedRoute == selectedTopLevelDestination.route) {
            topLevelStacks.remove(selectedTopLevelDestination.route)
            val previousTopLevelRoute = topLevelStacks.keys.last()
            selectedTopLevelDestination =
                requireNotNull(
                    selectedTopLevelDestinationFor(previousTopLevelRoute),
                )
        }

        updateBackStack()
        return true
    }

    private fun updateBackStack() {
        backStack.apply {
            clear()
            addAll(topLevelStacks.flatMap { it.value })
        }
    }

    private fun selectedTopLevelDestinationFor(route: RcvRoute): RcvTopLevelDestination? = RcvTopLevelDestinations.firstOrNull { it.route == route }
}
