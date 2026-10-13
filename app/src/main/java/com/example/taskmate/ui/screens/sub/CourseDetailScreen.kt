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
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.data.DummyData
import com.example.taskmate.ui.theme.BorderColor
import com.example.taskmate.ui.theme.TextPrimary
import com.example.taskmate.ui.theme.TextSecondary
import com.example.taskmate.ui.theme.White

@Composable
fun CourseDetailScreen(
    courseId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToEditCourse: (Int) -> Unit
) {
    val course = DummyData.courses.find { it.id == courseId } ?: DummyData.courses.first()

    Scaffold(
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
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
                            .border(1.5.dp, TextPrimary, CircleShape)
                            .clickable { onNavigateBack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = TextPrimary,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                        )
                    }

                    Text(
                        text = "Course Detail",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                IconButton(
                    onClick = { onNavigateToEditCourse(courseId) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit Course",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Course Name
            CourseDetailItem(
                label = "Course Name",
                value = course.title
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Lecturer Name
            CourseDetailItem(
                label = "Lecturer Name",
                value = course.lecturer
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Schedule Information
            CourseDetailItem(
                label = "Schedule Information",
                value = "${course.day}, ${course.time.substringAfter(", ")}\n${course.room}"
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
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = BorderColor
        )
    }
}