package com.gasparian.rob.navigation.ui

import androidx.navigation3.runtime.NavEntry
import com.gasparian.rob.core.navigation.RcvRoute
import com.gasparian.rob.feature.experience.ui.navigation.RcvExperienceDetailRoute
import com.gasparian.rob.feature.experience.ui.navigation.RcvExperienceRoute
import com.gasparian.rob.feature.experience.ui.screen.RcvExperienceDetailScreen
import com.gasparian.rob.feature.experience.ui.screen.RcvExperienceScreen
import com.gasparian.rob.feature.home.ui.navigation.RcvHomeDetailRoute
import com.gasparian.rob.feature.home.ui.navigation.RcvHomeRoute
import com.gasparian.rob.feature.home.ui.screen.RcvHomeDetailScreen
import com.gasparian.rob.feature.home.ui.screen.RcvHomeScreen
import com.gasparian.rob.feature.profile.ui.navigation.RcvProfileDetailRoute
import com.gasparian.rob.feature.profile.ui.navigation.RcvProfileRoute
import com.gasparian.rob.feature.profile.ui.screen.RcvProfileDetailScreen
import com.gasparian.rob.feature.profile.ui.screen.RcvProfileScreen
import com.gasparian.rob.feature.skills.ui.navigation.RcvSkillsDetailRoute
import com.gasparian.rob.feature.skills.ui.navigation.RcvSkillsRoute
import com.gasparian.rob.feature.skills.ui.screen.RcvSkillsDetailScreen
import com.gasparian.rob.feature.skills.ui.screen.RcvSkillsScreen
import com.gasparian.rob.navigation.state.RcvNavigationState

fun rcvNavigationEntry(
    route: RcvRoute,
    navigationState: RcvNavigationState,
): NavEntry<RcvRoute> = when (route) {
    RcvHomeRoute ->
        NavEntry(route) {
            RcvHomeScreen(
                onOpenHomeDetail = { navigationState.navigateTo(RcvHomeDetailRoute) },
            )
        }

    RcvExperienceRoute ->
        NavEntry(route) {
            RcvExperienceScreen(
                onOpenExperienceDetail = { experienceId ->
                    navigationState.navigateTo(
                        RcvExperienceDetailRoute(experienceId = experienceId),
                    )
                },
            )
        }

    RcvSkillsRoute ->
        NavEntry(route) {
            RcvSkillsScreen(
                onOpenSkillDetail = { skillId ->
                    navigationState.navigateTo(
                        RcvSkillsDetailRoute(skillId = skillId),
                    )
                },
            )
        }

    RcvProfileRoute ->
        NavEntry(route) {
            RcvProfileScreen(
                onOpenProfileSection = { sectionId ->
                    navigationState.navigateTo(
                        RcvProfileDetailRoute(sectionId = sectionId),
                    )
                },
            )
        }

    RcvHomeDetailRoute ->
        NavEntry(route) {
            RcvHomeDetailScreen(
                onBackClick = { navigationState.navigateBack() },
            )
        }

    is RcvExperienceDetailRoute ->
        NavEntry(route) {
            RcvExperienceDetailScreen(
                experienceId = route.experienceId,
                onBackClick = { navigationState.navigateBack() },
            )
        }

    is RcvSkillsDetailRoute ->
        NavEntry(route) {
            RcvSkillsDetailScreen(
                skillId = route.skillId,
                onBackClick = { navigationState.navigateBack() },
            )
        }

    is RcvProfileDetailRoute ->
        NavEntry(route) {
            RcvProfileDetailScreen(
                sectionId = route.sectionId,
                onBackClick = { navigationState.navigateBack() },
            )
        }

    else -> error("Unknown route: $route")
}
