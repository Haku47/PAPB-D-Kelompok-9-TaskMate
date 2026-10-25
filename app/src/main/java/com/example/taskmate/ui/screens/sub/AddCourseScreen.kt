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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmate.data.SessionInputState
import com.example.taskmate.ui.components.AppAlertDialog
import com.example.taskmate.ui.components.CourseTimeInputField
import com.example.taskmate.ui.components.DialogType
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.White
import com.example.taskmate.ui.theme.strings
import com.example.taskmate.ui.viewmodel.AddCourseViewModel

@Composable
fun AddCourseScreen(
    onNavigateBack: () -> Unit,
    onCourseAdded: () -> Unit,
    viewModel: AddCourseViewModel = viewModel()
) {
    val s = strings
    val focusManager = LocalFocusManager.current
    var showDialog by remember { mutableStateOf(false) }

    val fieldShape = RoundedCornerShape(10.dp)
    // 5 hari akademik wajib: Senin sampai Jumat
    val weekDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    if (showDialog) {
        AppAlertDialog(
            type = DialogType.COURSE_ADDED,
            onDismiss = {
                showDialog = false
                onCourseAdded()
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
                        focusManager.clearFocus()
                        viewModel.submitCourse(
                            errCourseTitleReq = s.errorCourseTitleRequired,
                            errLecturerFirstReq = s.errorLecturerFirstNameRequired,
                            errLecturerLastReq = s.errorLecturerLastNameRequired,
                            errDayReq = s.errorDayRequired,
                            errRoomReq = s.errorRoomRequired,
                            onSuccess = {
                                showDialog = true
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
                ) {
                    Text(
                        text = s.addCourseTitle,
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

            // Header: Tombol Lingkaran Kembali & Judul Layar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AppThemeColors.searchBarContainer)
                        .clickable { onNavigateBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = AppThemeColors.textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = s.addCourseTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppThemeColors.textPrimary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Course Title (dengan mekanisme validasi ViewModel)
            Text(s.courseTitleLabel, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = viewModel.courseTitle,
                onValueChange = { viewModel.onCourseTitleChange(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                isError = viewModel.courseTitleError != null,
                placeholder = {
                    Text(
                        text = "e.g. PAPB D",
                        color = AppThemeColors.textSecondary.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (viewModel.courseTitleError != null) Color(0xFFE53935) else BlueNormal,
                    unfocusedBorderColor = if (viewModel.courseTitleError != null) Color(0xFFE53935) else AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )
            if (viewModel.courseTitleError != null) {
                Text(
                    text = viewModel.courseTitleError!!,
                    color = Color(0xFFE53935),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Lecturer Name: Penggabungan data dari dua input (Nama Depan & Nama Belakang)
            Text(s.lecturerNameLabel, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Nama Depan Dosen
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = viewModel.lecturerFirstName,
                        onValueChange = { viewModel.onLecturerFirstNameChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = fieldShape,
                        singleLine = true,
                        isError = viewModel.lecturerFirstNameError != null,
                        placeholder = {
                            Text(
                                text = "e.g. Herman",
                                color = AppThemeColors.textSecondary.copy(alpha = 0.5f),
                                fontSize = 12.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (viewModel.lecturerFirstNameError != null) Color(0xFFE53935) else BlueNormal,
                            unfocusedBorderColor = if (viewModel.lecturerFirstNameError != null) Color(0xFFE53935) else AppThemeColors.border,
                            focusedTextColor = AppThemeColors.textPrimary,
                            unfocusedTextColor = AppThemeColors.textPrimary,
                            focusedContainerColor = AppThemeColors.searchBarContainer,
                            unfocusedContainerColor = AppThemeColors.searchBarContainer
                        )
                    )
                    if (viewModel.lecturerFirstNameError != null) {
                        Text(
                            text = viewModel.lecturerFirstNameError!!,
                            color = Color(0xFFE53935),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(start = 2.dp, top = 2.dp)
                        )
                    }
                }

                // Nama Belakang / Gelar Dosen
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = viewModel.lecturerLastName,
                        onValueChange = { viewModel.onLecturerLastNameChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = fieldShape,
                        singleLine = true,
                        isError = viewModel.lecturerLastNameError != null,
                        placeholder = {
                            Text(
                                text = "e.g. Tolle, S.T., M.T.",
                                color = AppThemeColors.textSecondary.copy(alpha = 0.5f),
                                fontSize = 12.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (viewModel.lecturerLastNameError != null) Color(0xFFE53935) else BlueNormal,
                            unfocusedBorderColor = if (viewModel.lecturerLastNameError != null) Color(0xFFE53935) else AppThemeColors.border,
                            focusedTextColor = AppThemeColors.textPrimary,
                            unfocusedTextColor = AppThemeColors.textPrimary,
                            focusedContainerColor = AppThemeColors.searchBarContainer,
                            unfocusedContainerColor = AppThemeColors.searchBarContainer
                        )
                    )
                    if (viewModel.lecturerLastNameError != null) {
                        Text(
                            text = viewModel.lecturerLastNameError!!,
                            color = Color(0xFFE53935),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(start = 2.dp, top = 2.dp)
                        )
                    }
                }
            }

            // Preview hasil pengolahan gabungan dua input (Nama Depan + Belakang)
            if (viewModel.fullLecturerName.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = s.combinedLecturerPreview(viewModel.fullLecturerName),
                    color = BlueNormal,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Sessions List
            viewModel.sessions.forEachIndexed { index, sessionState ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = AppThemeColors.border
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                val errState = viewModel.sessionErrors.getOrNull(index)

                SessionFormFields(
                    index = index,
                    sessionState = sessionState,
                    totalSessions = viewModel.sessions.size,
                    fieldShape = fieldShape,
                    weekDays = weekDays,
                    dayError = errState?.dayError,
                    roomError = errState?.roomError,
                    onUpdateSession = { updated -> viewModel.onUpdateSession(index, updated) },
                    onRemoveSession = { viewModel.removeSession(index) }
                )
            }

            // Tombol "+ Another Session" (bisa tambah sampai 5 sesi / 5 hari)
            if (viewModel.sessions.size < 5) {
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.addSession()
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
    dayError: String? = null,
    roomError: String? = null,
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
        Text(s.dayFieldLabel, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = sessionState.day,
                onValueChange = {},
                readOnly = true,
                isError = dayError != null,
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
                    focusedBorderColor = if (dayError != null) Color(0xFFE53935) else BlueNormal,
                    unfocusedBorderColor = if (dayError != null) Color(0xFFE53935) else AppThemeColors.border,
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
        if (dayError != null) {
            Text(
                text = dayError,
                color = Color(0xFFE53935),
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Start Time & End Time (Interactive Clock Picker)
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

        // Preview hasil olahan gabungan waktu (Start Time - End Time)
        val combinedTime = "${sessionState.startTime} - ${sessionState.endTime}".trim()
        if (sessionState.startTime.isNotBlank() && sessionState.endTime.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = s.combinedTimeRangePreview(combinedTime),
                color = BlueNormal,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location / Room
        Text(s.locationLabel, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = sessionState.room,
            onValueChange = { onUpdateSession(sessionState.copy(room = it)) },
            modifier = Modifier.fillMaxWidth(),
            shape = fieldShape,
            singleLine = true,
            isError = roomError != null,
            placeholder = {
                Text(
                    text = "e.g. Room RV1.3",
                    color = AppThemeColors.textSecondary.copy(alpha = 0.5f),
                    fontSize = 13.sp
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (roomError != null) Color(0xFFE53935) else BlueNormal,
                unfocusedBorderColor = if (roomError != null) Color(0xFFE53935) else AppThemeColors.border,
                focusedTextColor = AppThemeColors.textPrimary,
                unfocusedTextColor = AppThemeColors.textPrimary,
                focusedContainerColor = AppThemeColors.searchBarContainer,
                unfocusedContainerColor = AppThemeColors.searchBarContainer
            )
        )
        if (roomError != null) {
            Text(
                text = roomError,
                color = Color(0xFFE53935),
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }
    }
}
