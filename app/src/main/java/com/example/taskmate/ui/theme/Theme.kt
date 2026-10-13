package com.example.taskmate.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BlueNormal,
    secondary = BlueDark,
    tertiary = BlueLight,
    background = White,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun TaskmateTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Selalu gunakan LightColorScheme agar tampilan layar dan kartu berwarna putih bersih sesuai Figma
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}