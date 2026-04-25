package com.ushyaku.ushyakuminiapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a Task in the Room database.
 */
@Entity(tableName = "task_table")
data class TaskEntity(
    // Primary key with auto-generation enabled
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    // Column for the task title
    @ColumnInfo(name = "title") val title: String,
    
    // Column for the task description
    @ColumnInfo(name = "description") val description: String,
)