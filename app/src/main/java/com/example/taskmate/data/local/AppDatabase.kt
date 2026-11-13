package com.example.taskmate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskmate.data.local.dao.CourseDao
import com.example.taskmate.data.local.dao.TaskDao
import com.example.taskmate.data.local.dao.UserDao
import com.example.taskmate.data.local.entity.CourseEntity
import com.example.taskmate.data.local.entity.TaskEntity
import com.example.taskmate.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, CourseEntity::class, TaskEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun courseDao(): CourseDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "taskmate_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
