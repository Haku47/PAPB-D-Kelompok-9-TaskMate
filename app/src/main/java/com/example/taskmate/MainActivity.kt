package com.example.taskmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.taskmate.navigation.TaskMateNavGraph
import com.example.taskmate.ui.theme.TaskmateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskmateTheme {
                val navController = rememberNavController()
                TaskMateNavGraph(navController = navController)
            }
        }
    }
}