package com.ushyaku.ushyakuminiapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database class to manage the local database for tasks.
 */
@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    /**
     * Provides access to the Tasks DAO.
     */
    abstract fun taskDao(): TaskDao

    companion object {
        // Singleton instance of the TaskDatabase
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        /**
         * Gets the singleton instance of the TaskDatabase.
         */
        fun getDatabase(context: Context): TaskDatabase {
            // If INSTANCE is not null, return it; otherwise, create it in a thread-safe manner
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}