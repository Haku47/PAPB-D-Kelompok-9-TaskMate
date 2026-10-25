package com.example.taskmate.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskmate.data.UserManager

class SignUpViewModel : ViewModel() {
    // Front Name dan Back Name (Dua input terpisah yang digabung menjadi Full Name)
    var firstName by mutableStateOf("")
        private set
    var lastName by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set
    var isConfirmPasswordVisible by mutableStateOf(false)
        private set

    var firstNameError by mutableStateOf<String?>(null)
        private set
    var lastNameError by mutableStateOf<String?>(null)
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var confirmPasswordError by mutableStateOf<String?>(null)
        private set
    var generalError by mutableStateOf<String?>(null)
        private set
    var successMessage by mutableStateOf<String?>(null)
        private set

    // Data olahan / gabungan dari Front Name dan Back Name -> Full Name
    val fullName: String
        get() = "$firstName $lastName".trim()

    fun onFirstNameChange(value: String) {
        firstName = value
        if (firstNameError != null && value.isNotBlank()) firstNameError = null
        if (generalError != null) generalError = null
    }

    fun onLastNameChange(value: String) {
        lastName = value
        if (lastNameError != null && value.isNotBlank()) lastNameError = null
        if (generalError != null) generalError = null
    }

    fun onEmailChange(value: String) {
        email = value
        if (emailError != null && value.isNotBlank()) emailError = null
        if (generalError != null) generalError = null
    }

    fun onPasswordChange(value: String) {
        password = value
        if (passwordError != null && value.isNotBlank()) passwordError = null
        if (generalError != null) generalError = null
    }

    fun onConfirmPasswordChange(value: String) {
        confirmPassword = value
        if (confirmPasswordError != null && value.isNotBlank()) confirmPasswordError = null
        if (generalError != null) generalError = null
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible = !isConfirmPasswordVisible
    }

    private fun isValidEmail(emailStr: String): Boolean {
        val pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailStr.matches(pattern)
    }

    fun signUp(
        errFirstNameRequired: String,
        errLastNameRequired: String,
        errEmailRequired: String,
        errInvalidEmail: String,
        errEmailRegistered: String,
        errPasswordRequired: String,
        errPasswordTooShort: String,
        errConfirmPasswordRequired: String,
        errPasswordsDoNotMatch: String,
        msgSuccess: String,
        onSuccess: () -> Unit
    ): Boolean {
        var isValid = true

        val trimmedFirstName = firstName.trim()
        val trimmedLastName = lastName.trim()
        val trimmedEmail = email.trim()

        // Validate Front Name
        if (trimmedFirstName.isBlank()) {
            firstNameError = errFirstNameRequired
            isValid = false
        } else {
            firstNameError = null
        }

        // Validate Back Name
        if (trimmedLastName.isBlank()) {
            lastNameError = errLastNameRequired
            isValid = false
        } else {
            lastNameError = null
        }

        // Validate email
        if (trimmedEmail.isBlank()) {
            emailError = errEmailRequired
            isValid = false
        } else if (!isValidEmail(trimmedEmail)) {
            emailError = errInvalidEmail
            isValid = false
        } else if (UserManager.isEmailRegistered(trimmedEmail)) {
            emailError = errEmailRegistered
            isValid = false
        } else {
            emailError = null
        }

        // Validate password
        if (password.isBlank()) {
            passwordError = errPasswordRequired
            isValid = false
        } else if (password.length < 6) {
            passwordError = errPasswordTooShort
            isValid = false
        } else {
            passwordError = null
        }

        // Validate confirm password
        if (confirmPassword.isBlank()) {
            confirmPasswordError = errConfirmPasswordRequired
            isValid = false
        } else if (confirmPassword != password) {
            confirmPasswordError = errPasswordsDoNotMatch
            isValid = false
        } else {
            confirmPasswordError = null
        }

        if (!isValid) {
            return false
        }

        val derivedUsername = trimmedEmail.substringBefore('@').ifBlank {
            "${trimmedFirstName}${trimmedLastName}".lowercase()
        }

        val registered = UserManager.register(
            username = derivedUsername,
            email = trimmedEmail,
            password = password,
            fullName = fullName
        )

        return if (registered) {
            generalError = null
            successMessage = msgSuccess
            onSuccess()
            true
        } else {
            generalError = errEmailRegistered
            false
        }
    }
}
