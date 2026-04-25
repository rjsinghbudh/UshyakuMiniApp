package com.ushyaku.ushyakuminiapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.ushyaku.ushyakuminiapp.data.local.TaskEntity
import com.ushyaku.ushyakuminiapp.data.repo.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for managing task data and handling UI-related logic.
 * Communicates with the repository to perform CRUD operations.
 */
class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Internal MutableLiveData to hold the current search query
    private val _searchQuery = MutableLiveData<String>("")

    /**
     * LiveData that provides a list of tasks. It switches between all tasks 
     * and filtered tasks based on the search query.
     */
    val tasks: LiveData<List<TaskEntity>> = _searchQuery.switchMap { query ->
        if (query.isNullOrEmpty()) {
            repository.tasks
        } else {
            repository.searchTasks(query)
        }
    }

    /**
     * Inserts a new task into the database using a coroutine.
     */
    fun insert(taskEntity: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(taskEntity)
    }

    /**
     * Updates an existing task in the database using a coroutine.
     */
    fun update(taskEntity: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(taskEntity)
    }

    /**
     * Deletes a task from the database using a coroutine.
     */
    fun delete(taskEntity: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(taskEntity)
    }

    /**
     * Updates the search query, which triggers the all Tasks LiveData to refresh.
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}