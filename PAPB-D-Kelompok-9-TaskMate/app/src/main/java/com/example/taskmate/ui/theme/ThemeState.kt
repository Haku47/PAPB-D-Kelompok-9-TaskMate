package com.example.taskmate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

enum class FontSizeMode(val displayName: String, val scale: Float) {
    SMALL("Small", 0.85f),
    REGULAR("Regular", 1.0f),
    LARGE("Large", 1.15f)
}

enum class AppLanguage(val displayName: String, val code: String) {
    ENGLISH("English", "en"),
    INDONESIAN("Indonesian", "id")
}

object ThemeState {
    // Mode tema pilihan pengguna, default SYSTEM otomatis mengikuti tema sistem perangkat
    var themeMode by mutableStateOf(ThemeMode.SYSTEM)

    // Ukuran font pilihan pengguna untuk kenyamanan aksesibilitas
    var fontSizeMode by mutableStateOf(FontSizeMode.REGULAR)

    // Bahasa antarmuka aplikasi
    var language by mutableStateOf(AppLanguage.ENGLISH)

    val fontScale: Float
        @Composable
        get() = fontSizeMode.scale

    // Indikator aktif dark mode yang bereaksi terhadap themeMode dan preferensi sistem
    val isDarkMode: Boolean
        @Composable
        get() = when (themeMode) {
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
        }

    fun setDarkMode(enabled: Boolean) {
        themeMode = if (enabled) ThemeMode.DARK else ThemeMode.LIGHT
    }

    fun setSystemTheme() {
        themeMode = ThemeMode.SYSTEM
    }

    fun setFontSize(mode: FontSizeMode) {
        fontSizeMode = mode
    }
}