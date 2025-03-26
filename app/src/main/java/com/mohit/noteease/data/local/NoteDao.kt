package com.mohit.noteease.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<Note>)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM notes WHERE id IN (:noteIds)")
    suspend fun deleteNotesByIds(noteIds: List<Int>)

    @Query("SELECT * FROM notes WHERE id IN (:noteIds)")
    suspend fun getNotesByIds(noteIds: List<Int>): List<Note>

    @Query("SELECT * FROM notes WHERE isPinned = 1 ORDER BY id DESC")
    fun getPinnedNotes(): Flow<List<Note>>
}
