package com.example.taskmate.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.taskmate.ui.components.CourseTimeHelper
import org.json.JSONArray
import org.json.JSONObject

data class CourseSession(
    val day: String,
    val time: String,
    val room: String = ""
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("day", day)
        put("time", time)
        put("room", room)
    }

    companion object {
        fun fromJson(json: JSONObject): CourseSession = CourseSession(
            day = json.optString("day", ""),
            time = json.optString("time", ""),
            room = json.optString("room", "")
        )
    }
}

data class SessionInputState(
    val day: String = "",
    val startTime: String = "09:35 AM",
    val endTime: String = "11:15 AM",
    val room: String = ""
)

fun CourseSession.toInputState(): SessionInputState {
    val times = CourseTimeHelper.parseTimeRange(time)
    return SessionInputState(
        day = day,
        startTime = times.first.ifBlank { "09:35 AM" },
        endTime = times.second.ifBlank { "11:15 AM" },
        room = room
    )
}

fun SessionInputState.toCourseSession(): CourseSession {
    val range = CourseTimeHelper.buildTimeRange(startTime, endTime)
    return CourseSession(
        day = CourseTimeHelper.normalizeDay(day),
        time = range,
        room = room
    )
}

data class CourseItem(
    val id: Int,
    val title: String,
    val day: String,
    val time: String,
    val room: String,
    val lecturer: String = "Dr. Eng. Herman Tolle, S.T., M.T.",
    val sessions: List<CourseSession> = emptyList()
) {
    val allSessions: List<CourseSession>
        get() = if (sessions.isNotEmpty()) {
            sessions
        } else if (day.isNotBlank() || time.isNotBlank()) {
            listOf(CourseSession(day = day, time = time, room = room))
        } else {
            emptyList()
        }

    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("title", title)
        put("day", day)
        put("time", time)
        put("room", room)
        put("lecturer", lecturer)
        val arr = JSONArray()
        sessions.forEach { arr.put(it.toJson()) }
        put("sessions", arr)
    }

    companion object {
        fun fromJson(json: JSONObject): CourseItem {
            val sessionsList = mutableListOf<CourseSession>()
            val sessionsArray = json.optJSONArray("sessions")
            if (sessionsArray != null) {
                for (i in 0 until sessionsArray.length()) {
                    val sObj = sessionsArray.optJSONObject(i)
                    if (sObj != null) sessionsList.add(CourseSession.fromJson(sObj))
                }
            }
            return CourseItem(
                id = json.optInt("id", 0),
                title = json.optString("title", ""),
                day = json.optString("day", ""),
                time = json.optString("time", ""),
                room = json.optString("room", ""),
                lecturer = json.optString("lecturer", "Dr. Eng. Herman Tolle, S.T., M.T."),
                sessions = sessionsList
            )
        }
    }
}

data class TaskItem(
    val id: Int,
    val title: String,
    val course: String,
    val daysLeft: String,
    val dueDate: String,
    val status: String = "In Progress",
    val description: String = "Complete the exercise assigned in class and upload the report before the deadline."
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("title", title)
        put("course", course)
        put("daysLeft", daysLeft)
        put("dueDate", dueDate)
        put("status", status)
        put("description", description)
    }

    companion object {
        fun fromJson(json: JSONObject): TaskItem = TaskItem(
            id = json.optInt("id", 0),
            title = json.optString("title", ""),
            course = json.optString("course", ""),
            daysLeft = json.optString("daysLeft", ""),
            dueDate = json.optString("dueDate", ""),
            status = json.optString("status", "In Progress"),
            description = json.optString("description", "")
        )
    }
}

object DummyData {
    private var prefs: SharedPreferences? = null
    var finishedTasksCount by mutableStateOf(20)

    val courses = mutableStateListOf<CourseItem>()
    val schedules = mutableStateListOf<CourseItem>()
    val tasks = mutableStateListOf<TaskItem>()

    private val initialCourses = listOf(
        CourseItem(
            id = 1,
            title = "PAPB D",
            day = "Tuesday",
            time = "09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T.",
            sessions = listOf(
                CourseSession(day = "Tuesday", time = "09:35 AM - 11:15 AM", room = "Room RV1.3"),
                CourseSession(day = "Friday", time = "12:30 PM - 02:10 PM", room = "Room RV1.3")
            )
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

    private val initialSchedules = listOf(
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
        ),
        CourseItem(
            id = 107,
            title = "KI",
            day = "Wednesday",
            time = "10:30 AM - 12:10 PM",
            room = "Room F4.2",
            lecturer = "Ir. Tri Astoto, M.Kom."
        ),
        CourseItem(
            id = 108,
            title = "Jaringan Nirkabel",
            day = "Thursday",
            time = "09:35 AM - 12:10 PM",
            room = "Room G1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 106,
            title = "PAPB D",
            day = "Friday",
            time = "12:30 PM - 02:10 PM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        )
    )

    private val initialTasks = listOf(
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

    init {
        resetToInitialDefaults()
    }

    fun resetToInitialDefaults() {
        courses.clear()
        courses.addAll(initialCourses)
        schedules.clear()
        schedules.addAll(initialSchedules)
        tasks.clear()
        tasks.addAll(initialTasks)
        finishedTasksCount = 20
    }

    /**
     * Menginisialisasi penyimpanan lokal SharedPreferences agar data Course dan Task
     * tetap tersimpan meskipun aplikasi ditutup / diberhentikan.
     */
    fun init(context: Context) {
        prefs = context.getSharedPreferences("taskmate_storage", Context.MODE_PRIVATE)
        loadData()
    }

    private fun loadData() {
        val sp = prefs ?: return
        val coursesJson = sp.getString("courses", null)
        val schedulesJson = sp.getString("schedules", null)
        val tasksJson = sp.getString("tasks", null)
        finishedTasksCount = sp.getInt("finished_count", 20)

        if (coursesJson != null) {
            try {
                courses.clear()
                val arr = JSONArray(coursesJson)
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i)
                    if (obj != null) courses.add(CourseItem.fromJson(obj))
                }
            } catch (e: Exception) {
                courses.clear()
                courses.addAll(initialCourses)
            }
        } else {
            saveData()
        }

        if (schedulesJson != null) {
            try {
                schedules.clear()
                val arr = JSONArray(schedulesJson)
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i)
                    if (obj != null) schedules.add(CourseItem.fromJson(obj))
                }
            } catch (e: Exception) {
                schedules.clear()
                schedules.addAll(initialSchedules)
            }
        } else {
            saveData()
        }

        if (tasksJson != null) {
            try {
                tasks.clear()
                val arr = JSONArray(tasksJson)
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i)
                    if (obj != null) tasks.add(TaskItem.fromJson(obj))
                }
            } catch (e: Exception) {
                tasks.clear()
                tasks.addAll(initialTasks)
            }
        } else {
            saveData()
        }
    }

    private fun saveData() {
        val sp = prefs ?: return
        try {
            val coursesArr = JSONArray()
            courses.forEach { coursesArr.put(it.toJson()) }

            val schedulesArr = JSONArray()
            schedules.forEach { schedulesArr.put(it.toJson()) }

            val tasksArr = JSONArray()
            tasks.forEach { tasksArr.put(it.toJson()) }

            sp.edit()
                .putString("courses", coursesArr.toString())
                .putString("schedules", schedulesArr.toString())
                .putString("tasks", tasksArr.toString())
                .putInt("finished_count", finishedTasksCount)
                .apply()
        } catch (_: Exception) {
        }
    }

    fun getCourseById(id: Int): CourseItem? {
        return courses.find { it.id == id } ?: schedules.find { it.id == id }
    }

    fun getTaskById(id: Int): TaskItem? {
        return tasks.find { it.id == id }
    }

    fun addCourse(course: CourseItem) {
        courses.add(course)
        val sessionList = course.allSessions
        for (session in sessionList) {
            val normDay = CourseTimeHelper.normalizeDay(session.day)
            if (normDay.isNotBlank()) {
                schedules.add(
                    CourseItem(
                        id = course.id,
                        title = course.title,
                        day = normDay,
                        time = session.time,
                        room = session.room,
                        lecturer = course.lecturer,
                        sessions = course.sessions
                    )
                )
            }
        }
        saveData()
    }

    fun updateCourse(course: CourseItem) {
        val courseIndex = courses.indexOfFirst { it.id == course.id }
        if (courseIndex != -1) {
            courses[courseIndex] = course
        }
        schedules.removeAll { it.id == course.id }
        val sessionList = course.allSessions
        for (session in sessionList) {
            val normDay = CourseTimeHelper.normalizeDay(session.day)
            if (normDay.isNotBlank()) {
                schedules.add(
                    CourseItem(
                        id = course.id,
                        title = course.title,
                        day = normDay,
                        time = session.time,
                        room = session.room,
                        lecturer = course.lecturer,
                        sessions = course.sessions
                    )
                )
            }
        }
        saveData()
    }

    fun addTask(task: TaskItem) {
        tasks.add(0, task)
        saveData()
    }

    fun updateTask(task: TaskItem) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
            saveData()
        }
    }

    fun markTaskFinished(id: Int) {
        val index = tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            val task = tasks[index]
            tasks[index] = task.copy(status = "Finished", daysLeft = "Completed")
            finishedTasksCount++
            saveData()
        }
    }

    fun deleteCourse(id: Int): Boolean {
        val courseRemoved = courses.removeAll { it.id == id }
        val scheduleRemoved = schedules.removeAll { it.id == id }
        saveData()
        return courseRemoved || scheduleRemoved
    }

    fun deleteTask(id: Int): Boolean {
        val removed = tasks.removeAll { it.id == id }
        saveData()
        return removed
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
