package com.example.taskmate.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.json.JSONArray
import org.json.JSONObject

data class UserAccount(
    val username: String,
    val email: String,
    val password: String,
    val fullName: String = ""
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("username", username)
        put("email", email)
        put("password", password)
        put("fullName", fullName)
    }

    companion object {
        fun fromJson(json: JSONObject): UserAccount = UserAccount(
            username = json.optString("username", ""),
            email = json.optString("email", ""),
            password = json.optString("password", ""),
            fullName = json.optString("fullName", "")
        )
    }
}

object UserManager {
    private var prefs: SharedPreferences? = null

    val defaultUsers = listOf(
        UserAccount(
            username = "madnessesroman",
            email = "madnessesroman@abcde.xyz",
            password = "mad123",
            fullName = "Madnesses Roman"
        ),
        UserAccount(
            username = "admin",
            email = "admin@taskmate.com",
            password = "admin123",
            fullName = "Administrator"
        )
    )

    val users = mutableStateListOf<UserAccount>()

    var currentUser by mutableStateOf<UserAccount?>(null)
        private set

    fun init(context: Context) {
        if (prefs != null) return
        prefs = context.getSharedPreferences("taskmate_auth_prefs", Context.MODE_PRIVATE)
        loadData()
    }

    fun loadData() {
        val sp = prefs
        users.clear()
        val usersJson = sp?.getString("users_list", null)
        if (usersJson != null) {
            try {
                val arr = JSONArray(usersJson)
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i)
                    if (obj != null) {
                        users.add(UserAccount.fromJson(obj))
                    }
                }
            } catch (_: Exception) {
                users.addAll(defaultUsers)
            }
        }
        if (users.isEmpty()) {
            users.addAll(defaultUsers)
            saveUsers()
        }

        val loggedUserJson = sp?.getString("logged_in_user", null)
        if (loggedUserJson != null) {
            try {
                val obj = JSONObject(loggedUserJson)
                val user = UserAccount.fromJson(obj)
                val existing = users.find { it.username.equals(user.username, ignoreCase = true) }
                currentUser = existing ?: user
            } catch (_: Exception) {
                currentUser = null
            }
        } else {
            currentUser = null
        }
    }

    fun saveUsers() {
        val sp = prefs ?: return
        try {
            val arr = JSONArray()
            users.forEach { arr.put(it.toJson()) }
            sp.edit().putString("users_list", arr.toString()).apply()
        } catch (_: Exception) {}
    }

    private fun saveCurrentUser() {
        val sp = prefs ?: return
        val user = currentUser
        if (user != null) {
            sp.edit().putString("logged_in_user", user.toJson().toString()).apply()
        } else {
            sp.edit().remove("logged_in_user").apply()
        }
    }

    fun login(identifier: String, password: String): Boolean {
        val trimmed = identifier.trim()
        val user = users.find {
            (it.username.equals(trimmed, ignoreCase = true) || it.email.equals(trimmed, ignoreCase = true)) &&
                    it.password == password
        }
        return if (user != null) {
            currentUser = user
            saveCurrentUser()
            true
        } else {
            false
        }
    }

    fun register(username: String, email: String, password: String, fullName: String = ""): Boolean {
        val trimmedUser = username.trim()
        val trimmedEmail = email.trim().lowercase()

        if (isUsernameTaken(trimmedUser) || isEmailRegistered(trimmedEmail)) {
            return false
        }

        val newUser = UserAccount(
            username = trimmedUser,
            email = trimmedEmail,
            password = password,
            fullName = fullName.ifBlank { trimmedUser }
        )
        users.add(newUser)
        saveUsers()
        currentUser = newUser
        saveCurrentUser()
        return true
    }

    fun logout() {
        currentUser = null
        saveCurrentUser()
    }

    fun isUsernameTaken(username: String): Boolean {
        val trimmed = username.trim()
        return users.any { it.username.equals(trimmed, ignoreCase = true) }
    }

    fun isEmailRegistered(email: String): Boolean {
        val trimmed = email.trim()
        return users.any { it.email.equals(trimmed, ignoreCase = true) }
    }

    fun resetPassword(email: String, newPassword: String): Boolean {
        val trimmed = email.trim()
        val index = users.indexOfFirst { it.email.equals(trimmed, ignoreCase = true) }
        return if (index != -1) {
            users[index] = users[index].copy(password = newPassword)
            saveUsers()
            if (currentUser?.email.equals(trimmed, ignoreCase = true)) {
                currentUser = users[index]
                saveCurrentUser()
            }
            true
        } else {
            false
        }
    }

    fun resetToInitialDefaults() {
        users.clear()
        users.addAll(defaultUsers)
        currentUser = null
        saveUsers()
        saveCurrentUser()
    }
}

