package com.example.taskmate.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.data.DummyData
import com.example.taskmate.ui.components.ScheduleItemCard
import com.example.taskmate.ui.theme.AppThemeColors

import com.example.taskmate.ui.theme.strings

@Composable
fun ScheduleScreen() {
    val s = strings
    val daysOrder = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val groupedSchedules = DummyData.schedules.groupBy { it.day }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppThemeColors.background)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = s.classScheduleTitle,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.textPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 120.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            daysOrder.forEach { dayName ->
                val coursesForDay = groupedSchedules[dayName]
                if (!coursesForDay.isNullOrEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = s.translateDay(dayName),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppThemeColors.textPrimary,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }

                    items(coursesForDay) { course ->
                        ScheduleItemCard(course = course)
                    }
                }
            }
        }
    }
}