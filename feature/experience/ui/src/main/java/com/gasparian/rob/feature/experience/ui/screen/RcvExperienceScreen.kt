package com.gasparian.rob.feature.experience.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gasparian.rob.feature.experience.ui.component.RcvExperienceComponent

@Composable
fun RcvExperienceScreen(
    onOpenExperienceDetail: (experienceId: String) -> Unit,
) {
    RcvExperienceComponent(
        title = "Experience",
        description = "Professional timeline and selected career impact.",
        color = Color(0xFF6750A4),
        buttonLabel = "Open experience detail",
        onButtonClick = { onOpenExperienceDetail("priceline") },
    )
}

@Composable
fun RcvExperienceDetailScreen(
    experienceId: String,
    onBackClick: () -> Unit,
) {
    RcvExperienceComponent(
        title = "Experience detail",
        description = "Selected experience id: $experienceId",
        color = Color(0xFF4F378B),
        buttonLabel = "Back",
        onButtonClick = onBackClick,
    )
}
