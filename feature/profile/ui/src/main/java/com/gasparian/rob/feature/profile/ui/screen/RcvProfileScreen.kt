package com.gasparian.rob.feature.profile.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gasparian.rob.feature.profile.ui.component.RcvProfileComponent

@Composable
fun RcvProfileScreen(
    onOpenProfileSection: (sectionId: String) -> Unit,
) {
    RcvProfileComponent(
        title = "Profile",
        description = "Education, contact shortcuts, milestones, and supporting info.",
        color = Color(0xFF735B00),
        buttonLabel = "Open profile detail",
        onButtonClick = { onOpenProfileSection("contact") },
    )
}

@Composable
fun RcvProfileDetailScreen(
    sectionId: String,
    onBackClick: () -> Unit,
) {
    RcvProfileComponent(
        title = "Profile detail",
        description = "Selected profile section: $sectionId",
        color = Color(0xFF574400),
        buttonLabel = "Back",
        onButtonClick = onBackClick,
    )
}
