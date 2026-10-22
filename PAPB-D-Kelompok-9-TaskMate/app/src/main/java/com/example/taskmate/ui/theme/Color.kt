package com.example.taskmate.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Default template colors (dipertahankan agar tidak bentrok)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// --- TASKMATE BLUE PALETTE (Token Figma) ---
// Light Shades
val BlueLight = Color(0xFFEFF6FF)          // #eff6ff | rgb(239, 246, 255)
val BlueLightHover = Color(0xFFE7F2FE)     // #e7f2fe | rgb(231, 242, 254)
val BlueLightActive = Color(0xFFCEE3FD)    // #cee3fd | rgb(206, 227, 253)

// Normal Shades (Primary Brand Colors)
val BlueNormal = Color(0xFF60A5FA)         // #60a5fa | rgb(96, 165, 250)
val BlueNormalHover = Color(0xFF5695E1)    // #5695e1 | rgb(86, 149, 225)
val BlueNormalActive = Color(0xFF4D84C8)   // #4d84c8 | rgb(77, 132, 200)

// Dark Shades
val BlueDark = Color(0xFF487CBC)           // #487cbc | rgb(72, 124, 188)
val BlueDarkHover = Color(0xFF3A6396)      // #3a6396 | rgb(58, 99, 150)
val BlueDarkActive = Color(0xFF2B4A70)     // #2b4a70 | rgb(43, 74, 112)

// Darker Shade
val BlueDarker = Color(0xFF223A58)         // #223a58 | rgb(34, 58, 88)

// Utility / Neutral Colors
val White = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF1F2937)
val TextSecondary = Color(0xFF6B7280)
val BorderColor = Color(0xFFE5E7EB)

// --- DARK MODE PALETTE ---
val BackgroundDark = Color(0xFF121212)
val SurfaceDark = Color(0xFF1E1E1E)
val SurfaceCardDark = Color(0xFF252525)
val TextPrimaryDark = Color(0xFFF3F4F6)
val TextSecondaryDark = Color(0xFF9CA3AF)
val BorderColorDark = Color(0xFF374151)
val BottomBarDark = Color(0xFF1E293B)

// --- DYNAMIC THEME COLORS HELPER ---
object AppThemeColors {
    val background: Color
        @Composable get() = if (ThemeState.isDarkMode) BackgroundDark else White

    val surface: Color
        @Composable get() = if (ThemeState.isDarkMode) SurfaceDark else White

    val cardBackground: Color
        @Composable get() = if (ThemeState.isDarkMode) SurfaceCardDark else White

    val textPrimary: Color
        @Composable get() = if (ThemeState.isDarkMode) TextPrimaryDark else TextPrimary

    val textSecondary: Color
        @Composable get() = if (ThemeState.isDarkMode) TextSecondaryDark else TextSecondary

    val border: Color
        @Composable get() = if (ThemeState.isDarkMode) BorderColorDark else BorderColor

    val searchBarContainer: Color
        @Composable get() = if (ThemeState.isDarkMode) SurfaceCardDark else White

    val bottomBarBackground: Color
        @Composable get() = if (ThemeState.isDarkMode) BottomBarDark else BlueNormal

    val fabBackground: Color
        @Composable get() = if (ThemeState.isDarkMode) BottomBarDark else White

    val fabContentColor: Color
        @Composable get() = if (ThemeState.isDarkMode) White else TextPrimary

    val profileBannerBg: Color
        @Composable get() = if (ThemeState.isDarkMode) SurfaceDark else Color(0xFFE8F1FC)

    val avatarBg: Color
        @Composable get() = if (ThemeState.isDarkMode) Color(0xFF2C3E50) else Color(0xFFD3E4F8)
}