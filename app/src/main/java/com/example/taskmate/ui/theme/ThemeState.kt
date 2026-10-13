package com.example.taskmate.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ThemeState {
    // Default mengikuti preferensi sistem atau false (Light)
    var isDarkMode by mutableStateOf(false)
}