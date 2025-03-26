package com.mohit.noteease.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val color: Int = 0xFFFFF59D.toInt(),
    val isPinned: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
