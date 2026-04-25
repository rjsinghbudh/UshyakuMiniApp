package com.ushyaku.ushyakuminiapp.data.repo

import androidx.lifecycle.LiveData
import com.ushyaku.ushyakuminiapp.data.local.Note
import com.ushyaku.ushyakuminiapp.data.local.NotesDao

/**
 * Repository class that abstracts access to the data source.
 */
class NoteRepository(private val noteDao: NotesDao) {

    // LiveData holding all notes from the database
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    /**
     * Inserts a note into the database.
     */
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    /**
     * Deletes a note from the database.
     */
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    /**
     * Updates an existing note in the database.
     */
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    /**
     * Searches for notes based on the provided query.
     */
    fun searchNotes(query: String): LiveData<List<Note>> {
        return noteDao.searchNotes("%$query%")
    }
}
