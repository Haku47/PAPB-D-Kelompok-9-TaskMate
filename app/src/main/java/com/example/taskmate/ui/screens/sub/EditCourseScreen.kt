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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.ui.components.AppAlertDialog
import com.example.taskmate.ui.components.DialogType
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.BorderColor
import com.example.taskmate.ui.theme.TextPrimary
import com.example.taskmate.ui.theme.White

@Composable
fun EditCourseScreen(
    courseId: Int,
    onNavigateBack: () -> Unit
) {
    var courseTitle by remember { mutableStateOf("PAPB D") }
    var lecturerName by remember { mutableStateOf("Dr. Eng. Herman Tolle, S.T., M.T.") }
    var day by remember { mutableStateOf("Tuesday") }
    var startTime by remember { mutableStateOf("09.35") }
    var endTime by remember { mutableStateOf("11.15") }
    var location by remember { mutableStateOf("Room RV1.3") }
    var showDialog by remember { mutableStateOf(false) }

    val fieldShape = RoundedCornerShape(10.dp)

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
        containerColor = White,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
                ) {
                    Text(
                        text = "Edit Course",
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

            // Header: Tombol Circle Back & Judul Screen
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    text = "Edit Course",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Course Title
            Text("Course Title", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = courseTitle,
                onValueChange = { courseTitle = it },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BlueNormal,
                    unfocusedBorderColor = BorderColor,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lecturer Name
            Text("Lecturer Name", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = lecturerName,
                onValueChange = { lecturerName = it },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BlueNormal,
                    unfocusedBorderColor = BorderColor,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Day
            Text("Day", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = day,
                onValueChange = { day = it },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BlueNormal,
                    unfocusedBorderColor = BorderColor,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Start Time & End Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Start Time", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = fieldShape,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueNormal,
                            unfocusedBorderColor = BorderColor,
                            focusedContainerColor = White,
                            unfocusedContainerColor = White
                        )
                    )
                }

                Text(
                    text = "-",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 36.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text("End Time", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = fieldShape,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueNormal,
                            unfocusedBorderColor = BorderColor,
                            focusedContainerColor = White,
                            unfocusedContainerColor = White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Location
            Text("Location", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BlueNormal,
                    unfocusedBorderColor = BorderColor,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Tombol "+ Another Session" (Biru Solid di Figma)
            Button(
                onClick = { /* Handle tambah sesi */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
            ) {
                Text(
                    text = "+ Another Session",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}