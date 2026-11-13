package com.example.taskmate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskmate.data.CourseItem
import com.example.taskmate.data.CourseSession
import org.json.JSONArray
import org.json.JSONObject

/**
 * Entitas tabel "courses" untuk persistence database SQLite dengan Room.
 */
@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val day: String,
    val time: String,
    val room: String,
    val lecturer: String,
    val isSchedule: Boolean = false, // Flag untuk membedakan mata kuliah utama dan entitas jadwal harian
    val sessionsJson: String = "[]"
)

fun CourseEntity.toDomain(): CourseItem {
    val sessionsList = mutableListOf<CourseSession>()
    if (sessionsJson.isNotBlank()) {
        try {
            val arr = JSONArray(sessionsJson)
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i)
                if (obj != null) {
                    sessionsList.add(CourseSession.fromJson(obj))
                }
            }
        } catch (_: Exception) {}
    }
    return CourseItem(
        id = id,
        title = title,
        day = day,
        time = time,
        room = room,
        lecturer = lecturer,
        sessions = sessionsList
    )
}

fun CourseItem.toEntity(isSchedule: Boolean = false): CourseEntity {
    val arr = JSONArray()
    sessions.forEach { arr.put(it.toJson()) }
    return CourseEntity(
        id = id,
        title = title,
        day = day,
        time = time,
        room = room,
        lecturer = lecturer,
        isSchedule = isSchedule,
        sessionsJson = arr.toString()
    )
}
