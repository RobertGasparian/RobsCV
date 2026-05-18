package com.gasparian.rob.feature.skills.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gasparian.rob.feature.skills.ui.component.RcvSkillsComponent

@Composable
fun RcvSkillsScreen(
    onOpenSkillDetail: (skillId: String) -> Unit,
) {
    RcvSkillsComponent(
        title = "Skills",
        description = "Android, Kotlin, architecture, testing, KMP, and AI workflow.",
        color = Color(0xFFB3261E),
        buttonLabel = "Open skill detail",
        onButtonClick = { onOpenSkillDetail("compose") },
    )
}

@Composable
fun RcvSkillsDetailScreen(
    skillId: String,
    onBackClick: () -> Unit,
) {
    RcvSkillsComponent(
        title = "Skill detail",
        description = "Selected skill id: $skillId",
        color = Color(0xFF8C1D18),
        buttonLabel = "Back",
        onButtonClick = onBackClick,
    )
}
