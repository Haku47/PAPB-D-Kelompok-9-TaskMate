package com.example.taskmate.data.remote.dto

import com.example.taskmate.data.model.UserAccount

/**
 * Data Transfer Object (DTO) untuk dokumen di Cloud Firestore collection `users/{uid}`.
 * Tidak menyimpan password demi keamanan data cloud.
 */
data class UserDto(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val fullName: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): UserAccount = UserAccount(
        uid = uid,
        username = username,
        email = email,
        fullName = fullName,
        createdAt = createdAt
    )

    fun toMap(): Map<String, Any?> = mapOf(
        "uid" to uid,
        "username" to username,
        "email" to email,
        "fullName" to fullName,
        "createdAt" to createdAt,
        "updatedAt" to updatedAt
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): UserDto = UserDto(
            uid = map["uid"] as? String ?: "",
            username = map["username"] as? String ?: "",
            email = map["email"] as? String ?: "",
            fullName = map["fullName"] as? String ?: "",
            createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
            updatedAt = (map["updatedAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
        )
    }
}

fun UserAccount.toDto(): UserDto = UserDto(
    uid = uid,
    username = username,
    email = email,
    fullName = fullName,
    createdAt = createdAt,
    updatedAt = System.currentTimeMillis()
)

/**
 * Data Transfer Object untuk reservasi username unik pada collection `usernames/{usernameLower}`.
 */
data class UsernameReservationDto(
    val uid: String = "",
    val email: String = "",
    val reservedAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "uid" to uid,
        "email" to email,
        "reservedAt" to reservedAt
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): UsernameReservationDto = UsernameReservationDto(
            uid = map["uid"] as? String ?: "",
            email = map["email"] as? String ?: "",
            reservedAt = (map["reservedAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
        )
    }
}

