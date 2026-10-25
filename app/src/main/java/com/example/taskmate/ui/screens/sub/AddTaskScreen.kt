package com.example.taskmate.ui.screens.sub

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmate.data.DummyData
import com.example.taskmate.ui.components.AppAlertDialog
import com.example.taskmate.ui.components.CourseTimeInputField
import com.example.taskmate.ui.components.DialogType
import com.example.taskmate.ui.components.TaskDateInputField
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.White
import com.example.taskmate.ui.theme.strings
import com.example.taskmate.ui.viewmodel.AddTaskViewModel

@Composable
fun AddTaskScreen(
    onNavigateBack: () -> Unit,
    onTaskAdded: () -> Unit,
    viewModel: AddTaskViewModel = viewModel()
) {
    val s = strings
    val focusManager = LocalFocusManager.current
    val courseOptions = remember {
        val dynamicCourses = (listOf("Course A") + DummyData.courses.map { it.title } + DummyData.schedules.map { it.title }).distinct()
        if (dynamicCourses.isNotEmpty()) dynamicCourses else listOf("Course A", "PAPB D", "JST D", "Keamanan Jaringan A")
    }

    var isCourseDropdownOpen by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

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
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = AppThemeColors.background,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppThemeColors.background)
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.submitTask(
                            errTitleReq = s.errorTaskTitleRequired,
                            errDescReq = s.errorDescriptionRequired,
                            errCourseReq = s.errorCourseRequired,
                            errDateReq = s.errorDueDateRequired,
                            errTimeReq = s.errorDueTimeRequired,
                            onSuccess = {
                                showDialog = true
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
                ) {
                    Text(
                        text = s.addTaskTitle,
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
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header: Tombol Lingkaran Kembali & Judul Layar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AppThemeColors.searchBarContainer)
                        .clickable { onNavigateBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = AppThemeColors.textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = s.addTaskTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppThemeColors.textPrimary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Task Title (Mekanisme Validasi ViewModel)
            Text(s.taskTitleLabel, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = viewModel.taskTitle,
                onValueChange = { viewModel.onTaskTitleChange(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                isError = viewModel.taskTitleError != null,
                placeholder = {
                    Text(
                        text = "e.g. Assignment 1: abcd",
                        color = AppThemeColors.textSecondary.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (viewModel.taskTitleError != null) Color(0xFFE53935) else BlueNormal,
                    unfocusedBorderColor = if (viewModel.taskTitleError != null) Color(0xFFE53935) else AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )
            if (viewModel.taskTitleError != null) {
                Text(
                    text = viewModel.taskTitleError!!,
                    color = Color(0xFFE53935),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Description (Mekanisme Validasi ViewModel)
            Text(s.descriptionLabel, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = fieldShape,
                isError = viewModel.descriptionError != null,
                placeholder = {
                    Text(
                        text = "e.g. Selesaikan latihan bab 4 dan kumpulkan sebelum batas waktu.",
                        color = AppThemeColors.textSecondary.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (viewModel.descriptionError != null) Color(0xFFE53935) else BlueNormal,
                    unfocusedBorderColor = if (viewModel.descriptionError != null) Color(0xFFE53935) else AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )
            if (viewModel.descriptionError != null) {
                Text(
                    text = viewModel.descriptionError!!,
                    color = Color(0xFFE53935),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Course Dropdown
            Text(s.courseLabel, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = viewModel.selectedCourse,
                    onValueChange = {},
                    readOnly = true,
                    isError = viewModel.courseError != null,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = AppThemeColors.textPrimary
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = fieldShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (viewModel.courseError != null) Color(0xFFE53935) else BlueNormal,
                        unfocusedBorderColor = if (viewModel.courseError != null) Color(0xFFE53935) else AppThemeColors.border,
                        focusedTextColor = AppThemeColors.textPrimary,
                        unfocusedTextColor = AppThemeColors.textPrimary,
                        focusedContainerColor = AppThemeColors.searchBarContainer,
                        unfocusedContainerColor = AppThemeColors.searchBarContainer
                    )
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(fieldShape)
                        .clickable { isCourseDropdownOpen = true }
                )

                DropdownMenu(
                    expanded = isCourseDropdownOpen,
                    onDismissRequest = { isCourseDropdownOpen = false },
                    modifier = Modifier
                        .background(AppThemeColors.cardBackground)
                        .fillMaxWidth(0.85f)
                ) {
                    courseOptions.forEach { course ->
                        val isSelected = viewModel.selectedCourse.equals(course, ignoreCase = true)
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = course,
                                    color = if (isSelected) BlueNormal else AppThemeColors.textPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                viewModel.onCourseChange(course)
                                isCourseDropdownOpen = false
                            }
                        )
                    }
                }
            }
            if (viewModel.courseError != null) {
                Text(
                    text = viewModel.courseError!!,
                    color = Color(0xFFE53935),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Penggabungan Data: Tanggal Tenggat (Kalender) + Jam Tenggat (Tempat Jam)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tanggal Tenggat (Kalender DatePicker Interaktif)
                TaskDateInputField(
                    label = s.dueDateLabel,
                    value = viewModel.dueDatePart,
                    onValueChange = { viewModel.onDueDateChange(it) },
                    isError = viewModel.dueDateError != null,
                    errorMessage = viewModel.dueDateError,
                    modifier = Modifier.weight(1f)
                )

                // Jam Tenggat (Clock Picker Interaktif)
                Column(modifier = Modifier.weight(1f)) {
                    CourseTimeInputField(
                        label = s.dueTimeLabel,
                        value = viewModel.dueTimePart,
                        onValueChange = { viewModel.onDueTimeChange(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (viewModel.dueTimeError != null) {
                        Text(
                            text = viewModel.dueTimeError!!,
                            color = Color(0xFFE53935),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(start = 2.dp, top = 2.dp)
                        )
                    }
                }
            }

            // Preview Hasil Pengolahan Gabungan Dua Input (Tanggal & Waktu)
            if (viewModel.dueDatePart.isNotBlank() && viewModel.dueTimePart.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = s.combinedDueDatePreview("${viewModel.dueDatePart} - ${viewModel.dueTimePart}"),
                    color = BlueNormal,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
