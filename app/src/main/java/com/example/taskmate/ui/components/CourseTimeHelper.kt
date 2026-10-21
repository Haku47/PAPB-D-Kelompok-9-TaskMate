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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.White
import com.example.taskmate.ui.theme.strings

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
        val s = start.trim().replace(".", ":")
        val e = end.trim().replace(".", ":")
        return when {
            s.isNotBlank() && e.isNotBlank() -> "$s - $e"
            s.isNotBlank() -> s
            else -> e
        }
    }

    /**
     * Menormalisasi nama hari (ID atau EN) ke format standar ("Monday", "Tuesday", dsb).
     */
    fun normalizeDay(day: String): String {
        val cleaned = day.trim().lowercase()
        return when {
            cleaned in listOf("senin", "monday", "mon") -> "Monday"
            cleaned in listOf("selasa", "tuesday", "tue") -> "Tuesday"
            cleaned in listOf("rabu", "wednesday", "wed") -> "Wednesday"
            cleaned in listOf("kamis", "thursday", "thu") -> "Thursday"
            cleaned in listOf("jumat", "jum'at", "friday", "fri") -> "Friday"
            cleaned in listOf("sabtu", "saturday", "sat") -> "Saturday"
            cleaned in listOf("minggu", "ahad", "sunday", "sun") -> "Sunday"
            cleaned.isEmpty() -> ""
            else -> day.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }

    /**
     * Mem-parse string jam (misal "09:35 AM", "14:10", "09.35", atau "") ke Pair(hour 0-23, minute 0-59).
     */
    fun parseHourMinute(timeStr: String): Pair<Int, Int> {
        if (timeStr.isBlank()) return Pair(9, 0)
        val isPm = timeStr.lowercase().contains("pm")
        val isAm = timeStr.lowercase().contains("am")
        val clean = timeStr.replace(Regex("(?i)\\s*(am|pm)"), "").trim().replace(".", ":")
        val parts = clean.split(":")
        var hour = parts.getOrNull(0)?.trim()?.toIntOrNull() ?: 9
        val minute = parts.getOrNull(1)?.trim()?.toIntOrNull() ?: 0

        if (isPm) {
            if (hour in 1..11) hour += 12
        } else if (isAm) {
            if (hour == 12) hour = 0
        }
        return Pair(hour.coerceIn(0, 23), minute.coerceIn(0, 59))
    }

    /**
     * Format jam dan menit ke string "hh:mm AM/PM" (atau "HH:mm" jika 24-jam).
     */
    fun formatTime(hour: Int, minute: Int, is24Hour: Boolean = false): String {
        return if (is24Hour) {
            String.format(java.util.Locale.US, "%02d:%02d", hour, minute)
        } else {
            val isPm = hour >= 12
            val displayHour = when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }
            val period = if (isPm) "PM" else "AM"
            String.format(java.util.Locale.US, "%02d:%02d %s", displayHour, minute, period)
        }
    }
}

/**
 * Dialog Jam Interaktif (Clock / Time Picker) Material 3.
 * Menampilkan dial jarum jam analog interaktif dan pemilih AM/PM atau 24 jam.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerDialog(
    title: String,
    initialTime: String,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val s = strings
    val initialMode = remember(initialTime) { CourseTimeHelper.detectMode(initialTime) }
    var is24Hour by remember(initialTime) {
        mutableStateOf(initialMode == TimeFormatMode.TWENTY_FOUR_HOUR && initialTime.isNotBlank())
    }
    val (initHour, initMinute) = remember(initialTime) {
        CourseTimeHelper.parseHourMinute(initialTime)
    }

    var selectedHour by remember { mutableStateOf(initHour) }
    var selectedMinute by remember { mutableStateOf(initMinute) }

    val timePickerState = key(is24Hour) {
        rememberTimePickerState(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            is24Hour = is24Hour
        )
    }

    selectedHour = timePickerState.hour
    selectedMinute = timePickerState.minute

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val formatted = CourseTimeHelper.formatTime(
                        hour = timePickerState.hour,
                        minute = timePickerState.minute,
                        is24Hour = is24Hour
                    )
                    onTimeSelected(formatted)
                    onDismiss()
                }
            ) {
                Text(
                    text = s.dialogOk,
                    fontWeight = FontWeight.Bold,
                    color = BlueNormal,
                    fontSize = 15.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = s.cancelButton,
                    fontWeight = FontWeight.Medium,
                    color = AppThemeColors.textSecondary,
                    fontSize = 15.sp
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppThemeColors.textPrimary
                )

                // Tombol Switch Mode: 12 Jam (AM/PM) vs 24 Jam
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppThemeColors.searchBarContainer)
                        .border(1.dp, AppThemeColors.border, RoundedCornerShape(8.dp))
                        .clickable { is24Hour = !is24Hour }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (is24Hour) s.timeFormat24Hour else s.timeFormat12Hour,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BlueNormal
                    )
                }
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = AppThemeColors.searchBarContainer,
                        selectorColor = BlueNormal,
                        containerColor = AppThemeColors.cardBackground,
                        periodSelectorBorderColor = BlueNormal,
                        periodSelectorSelectedContainerColor = BlueNormal,
                        periodSelectorUnselectedContainerColor = AppThemeColors.searchBarContainer,
                        periodSelectorSelectedContentColor = White,
                        periodSelectorUnselectedContentColor = AppThemeColors.textPrimary,
                        timeSelectorSelectedContainerColor = BlueNormal.copy(alpha = 0.15f),
                        timeSelectorUnselectedContainerColor = AppThemeColors.searchBarContainer,
                        timeSelectorSelectedContentColor = BlueNormal,
                        timeSelectorUnselectedContentColor = AppThemeColors.textPrimary
                    )
                )
            }
        },
        containerColor = AppThemeColors.cardBackground,
        shape = RoundedCornerShape(20.dp)
    )
}

/**
 * Komponen Input Waktu Kursus/Sesi.
 * Menggantikan pengetikan manual dengan tempat jam (Clock Picker dialog interaktif).
 */
@Composable
fun CourseTimeInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val fieldShape = RoundedCornerShape(10.dp)
    var showTimePicker by remember { mutableStateOf(false) }

    if (showTimePicker) {
        AppTimePickerDialog(
            title = label,
            initialTime = value,
            onTimeSelected = { newTime ->
                onValueChange(newTime)
            },
            onDismiss = {
                showTimePicker = false
            }
        )
    }

    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.textPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text(
                        text = "09:35 AM",
                        color = AppThemeColors.textSecondary.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = label,
                        tint = AppThemeColors.textPrimary,
                        modifier = Modifier.size(20.dp)
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

            // Transparent overlay agar klik pada input field langsung membuka clock picker
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(fieldShape)
                    .clickable { showTimePicker = true }
            )
        }
    }
}
