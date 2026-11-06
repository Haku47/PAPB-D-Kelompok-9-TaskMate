package com.example.taskmate.data.local

import com.example.taskmate.data.local.dao.UserDao
import com.example.taskmate.data.local.entity.toDomain
import com.example.taskmate.data.local.entity.toEntity
import com.example.taskmate.data.model.UserAccount
import com.example.taskmate.data.preferences.SessionPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local Data Source yang mengabstraksi Room Database dan DataStore Preferences.
 */
class UserLocalDataSource(
    private val userDao: UserDao,
    private val sessionPreferences: SessionPreferences
) {
    val sessionFlow: Flow<UserAccount?> = sessionPreferences.sessionFlow

    fun getUser(uid: String): Flow<UserAccount?> {
        return userDao.getUser(uid).map { it?.toDomain() }
    }

    suspend fun getUserOnce(uid: String): UserAccount? {
        return userDao.getUserOnce(uid)?.toDomain()
    }

    suspend fun getAnyCachedUser(): UserAccount? {
        return userDao.getAnyUser()?.toDomain()
    }

    suspend fun saveUser(user: UserAccount) {
        userDao.insertUser(user.toEntity())
        sessionPreferences.saveSession(user)
    }

    suspend fun clearSession() {
        sessionPreferences.clearSession()
    }

    suspend fun deleteUser(uid: String) {
        userDao.deleteUser(uid)
        sessionPreferences.clearSession()
    }
}

