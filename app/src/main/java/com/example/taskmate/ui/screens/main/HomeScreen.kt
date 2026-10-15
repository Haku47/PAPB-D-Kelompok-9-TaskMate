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
import com.example.taskmate.data.DummyData
import com.example.taskmate.ui.components.ScheduleItemCard
import com.example.taskmate.ui.components.TaskItemCard
import com.example.taskmate.ui.theme.AppThemeColors

@Composable
fun HomeScreen(
    onNavigateToTaskDetail: (Int) -> Unit
) {
    // Ambil 3 jadwal
    val homeSchedules = DummyData.schedules.filter { it.day == "Tuesday" }

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
            Text(
                text = "Hello, Mad!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "You've got 5 active tasks!",
                fontSize = 13.sp,
                color = AppThemeColors.textSecondary
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Header Section Tasks + Divider
            Text(
                text = "Tasks",
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
        items(DummyData.tasks.take(3)) { task ->
            TaskItemCard(
                task = task,
                onClick = { onNavigateToTaskDetail(task.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(14.dp))

            // Header Section Your Schedule + Divider
            Text(
                text = "Your Schedule",
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