package com.example.taskmate.ui.screens.sub

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.data.CourseItem
import com.example.taskmate.data.CourseSession
import com.example.taskmate.data.DummyData
import com.example.taskmate.data.SessionInputState
import com.example.taskmate.data.toCourseSession
import com.example.taskmate.data.toInputState
import com.example.taskmate.ui.components.AppAlertDialog
import com.example.taskmate.ui.components.CourseTimeInputField
import com.example.taskmate.ui.components.DialogType
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.White
import com.example.taskmate.ui.theme.strings

@Composable
fun EditCourseScreen(
    courseId: Int,
    onNavigateBack: () -> Unit
) {
    val s = strings
    val focusManager = LocalFocusManager.current
    val course = DummyData.getCourseById(courseId)

    var courseTitle by remember(course) { mutableStateOf(course?.title ?: "") }
    var lecturerName by remember(course) { mutableStateOf(course?.lecturer ?: "") }

    val initialSessions = remember(course) {
        val courseSessions = course?.allSessions
        if (!courseSessions.isNullOrEmpty()) {
            courseSessions.map { it.toInputState() }
        } else {
            val times = course?.time ?: ""
            val parsed = com.example.taskmate.ui.components.CourseTimeHelper.parseTimeRange(times)
            listOf(
                SessionInputState(
                    day = course?.day ?: "",
                    startTime = parsed.first.ifBlank { "09:35 AM" },
                    endTime = parsed.second.ifBlank { "11:15 AM" },
                    room = course?.room ?: ""
                )
            )
        }
    }

    val sessions = remember(initialSessions) {
        mutableStateListOf<SessionInputState>().apply {
            addAll(initialSessions)
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    val fieldShape = RoundedCornerShape(10.dp)
    // 5 hari akademik wajib: Senin sampai Jumat
    val weekDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    if (showDialog) {
        AppAlertDialog(
            type = DialogType.CHANGES_SAVED,
            onDismiss = {
                showDialog = false
                onNavigateBack()
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = AppThemeColors.background,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppThemeColors.background)
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = {
                        val courseSessions = sessions.map { it.toCourseSession() }
                        val primarySession = courseSessions.firstOrNull() ?: CourseSession(
                            day = course?.day ?: "Monday",
                            time = course?.time ?: "08:00 AM - 10:00 AM",
                            room = course?.room ?: "Room -"
                        )
                        DummyData.updateCourse(
                            CourseItem(
                                id = courseId,
                                title = courseTitle.ifBlank { course?.title ?: "" },
                                day = primarySession.day.ifBlank { course?.day ?: "Monday" },
                                time = primarySession.time.ifBlank { course?.time ?: "08:00 AM - 10:00 AM" },
                                room = primarySession.room.ifBlank { course?.room ?: "Room -" },
                                lecturer = lecturerName.ifBlank { course?.lecturer ?: "" },
                                sessions = courseSessions
                            )
                        )
                        showDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
                ) {
                    Text(
                        text = s.saveChanges,
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
                .background(AppThemeColors.background)
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header: Tombol Circle Back & Judul Screen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
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
                    text = s.editCourseTitle,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppThemeColors.textPrimary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Course Title
            Text(
                text = s.courseTitleLabel,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = courseTitle,
                onValueChange = { courseTitle = it },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                placeholder = {
                    Text(
                        text = "e.g. PAPB D",
                        color = AppThemeColors.textSecondary.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BlueNormal,
                    unfocusedBorderColor = AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lecturer Name
            Text(
                text = s.lecturerNameLabel,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = lecturerName,
                onValueChange = { lecturerName = it },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                placeholder = {
                    Text(
                        text = "e.g. Dr. Eng. Herman Tolle, S.T., M.T.",
                        color = AppThemeColors.textSecondary.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BlueNormal,
                    unfocusedBorderColor = AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sessions List (mempertahankan form asli)
            sessions.forEachIndexed { index, sessionState ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = AppThemeColors.border
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                SessionFormFields(
                    index = index,
                    sessionState = sessionState,
                    totalSessions = sessions.size,
                    fieldShape = fieldShape,
                    weekDays = weekDays,
                    onUpdateSession = { updated -> sessions[index] = updated },
                    onRemoveSession = { sessions.removeAt(index) }
                )
            }

            // Tombol "+ Another Session" (bisa tambah sampai 5 sesi / 5 hari)
            if (sessions.size < 5) {
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val lastRoom = sessions.lastOrNull()?.room ?: ""
                        sessions.add(
                            SessionInputState(
                                day = "",
                                startTime = "09:35 AM",
                                endTime = "11:15 AM",
                                room = lastRoom
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
                ) {
                    Text(
                        text = s.anotherSessionButton,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SessionFormFields(
    index: Int,
    sessionState: SessionInputState,
    totalSessions: Int,
    fieldShape: RoundedCornerShape,
    weekDays: List<String>,
    onUpdateSession: (SessionInputState) -> Unit,
    onRemoveSession: () -> Unit
) {
    val s = strings
    var isDayDropdownOpen by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (totalSessions > 1) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = s.sessionHeader(index + 1),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppThemeColors.textPrimary
                )

                IconButton(
                    onClick = onRemoveSession,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = s.removeSession,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Day Dropdown (5 hari akademik: Senin sampai Jumat)
        Text(
            text = s.dayFieldLabel,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.textPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = sessionState.day,
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text(
                        text = "e.g. Tuesday / Selasa",
                        color = AppThemeColors.textSecondary.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select Day",
                        tint = AppThemeColors.textPrimary
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

            // Transparent overlay agar klik pada input field langsung membuka dropdown 5 hari
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(fieldShape)
                    .clickable { isDayDropdownOpen = true }
            )

            DropdownMenu(
                expanded = isDayDropdownOpen,
                onDismissRequest = { isDayDropdownOpen = false },
                modifier = Modifier
                    .background(AppThemeColors.cardBackground)
                    .fillMaxWidth(0.85f)
            ) {
                weekDays.forEach { dayName ->
                    val localizedDay = s.translateDay(dayName)
                    val isSelected = sessionState.day.equals(localizedDay, ignoreCase = true) ||
                            sessionState.day.equals(dayName, ignoreCase = true)
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = localizedDay,
                                color = if (isSelected) BlueNormal else AppThemeColors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            onUpdateSession(sessionState.copy(day = localizedDay))
                            isDayDropdownOpen = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Start Time & End Time
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CourseTimeInputField(
                label = s.startTimeLabel,
                value = sessionState.startTime,
                onValueChange = { onUpdateSession(sessionState.copy(startTime = it)) },
                modifier = Modifier.weight(1f)
            )

            CourseTimeInputField(
                label = s.endTimeLabel,
                value = sessionState.endTime,
                onValueChange = { onUpdateSession(sessionState.copy(endTime = it)) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location
        Text(
            text = s.locationLabel,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.textPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = sessionState.room,
            onValueChange = { onUpdateSession(sessionState.copy(room = it)) },
            modifier = Modifier.fillMaxWidth(),
            shape = fieldShape,
            singleLine = true,
            placeholder = {
                Text(
                    text = "e.g. Room RV1.3",
                    color = AppThemeColors.textSecondary.copy(alpha = 0.5f),
                    fontSize = 13.sp
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BlueNormal,
                unfocusedBorderColor = AppThemeColors.border,
                focusedTextColor = AppThemeColors.textPrimary,
                unfocusedTextColor = AppThemeColors.textPrimary,
                focusedContainerColor = AppThemeColors.searchBarContainer,
                unfocusedContainerColor = AppThemeColors.searchBarContainer
            )
        )
    }
}
