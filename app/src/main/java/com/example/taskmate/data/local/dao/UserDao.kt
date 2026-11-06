package com.example.taskmate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskmate.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    fun getUser(uid: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    suspend fun getUserOnce(uid: String): UserEntity?

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getAnyUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM users WHERE uid = :uid")
    suspend fun deleteUser(uid: String): Int

    @Query("DELETE FROM users")
    suspend fun clearUsers(): Int
}
