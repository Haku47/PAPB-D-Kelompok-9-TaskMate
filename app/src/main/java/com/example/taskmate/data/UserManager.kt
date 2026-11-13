package com.example.taskmate.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.taskmate.data.model.UserAccount
import com.example.taskmate.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

typealias UserAccount = com.example.taskmate.data.model.UserAccount

// Helper singleton manajemen user & kompatibilitas ke AuthRepository
object UserManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    var repository: AuthRepository? = null
        private set

    // Akun default untuk fallback offline / testing (hanya identitas profil publik, tanpa plaintext password di storage)
    val defaultUsers = listOf(
        UserAccount(
            uid = "default_user_1",
            username = "madnessesroman",
            email = "madnessesroman@abcde.xyz",
            fullName = "Madnesses Roman"
        ),
        UserAccount(
            uid = "default_admin_1",
            username = "admin",
            email = "admin@taskmate.com",
            fullName = "Administrator"
        )
    )

    // In-memory list untuk testing lokal
    val users = mutableStateListOf<UserAccount>().apply {
        addAll(defaultUsers)
    }

    var currentUser by mutableStateOf<UserAccount?>(null)

    fun init(context: Context) {
        val repo = AuthRepository.getInstance(context)
        repository = repo

        scope.launch {
            repo.currentUser.collect { user ->
                currentUser = user
            }
        }
    }

    /**
     * Login adapter yang memanggil AuthRepository secara aman.
     */
    fun login(identifier: String, password: String): Boolean {
        val repo = repository
        return if (repo != null) {
            runBlocking {
                val result = repo.login(identifier, password)
                result.isSuccess
            }
        } else {
            // Fallback untuk unit test lokal host JVM tanpa Firebase
            val trimmed = identifier.trim()
            val user = users.find {
                it.username.equals(trimmed, ignoreCase = true) || it.email.equals(trimmed, ignoreCase = true)
            }
            if (user != null && password.isNotBlank()) {
                currentUser = user
                true
            } else {
                false
            }
        }
    }

    /**
     * Register adapter yang memanggil AuthRepository secara aman.
     */
    fun register(username: String, email: String, password: String, fullName: String = ""): Boolean {
        val repo = repository
        return if (repo != null) {
            runBlocking {
                val result = repo.register(username, email, password, fullName)
                result.isSuccess
            }
        } else {
            // Fallback untuk unit test lokal host JVM tanpa Firebase
            val trimmedUser = username.trim()
            val trimmedEmail = email.trim().lowercase()
            if (isUsernameTaken(trimmedUser) || isEmailRegistered(trimmedEmail)) {
                return false
            }
            val newUser = UserAccount(
                uid = "test_${System.currentTimeMillis()}",
                username = trimmedUser,
                email = trimmedEmail,
                fullName = fullName.ifBlank { trimmedUser }
            )
            users.add(newUser)
            currentUser = newUser
            true
        }
    }

    fun logout() {
        val repo = repository
        if (repo != null) {
            scope.launch {
                repo.logout()
            }
        }
        currentUser = null
    }

    fun isUsernameTaken(username: String): Boolean {
        val trimmed = username.trim()
        return users.any { it.username.equals(trimmed, ignoreCase = true) }
    }

    fun isEmailRegistered(email: String): Boolean {
        val trimmed = email.trim()
        return users.any { it.email.equals(trimmed, ignoreCase = true) }
    }

    fun resetPassword(email: String, newPassword: String = ""): Boolean {
        val repo = repository
        return if (repo != null) {
            runBlocking {
                repo.sendPasswordReset(email).isSuccess
            }
        } else {
            isEmailRegistered(email)
        }
    }

    fun resetToInitialDefaults() {
        users.clear()
        users.addAll(defaultUsers)
        currentUser = null
    }
}
