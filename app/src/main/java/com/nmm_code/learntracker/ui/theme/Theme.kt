package com.nmm_code.learntracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = primary,
    secondary = secondary,
    tertiary = ternary,
    background = Color(26, 26, 26, 255)
)

private val LightColorScheme = lightColorScheme(

    primary = primary,
    secondary = secondary,
    tertiary = ternary,
    background = Color(248, 248, 248, 255)
)


data class Space(
    val padding0: Dp = 4.dp,
    val padding1: Dp = 8.dp,
    val padding2: Dp = 16.dp,
    val padding3: Dp = 24.dp,
    val padding4: Dp = 32.dp,
    val padding5: Dp = 40.dp,
    val padding6: Dp = 48.dp,
    val padding7: Dp = 56.dp,
    val padding8: Dp = 64.dp,
    val padding9: Dp = 72.dp
)
val LocalSpace = compositionLocalOf { Space() }

val MaterialTheme.space: Space
    @Composable
    @ReadOnlyComposable
    get() = LocalSpace.current


@Composable
fun LearnTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}