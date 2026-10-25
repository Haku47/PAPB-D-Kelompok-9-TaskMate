package com.example.taskmate.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskmate.data.UserManager

class ForgotPasswordViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var generalError by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(value: String) {
        email = value
        if (emailError != null && value.isNotBlank()) emailError = null
        if (generalError != null) generalError = null
    }

    private fun isValidEmail(emailStr: String): Boolean {
        val pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailStr.matches(pattern)
    }

    fun sendResetEmail(
        errEmailRequired: String,
        errInvalidEmail: String,
        errUserNotFound: String,
        onSuccess: () -> Unit
    ): Boolean {
        val trimmed = email.trim()
        if (trimmed.isBlank()) {
            emailError = errEmailRequired
            return false
        }
        if (!isValidEmail(trimmed)) {
            emailError = errInvalidEmail
            return false
        }
        if (!UserManager.isEmailRegistered(trimmed)) {
            emailError = errUserNotFound
            return false
        }

        emailError = null
        generalError = null
        onSuccess()
        return true
    }
}

