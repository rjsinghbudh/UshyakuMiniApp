package com.ushyaku.ushyakuminiapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.ushyaku.ushyakuminiapp.data.local.Note
import com.ushyaku.ushyakuminiapp.data.repo.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for managing note data and handling UI-related logic.
 * Communicates with the repository to perform CRUD operations.
 */
class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    // Internal MutableLiveData to hold the current search query
    private val _searchQuery = MutableLiveData<String>("")

    /**
     * LiveData that provides a list of notes. It switches between all notes 
     * and filtered notes based on the search query.
     */
    val allNotes: LiveData<List<Note>> = _searchQuery.switchMap { query ->
        if (query.isNullOrEmpty()) {
            repository.allNotes
        } else {
            repository.searchNotes(query)
        }
    }

    /**
     * Inserts a new note into the database using a coroutine.
     */
    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    /**
     * Updates an existing note in the database using a coroutine.
     */
    fun update(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }

    /**
     * Deletes a note from the database using a coroutine.
     */
    fun delete(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    /**
     * Manually searches for notes (can be used for specific queries).
     */
    fun searchNotes(query: String): LiveData<List<Note>> {
        return repository.searchNotes(query)
    }

    /**
     * Updates the search query, which triggers the allNotes LiveData to refresh.
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}