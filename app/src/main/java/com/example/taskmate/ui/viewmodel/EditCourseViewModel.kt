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
import com.example.taskmate.data.toInputState

class EditCourseViewModel : ViewModel() {
    var courseId by mutableStateOf<Int?>(null)
        private set
    var courseTitle by mutableStateOf("")
        private set
    var lecturerFirstName by mutableStateOf("")
        private set
    var lecturerLastName by mutableStateOf("")
        private set
    val sessions = mutableStateListOf<SessionInputState>()

    var courseTitleError by mutableStateOf<String?>(null)
        private set
    var lecturerFirstNameError by mutableStateOf<String?>(null)
        private set
    var lecturerLastNameError by mutableStateOf<String?>(null)
        private set
    val sessionErrors = mutableStateListOf<SessionErrorState>()

    val fullLecturerName: String
        get() = "$lecturerFirstName $lecturerLastName".trim()

    fun loadCourse(id: Int) {
        if (courseId == id) return // Sudah dimuat, pertahankan saat rotasi layar!
        courseId = id
        val course = DummyData.getCourseById(id) ?: return
        courseTitle = course.title
        val parts = course.lecturer.trim().split(" ", limit = 2)
        lecturerFirstName = parts.getOrNull(0) ?: ""
        lecturerLastName = parts.getOrNull(1) ?: ""

        sessions.clear()
        sessionErrors.clear()
        val allSes = course.allSessions
        if (allSes.isNotEmpty()) {
            allSes.forEach {
                sessions.add(it.toInputState())
                sessionErrors.add(SessionErrorState())
            }
        } else {
            sessions.add(SessionInputState())
            sessionErrors.add(SessionErrorState())
        }
    }

    fun onCourseTitleChange(value: String) {
        courseTitle = value
        if (courseTitleError != null && value.isNotBlank()) courseTitleError = null
    }

    fun onLecturerFirstNameChange(value: String) {
        lecturerFirstName = value
        if (lecturerFirstNameError != null && value.isNotBlank()) lecturerFirstNameError = null
    }

    fun onLecturerLastNameChange(value: String) {
        lecturerLastName = value
        if (lecturerLastNameError != null && value.isNotBlank()) lecturerLastNameError = null
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

    fun saveChanges(
        errCourseTitleReq: String,
        errLecturerFirstReq: String,
        errLecturerLastReq: String,
        errDayReq: String,
        errRoomReq: String,
        onSuccess: () -> Unit
    ) {
        val id = courseId ?: return
        if (!validate(errCourseTitleReq, errLecturerFirstReq, errLecturerLastReq, errDayReq, errRoomReq)) {
            return
        }

        val combinedLecturer = fullLecturerName
        val courseSessions = sessions.map { it.toCourseSession() }
        val primarySession = courseSessions.firstOrNull()
            ?: CourseSession("Monday", "08:00 AM - 10:00 AM", "Room -")

        val updatedCourse = CourseItem(
            id = id,
            title = courseTitle.trim(),
            day = primarySession.day,
            time = primarySession.time,
            room = primarySession.room,
            lecturer = combinedLecturer,
            sessions = courseSessions
        )

        DummyData.updateCourse(updatedCourse)
        onSuccess()
    }
}

