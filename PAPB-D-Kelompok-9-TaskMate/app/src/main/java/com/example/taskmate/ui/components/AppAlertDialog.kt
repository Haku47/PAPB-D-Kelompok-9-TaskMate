package com.example.taskmate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.White

import com.example.taskmate.ui.theme.strings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color

enum class DialogType {
    CHANGES_SAVED,
    COURSE_ADDED,
    TASK_ADDED,
    TASK_FINISHED,
    COURSE_DELETED,
    TASK_DELETED
}

@Composable
fun AppAlertDialog(
    type: DialogType? = null,
    customMessage: String? = null,
    onDismiss: () -> Unit
) {
    val s = strings
    val displayMessage = customMessage ?: when (type) {
        DialogType.CHANGES_SAVED -> s.dialogChangesSaved
        DialogType.COURSE_ADDED -> s.dialogCourseAdded
        DialogType.TASK_ADDED -> s.dialogTaskAdded
        DialogType.TASK_FINISHED -> s.dialogTaskFinished
        DialogType.COURSE_DELETED -> s.dialogCourseDeleted
        DialogType.TASK_DELETED -> s.dialogTaskDeleted
        null -> s.dialogChangesSaved
    }
    val cardShape = RoundedCornerShape(18.dp)
    val buttonShape = RoundedCornerShape(12.dp)

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clip(cardShape)
                .background(AppThemeColors.cardBackground)
                .border(1.5.dp, AppThemeColors.border, cardShape)
                .padding(horizontal = 24.dp, vertical = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = displayMessage,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppThemeColors.textPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = buttonShape,
                    colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
                ) {
                    Text(
                        text = s.dialogClose,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
            }
        }
    }
}

@Composable
fun AppConfirmDialog(
    title: String,
    message: String,
    confirmText: String = strings.deleteButton,
    cancelText: String = strings.cancelButton,
    isDanger: Boolean = true,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val cardShape = RoundedCornerShape(18.dp)
    val buttonShape = RoundedCornerShape(12.dp)

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clip(cardShape)
                .background(AppThemeColors.cardBackground)
                .border(1.5.dp, AppThemeColors.border, cardShape)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Delete Icon in circular badge
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(if (isDanger) Color(0xFFFFEBEE) else Color(0xFFE3F2FD)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = if (isDanger) Color(0xFFD32F2F) else BlueNormal,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppThemeColors.textPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = AppThemeColors.textSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = buttonShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppThemeColors.searchBarContainer,
                            contentColor = AppThemeColors.textPrimary
                        ),
                        border = BorderStroke(1.dp, AppThemeColors.border)
                    ) {
                        Text(
                            text = cancelText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AppThemeColors.textPrimary
                        )
                    }

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = buttonShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDanger) Color(0xFFE53935) else BlueNormal
                        )
                    ) {
                        Text(
                            text = confirmText,
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