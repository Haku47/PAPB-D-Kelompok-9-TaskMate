package com.example.taskmate.ui.screens.auth

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.White
import com.example.taskmate.ui.theme.strings
import com.example.taskmate.ui.viewmodel.ChangePasswordViewModel

@Composable
fun ChangePasswordScreen(
    onChangeSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ChangePasswordViewModel = viewModel()
) {
    val s = strings
    val focusManager = LocalFocusManager.current
    val fieldShape = RoundedCornerShape(10.dp)
    val errorColor = Color(0xFFE53935)

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
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.savePassword(
                            errPasswordRequired = s.errorPasswordRequired,
                            errPasswordTooShort = s.errorPasswordTooShort,
                            errConfirmPasswordRequired = s.errorConfirmPasswordRequired,
                            errPasswordsDoNotMatch = s.errorPasswordsDoNotMatch,
                            onSuccess = onChangeSuccess
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
                ) {
                    Text(
                        text = s.savePasswordButton,
                        fontSize = 18.sp,
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

            // Circle Back Button & Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, AppThemeColors.textPrimary, CircleShape)
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
                    text = s.changePasswordTitle,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppThemeColors.textPrimary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // New Password with toggle
            Text(
                text = s.newPasswordLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = viewModel.newPassword,
                onValueChange = { viewModel.onNewPasswordChange(it) },
                placeholder = {
                    Text(
                        text = s.enterNewPasswordPlaceholder,
                        color = AppThemeColors.textSecondary,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                isError = viewModel.newPasswordError != null,
                visualTransformation = if (viewModel.isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { viewModel.toggleNewPasswordVisibility() }) {
                        Icon(
                            imageVector = if (viewModel.isNewPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (viewModel.isNewPasswordVisible) "Hide password" else "Show password",
                            tint = AppThemeColors.textPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (viewModel.newPasswordError != null) errorColor else BlueNormal,
                    unfocusedBorderColor = if (viewModel.newPasswordError != null) errorColor else AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )
            if (viewModel.newPasswordError != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = viewModel.newPasswordError!!,
                    color = errorColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm New Password with toggle
            Text(
                text = s.confirmNewPasswordLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = viewModel.confirmNewPassword,
                onValueChange = { viewModel.onConfirmNewPasswordChange(it) },
                placeholder = {
                    Text(
                        text = s.enterConfirmNewPasswordPlaceholder,
                        color = AppThemeColors.textSecondary,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                isError = viewModel.confirmNewPasswordError != null,
                visualTransformation = if (viewModel.isConfirmNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { viewModel.toggleConfirmNewPasswordVisibility() }) {
                        Icon(
                            imageVector = if (viewModel.isConfirmNewPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (viewModel.isConfirmNewPasswordVisible) "Hide password" else "Show password",
                            tint = AppThemeColors.textPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (viewModel.confirmNewPasswordError != null) errorColor else BlueNormal,
                    unfocusedBorderColor = if (viewModel.confirmNewPasswordError != null) errorColor else AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )
            if (viewModel.confirmNewPasswordError != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = viewModel.confirmNewPasswordError!!,
                    color = errorColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
