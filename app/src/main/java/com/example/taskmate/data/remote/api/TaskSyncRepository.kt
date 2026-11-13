package com.example.taskmate.data.remote.api

import android.content.Context
import com.example.taskmate.data.TaskItem
import com.example.taskmate.data.local.AppDatabase
import com.example.taskmate.data.local.entity.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositori sinkronisasi tugas dari Retrofit REST API ke database lokal Room DB.
 */
class TaskSyncRepository(
    private val apiService: TaskApiService = TaskApiService.create()
) {
    /**
     * Mengambil tugas terbaru dari REST API server via Retrofit dan menyimpannya ke Room DB.
     */
    suspend fun syncRemoteTasksToRoom(context: Context): Result<List<TaskItem>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.fetchRemoteTasks()
            if (response.isSuccessful && response.body() != null) {
                val remoteTasks = response.body()!!.todos
                val convertedTasks = remoteTasks.mapIndexed { index, dto ->
                    TaskItem(
                        id = 5000 + dto.id,
                        title = dto.todo.take(35),
                        course = "PAPB D (API Sync)",
                        daysLeft = if (dto.completed) "Completed" else "${index + 2} Days left",
                        dueDate = "Due Friday, Nov ${10 + index}\n11:59 PM",
                        status = if (dto.completed) "Finished" else "In Progress",
                        description = "Tugas tersinkronisasi otomatis via Retrofit REST API dari server."
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
