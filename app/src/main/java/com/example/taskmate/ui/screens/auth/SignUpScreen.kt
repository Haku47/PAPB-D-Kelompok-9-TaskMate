package com.example.taskmate.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.ui.theme.AppThemeColors
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.White

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val fieldShape = RoundedCornerShape(10.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppThemeColors.background)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Sign Up",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.textPrimary
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Username
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Username", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Enter your username", color = AppThemeColors.textSecondary, fontSize = 14.sp) },
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
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Email
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Email", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Enter your email", color = AppThemeColors.textSecondary, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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

        Spacer(modifier = Modifier.height(16.dp))

        // Password dengan toggle
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Password", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Enter your password", color = AppThemeColors.textSecondary, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = AppThemeColors.textPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
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

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password dengan toggle
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Confirm Password", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppThemeColors.textPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Enter your password", color = AppThemeColors.textSecondary, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape,
                singleLine = true,
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password",
                            tint = AppThemeColors.textPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
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

        Spacer(modifier = Modifier.height(28.dp))

        // Button Sign Up
        Button(
            onClick = onSignUpSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
        ) {
            Text(
                text = "Sign Up",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Already have an account? Login
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account? ",
                fontSize = 13.sp,
                color = AppThemeColors.textPrimary
            )
            Text(
                text = "Login",
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