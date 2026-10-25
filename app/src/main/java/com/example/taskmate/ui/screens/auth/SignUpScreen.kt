package com.example.taskmate.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.White
import com.example.taskmate.ui.theme.strings
import com.example.taskmate.ui.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SignUpViewModel = viewModel()
) {
    val s = strings
    val focusManager = LocalFocusManager.current
    val fieldShape = RoundedCornerShape(10.dp)
    val errorColor = Color(0xFFE53935)
    val successColor = Color(0xFF2E7D32)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppThemeColors.background)
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(45.dp))

        Text(
            text = s.signUpTitle,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.textPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Banner Error (Email sudah terdaftar, etc.)
        if (viewModel.generalError != null) {
            Surface(
                color = Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, errorColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = viewModel.generalError!!,
                    color = errorColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Banner Sukses
        if (viewModel.successMessage != null) {
            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, successColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = viewModel.successMessage!!,
                    color = successColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // 1. Front Name & Back Name (Dua input terpisah yang digabung menjadi Full Name)
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Front Name (Nama Depan)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = s.frontNameLabel,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppThemeColors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = viewModel.firstName,
                        onValueChange = { viewModel.onFirstNameChange(it) },
                        placeholder = {
                            Text(
                                text = s.enterFrontNamePlaceholder,
                                color = AppThemeColors.textSecondary,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = fieldShape,
                        singleLine = true,
                        isError = viewModel.firstNameError != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (viewModel.firstNameError != null) errorColor else BlueNormal,
                            unfocusedBorderColor = if (viewModel.firstNameError != null) errorColor else AppThemeColors.border,
                            focusedTextColor = AppThemeColors.textPrimary,
                            unfocusedTextColor = AppThemeColors.textPrimary,
                            focusedContainerColor = AppThemeColors.searchBarContainer,
                            unfocusedContainerColor = AppThemeColors.searchBarContainer
                        )
                    )
                    if (viewModel.firstNameError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = viewModel.firstNameError!!,
                            color = errorColor,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }

                // Back Name (Nama Belakang)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = s.backNameLabel,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppThemeColors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = viewModel.lastName,
                        onValueChange = { viewModel.onLastNameChange(it) },
                        placeholder = {
                            Text(
                                text = s.enterBackNamePlaceholder,
                                color = AppThemeColors.textSecondary,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = fieldShape,
                        singleLine = true,
                        isError = viewModel.lastNameError != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (viewModel.lastNameError != null) errorColor else BlueNormal,
                            unfocusedBorderColor = if (viewModel.lastNameError != null) errorColor else AppThemeColors.border,
                            focusedTextColor = AppThemeColors.textPrimary,
                            unfocusedTextColor = AppThemeColors.textPrimary,
                            focusedContainerColor = AppThemeColors.searchBarContainer,
                            unfocusedContainerColor = AppThemeColors.searchBarContainer
                        )
                    )
                    if (viewModel.lastNameError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = viewModel.lastNameError!!,
                            color = errorColor,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Email
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(s.emailLabel, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEmailChange(it) },
                placeholder = { Text(s.enterEmailPlaceholder, color = AppThemeColors.textSecondary, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                isError = viewModel.emailError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (viewModel.emailError != null) errorColor else BlueNormal,
                    unfocusedBorderColor = if (viewModel.emailError != null) errorColor else AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )
            if (viewModel.emailError != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = viewModel.emailError!!,
                    color = errorColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Password dengan toggle
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(s.passwordLabel, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                placeholder = { Text(s.enterPasswordPlaceholder, color = AppThemeColors.textSecondary, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                isError = viewModel.passwordError != null,
                visualTransformation = if (viewModel.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (viewModel.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (viewModel.isPasswordVisible) "Hide password" else "Show password",
                            tint = AppThemeColors.textPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (viewModel.passwordError != null) errorColor else BlueNormal,
                    unfocusedBorderColor = if (viewModel.passwordError != null) errorColor else AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )
            if (viewModel.passwordError != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = viewModel.passwordError!!,
                    color = errorColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Confirm Password dengan toggle
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(s.confirmPasswordLabel, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChange(it) },
                placeholder = { Text(s.enterConfirmPasswordPlaceholder, color = AppThemeColors.textSecondary, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                isError = viewModel.confirmPasswordError != null,
                visualTransformation = if (viewModel.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { viewModel.toggleConfirmPasswordVisibility() }) {
                        Icon(
                            imageVector = if (viewModel.isConfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (viewModel.isConfirmPasswordVisible) "Hide password" else "Show password",
                            tint = AppThemeColors.textPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (viewModel.confirmPasswordError != null) errorColor else BlueNormal,
                    unfocusedBorderColor = if (viewModel.confirmPasswordError != null) errorColor else AppThemeColors.border,
                    focusedTextColor = AppThemeColors.textPrimary,
                    unfocusedTextColor = AppThemeColors.textPrimary,
                    focusedContainerColor = AppThemeColors.searchBarContainer,
                    unfocusedContainerColor = AppThemeColors.searchBarContainer
                )
            )
            if (viewModel.confirmPasswordError != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = viewModel.confirmPasswordError!!,
                    color = errorColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // 5. Button Sign Up
        Button(
            onClick = {
                viewModel.signUp(
                    errFirstNameRequired = s.errorFrontNameRequired,
                    errLastNameRequired = s.errorBackNameRequired,
                    errEmailRequired = s.errorEmailRequired,
                    errInvalidEmail = s.errorInvalidEmail,
                    errEmailRegistered = s.errorEmailRegistered,
                    errPasswordRequired = s.errorPasswordRequired,
                    errPasswordTooShort = s.errorPasswordTooShort,
                    errConfirmPasswordRequired = s.errorConfirmPasswordRequired,
                    errPasswordsDoNotMatch = s.errorPasswordsDoNotMatch,
                    msgSuccess = s.registrationSuccessful,
                    onSuccess = onSignUpSuccess
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
        ) {
            Text(
                text = s.signUpButton,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 6. Already have an account? Login
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = s.alreadyHaveAccount,
                fontSize = 13.sp,
                color = AppThemeColors.textPrimary
            )
            Text(
                text = s.loginLink,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
