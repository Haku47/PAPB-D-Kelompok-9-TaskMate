package com.example.taskmate.ui.screens.auth

import androidx.compose.foundation.background
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
import com.example.taskmate.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val s = strings
    val focusManager = LocalFocusManager.current
    val fieldShape = RoundedCornerShape(10.dp)
    val errorColor = Color(0xFFE53935)

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
        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = s.loginTitle,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.textPrimary
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Banner Error (jika kredensial salah)
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

        // Email Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = s.emailLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEmailChange(it) },
                placeholder = {
                    Text(
                        text = s.enterEmailPlaceholder,
                        color = AppThemeColors.textSecondary,
                        fontSize = 14.sp
                    )
                },
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

        Spacer(modifier = Modifier.height(18.dp))

        // Password dengan Show/Hide Toggle
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = s.passwordLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                placeholder = {
                    Text(
                        text = s.enterPasswordPlaceholder,
                        color = AppThemeColors.textSecondary,
                        fontSize = 14.sp
                    )
                },
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

        Spacer(modifier = Modifier.height(10.dp))

        // Forgot Password
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = s.forgotPassword,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = AppThemeColors.textPrimary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToForgotPassword() }
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Button Login
        Button(
            onClick = {
                viewModel.login(
                    errEmailRequired = s.errorEmailRequired,
                    errInvalidEmail = s.errorInvalidEmail,
                    errPasswordRequired = s.errorPasswordRequired,
                    errInvalidCredentials = s.errorInvalidCredentials,
                    onSuccess = onLoginSuccess
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
        ) {
            Text(
                text = s.loginButton,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Don't have an account? Sign Up
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = s.dontHaveAccount,
                fontSize = 13.sp,
                color = AppThemeColors.textPrimary
            )
            Text(
                text = s.signUpLink,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AppThemeColors.textPrimary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToSignUp() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
