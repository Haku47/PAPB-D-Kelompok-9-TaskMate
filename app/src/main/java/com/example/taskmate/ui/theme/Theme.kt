package com.example.taskmate.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = BlueNormal,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceCardDark,
    onPrimary = White,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderColorDark,
    outlineVariant = BorderColorDark
)

private val LightColorScheme = lightColorScheme(
    primary = BlueNormal,
    background = White,
    surface = White,
    surfaceVariant = Color(0xFFF1F5F9),
    onPrimary = White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = BorderColor,
    outlineVariant = BorderColor
)

@Composable
fun TaskmateTheme(
    darkTheme: Boolean = ThemeState.isDarkMode,
    fontSizeMode: FontSizeMode = ThemeState.fontSizeMode,
    language: AppLanguage = ThemeState.language,
    content: @Composable () -> Unit
) {
    // Explicit read within the composable body to ensure the recomposition scope tracks changes
    val activeDarkTheme = darkTheme
    val activeFontSizeMode = fontSizeMode
    val activeLanguage = language
    val activeFontScale = activeFontSizeMode.scale

    val colorScheme = if (activeDarkTheme) DarkColorScheme else LightColorScheme

    val currentStrings = when (activeLanguage) {
        AppLanguage.ENGLISH -> EnglishStrings
        AppLanguage.INDONESIAN -> IndonesianStrings
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !activeDarkTheme
                }
            }
        }
    }

    val currentDensity = LocalDensity.current
    val customDensity = Density(
        density = currentDensity.density,
        fontScale = currentDensity.fontScale * activeFontScale
    )

    CompositionLocalProvider(
        LocalDensity provides customDensity,
        LocalAppStrings provides currentStrings
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}