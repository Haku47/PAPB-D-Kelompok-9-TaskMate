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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
 * Dialog Jam Interaktif Sesuai Saran Umar:
 * Sisi A: Mode Dial (Jarum Jam analog interaktif)
 * Sisi B: Mode Keyboard (Ketik Manual jam & menit dengan mudah)
 * Fitur:
 * - Segmented toggle 12-Hour (AM/PM) vs 24-Hour
 * - Tombol beralih di pojok kiri bawah (Ikon Keyboard / Ikon Jam)
 * - Tombol Cancel & OK
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerDialog(
    title: String,
    initialTime: String,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    subtitle: String? = null
) {
    val s = strings
    val initialMode = remember(initialTime) { CourseTimeHelper.detectMode(initialTime) }
    var is24Hour by remember(initialTime) {
        mutableStateOf(initialMode == TimeFormatMode.TWENTY_FOUR_HOUR && initialTime.isNotBlank())
    }
    // false = Mode Dial (Jarum Jam), true = Mode Keyboard (Ketik Manual)
    var isKeyboardMode by remember { mutableStateOf(false) }

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

    val timePickerColors = TimePickerDefaults.colors(
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

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(AppThemeColors.cardBackground)
                .border(1.dp, AppThemeColors.border, RoundedCornerShape(24.dp))
                .padding(horizontal = 20.dp, vertical = 22.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Bar: Title & Subtitle di Kiri, Segmented Toggle 12h/24h di Kanan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f, fill = false)) {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppThemeColors.textPrimary
                        )
                        val subText = subtitle ?: s.timePickerSubtitle
                        if (subText.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = subText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = AppThemeColors.textSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Segmented Control: [ 12-Hour (AM/PM) | 24-Hour ]
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(AppThemeColors.searchBarContainer)
                            .border(1.dp, AppThemeColors.border, RoundedCornerShape(20.dp))
                            .padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 12-Hour Segment
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (!is24Hour) BlueNormal.copy(alpha = 0.2f) else Color.Transparent
                                )
                                .clickable { is24Hour = false }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = s.timeFormat12Hour,
                                fontSize = 11.sp,
                                fontWeight = if (!is24Hour) FontWeight.Bold else FontWeight.Medium,
                                color = if (!is24Hour) BlueNormal else AppThemeColors.textSecondary
                            )
                        }

                        // 24-Hour Segment
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (is24Hour) BlueNormal.copy(alpha = 0.2f) else Color.Transparent
                                )
                                .clickable { is24Hour = true }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = s.timeFormat24Hour,
                                fontSize = 11.sp,
                                fontWeight = if (is24Hour) FontWeight.Bold else FontWeight.Medium,
                                color = if (is24Hour) BlueNormal else AppThemeColors.textSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Konten Jam: Mode A (TimePicker Dial) atau Mode B (TimeInput Manual)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = if (isKeyboardMode) 28.dp else 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isKeyboardMode) {
                        TimePicker(
                            state = timePickerState,
                            colors = timePickerColors
                        )
                    } else {
                        TimeInput(
                            state = timePickerState,
                            colors = timePickerColors
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bar Bawah: Tombol Ganti Mode (Kiri) & Tombol Cancel/OK (Kanan)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tombol Switch Mode: Ikon Keyboard (ke ketik manual) atau Ikon Jam (kembali ke dial)
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(BlueNormal)
                            .clickable { isKeyboardMode = !isKeyboardMode },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (!isKeyboardMode) Icons.Outlined.Keyboard else Icons.Outlined.Schedule,
                            contentDescription = if (!isKeyboardMode) s.switchToKeyboardMode else s.switchToDialMode,
                            tint = White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Tombol Aksi: Cancel & OK
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(AppThemeColors.searchBarContainer)
                                .border(1.dp, AppThemeColors.border, RoundedCornerShape(10.dp))
                                .clickable { onDismiss() }
                                .padding(horizontal = 18.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = s.cancelButton,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AppThemeColors.textPrimary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(BlueNormal)
                                .clickable {
                                    val formatted = CourseTimeHelper.formatTime(
                                        hour = timePickerState.hour,
                                        minute = timePickerState.minute,
                                        is24Hour = is24Hour
                                    )
                                    onTimeSelected(formatted)
                                    onDismiss()
                                }
                                .padding(horizontal = 22.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = s.dialogOk,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        }
                    }
                }
            }
        }
    }
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
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    val fieldShape = RoundedCornerShape(10.dp)
    var showTimePicker by remember { mutableStateOf(false) }

    if (showTimePicker) {
        AppTimePickerDialog(
            title = label,
            subtitle = subtitle,
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
