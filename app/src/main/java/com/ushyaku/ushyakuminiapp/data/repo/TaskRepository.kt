package com.ushyaku.ushyakuminiapp.data.repo

import androidx.lifecycle.LiveData
import com.ushyaku.ushyakuminiapp.data.local.TaskEntity
import com.ushyaku.ushyakuminiapp.data.local.TaskDao

/**
 * Repository class that abstracts access to the data source.
 */
class TaskRepository(private val taskDao: TaskDao) {

    // LiveData holding all tasks from the database
    val tasks: LiveData<List<TaskEntity>> = taskDao.getAllTasks()

    /**
     * Inserts a task into the database.
     */
    suspend fun insert(taskEntity: TaskEntity) {
        taskDao.insert(taskEntity)
    }

    /**
     * Deletes a task from the database.
     */
    suspend fun delete(taskEntity: TaskEntity) {
        taskDao.delete(taskEntity)
    }

    /**
     * Updates an existing task in the database.
     */
    suspend fun update(taskEntity: TaskEntity) {
        taskDao.update(taskEntity)
    }

    /**
     * Searches for tasks based on the provided query.
     */
    fun searchTasks(query: String): LiveData<List<TaskEntity>> {
        return taskDao.searchTask("%$query%")
    }
}
