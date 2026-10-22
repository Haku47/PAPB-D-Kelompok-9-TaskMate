package com.example.taskmate.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.taskmate.data.DummyData
import com.example.taskmate.data.TaskItem
import com.example.taskmate.ui.components.AppConfirmDialog
import com.example.taskmate.ui.components.TaskItemCard
import com.example.taskmate.ui.theme.AppThemeColors

import com.example.taskmate.ui.theme.strings

@Composable
fun TasksListScreen(
    onNavigateToAddTask: () -> Unit,
    onNavigateToTaskDetail: (Int) -> Unit
) {
    val s = strings
    val haptic = LocalHapticFeedback.current
    var searchQuery by remember { mutableStateOf("") }
    var taskToDelete by remember { mutableStateOf<TaskItem?>(null) }

    val filteredTasks = DummyData.tasks.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.course.contains(searchQuery, ignoreCase = true)
    }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppThemeColors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = s.tasksListTitle,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = s.searchTaskPlaceholder,
                        fontSize = 13.sp,
                        color = AppThemeColors.textSecondary
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = AppThemeColors.textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppThemeColors.border,
                    unfocusedBorderColor = AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 120.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = filteredTasks,
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
            }
        }

        FloatingActionButton(
            onClick = onNavigateToAddTask,
            shape = CircleShape,
            containerColor = AppThemeColors.fabBackground,
            contentColor = AppThemeColors.fabContentColor,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 6.dp,
                pressedElevation = 2.dp
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 96.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = s.addTaskFab,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}