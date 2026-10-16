package com.example.taskmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.example.taskmate.navigation.TaskMateNavGraph
import com.example.taskmate.ui.theme.TaskmateTheme

import androidx.compose.runtime.Composable
import com.example.taskmate.ui.theme.ThemeState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            TaskmateApp()
        }
    }
}

@Composable
fun TaskmateApp() {
    val darkTheme = ThemeState.isDarkMode
    val fontSizeMode = ThemeState.fontSizeMode
    val language = ThemeState.language

    TaskmateTheme(
        darkTheme = darkTheme,
        fontSizeMode = fontSizeMode,
        language = language
    ) {
        val navController = rememberNavController()
        TaskMateNavGraph(
            navController = navController,
            fontSizeMode = fontSizeMode,
            language = language
        )
    }
}