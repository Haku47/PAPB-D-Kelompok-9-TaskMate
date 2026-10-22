package com.example.taskmate.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.data.TaskItem
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.BlueLight
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.ThemeState

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import com.example.taskmate.ui.theme.strings

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItemCard(
    task: TaskItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null
) {
    val s = strings
    // Badge
    val badgeBg = if (ThemeState.isDarkMode) Color(0xFF1E3A5F) else BlueLight
    val badgeTextColor = if (ThemeState.isDarkMode) Color(0xFF93C5FD) else BlueNormal

    val clickModifier = if (onLongClick != null) {
        Modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        )
    } else {
        Modifier.clickable { onClick() }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(clickModifier)
            .border(1.dp, AppThemeColors.border, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppThemeColors.cardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Description,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AppThemeColors.textPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = s.taskBadge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppThemeColors.textPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeBg
                ) {
                    Text(
                        text = s.formatDaysLeft(task.daysLeft),
                        fontSize = 10.sp,
                        color = badgeTextColor,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppThemeColors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = task.course,
                        fontSize = 11.sp,
                        color = AppThemeColors.textSecondary
                    )
                }

                Row(modifier = Modifier.padding(top = 10.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = AppThemeColors.textSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = task.dueDate,
                        fontSize = 10.sp,
                        color = AppThemeColors.textSecondary,
                        lineHeight = 12.sp
                    )
                }
            }
        }
    }
}