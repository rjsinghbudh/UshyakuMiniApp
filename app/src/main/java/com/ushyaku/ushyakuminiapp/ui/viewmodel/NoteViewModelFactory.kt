package com.ushyaku.ushyakuminiapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ushyaku.ushyakuminiapp.data.repo.NoteRepository

/**
 * Factory class to instantiate the NoteViewModel with its required dependencies.
 */
class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    
    /**
     * Creates a new instance of the given ViewModel class.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the modelClass is NoteViewModel
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}