package com.example.taskmate.data.remote.api

import android.content.Context
import com.example.taskmate.data.TaskItem
import com.example.taskmate.data.local.AppDatabase
import com.example.taskmate.data.local.entity.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repository sync tugas dari REST API ke Room DB
class TaskSyncRepository(
    private val apiService: TaskApiService = TaskApiService.create()
) {
    // Ambil data tugas dari API dan simpan ke Room
    suspend fun syncRemoteTasksToRoom(context: Context): Result<List<TaskItem>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.fetchRemoteTasks()
            if (response.isSuccessful && response.body() != null) {
                val remoteTasks = response.body()!!.todos
                val convertedTasks = remoteTasks.mapIndexed { index, dto ->
                    TaskItem(
                        id = 5000 + dto.id,
                        title = "Assignment ${index + 1}: ${dto.todo.take(15)}",
                        course = "Course A",
                        daysLeft = if (dto.completed) "Completed" else "7 Days left",
                        dueDate = "Due Friday, Oct 16\n11:59 PM",
                        status = if (dto.completed) "Finished" else "In Progress",
                        description = "Complete the exercise assigned in class and upload the report before the deadline."
                    )
                }

                val db = AppDatabase.getInstance(context)
                db.taskDao().insertTasks(convertedTasks.map { it.toEntity() })

                Result.success(convertedTasks)
            } else {
                Result.failure(Exception("Gagal mengambil data dari REST API: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
