package com.example.taskmate.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.taskmate.ui.components.TaskMateBottomBar
import com.example.taskmate.ui.screens.auth.*
import com.example.taskmate.ui.screens.main.*
import com.example.taskmate.ui.screens.sub.*
import com.example.taskmate.ui.theme.AppLanguage
import com.example.taskmate.ui.theme.FontSizeMode
import com.example.taskmate.ui.theme.ThemeState

@Composable
fun TaskMateNavGraph(
    navController: NavHostController,
    fontSizeMode: FontSizeMode = ThemeState.fontSizeMode,
    language: AppLanguage = ThemeState.language
) {
    // Explicit read to register recomposition when font size mode or language changes
    val activeFontSizeMode = fontSizeMode
    val activeLanguage = language
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Daftar rute layar utama (bottom navigation bar)
    val mainTabs = listOf(
        Screen.Home.route,
        Screen.Courses.route,
        Screen.Tasks.route,
        Screen.Schedule.route,
        Screen.Profile.route
    )
    val shouldShowBottomBar = currentRoute in mainTabs

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding() // Status Bar
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.fillMaxSize()
        ) {
            // --- 1. AUTH FLOW ---
            composable(Screen.Splash.route) {
                SplashScreen(
                    onTimeout = {
                        navController.navigate(Screen.Welcome.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Welcome.route) {
                WelcomeScreen(
                    onContinue = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                    onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) }
                )
            }

            composable(Screen.SignUp.route) {
                SignUpScreen(
                    onSignUpSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SignUp.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onSendEmail = { navController.navigate(Screen.EmailSent.route) },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.EmailSent.route) {
                EmailSentScreen(
                    onBackToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.ChangePassword.route) {
                ChangePasswordScreen(
                    onChangeSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.ChangePassword.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // --- 2. MAIN TABS (BOTTOM NAVIGATION) ---
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToTaskDetail = { taskId ->
                        navController.navigate(Screen.TaskDetail.createRoute(taskId))
                    }
                )
            }

            composable(Screen.Courses.route) {
                CourseListScreen(
                    onNavigateToAddCourse = { navController.navigate(Screen.AddCourse.route) },
                    onNavigateToCourseDetail = { courseId ->
                        navController.navigate(Screen.CourseDetail.createRoute(courseId))
                    }
                )
            }

            composable(Screen.Tasks.route) {
                TasksListScreen(
                    onNavigateToAddTask = { navController.navigate(Screen.AddTask.route) },
                    onNavigateToTaskDetail = { taskId ->
                        navController.navigate(Screen.TaskDetail.createRoute(taskId))
                    }
                )
            }

            composable(Screen.Schedule.route) {
                ScheduleScreen()
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                )
            }

            // --- 3. SUB SCREENS (CRUD & DETAIL) ---
            composable(
                route = Screen.TaskDetail.route,
                arguments = listOf(navArgument("taskId") { type = NavType.IntType; defaultValue = 1 })
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getInt("taskId") ?: 1
                TaskDetailScreen(
                    taskId = taskId,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEditTask = { id ->
                        navController.navigate(Screen.EditTaskDetail.createRoute(id))
                    }
                )
            }

            composable(Screen.AddTask.route) {
                AddTaskScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onTaskAdded = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.EditTaskDetail.route,
                arguments = listOf(navArgument("taskId") { type = NavType.IntType; defaultValue = 1 })
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getInt("taskId") ?: 1
                EditTaskDetailScreen(
                    taskId = taskId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.CourseDetail.route,
                arguments = listOf(navArgument("courseId") { type = NavType.IntType; defaultValue = 1 })
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getInt("courseId") ?: 1
                CourseDetailScreen(
                    courseId = courseId,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEditCourse = { id ->
                        navController.navigate(Screen.EditCourse.createRoute(id))
                    }
                )
            }

            composable(Screen.AddCourse.route) {
                AddCourseScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onCourseAdded = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.EditCourse.route,
                arguments = listOf(navArgument("courseId") { type = NavType.IntType; defaultValue = 1 })
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getInt("courseId") ?: 1
                EditCourseScreen(
                    courseId = courseId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Floating Navbar
        if (shouldShowBottomBar) {
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                TaskMateBottomBar(navController = navController)
            }
        }
    }
}