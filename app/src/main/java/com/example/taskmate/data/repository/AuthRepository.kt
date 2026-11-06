package com.example.taskmate.data.repository

import android.content.Context
import com.example.taskmate.data.local.AppDatabase
import com.example.taskmate.data.local.UserLocalDataSource
import com.example.taskmate.data.model.UserAccount
import com.example.taskmate.data.preferences.SessionPreferences
import com.example.taskmate.data.remote.UserRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Repository utama untuk autentikasi dan manajemen pengguna.
 * Menerapkan pola Hybrid Local + Cloud:
 * - Cloud Firestore & Firebase Auth sebagai Single Source of Truth
 * - Room Database & DataStore sebagai Local Cache & Offline Support
 */
class AuthRepository(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource = UserRemoteDataSource()
) {
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _currentUser = MutableStateFlow<UserAccount?>(null)
    val currentUser: StateFlow<UserAccount?> = _currentUser.asStateFlow()

    val currentAccount: UserAccount?
        get() = _currentUser.value

    init {
        // Ambil sesi lokal yang tersimpan saat inisialisasi awal
        repositoryScope.launch {
            initializeSession()
        }
    }

    /**
     * Memulihkan sesi pengguna:
     * 1. Cek sesi lokal dari DataStore / Room cache (cepat, mendukung mode offline).
     * 2. Jika ada koneksi dan Firebase user aktif, perbarui cache lokal dari Firestore.
     */
    suspend fun initializeSession() = withContext(Dispatchers.IO) {
        // 1. Baca sesi lokal
        val localSession = localDataSource.sessionFlow.firstOrNull()
        if (localSession != null) {
            _currentUser.value = localSession
        }

        // 2. Sinkronkan dengan Firebase Auth jika ada
        val fbUser = remoteDataSource.currentFirebaseUser
        if (fbUser != null) {
            val uid = fbUser.uid
            val cloudProfileResult = remoteDataSource.fetchUserProfile(uid)
            if (cloudProfileResult.isSuccess) {
                val cloudUser = cloudProfileResult.getOrNull()
                if (cloudUser != null) {
                    localDataSource.saveUser(cloudUser)
                    _currentUser.value = cloudUser
                }
            } else if (_currentUser.value == null) {
                // Fallback jika belum ada cache lokal tapi ada sesi Firebase
                val fallback = UserAccount(
                    uid = uid,
                    username = fbUser.displayName ?: fbUser.email?.substringBefore("@") ?: "User",
                    email = fbUser.email ?: "",
                    fullName = fbUser.displayName ?: ""
                )
                localDataSource.saveUser(fallback)
                _currentUser.value = fallback
            }
        }
    }

    /**
     * Mendaftarkan pengguna baru via Firebase Auth + Firestore, lalu menyimpan cache ke Room & DataStore.
     */
    suspend fun register(
        username: String,
        email: String,
        password: String,
        fullName: String
    ): Result<UserAccount> = withContext(Dispatchers.IO) {
        val result = remoteDataSource.register(
            username = username,
            email = email,
            password = password,
            fullName = fullName
        )

        if (result.isSuccess) {
            val user = result.getOrThrow()
            localDataSource.saveUser(user)
            _currentUser.value = user
        }
        result
    }

    /**
     * Login via Firebase Auth (bisa menggunakan Email atau Username),
     * mengambil data profil dari Firestore, dan memperbarui cache Room & DataStore.
     */
    suspend fun login(
        identifier: String,
        password: String
    ): Result<UserAccount> = withContext(Dispatchers.IO) {
        val result = remoteDataSource.login(identifier, password)

        if (result.isSuccess) {
            val user = result.getOrThrow()
            localDataSource.saveUser(user)
            _currentUser.value = user
        }
        result
    }

    /**
     * Mengirim link reset password ke email pengguna melalui Firebase Authentication.
     */
    suspend fun sendPasswordReset(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        remoteDataSource.sendPasswordResetEmail(email)
    }

    /**
     * Logout pengguna: keluar dari Firebase Auth dan bersihkan sesi aktif.
     */
    suspend fun logout() = withContext(Dispatchers.IO) {
        remoteDataSource.logout()
        localDataSource.clearSession()
        _currentUser.value = null
    }

    /**
     * Memeriksa apakah username masih tersedia di Cloud Firestore.
     */
    suspend fun isUsernameAvailable(username: String): Boolean = withContext(Dispatchers.IO) {
        remoteDataSource.isUsernameAvailable(username)
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthRepository? = null

        fun getInstance(context: Context): AuthRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: run {
                    val db = AppDatabase.getInstance(context)
                    val sessionPrefs = SessionPreferences(context)
                    val localSource = UserLocalDataSource(db.userDao(), sessionPrefs)
                    val remoteSource = UserRemoteDataSource()
                    AuthRepository(localSource, remoteSource).also { INSTANCE = it }
                }
            }
        }
    }
}

