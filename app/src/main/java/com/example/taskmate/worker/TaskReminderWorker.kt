package com.example.taskmate.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.taskmate.R
import com.example.taskmate.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

// Background worker untuk notifikasi pengingat deadline tugas
class TaskReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): ListenableWorker.Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getInstance(context)
            val pendingTasks = database.taskDao().getAllTasksOnce().filter { it.status != "Finished" }

            if (pendingTasks.isNotEmpty()) {
                val nextTask = pendingTasks.first()
                sendNotification(
                    title = "Pengingat Tugas TaskMate 📌",
                    message = "Tugas '${nextTask.title}' (${nextTask.course}) deadline: ${nextTask.daysLeft}"
                )
            }
            ListenableWorker.Result.success()
        } catch (_: Exception) {
            ListenableWorker.Result.retry()
        }
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "taskmate_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "TaskMate Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifikasi pengingat deadline tugas kuliah"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }

    companion object {
        const val WORK_NAME = "TaskMateReminderWorker"

        // Schedule worker berkala tiap 24 jam
        fun scheduleTaskReminders(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()

            val periodicWork = PeriodicWorkRequestBuilder<TaskReminderWorker>(24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWork
            )
        }

        // Run worker 1x langsung
        fun runImmediateCheck(context: Context) {
            val oneTimeWork = OneTimeWorkRequestBuilder<TaskReminderWorker>().build()
            WorkManager.getInstance(context).enqueue(oneTimeWork)
        }
    }
}
