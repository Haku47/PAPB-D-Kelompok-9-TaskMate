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
    private var lightSensorManager: com.example.taskmate.sensor.AmbientLightSensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.taskmate.data.DummyData.init(applicationContext)
        com.example.taskmate.data.UserManager.init(applicationContext)
        com.example.taskmate.worker.TaskReminderWorker.scheduleTaskReminders(applicationContext)
        lightSensorManager = com.example.taskmate.sensor.AmbientLightSensorManager(applicationContext)
        enableEdgeToEdge()

        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            TaskmateApp()
        }
    }

    override fun onResume() {
        super.onResume()
        lightSensorManager?.startListening()
    }

    override fun onPause() {
        super.onPause()
        lightSensorManager?.stopListening()
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