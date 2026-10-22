package com.example.taskmate.navigation

sealed class Screen(val route: String) {
    // Auth & Onboarding Flow
    data object Splash : Screen("splash")
    data object Welcome : Screen("welcome")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object ForgotPassword : Screen("forgot_password")
    data object EmailSent : Screen("email_sent")
    data object ChangePassword : Screen("change_password")

    // Main Tabs (Bottom Navigation)
    data object Home : Screen("home")
    data object Courses : Screen("courses")
    data object Tasks : Screen("tasks")
    data object Schedule : Screen("schedule")
    data object Profile : Screen("profile")

    // Sub Screens / CRUD
    data object AddCourse : Screen("add_course")
    data object CourseDetail : Screen("course_detail/{courseId}") {
        fun createRoute(courseId: Int) = "course_detail/$courseId"
    }
    data object EditCourse : Screen("edit_course/{courseId}") {
        fun createRoute(courseId: Int) = "edit_course/$courseId"
    }

    data object AddTask : Screen("add_task")
    data object TaskDetail : Screen("task_detail/{taskId}") {
        fun createRoute(taskId: Int) = "task_detail/$taskId"
    }
    data object EditTaskDetail : Screen("edit_task_detail/{taskId}") {
        fun createRoute(taskId: Int) = "edit_task_detail/$taskId"
    }

    data object Settings : Screen("settings")
}