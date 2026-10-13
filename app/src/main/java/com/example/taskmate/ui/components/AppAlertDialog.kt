package com.example.taskmate.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.TextPrimary
import com.example.taskmate.ui.theme.White

enum class DialogType(val message: String) {
    CHANGES_SAVED("Changes saved successfully!"),
    COURSE_ADDED("Course added successfully!"),
    TASK_ADDED("Task added successfully!")
}

@Composable
fun AppAlertDialog(
    type: DialogType? = null,
    customMessage: String? = null,
    onDismiss: () -> Unit
) {
    val displayMessage = customMessage ?: type?.message ?: "Operation successful!"

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = White,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = displayMessage,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
                ) {
                    Text("Close", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}