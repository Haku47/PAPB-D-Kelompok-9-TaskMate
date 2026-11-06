package com.example.taskmate.data.model

/**
 * Domain model untuk profil pengguna TaskMate.
 * Tidak menyimpan password demi keamanan data.
 */
data class UserAccount(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val fullName: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

