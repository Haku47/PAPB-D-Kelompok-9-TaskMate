package com.example.taskmate.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmate.data.UserManager
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isPasswordVisible by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf(false)
        private set

    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var generalError by mutableStateOf<String?>(null)
        private set

    // Alias identifier agar mendukung input Email atau Username
    val identifier: String get() = email

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
        password = "password123"
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

        val trimmedIdentifier = email.trim()

        if (trimmedIdentifier.isBlank()) {
            emailError = errEmailRequired
            isValid = false
        } else if (trimmedIdentifier.contains("@") && !isValidEmail(trimmedIdentifier)) {
            emailError = errInvalidEmail
            isValid = false
        } else if (!trimmedIdentifier.contains("@") && trimmedIdentifier.length < 3) {
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

        val repo = UserManager.repository
        if (repo != null) {
            isLoading = true
            generalError = null
            viewModelScope.launch {
                val result = repo.login(trimmedIdentifier, password)
                isLoading = false
                if (result.isSuccess) {
                    generalError = null
                    onSuccess()
                } else {
                    val ex = result.exceptionOrNull()
                    generalError = ex?.localizedMessage ?: errInvalidCredentials
                }
            }
            return true
        } else {
            // Fallback sinkronus untuk JVM Unit Test
            val success = UserManager.login(trimmedIdentifier, password)
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
}
