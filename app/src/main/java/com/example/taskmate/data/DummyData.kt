package com.example.taskmate.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class CourseItem(
    val id: Int,
    val title: String,
    val day: String,
    val time: String,
    val room: String,
    val lecturer: String = "Dr. Eng. Herman Tolle, S.T., M.T."
)

data class TaskItem(
    val id: Int,
    val title: String,
    val course: String,
    val daysLeft: String,
    val dueDate: String,
    val status: String = "In Progress",
    val description: String = "Complete the exercise assigned in class and upload the report before the deadline."
)

object DummyData {
    var finishedTasksCount by mutableStateOf(20)

    // -------------------------------------------------------------------------
    // 1. Course List
    // -------------------------------------------------------------------------
    val courses = mutableStateListOf(
        CourseItem(
            id = 1,
            title = "PAPB D",
            day = "Tuesday",
            time = "09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 2,
            title = "PAPB D",
            day = "Tuesday",
            time = "09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 3,
            title = "PAPB D",
            day = "Tuesday",
            time = "09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 4,
            title = "PAPB D",
            day = "Tuesday",
            time = "09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 5,
            title = "PAPB D",
            day = "Tuesday",
            time = "09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 6,
            title = "PAPB D",
            day = "Tuesday",
            time = "09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        )
    )

    // -------------------------------------------------------------------------
    // 2. Schedule List
    // -------------------------------------------------------------------------
    val schedules = mutableStateListOf(
        CourseItem(
            id = 101,
            title = "JST D",
            day = "Monday",
            time = "10:30 AM - 12:10 PM",
            room = "Room G1.2",
            lecturer = "Prof. Agus Widodo"
        ),
        CourseItem(
            id = 102,
            title = "Keamanan Jaringan A",
            day = "Monday",
            time = "12:30 PM - 03:15 PM",
            room = "Room F4.6",
            lecturer = "Ir. Tri Astoto, M.Kom."
        ),
        CourseItem(
            id = 103,
            title = "JST D",
            day = "Tuesday",
            time = "07:00 AM - 08:40 AM",
            room = "Room F4.2",
            lecturer = "Prof. Agus Widodo"
        ),
        CourseItem(
            id = 104,
            title = "PAPB D",
            day = "Tuesday",
            time = "09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 105,
            title = "Komputer Grafis D",
            day = "Tuesday",
            time = "03:20 PM - 05:00 PM",
            room = "Room F3.1",
            lecturer = "Sabriansyah Rizqika, S.T., M.Eng."
        )
    )

    // -------------------------------------------------------------------------
    // 3. Task List
    // -------------------------------------------------------------------------
    val tasks = mutableStateListOf(
        TaskItem(
            id = 1,
            title = "Assignment 1: abcd",
            course = "Course A",
            daysLeft = "7 Days left",
            dueDate = "Due Friday, Oct 16\n11:59 PM",
            status = "In Progress",
            description = "Complete the exercise assigned in class and upload the report before the deadline."
        ),
        TaskItem(
            id = 2,
            title = "Assignment 1: abcd",
            course = "Course A",
            daysLeft = "7 Days left",
            dueDate = "Due Friday, Oct 16\n11:59 PM",
            status = "In Progress",
            description = "Complete the exercise assigned in class and upload the report before the deadline."
        ),
        TaskItem(
            id = 3,
            title = "Assignment 1: abcd",
            course = "Course A",
            daysLeft = "7 Days left",
            dueDate = "Due Friday, Oct 16\n11:59 PM",
            status = "In Progress",
            description = "Complete the exercise assigned in class and upload the report before the deadline."
        ),
        TaskItem(
            id = 4,
            title = "Assignment 1: abcd",
            course = "Course A",
            daysLeft = "7 Days left",
            dueDate = "Due Friday, Oct 16\n11:59 PM",
            status = "In Progress",
            description = "Complete the exercise assigned in class and upload the report before the deadline."
        ),
        TaskItem(
            id = 5,
            title = "Assignment 1: abcd",
            course = "Course A",
            daysLeft = "7 Days left",
            dueDate = "Due Friday, Oct 16\n11:59 PM",
            status = "In Progress",
            description = "Complete the exercise assigned in class and upload the report before the deadline."
        )
    )

    fun getCourseById(id: Int): CourseItem? {
        return courses.find { it.id == id } ?: schedules.find { it.id == id }
    }

    fun getTaskById(id: Int): TaskItem? {
        return tasks.find { it.id == id }
    }

    fun addCourse(course: CourseItem) {
        courses.add(course)
        if (course.day.isNotBlank()) {
            schedules.add(course)
        }
    }

    fun updateCourse(course: CourseItem) {
        val courseIndex = courses.indexOfFirst { it.id == course.id }
        if (courseIndex != -1) {
            courses[courseIndex] = course
        }
        val scheduleIndex = schedules.indexOfFirst { it.id == course.id }
        if (scheduleIndex != -1) {
            schedules[scheduleIndex] = course
        }
    }

    fun addTask(task: TaskItem) {
        tasks.add(0, task)
    }

    fun updateTask(task: TaskItem) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
        }
    }

    fun markTaskFinished(id: Int) {
        val index = tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            val task = tasks[index]
            tasks[index] = task.copy(status = "Finished", daysLeft = "Completed")
            finishedTasksCount++
        }
    }

    fun deleteCourse(id: Int): Boolean {
        val courseRemoved = courses.removeAll { it.id == id }
        val scheduleRemoved = schedules.removeAll { it.id == id }
        return courseRemoved || scheduleRemoved
    }

    fun deleteTask(id: Int): Boolean {
        return tasks.removeAll { it.id == id }
    }

    fun generateCourseId(): Int {
        val maxCoursesId = courses.maxOfOrNull { it.id } ?: 0
        val maxScheduleId = schedules.maxOfOrNull { it.id } ?: 0
        return maxOf(maxCoursesId, maxScheduleId) + 1
    }

    fun generateTaskId(): Int {
        return (tasks.maxOfOrNull { it.id } ?: 0) + 1
    }
}