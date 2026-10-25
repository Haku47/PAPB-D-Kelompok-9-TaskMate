package com.example.taskmate.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskmate.data.CourseItem
import com.example.taskmate.data.CourseSession
import com.example.taskmate.data.DummyData
import com.example.taskmate.data.SessionInputState
import com.example.taskmate.data.toCourseSession

data class SessionErrorState(
    val dayError: String? = null,
    val roomError: String? = null
)

/**
 * ViewModel untuk AddCourseScreen.
 * Bertanggung jawab mempertahankan state saat orientasi layar berubah (Portrait <-> Landscape)
 * dan mengelola mekanisme validasi setiap elemen input serta penggabungan input (Nama Dosen & Waktu).
 */
class AddCourseViewModel : ViewModel() {
    var courseTitle by mutableStateOf("")
        private set

    // Dua input terpisah yang akan diolah & digabungkan menjadi Nama Lengkap Dosen:
    var lecturerFirstName by mutableStateOf("")
        private set
    var lecturerLastName by mutableStateOf("")
        private set

    val sessions = mutableStateListOf(SessionInputState())

    // State Validasi Error
    var courseTitleError by mutableStateOf<String?>(null)
        private set
    var lecturerFirstNameError by mutableStateOf<String?>(null)
        private set
    var lecturerLastNameError by mutableStateOf<String?>(null)
        private set
    val sessionErrors = mutableStateListOf(SessionErrorState())

    // Data Olahan / Gabungan:
    val fullLecturerName: String
        get() = "$lecturerFirstName $lecturerLastName".trim()

    val primarySessionCombinedTime: String
        get() {
            val first = sessions.firstOrNull() ?: return ""
            return "${first.startTime} - ${first.endTime}"
        }

    fun onCourseTitleChange(value: String) {
        courseTitle = value
        if (courseTitleError != null && value.isNotBlank()) {
            courseTitleError = null
        }
    }

    fun onLecturerFirstNameChange(value: String) {
        lecturerFirstName = value
        if (lecturerFirstNameError != null && value.isNotBlank()) {
            lecturerFirstNameError = null
        }
    }

    fun onLecturerLastNameChange(value: String) {
        lecturerLastName = value
        if (lecturerLastNameError != null && value.isNotBlank()) {
            lecturerLastNameError = null
        }
    }

    fun onUpdateSession(index: Int, updated: SessionInputState) {
        if (index in sessions.indices) {
            sessions[index] = updated
            if (index in sessionErrors.indices) {
                val currentErr = sessionErrors[index]
                sessionErrors[index] = currentErr.copy(
                    dayError = if (updated.day.isNotBlank()) null else currentErr.dayError,
                    roomError = if (updated.room.isNotBlank()) null else currentErr.roomError
                )
            }
        }
    }

    fun addSession() {
        if (sessions.size < 5) {
            val lastRoom = sessions.lastOrNull()?.room ?: ""
            sessions.add(
                SessionInputState(
                    day = "",
                    startTime = "09:35 AM",
                    endTime = "11:15 AM",
                    room = lastRoom
                )
            )
            sessionErrors.add(SessionErrorState())
        }
    }

    fun removeSession(index: Int) {
        if (sessions.size > 1 && index in sessions.indices) {
            sessions.removeAt(index)
            if (index in sessionErrors.indices) {
                sessionErrors.removeAt(index)
            }
        }
    }

    fun validate(
        errCourseTitleReq: String,
        errLecturerFirstReq: String,
        errLecturerLastReq: String,
        errDayReq: String,
        errRoomReq: String
    ): Boolean {
        var isValid = true

        if (courseTitle.isBlank()) {
            courseTitleError = errCourseTitleReq
            isValid = false
        } else {
            courseTitleError = null
        }

        if (lecturerFirstName.isBlank()) {
            lecturerFirstNameError = errLecturerFirstReq
            isValid = false
        } else {
            lecturerFirstNameError = null
        }

        if (lecturerLastName.isBlank()) {
            lecturerLastNameError = errLecturerLastReq
            isValid = false
        } else {
            lecturerLastNameError = null
        }

        while (sessionErrors.size < sessions.size) {
            sessionErrors.add(SessionErrorState())
        }

        sessions.forEachIndexed { i, session ->
            val dayErr = if (session.day.isBlank()) {
                isValid = false
                errDayReq
            } else null

            val roomErr = if (session.room.isBlank()) {
                isValid = false
                errRoomReq
            } else null

            sessionErrors[i] = SessionErrorState(dayError = dayErr, roomError = roomErr)
        }

        return isValid
    }

    fun submitCourse(
        errCourseTitleReq: String,
        errLecturerFirstReq: String,
        errLecturerLastReq: String,
        errDayReq: String,
        errRoomReq: String,
        onSuccess: () -> Unit
    ) {
        if (!validate(errCourseTitleReq, errLecturerFirstReq, errLecturerLastReq, errDayReq, errRoomReq)) {
            return
        }

        // Penggabungan data (Combined Input):
        // 1. Nama Dosen: Gabungan dari Nama Depan dan Nama Belakang
        val combinedLecturer = fullLecturerName
        // 2. Waktu Sesi: Gabungan dari Start Time dan End Time
        val courseSessions = sessions.map { it.toCourseSession() }
        val primarySession = courseSessions.firstOrNull()
            ?: CourseSession("Monday", "08:00 AM - 10:00 AM", "Room -")

        val newCourse = CourseItem(
            id = DummyData.generateCourseId(),
            title = courseTitle.trim(),
            day = primarySession.day,
            time = primarySession.time,
            room = primarySession.room,
            lecturer = combinedLecturer,
            sessions = courseSessions
        )

        DummyData.addCourse(newCourse)
        onSuccess()
    }
}

