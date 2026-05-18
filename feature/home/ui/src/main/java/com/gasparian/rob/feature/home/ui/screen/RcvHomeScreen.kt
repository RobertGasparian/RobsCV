package com.gasparian.rob.feature.home.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gasparian.rob.feature.home.ui.component.RcvHomeComponent

@Composable
fun RcvHomeScreen(
    onOpenHomeDetail: () -> Unit,
) {
    RcvHomeComponent(
        title = "Home",
        description = "Overview, highlights, current focus, and quick actions.",
        color = Color(0xFF0E7C7B),
        buttonLabel = "Open home detail",
        onButtonClick = onOpenHomeDetail,
    )
}

@Composable
fun RcvHomeDetailScreen(
    onBackClick: () -> Unit,
) {
    RcvHomeComponent(
        title = "Home detail",
        description = "One level deeper from Home.",
        color = Color(0xFF00504F),
        buttonLabel = "Back",
        onButtonClick = onBackClick,
    )
}
