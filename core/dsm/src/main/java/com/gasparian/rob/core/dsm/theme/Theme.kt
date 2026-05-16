package com.gasparian.rob.core.dsm.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme: ColorScheme =
    darkColorScheme(
        primary = Teal80,
        onPrimary = Neutral10,
        primaryContainer = TealContainer80,
        onPrimaryContainer = Neutral95,
        secondary = BlueGray80,
        onSecondary = Neutral10,
        secondaryContainer = BlueGrayContainer80,
        onSecondaryContainer = Neutral95,
        tertiary = WarmGold80,
        onTertiary = Neutral10,
        tertiaryContainer = WarmGoldContainer80,
        onTertiaryContainer = Neutral95,
        background = Neutral10,
        onBackground = Neutral90,
        surface = Neutral10,
        onSurface = Neutral90,
        surfaceVariant = Neutral30,
        onSurfaceVariant = Neutral80,
        outline = Neutral80,
        outlineVariant = Neutral30,
        error = Error80,
    )

private val LightColorScheme: ColorScheme =
    lightColorScheme(
        primary = Teal40,
        onPrimary = Neutral98,
        primaryContainer = TealContainer40,
        onPrimaryContainer = Neutral10,
        secondary = BlueGray40,
        onSecondary = Neutral98,
        secondaryContainer = BlueGrayContainer40,
        onSecondaryContainer = Neutral10,
        tertiary = WarmGold40,
        onTertiary = Neutral98,
        tertiaryContainer = WarmGoldContainer40,
        onTertiaryContainer = Neutral10,
        background = Neutral98,
        onBackground = Neutral10,
        surface = Neutral98,
        onSurface = Neutral10,
        surfaceVariant = Neutral95,
        onSurfaceVariant = Neutral30,
        outline = BlueGray40,
        outlineVariant = Neutral80,
        error = Error40,
    )

object RcvTheme {
    val spacing: RcvSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalRcvSpacing.current
}

@Composable
fun RcvTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme

            else -> LightColorScheme
        }

    CompositionLocalProvider(LocalRcvSpacing provides RcvSpacing()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = RcvTypography,
            shapes = RcvShapes,
            content = content,
        )
    }
}
