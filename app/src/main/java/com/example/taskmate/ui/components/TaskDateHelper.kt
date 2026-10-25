package com.example.taskmate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.White
import com.example.taskmate.ui.theme.strings
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object TaskDateHelper {
    /**
     * Memformat epoch millis (UTC) menjadi string tanggal e.g. "Friday, Oct 16"
     */
    fun formatDate(millis: Long?, pattern: String = "EEEE, MMM d"): String {
        if (millis == null) return ""
        return try {
            val localDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
            val formatter = DateTimeFormatter.ofPattern(pattern, Locale.US)
            localDate.format(formatter)
        } catch (_: Exception) {
            ""
        }
    }

    /**
     * Memisahkan string dueDate lengkap (misal "Due Friday, Oct 16\n11:59 PM")
     * menjadi Pair(datePart, timePart), e.g. ("Friday, Oct 16", "11:59 PM").
     */
    fun parseDueDate(rawDueDate: String): Pair<String, String> {
        val cleaned = rawDueDate.replace(Regex("(?i)^due\\s*"), "").trim()
        return if (cleaned.contains("\n")) {
            val date = cleaned.substringBefore("\n").trim()
            val time = cleaned.substringAfter("\n").trim()
            Pair(date, time)
        } else if (cleaned.contains(" - ")) {
            val date = cleaned.substringBefore(" - ").trim()
            val time = cleaned.substringAfter(" - ").trim()
            Pair(date, time)
        } else if (cleaned.contains(",")) {
            val lastComma = cleaned.lastIndexOf(",")
            val date = cleaned.substring(0, lastComma).trim()
            val time = cleaned.substring(lastComma + 1).trim()
            Pair(date, time)
        } else {
            Pair(cleaned, "11:59 PM")
        }
    }

    /**
     * Menggabungkan tanggal dan waktu menjadi format dueDate standar Taskmate:
     * "Due Friday, Oct 16\n11:59 PM"
     */
    fun buildDueDate(datePart: String, timePart: String): String {
        val d = datePart.replace(Regex("(?i)^due\\s*"), "").trim()
        val t = timePart.trim()
        return if (t.isNotBlank()) {
            "Due $d\n$t"
        } else {
            "Due $d"
        }
    }
}

/**
 * Dialog Kalender Interaktif (Date Picker Dialog) Material 3.
 * Memudahkan pengguna memilih tanggal tanpa mengetik manual.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    initialDateMillis: Long? = System.currentTimeMillis()
) {
    val s = strings
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val formatted = TaskDateHelper.formatDate(datePickerState.selectedDateMillis)
                    if (formatted.isNotBlank()) {
                        onDateSelected(formatted)
                    }
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
        shape = RoundedCornerShape(24.dp),
        colors = DatePickerDefaults.colors(
            containerColor = AppThemeColors.cardBackground
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = AppThemeColors.cardBackground,
                titleContentColor = AppThemeColors.textPrimary,
                headlineContentColor = BlueNormal,
                weekdayContentColor = AppThemeColors.textSecondary,
                subheadContentColor = AppThemeColors.textPrimary,
                yearContentColor = AppThemeColors.textPrimary,
                currentYearContentColor = BlueNormal,
                selectedYearContentColor = White,
                selectedYearContainerColor = BlueNormal,
                dayContentColor = AppThemeColors.textPrimary,
                selectedDayContentColor = White,
                selectedDayContainerColor = BlueNormal,
                todayContentColor = BlueNormal,
                todayDateBorderColor = BlueNormal
            )
        )
    }
}

/**
 * Komponen Input Tanggal Tugas (Task Date Input Field).
 * Menampilkan kolom read-only dengan ikon kalender 📅.
 * Ketika diklik, langsung membuka kalender interaktif DatePickerDialog.
 */
@Composable
fun TaskDateInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val fieldShape = RoundedCornerShape(10.dp)
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        AppDatePickerDialog(
            onDateSelected = { newDate ->
                onValueChange(newDate)
            },
            onDismiss = {
                showDatePicker = false
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
                isError = isError,
                placeholder = {
                    Text(
                        text = "e.g. Friday, Oct 16",
                        color = AppThemeColors.textSecondary.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = label,
                        tint = AppThemeColors.textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isError) Color(0xFFE53935) else BlueNormal,
                    unfocusedBorderColor = if (isError) Color(0xFFE53935) else AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )

            // Transparent overlay agar klik pada input field langsung membuka interactive date picker kalender
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(fieldShape)
                    .clickable { showDatePicker = true }
            )
        }

        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = Color(0xFFE53935),
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 2.dp, top = 2.dp)
            )
        }
    }
}

