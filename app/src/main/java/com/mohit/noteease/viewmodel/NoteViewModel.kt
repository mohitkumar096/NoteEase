package com.mohit.noteease.viewmodel

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mohit.noteease.data.local.Note
import com.mohit.noteease.data.repository.NoteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote

    init {
        viewModelScope.launch {
            repository.allNotes
                .combine(_searchQuery) { notes, query ->
                    if (query.isBlank()) {
                        notes  // If search is empty, show all notes
                    } else {
                        notes.filter {
                            it.title.contains(query, ignoreCase = true) ||
                                    it.content.contains(query, ignoreCase = true)
                        }
                    }
                }
                .collect { filteredNotes ->
                    _notes.value = filteredNotes
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query  // ✅ Update search query dynamically
    }

    fun addOrUpdateNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

    fun clearSelectedNote() {
        _selectedNote.value = null
        println("🛑 Selected note cleared")  // ✅ Debug log
    }

    fun loadNote(noteId: Int) {
        viewModelScope.launch {
            val note = repository.getNoteById(noteId)
            _selectedNote.value = note
            println("✅ Loaded note: $note")  // ✅ Debug log
        }
    }



    fun togglePin(note: Note) = viewModelScope.launch {
        repository.insertNote(note.copy(isPinned = !note.isPinned))
    }

    suspend fun getNoteById(noteId: Int): Note? {
        return repository.getNoteById(noteId)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }
    val pinnedNotes: StateFlow<List<Note>> = repository.getPinnedNotes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    //


    fun copyNotes(noteIds: List<Int>) {
        viewModelScope.launch {
            repository.copyNotes(noteIds)
        }
    }

    suspend fun shareNotes(noteIds: List<Int>): Intent {
        val notes = repository.getNotesByIds(noteIds)
        val textToShare = notes.joinToString("\n\n") { it.title + "\n" + it.content }

        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, textToShare)
        }
    }


    suspend fun copyToGoogleDocs(noteIds: List<Int>): Intent {
        val notes = repository.getNotesByIds(noteIds) // ✅ Now correctly called inside a suspend function
        val textToCopy = notes.joinToString("\n\n") { it.title + "\n" + it.content }

        return Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://docs.google.com/document/u/0/")
            putExtra(Intent.EXTRA_TEXT, textToCopy)
        }
    }





}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
