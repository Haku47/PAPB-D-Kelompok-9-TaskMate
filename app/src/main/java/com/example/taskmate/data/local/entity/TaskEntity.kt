package com.example.taskmate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskmate.data.TaskItem

// Table tasks untuk menyimpan data tugas
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val course: String,
    val daysLeft: String,
    val dueDate: String,
    val status: String = "In Progress",
    val description: String = ""
)

fun TaskEntity.toDomain(): TaskItem = TaskItem(
    id = id,
    title = title,
    course = course,
    daysLeft = daysLeft,
    dueDate = dueDate,
    status = status,
    description = description
)

fun TaskItem.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    course = course,
    daysLeft = daysLeft,
    dueDate = dueDate,
    status = status,
    description = description
)
