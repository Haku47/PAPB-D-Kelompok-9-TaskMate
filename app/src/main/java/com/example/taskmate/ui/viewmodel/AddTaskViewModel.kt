package com.example.taskmate.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskmate.data.DummyData
import com.example.taskmate.data.TaskItem

/**
 * ViewModel untuk AddTaskScreen.
 * Mempertahankan state saat orientasi layar berubah (Portrait <-> Landscape),
 * mengelola mekanisme validasi setiap elemen input,
 * serta menggabungkan dua input berbeda (Tanggal dan Waktu) menjadi string Tenggat Waktu tugas.
 */
class AddTaskViewModel : ViewModel() {
    var taskTitle by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var selectedCourse by mutableStateOf("Course A")
        private set

    // Dua input yang akan diolah & digabungkan menjadi Tenggat Waktu lengkap:
    var dueDatePart by mutableStateOf("Friday, Oct 16")
        private set
    var dueTimePart by mutableStateOf("11:59 PM")
        private set

    // State Validasi Error
    var taskTitleError by mutableStateOf<String?>(null)
        private set
    var descriptionError by mutableStateOf<String?>(null)
        private set
    var courseError by mutableStateOf<String?>(null)
        private set
    var dueDateError by mutableStateOf<String?>(null)
        private set
    var dueTimeError by mutableStateOf<String?>(null)
        private set

    // Data Olahan / Gabungan: Tanggal dan Waktu digabungkan menjadi Tenggat Waktu
    val combinedDueDate: String
        get() = "Due $dueDatePart\n$dueTimePart"

    fun onTaskTitleChange(value: String) {
        taskTitle = value
        if (taskTitleError != null && value.isNotBlank()) taskTitleError = null
    }

    fun onDescriptionChange(value: String) {
        description = value
        if (descriptionError != null && value.isNotBlank()) descriptionError = null
    }

    fun onCourseChange(value: String) {
        selectedCourse = value
        if (courseError != null && value.isNotBlank()) courseError = null
    }

    fun onDueDateChange(value: String) {
        dueDatePart = value
        if (dueDateError != null && value.isNotBlank()) dueDateError = null
    }

    fun onDueTimeChange(value: String) {
        dueTimePart = value
        if (dueTimeError != null && value.isNotBlank()) dueTimeError = null
    }

    fun validate(
        errTitleReq: String,
        errDescReq: String,
        errCourseReq: String,
        errDateReq: String,
        errTimeReq: String
    ): Boolean {
        var isValid = true

        if (taskTitle.isBlank()) {
            taskTitleError = errTitleReq
            isValid = false
        } else {
            taskTitleError = null
        }

        if (description.isBlank()) {
            descriptionError = errDescReq
            isValid = false
        } else {
            descriptionError = null
        }

        if (selectedCourse.isBlank()) {
            courseError = errCourseReq
            isValid = false
        } else {
            courseError = null
        }

        if (dueDatePart.isBlank()) {
            dueDateError = errDateReq
            isValid = false
        } else {
            dueDateError = null
        }

        if (dueTimePart.isBlank()) {
            dueTimeError = errTimeReq
            isValid = false
        } else {
            dueTimeError = null
        }

        return isValid
    }

    fun submitTask(
        errTitleReq: String,
        errDescReq: String,
        errCourseReq: String,
        errDateReq: String,
        errTimeReq: String,
        onSuccess: () -> Unit
    ) {
        if (!validate(errTitleReq, errDescReq, errCourseReq, errDateReq, errTimeReq)) {
            return
        }

        // Penggabungan data (Combined Input):
        // Menggabungkan Tanggal dan Waktu menjadi satu format tenggat waktu utuh
        val finalDueDate = combinedDueDate

        val newTask = TaskItem(
            id = DummyData.generateTaskId(),
            title = taskTitle.trim(),
            course = selectedCourse.trim(),
            daysLeft = "7 Days left",
            dueDate = finalDueDate,
            status = "In Progress",
            description = description.trim()
        )

        DummyData.addTask(newTask)
        onSuccess()
    }
}

