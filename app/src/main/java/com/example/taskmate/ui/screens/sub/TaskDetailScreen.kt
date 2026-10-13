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
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.TextPrimary
import com.example.taskmate.ui.theme.TextSecondary
import com.example.taskmate.ui.theme.White

@Composable
fun TaskDetailScreen(
    taskId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToEditTask: (Int) -> Unit
) {
    // Cari data task berdasarkan ID atau fallback ke task pertama
    val task = DummyData.tasks.find { it.id == taskId } ?: DummyData.tasks.first()

    Scaffold(
        containerColor = White,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
                ) {
                    Text(
                        text = "Mark as Finished",
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
                .background(White)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header: Circle Back Button, Judul Layar, & Edit Pencil Icon
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
                        text = "Task Detail",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                IconButton(
                    onClick = { onNavigateToEditTask(taskId) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit Task",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Task Title
            DetailItem(
                label = "Task Title",
                value = task.title
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Course
            DetailItem(
                label = "Course",
                value = task.course
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Due Date (Sesuai Figma: Friday, Oct 16 - 11:59 PM)
            DetailItem(
                label = "Due Date",
                value = "Friday, Oct 16 - 11:59 PM"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Task Status
            DetailItem(
                label = "Task Status",
                value = task.status
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Description
            DetailItem(
                label = "Description",
                value = task.description
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            lineHeight = 22.sp
        )
    }
}