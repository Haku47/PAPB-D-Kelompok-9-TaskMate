package com.example.taskmate.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskmate.data.UserManager

class ChangePasswordViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set
    var newPassword by mutableStateOf("")
        private set
    var confirmNewPassword by mutableStateOf("")
        private set

    var isNewPasswordVisible by mutableStateOf(false)
        private set
    var isConfirmNewPasswordVisible by mutableStateOf(false)
        private set

    var newPasswordError by mutableStateOf<String?>(null)
        private set
    var confirmNewPasswordError by mutableStateOf<String?>(null)
        private set
    var generalError by mutableStateOf<String?>(null)
        private set

    fun setEmailTarget(targetEmail: String) {
        email = targetEmail
    }

    fun onNewPasswordChange(value: String) {
        newPassword = value
        if (newPasswordError != null && value.isNotBlank()) newPasswordError = null
        if (generalError != null) generalError = null
    }

    fun onConfirmNewPasswordChange(value: String) {
        confirmNewPassword = value
        if (confirmNewPasswordError != null && value.isNotBlank()) confirmNewPasswordError = null
        if (generalError != null) generalError = null
    }

    fun toggleNewPasswordVisibility() {
        isNewPasswordVisible = !isNewPasswordVisible
    }

    fun toggleConfirmNewPasswordVisibility() {
        isConfirmNewPasswordVisible = !isConfirmNewPasswordVisible
    }

    fun savePassword(
        errPasswordRequired: String,
        errPasswordTooShort: String,
        errConfirmPasswordRequired: String,
        errPasswordsDoNotMatch: String,
        onSuccess: () -> Unit
    ): Boolean {
        var isValid = true

        if (newPassword.isBlank()) {
            newPasswordError = errPasswordRequired
            isValid = false
        } else if (newPassword.length < 6) {
            newPasswordError = errPasswordTooShort
            isValid = false
        } else {
            newPasswordError = null
        }

        if (confirmNewPassword.isBlank()) {
            confirmNewPasswordError = errConfirmPasswordRequired
            isValid = false
        } else if (confirmNewPassword != newPassword) {
            confirmNewPasswordError = errPasswordsDoNotMatch
            isValid = false
        } else {
            confirmNewPasswordError = null
        }

        if (!isValid) return false

        val target = email.ifBlank { UserManager.currentUser?.email ?: "" }
        val updated = if (target.isNotBlank()) {
            UserManager.resetPassword(target, newPassword)
        } else {
            true
        }

        return if (updated) {
            generalError = null
            onSuccess()
            true
        } else {
            false
        }
    }
}

