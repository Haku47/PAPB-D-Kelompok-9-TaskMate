package com.example.taskmate.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskmate.data.UserManager

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isPasswordVisible by mutableStateOf(false)
        private set

    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var generalError by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(value: String) {
        email = value
        if (emailError != null && value.isNotBlank()) {
            emailError = null
        }
        if (generalError != null) {
            generalError = null
        }
    }

    fun onPasswordChange(value: String) {
        password = value
        if (passwordError != null && value.isNotBlank()) {
            passwordError = null
        }
        if (generalError != null) {
            generalError = null
        }
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    private fun isValidEmail(emailStr: String): Boolean {
        val pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailStr.matches(pattern)
    }

    fun fillDemoAccount() {
        val defaultUser = UserManager.users.firstOrNull() ?: UserManager.defaultUsers.first()
        email = defaultUser.email
        password = defaultUser.password
        emailError = null
        passwordError = null
        generalError = null
    }

    fun login(
        errEmailRequired: String,
        errInvalidEmail: String,
        errPasswordRequired: String,
        errInvalidCredentials: String,
        onSuccess: () -> Unit
    ): Boolean {
        var isValid = true

        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank()) {
            emailError = errEmailRequired
            isValid = false
        } else if (!isValidEmail(trimmedEmail)) {
            emailError = errInvalidEmail
            isValid = false
        } else {
            emailError = null
        }

        if (password.isBlank()) {
            passwordError = errPasswordRequired
            isValid = false
        } else {
            passwordError = null
        }

        if (!isValid) {
            return false
        }

        val success = UserManager.login(trimmedEmail, password)
        return if (success) {
            generalError = null
            onSuccess()
            true
        } else {
            generalError = errInvalidCredentials
            false
        }
    }
}
