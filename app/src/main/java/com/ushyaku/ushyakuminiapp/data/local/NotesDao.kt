package com.ushyaku.ushyakuminiapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object (DAO) for the tasks table.
 * Defines methods to interact with the database.
 */
@Dao
interface NotesDao {

    /**
     * Inserts a new task. If the task already exists, it is ignored.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note :Note)

    /**
     * Deletes a specific task.
     */
    @Delete
    suspend fun delete(note: Note)

    /**
     * Retrieves all tasks from the database, ordered by ID in ascending order.
     */
    @Query("Select * from notesTable order by id ASC")
    fun getAllNotes(): LiveData<List<Note>>

    /**
     * Updates an existing task. If there's a conflict, it replaces the existing task.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(note: Note)

    /**
     * Searches for tasks based on a query string matching title or description.
     */
    @Query("SELECT * FROM notesTable WHERE title LIKE :searchQuery OR description LIKE :searchQuery ORDER BY id DESC")
    fun searchNotes(searchQuery: String): LiveData<List<Note>>

}