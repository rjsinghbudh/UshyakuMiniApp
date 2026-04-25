package com.ushyaku.ushyakuminiapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a Note in the Room database.
 */
@Entity(tableName = "notesTable")
data class Note(
    // Primary key with auto-generation enabled
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    // Column for the note title
    @ColumnInfo(name = "title") val noteTitle: String,
    
    // Column for the note description
    @ColumnInfo(name = "description") val noteDescription: String,
)