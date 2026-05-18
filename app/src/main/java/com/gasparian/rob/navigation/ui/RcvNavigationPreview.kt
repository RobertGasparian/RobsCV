package com.gasparian.rob.navigation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gasparian.rob.core.dsm.theme.RcvTheme

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun RcvPhoneNavigationPreview() {
    RcvTheme(dynamicColor = false) {
        RcvAppNavigation(
            onExit = {},
        )
    }
}

@Preview(widthDp = 900, heightDp = 600)
@Composable
private fun RcvTabletNavigationPreview() {
    RcvTheme(dynamicColor = false) {
        RcvAppNavigation(
            onExit = {},
        )
    }
}
