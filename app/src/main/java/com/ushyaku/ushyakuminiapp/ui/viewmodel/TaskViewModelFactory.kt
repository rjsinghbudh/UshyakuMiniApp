package com.ushyaku.ushyakuminiapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ushyaku.ushyakuminiapp.data.repo.TaskRepository

/**
 * Factory class to instantiate the TaskViewModel with its required dependencies.
 */
class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    
    /**
     * Creates a new instance of the given ViewModel class.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the modelClass is TaskViewModel
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}