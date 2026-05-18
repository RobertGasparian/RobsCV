package com.gasparian.rob.navigation.route

import androidx.compose.ui.graphics.Color
import com.gasparian.rob.core.navigation.RcvTopLevelDestination
import com.gasparian.rob.feature.experience.ui.navigation.RcvExperienceRoute
import com.gasparian.rob.feature.home.ui.navigation.RcvHomeRoute
import com.gasparian.rob.feature.profile.ui.navigation.RcvProfileRoute
import com.gasparian.rob.feature.skills.ui.navigation.RcvSkillsRoute

val RcvTopLevelDestinations: List<RcvTopLevelDestination> =
    listOf(
        RcvTopLevelDestination(
            route = RcvHomeRoute,
            label = "Home",
            symbol = "H",
            color = Color(0xFF0E7C7B),
        ),
        RcvTopLevelDestination(
            route = RcvExperienceRoute,
            label = "Experience",
            symbol = "E",
            color = Color(0xFF6750A4),
        ),
        RcvTopLevelDestination(
            route = RcvSkillsRoute,
            label = "Skills",
            symbol = "S",
            color = Color(0xFFB3261E),
        ),
        RcvTopLevelDestination(
            route = RcvProfileRoute,
            label = "Profile",
            symbol = "P",
            color = Color(0xFF735B00),
        ),
    )
