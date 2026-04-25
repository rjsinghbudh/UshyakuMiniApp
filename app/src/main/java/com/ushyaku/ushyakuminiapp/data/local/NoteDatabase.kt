package com.ushyaku.ushyakuminiapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database class to manage the local database for tasks.
 */
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    /**
     * Provides access to the Tasks DAO.
     */
    abstract fun getNotesDao(): NotesDao

    companion object {
        // Singleton instance of the NoteDatabase
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        /**
         * Gets the singleton instance of the NoteDatabase.
         */
        fun getDatabase(context: Context): NoteDatabase {
            // If INSTANCE is not null, return it; otherwise, create it in a thread-safe manner
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}