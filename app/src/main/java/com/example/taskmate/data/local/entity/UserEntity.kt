package com.example.taskmate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskmate.data.model.UserAccount

/**
 * Entitas tabel lokal Room untuk menyimpan cache pengguna yang aman (tanpa password).
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val username: String,
    val email: String,
    val fullName: String,
    val createdAt: Long = System.currentTimeMillis()
)

fun UserEntity.toDomain(): UserAccount = UserAccount(
    uid = uid,
    username = username,
    email = email,
    fullName = fullName,
    createdAt = createdAt
)

fun UserAccount.toEntity(): UserEntity = UserEntity(
    uid = uid,
    username = username,
    email = email,
    fullName = fullName,
    createdAt = createdAt
)

