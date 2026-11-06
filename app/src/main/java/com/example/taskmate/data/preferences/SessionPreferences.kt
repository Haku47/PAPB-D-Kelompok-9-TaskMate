package com.example.taskmate.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.taskmate.data.model.UserAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "taskmate_session_prefs")

class SessionPreferences(private val context: Context) {

    companion object {
        private val KEY_UID = stringPreferencesKey("session_uid")
        private val KEY_USERNAME = stringPreferencesKey("session_username")
        private val KEY_EMAIL = stringPreferencesKey("session_email")
        private val KEY_FULL_NAME = stringPreferencesKey("session_full_name")
        private val KEY_CREATED_AT = longPreferencesKey("session_created_at")
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("session_is_logged_in")
    }

    val sessionFlow: Flow<UserAccount?> = context.sessionDataStore.data.map { prefs ->
        val isLoggedIn = prefs[KEY_IS_LOGGED_IN] ?: false
        val uid = prefs[KEY_UID]
        if (isLoggedIn && !uid.isNullOrBlank()) {
            UserAccount(
                uid = uid,
                username = prefs[KEY_USERNAME] ?: "",
                email = prefs[KEY_EMAIL] ?: "",
                fullName = prefs[KEY_FULL_NAME] ?: "",
                createdAt = prefs[KEY_CREATED_AT] ?: System.currentTimeMillis()
            )
        } else {
            null
        }
    }

    suspend fun saveSession(user: UserAccount) {
        context.sessionDataStore.edit { prefs ->
            prefs[KEY_UID] = user.uid
            prefs[KEY_USERNAME] = user.username
            prefs[KEY_EMAIL] = user.email
            prefs[KEY_FULL_NAME] = user.fullName
            prefs[KEY_CREATED_AT] = user.createdAt
            prefs[KEY_IS_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { prefs ->
            prefs.remove(KEY_UID)
            prefs.remove(KEY_USERNAME)
            prefs.remove(KEY_EMAIL)
            prefs.remove(KEY_FULL_NAME)
            prefs.remove(KEY_CREATED_AT)
            prefs[KEY_IS_LOGGED_IN] = false
        }
    }
}

