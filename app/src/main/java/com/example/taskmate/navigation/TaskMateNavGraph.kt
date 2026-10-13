package com.example.taskmate.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmate.ui.components.TaskMateBottomBar
import com.example.taskmate.ui.screens.auth.*
import com.example.taskmate.ui.screens.main.*
import com.example.taskmate.ui.screens.sub.*

@Composable
fun TaskMateNavGraph(navController: NavHostController) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            TaskMateBottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
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
                    onNavigateToTaskDetail = { _ ->
                        navController.navigate(Screen.TaskDetail.route)
                    }
                )
            }

            composable(Screen.Courses.route) {
                CourseListScreen(
                    onNavigateToAddCourse = { navController.navigate(Screen.AddCourse.route) },
                    onNavigateToCourseDetail = { _ ->
                        navController.navigate(Screen.CourseDetail.route)
                    }
                )
            }

            composable(Screen.Tasks.route) {
                TasksListScreen(
                    onNavigateToAddTask = { navController.navigate(Screen.AddTask.route) },
                    onNavigateToTaskDetail = { _ ->
                        navController.navigate(Screen.TaskDetail.route)
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
            composable(Screen.TaskDetail.route) {
                TaskDetailScreen(
                    taskId = 1,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEditTask = { _ ->
                        navController.navigate(Screen.EditTaskDetail.route)
                    }
                )
            }

            composable(Screen.AddTask.route) {
                AddTaskScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onTaskAdded = { navController.popBackStack() }
                )
            }

            composable(Screen.EditTaskDetail.route) {
                EditTaskDetailScreen(
                    taskId = 1,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.CourseDetail.route) {
                CourseDetailScreen(
                    courseId = 1,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEditCourse = { _ ->
                        navController.navigate(Screen.EditCourse.route)
                    }
                )
            }

            composable(Screen.AddCourse.route) {
                AddCourseScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onCourseAdded = { navController.popBackStack() }
                )
            }

            composable(Screen.EditCourse.route) {
                EditCourseScreen(
                    courseId = 1,
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
    }
}