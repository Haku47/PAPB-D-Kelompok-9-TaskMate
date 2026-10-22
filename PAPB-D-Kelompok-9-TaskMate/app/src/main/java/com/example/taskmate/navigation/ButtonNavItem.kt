package com.example.taskmate.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home)
    object Courses : BottomNavItem(Screen.Courses.route, "Courses", Icons.Default.Book)
    object Tasks : BottomNavItem(Screen.Tasks.route, "Tasks", Icons.Default.Checklist)
    object Schedule : BottomNavItem(Screen.Schedule.route, "Schedule", Icons.Default.DateRange)
    object Profile : BottomNavItem(Screen.Profile.route, "Profile", Icons.Default.Person)
}