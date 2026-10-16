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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.data.DummyData
import com.example.taskmate.ui.components.AppConfirmDialog
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.strings

@Composable
fun CourseDetailScreen(
    courseId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToEditCourse: (Int) -> Unit
) {
    val s = strings
    val course = DummyData.getCourseById(courseId) ?: DummyData.courses.firstOrNull() ?: return
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AppConfirmDialog(
            title = s.deleteCourseConfirmTitle,
            message = s.deleteCourseConfirmMessage(course.title),
            confirmText = s.deleteButton,
            cancelText = s.cancelButton,
            onConfirm = {
                showDeleteDialog = false
                DummyData.deleteCourse(course.id)
                onNavigateBack()
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }

    Scaffold(
        containerColor = AppThemeColors.background
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

            // Header: Circle Back Button, Judul Screen, dan Icon Edit Pensil
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(
                                width = 1.5.dp,
                                color = AppThemeColors.textPrimary,
                                shape = CircleShape
                            )
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
                        text = s.courseDetailTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppThemeColors.textPrimary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete Course",
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(
                        onClick = { onNavigateToEditCourse(courseId) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Course",
                            tint = AppThemeColors.textPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Course Name
            CourseDetailItem(
                label = s.courseNameLabel,
                value = course.title
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Lecturer Name
            CourseDetailItem(
                label = s.lecturerNameLabel,
                value = course.lecturer
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Schedule Information
            val displayTime = if (course.time.contains(",")) course.time.substringAfter(",").trim() else course.time.trim()
            CourseDetailItem(
                label = s.scheduleInfoLabel,
                value = "${s.translateDay(course.day)}, $displayTime\n${course.room}"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CourseDetailItem(
    label: String,
    value: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.textPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = AppThemeColors.textSecondary,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = AppThemeColors.border
        )
    }
}