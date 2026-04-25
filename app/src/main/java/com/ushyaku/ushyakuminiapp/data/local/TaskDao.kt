package com.ushyaku.ushyakuminiapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object (DAO) for the tasks table.
 * Defines methods to interact with the database.
 */
@Dao
interface TaskDao {

    /**
     * Inserts a new task. If the task already exists, it is ignored.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(taskEntity :TaskEntity)

    /**
     * Deletes a specific task.
     */
    @Delete
    suspend fun delete(taskEntity: TaskEntity)

    /**
     * Retrieves all tasks from the database, ordered by ID in ascending order.
     */
    @Query("Select * from task_table order by id ASC")
    fun getAllTasks(): LiveData<List<TaskEntity>>

    /**
     * Updates an existing task. If there's a conflict, it replaces the existing task.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(taskEntity: TaskEntity)

    /**
     * Searches for tasks based on a query string matching title or description.
     */
    @Query("SELECT * FROM task_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery ORDER BY id DESC")
    fun searchTask(searchQuery: String): LiveData<List<TaskEntity>>

}