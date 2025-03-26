package com.mohit.noteease.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.noteease.viewmodel.NoteViewModel
import com.mohit.noteease.data.local.Note
import com.mohit.noteease.data.local.NoteDao
import com.mohit.noteease.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(navController: NavController, noteId: Int?, viewModel: NoteViewModel) {
    var note by remember { mutableStateOf<Note?>(null) } // ✅ Store the note in state

    LaunchedEffect(noteId) {
        if (noteId != null) {
            note = viewModel.getNoteById(noteId) // ✅ No need for withContext(Dispatchers.IO)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(note?.title ?: "Note Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            note?.let { noteData ->
                Text(text = noteData.title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = noteData.content, style = MaterialTheme.typography.bodyLarge)
            } ?: Text("Loading note...")
        }
    }
}



