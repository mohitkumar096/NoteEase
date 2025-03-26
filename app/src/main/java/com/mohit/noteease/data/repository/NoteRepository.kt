package com.mohit.noteease.data.repository

import com.mohit.noteease.data.local.Note
import com.mohit.noteease.data.local.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun getNoteById(noteId: Int): Note? {
        return noteDao.getNoteById(noteId)
    }

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    fun getPinnedNotes(): Flow<List<Note>> {
        return noteDao.getPinnedNotes()
    }

    //  delete multiple notes
    suspend fun deleteNotes(noteIds: List<Int>) {
        noteDao.deleteNotesByIds(noteIds)
    }

    //  copy multiple notes
    suspend fun copyNotes(noteIds: List<Int>) {
        val notesToCopy = noteDao.getNotesByIds(noteIds)
        val copiedNotes = notesToCopy.map { note ->
            note.copy(id = 0) // Create a new note with a unique ID
        }
        noteDao.insertNotes(copiedNotes)
    }

    //  get multiple notes by IDs
    suspend fun getNotesByIds(noteIds: List<Int>): List<Note> {
        return noteDao.getNotesByIds(noteIds)
    }
}
