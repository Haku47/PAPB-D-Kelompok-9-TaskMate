package com.example.taskmate.data.remote

import com.example.taskmate.data.model.UserAccount
import com.example.taskmate.data.remote.dto.UserDto
import com.example.taskmate.data.remote.dto.UsernameReservationDto
import com.example.taskmate.data.remote.dto.toDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Remote Data Source yang menangani Firebase Authentication dan Cloud Firestore.
 * Bertanggung jawab atas autentikasi, reservasi username unik, dan pengelolaan profil cloud.
 */
class UserRemoteDataSource(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    val currentFirebaseUser get() = auth.currentUser

    /**
     * Memeriksa apakah username masih tersedia di Cloud Firestore.
     */
    suspend fun isUsernameAvailable(username: String): Boolean {
        val usernameKey = username.trim().lowercase()
        if (usernameKey.isBlank()) return false
        return try {
            val doc = firestore.collection("usernames").document(usernameKey).get().await()
            !doc.exists()
        } catch (_: Exception) {
            // Jika offline atau error jaringan, lempar exception agar bisa ditangani
            throw IllegalStateException("Gagal memeriksa ketersediaan nama pengguna di cloud.")
        }
    }

    /**
     * Mendaftarkan akun baru ke Firebase Authentication dan menyimpan profil ke Cloud Firestore.
     * Menggunakan transaksi aman agar username ter-reserve secara atomik.
     */
    suspend fun register(
        username: String,
        email: String,
        password: String,
        fullName: String
    ): Result<UserAccount> {
        val trimmedEmail = email.trim()
        val trimmedUsername = username.trim()
        val usernameKey = trimmedUsername.lowercase()

        // 1. Periksa ketersediaan username di Firestore terlebih dahulu
        try {
            val usernameDoc = firestore.collection("usernames").document(usernameKey).get().await()
            if (usernameDoc.exists()) {
                return Result.failure(IllegalArgumentException("Username '$trimmedUsername' sudah digunakan oleh akun lain."))
            }
        } catch (e: Exception) {
            if (e is IllegalArgumentException) return Result.failure(e)
            return Result.failure(Exception("Koneksi gagal saat memeriksa ketersediaan username: ${e.localizedMessage}"))
        }

        // 2. Buat akun di Firebase Authentication
        val authResult = try {
            auth.createUserWithEmailAndPassword(trimmedEmail, password).await()
        } catch (e: Exception) {
            return Result.failure(e)
        }

        val firebaseUser = authResult.user
            ?: return Result.failure(IllegalStateException("Firebase Auth gagal menghasilkan objek pengguna."))
        val uid = firebaseUser.uid

        // 3. Update Display Name di Firebase Auth
        try {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(fullName.ifBlank { trimmedUsername })
                .build()
            firebaseUser.updateProfile(profileUpdates).await()
        } catch (_: Exception) {
            // Non-fatal, lanjutkan penyimpanan Firestore
        }

        val now = System.currentTimeMillis()
        val userDto = UserDto(
            uid = uid,
            username = trimmedUsername,
            email = trimmedEmail,
            fullName = fullName.ifBlank { trimmedUsername },
            createdAt = now,
            updatedAt = now
        )

        val reservationDto = UsernameReservationDto(
            uid = uid,
            email = trimmedEmail,
            reservedAt = now
        )

        // 4. Simpan profil dan reservasi username di Firestore secara atomik
        return try {
            val batch = firestore.batch()
            val userRef = firestore.collection("users").document(uid)
            val usernameRef = firestore.collection("usernames").document(usernameKey)

            batch.set(userRef, userDto.toMap())
            batch.set(usernameRef, reservationDto.toMap())
            batch.commit().await()

            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            // Mekanisme pemulihan (Rollback akun Firebase Auth jika simpan ke Firestore gagal)
            try {
                firebaseUser.delete().await()
            } catch (_: Exception) {}
            Result.failure(Exception("Gagal menyimpan profil ke Firestore: ${e.localizedMessage}"))
        }
    }

    /**
     * Login menggunakan Email atau Username.
     * Jika input adalah username (tanpa '@'), sistem mencari email terkait di `usernames/{username}` Firestore.
     */
    suspend fun login(
        identifier: String,
        password: String
    ): Result<UserAccount> {
        val trimmedIdentifier = identifier.trim()
        val resolvedEmail: String

        // Jika identifier bukan format email, cari email dari Firestore `usernames`
        if (!trimmedIdentifier.contains("@")) {
            val usernameKey = trimmedIdentifier.lowercase()
            try {
                val usernameDoc = firestore.collection("usernames").document(usernameKey).get().await()
                if (!usernameDoc.exists()) {
                    return Result.failure(IllegalArgumentException("Username '$trimmedIdentifier' tidak ditemukan."))
                }
                val foundEmail = usernameDoc.getString("email")
                if (foundEmail.isNullOrBlank()) {
                    return Result.failure(IllegalArgumentException("Data akun untuk username ini tidak valid."))
                }
                resolvedEmail = foundEmail
            } catch (e: Exception) {
                if (e is IllegalArgumentException) return Result.failure(e)
                return Result.failure(Exception("Gagal memverifikasi username ke server: ${e.localizedMessage}"))
            }
        } else {
            resolvedEmail = trimmedIdentifier
        }

        // Lakukan autentikasi Firebase
        val authResult = try {
            auth.signInWithEmailAndPassword(resolvedEmail, password).await()
        } catch (e: Exception) {
            return Result.failure(e)
        }

        val firebaseUser = authResult.user
            ?: return Result.failure(IllegalStateException("Pengguna tidak ditemukan."))
        val uid = firebaseUser.uid

        // Ambil profil dari Firestore
        return try {
            val userDoc = firestore.collection("users").document(uid).get().await()
            if (userDoc.exists() && userDoc.data != null) {
                val userDto = UserDto.fromMap(userDoc.data!!)
                Result.success(userDto.toDomain())
            } else {
                // Fallback: Jika profil Firestore belum ada, buat dari data FirebaseUser
                val fallbackUser = UserAccount(
                    uid = uid,
                    username = firebaseUser.displayName ?: resolvedEmail.substringBefore("@"),
                    email = resolvedEmail,
                    fullName = firebaseUser.displayName ?: ""
                )
                // Buat dokumen di Firestore
                firestore.collection("users").document(uid).set(fallbackUser.toDto().toMap()).await()
                Result.success(fallbackUser)
            }
        } catch (e: Exception) {
            // Jika gagal mengambil dari Firestore (misal kendala jaringan sementara), fallback ke objek Firebase Auth
            val fallbackUser = UserAccount(
                uid = uid,
                username = firebaseUser.displayName ?: resolvedEmail.substringBefore("@"),
                email = resolvedEmail,
                fullName = firebaseUser.displayName ?: ""
            )
            Result.success(fallbackUser)
        }
    }

    /**
     * Mengambil profil pengguna dari Cloud Firestore berdasarkan UID.
     */
    suspend fun fetchUserProfile(uid: String): Result<UserAccount> {
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            if (doc.exists() && doc.data != null) {
                Result.success(UserDto.fromMap(doc.data!!).toDomain())
            } else {
                Result.failure(NoSuchElementException("Profil pengguna tidak ditemukan di Firestore."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Mengirim email reset password melalui Firebase Authentication.
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        val trimmedEmail = email.trim()
        return try {
            auth.sendPasswordResetEmail(trimmedEmail).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }
}
