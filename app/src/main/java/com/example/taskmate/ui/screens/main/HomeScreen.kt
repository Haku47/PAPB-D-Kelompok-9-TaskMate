package com.example.taskmate.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.taskmate.data.DummyData
import com.example.taskmate.data.TaskItem
import com.example.taskmate.data.UserManager
import com.example.taskmate.ui.components.AppConfirmDialog
import com.example.taskmate.ui.components.ScheduleItemCard
import com.example.taskmate.ui.components.TaskItemCard
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.strings

@Composable
fun HomeScreen(
    onNavigateToTaskDetail: (Int) -> Unit
) {
    val s = strings
    val haptic = LocalHapticFeedback.current
    var taskToDelete by remember { mutableStateOf<TaskItem?>(null) }

    // Ambil 3 jadwal
    val homeSchedules = DummyData.schedules.filter { it.day == "Tuesday" }.ifEmpty { DummyData.schedules.take(3) }

    taskToDelete?.let { task ->
        AppConfirmDialog(
            title = s.deleteTaskConfirmTitle,
            message = s.deleteTaskConfirmMessage(task.title),
            confirmText = s.deleteButton,
            cancelText = s.cancelButton,
            onConfirm = {
                DummyData.deleteTask(task.id)
                taskToDelete = null
            },
            onDismiss = {
                taskToDelete = null
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppThemeColors.background)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            val user = UserManager.currentUser
            val greetingName = user?.fullName?.split(" ")?.firstOrNull()?.ifBlank { user.username } ?: user?.username ?: "Mad"
            Text(
                text = s.greetingWithName(greetingName),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = s.activeTasksNotice(DummyData.tasks.count { it.status != "Finished" }),
                fontSize = 13.sp,
                color = AppThemeColors.textSecondary
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Header Section Tasks + Divider
            Text(
                text = s.sectionTasks,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = AppThemeColors.border
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // 3 Task teratas
        items(
            items = DummyData.tasks.take(3),
            key = { it.id }
        ) { task ->
            TaskItemCard(
                task = task,
                onClick = { onNavigateToTaskDetail(task.id) },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    taskToDelete = task
                },
                modifier = Modifier.animateItem()
            )
        }

        item {
            Spacer(modifier = Modifier.height(14.dp))

            // Header Section Your Schedule + Divider
            Text(
                text = s.sectionSchedule,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = AppThemeColors.border
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // 3 Kartu Jadwal
        items(homeSchedules) { schedule ->
            ScheduleItemCard(course = schedule)
        }
    }
}
