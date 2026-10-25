package com.example.taskmate.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskmate.data.DummyData
import com.example.taskmate.data.TaskItem
import com.example.taskmate.ui.components.TaskDateHelper

/**
 * ViewModel untuk EditTaskDetailScreen.
 * Mempertahankan data isian saat rotasi layar, mengelola validasi setiap elemen input,
 * dan memproses penggabungan tanggal (kalender) & waktu (jam) menjadi tenggat waktu utuh.
 */
class EditTaskViewModel : ViewModel() {
    var taskId by mutableStateOf<Int?>(null)
        private set
    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var selectedCourse by mutableStateOf("")
        private set
    var dueDatePart by mutableStateOf("Friday, Oct 16")
        private set
    var dueTimePart by mutableStateOf("11:59 PM")
        private set

    // Validation Errors
    var titleError by mutableStateOf<String?>(null)
        private set
    var descriptionError by mutableStateOf<String?>(null)
        private set
    var courseError by mutableStateOf<String?>(null)
        private set
    var dueDateError by mutableStateOf<String?>(null)
        private set
    var dueTimeError by mutableStateOf<String?>(null)
        private set

    val combinedDueDate: String
        get() = TaskDateHelper.buildDueDate(dueDatePart, dueTimePart)

    fun loadTask(id: Int) {
        if (taskId == id) return // Data sudah dimuat, pertahankan saat rotasi!
        taskId = id
        val task = DummyData.getTaskById(id) ?: return
        title = task.title
        description = task.description
        selectedCourse = task.course

        val parsed = TaskDateHelper.parseDueDate(task.dueDate)
        dueDatePart = parsed.first.ifBlank { "Friday, Oct 16" }
        dueTimePart = parsed.second.ifBlank { "11:59 PM" }
    }

    fun onTitleChange(value: String) {
        title = value
        if (titleError != null && value.isNotBlank()) titleError = null
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

        if (title.isBlank()) {
            titleError = errTitleReq
            isValid = false
        } else {
            titleError = null
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

    fun saveChanges(
        errTitleReq: String,
        errDescReq: String,
        errCourseReq: String,
        errDateReq: String,
        errTimeReq: String,
        onSuccess: () -> Unit
    ) {
        val id = taskId ?: return
        if (!validate(errTitleReq, errDescReq, errCourseReq, errDateReq, errTimeReq)) {
            return
        }

        val existing = DummyData.getTaskById(id)
        val updatedTask = TaskItem(
            id = id,
            title = title.trim(),
            course = selectedCourse.trim(),
            daysLeft = existing?.daysLeft ?: "7 Days left",
            dueDate = combinedDueDate,
            status = existing?.status ?: "In Progress",
            description = description.trim()
        )

        DummyData.updateTask(updatedTask)
        onSuccess()
    }
}

