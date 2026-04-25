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

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _searchQuery = MutableLiveData<String>("")

    // This LiveData reacts whenever _searchQuery changes
    val allNotes: LiveData<List<Note>> = _searchQuery.switchMap { query ->
        if (query.isNullOrEmpty()) {
            repository.allNotes
        } else {
            repository.searchNotes(query)
        }
    }

    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun searchNotes(query: String): LiveData<List<Note>> {
        return repository.searchNotes(query)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}