package com.example.taskmate.ui.screens.sub

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.ui.components.AppAlertDialog
import com.example.taskmate.ui.components.DialogType
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.White

@Composable
fun AddTaskScreen(
    onNavigateBack: () -> Unit,
    onTaskAdded: () -> Unit
) {
    var taskTitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCourse by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var isCourseDropdownOpen by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val courseOptions = listOf("PAPB D", "JST D", "Keamanan Jaringan A", "Komputer Grafis D")
    val fieldShape = RoundedCornerShape(10.dp)

    if (showDialog) {
        AppAlertDialog(
            type = DialogType.TASK_ADDED,
            onDismiss = {
                showDialog = false
                onTaskAdded()
            }
        )
    }

    Scaffold(
        containerColor = AppThemeColors.background,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppThemeColors.background)
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
                ) {
                    Text(
                        text = "Add Task",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppThemeColors.background)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header dengan Tombol Circle Back
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, AppThemeColors.textPrimary, CircleShape)
                        .clickable { onNavigateBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = AppThemeColors.textPrimary,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    )
                }

                Text(
                    text = "Add Task",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppThemeColors.textPrimary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Task Title
            Text("Task Title", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BlueNormal,
                    unfocusedBorderColor = AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description (Area input lebih tinggi sesuai Figma)
            Text("Description", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = fieldShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BlueNormal,
                    unfocusedBorderColor = AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Course Dropdown (Sesuai Figma: ada Chevron Down)
            Text("Course", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedCourse,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = AppThemeColors.textPrimary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isCourseDropdownOpen = true },
                    shape = fieldShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BlueNormal,
                        unfocusedBorderColor = AppThemeColors.border,
                        focusedTextColor = AppThemeColors.textPrimary,
                        unfocusedTextColor = AppThemeColors.textPrimary,
                        focusedContainerColor = AppThemeColors.searchBarContainer,
                        unfocusedContainerColor = AppThemeColors.searchBarContainer
                    )
                )

                DropdownMenu(
                    expanded = isCourseDropdownOpen,
                    onDismissRequest = { isCourseDropdownOpen = false },
                    modifier = Modifier.background(AppThemeColors.cardBackground)
                ) {
                    courseOptions.forEach { course ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = course,
                                    fontSize = 14.sp,
                                    color = AppThemeColors.textPrimary
                                )
                            },
                            onClick = {
                                selectedCourse = course
                                isCourseDropdownOpen = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Due Date
            Text("Due Date", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = dueDate,
                onValueChange = { dueDate = it },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BlueNormal,
                    unfocusedBorderColor = AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}