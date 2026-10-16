package com.example.taskmate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.White

enum class TimeFormatMode {
    AM,
    PM,
    TWENTY_FOUR_HOUR // Format 24 jam / tanpa AM-PM
}

object CourseTimeHelper {
    /**
     * Mendeteksi format jam dari string input secara otomatis.
     */
    fun detectMode(timeStr: String): TimeFormatMode {
        val lower = timeStr.lowercase()
        return when {
            lower.contains("am") -> TimeFormatMode.AM
            lower.contains("pm") -> TimeFormatMode.PM
            else -> TimeFormatMode.TWENTY_FOUR_HOUR
        }
    }

    /**
     * Mengubah atau menerapkan mode format (AM, PM, atau 24h / tanpa AM-PM).
     */
    fun setTimeMode(currentTimeStr: String, targetMode: TimeFormatMode): String {
        // Hapus teks AM/PM sebelumnya jika ada
        val cleanDigits = currentTimeStr
            .replace(Regex("(?i)\\s*(am|pm)"), "")
            .trim()
            .replace(".", ":")

        if (cleanDigits.isEmpty()) {
            return when (targetMode) {
                TimeFormatMode.AM -> "AM"
                TimeFormatMode.PM -> "PM"
                TimeFormatMode.TWENTY_FOUR_HOUR -> ""
            }
        }

        return when (targetMode) {
            TimeFormatMode.AM -> "$cleanDigits AM"
            TimeFormatMode.PM -> "$cleanDigits PM"
            TimeFormatMode.TWENTY_FOUR_HOUR -> cleanDigits
        }
    }

    /**
     * Memisahkan string rentang waktu (misal "09:35 AM - 11:15 AM" atau "09:35 - 11:15")
     * menjadi Pair(startTime, endTime).
     */
    fun parseTimeRange(timeRange: String): Pair<String, String> {
        val cleaned = if (timeRange.contains(",")) timeRange.substringAfter(",").trim() else timeRange.trim()
        return if (cleaned.contains("-")) {
            val start = cleaned.substringBefore("-").trim()
            val end = cleaned.substringAfter("-").trim()
            Pair(start, end)
        } else {
            Pair(cleaned, "")
        }
    }

    /**
     * Menggabungkan startTime dan endTime menjadi string rentang waktu yang rapi.
     */
    fun buildTimeRange(start: String, end: String): String {
        val s = start.trim()
        val e = end.trim()
        return when {
            s.isNotBlank() && e.isNotBlank() -> "$s - $e"
            s.isNotBlank() -> s
            else -> e
        }
    }
}

@Composable
fun CourseTimeInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val fieldShape = RoundedCornerShape(10.dp)
    val currentMode = CourseTimeHelper.detectMode(value)

    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.textPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = "09:35 AM / 09:35",
                    color = AppThemeColors.textSecondary,
                    fontSize = 12.sp
                )
            },
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

        Spacer(modifier = Modifier.height(6.dp))

        // Pilihan / Deteksi Mode: AM, PM, 24h
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TimeModeChip(
                text = "AM",
                isSelected = currentMode == TimeFormatMode.AM,
                onClick = {
                    onValueChange(CourseTimeHelper.setTimeMode(value, TimeFormatMode.AM))
                },
                modifier = Modifier.weight(1f)
            )

            TimeModeChip(
                text = "PM",
                isSelected = currentMode == TimeFormatMode.PM,
                onClick = {
                    onValueChange(CourseTimeHelper.setTimeMode(value, TimeFormatMode.PM))
                },
                modifier = Modifier.weight(1f)
            )

            TimeModeChip(
                text = "24h",
                isSelected = currentMode == TimeFormatMode.TWENTY_FOUR_HOUR,
                onClick = {
                    onValueChange(CourseTimeHelper.setTimeMode(value, TimeFormatMode.TWENTY_FOUR_HOUR))
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TimeModeChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chipShape = RoundedCornerShape(6.dp)
    val bgColor = if (isSelected) BlueNormal else AppThemeColors.searchBarContainer
    val textColor = if (isSelected) White else AppThemeColors.textSecondary
    val borderModifier = if (isSelected) {
        Modifier
    } else {
        Modifier.border(1.dp, AppThemeColors.border, chipShape)
    }

    Box(
        modifier = modifier
            .clip(chipShape)
            .then(borderModifier)
            .background(bgColor)
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

