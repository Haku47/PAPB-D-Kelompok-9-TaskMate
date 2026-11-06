package com.example.taskmate.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmate.data.UserManager
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    var firstName by mutableStateOf("")
        private set
    var lastName by mutableStateOf("")
        private set
    var username by mutableStateOf("")
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
    var isLoading by mutableStateOf(false)
        private set

    var firstNameError by mutableStateOf<String?>(null)
        private set
    var lastNameError by mutableStateOf<String?>(null)
        private set
    var usernameError by mutableStateOf<String?>(null)
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

    fun onUsernameChange(value: String) {
        username = value
        if (usernameError != null && value.isNotBlank()) usernameError = null
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

        // 1. Validate Front Name
        if (trimmedFirstName.isBlank()) {
            firstNameError = errFirstNameRequired
            isValid = false
        } else {
            firstNameError = null
        }

        // 2. Validate Back Name
        if (trimmedLastName.isBlank()) {
            lastNameError = errLastNameRequired
            isValid = false
        } else {
            lastNameError = null
        }

        // 3. Validate Email
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

        // 4. Validate Password
        if (password.isBlank()) {
            passwordError = errPasswordRequired
            isValid = false
        } else if (password.length < 6) {
            passwordError = errPasswordTooShort
            isValid = false
        } else {
            passwordError = null
        }

        // 5. Validate Confirm Password
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

        val targetUsername = username.trim().ifBlank {
            trimmedEmail.substringBefore('@').ifBlank {
                "${trimmedFirstName}${trimmedLastName}".lowercase()
            }
        }

        val repo = UserManager.repository
        if (repo != null) {
            isLoading = true
            generalError = null
            viewModelScope.launch {
                val result = repo.register(
                    username = targetUsername,
                    email = trimmedEmail,
                    password = password,
                    fullName = fullName
                )
                isLoading = false
                if (result.isSuccess) {
                    generalError = null
                    successMessage = msgSuccess
                    onSuccess()
                } else {
                    val ex = result.exceptionOrNull()
                    generalError = ex?.localizedMessage ?: errEmailRegistered
                }
            }
            return true
        } else {
            // Fallback sinkronus untuk JVM Unit Test
            val registered = UserManager.register(
                username = targetUsername,
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
}
