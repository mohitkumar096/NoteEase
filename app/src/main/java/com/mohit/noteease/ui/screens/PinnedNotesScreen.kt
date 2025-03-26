package com.mohit.noteease.ui.screens

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColor
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.mohit.noteease.data.local.Note
import com.mohit.noteease.ui.components.NoteItem
import com.mohit.noteease.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinnedNotesScreen(navController: NavController, viewModel: NoteViewModel) {
    val pinnedNotes by viewModel.pinnedNotes.collectAsState()
    val isDarkMode = isSystemInDarkTheme()
    val context = LocalContext.current
    val window = (context as? Activity)?.window
    WindowCompat.setDecorFitsSystemWindows(window!!, true)
    val solidColor = if (isDarkMode) Color.Black.toArgb() else Color.White.toArgb()
    window?.statusBarColor = solidColor
    window?.navigationBarColor = solidColor

    var isSelectionMode by remember { mutableStateOf(false) }
    val notes by viewModel.notes.collectAsState()
    val selectedNotes = remember { mutableStateListOf<Int>() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Pinned Notes",
                        //style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(solidColor)
                )
            )
        }
    ) { paddingValues ->
        if (pinnedNotes.isEmpty()) {
            // Show empty state if no pinned notes
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(text = "No pinned notes", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pinnedNotes) { note ->  // ✅ Use note directly from pinnedNotes
                    val isSelected = selectedNotes.contains(note.id)

                    NoteItem(
                        note = note,
                        onNoteClick = {
                            if (isSelectionMode) {
                                if (isSelected) selectedNotes.remove(note.id) else selectedNotes.add(note.id)
                            } else {
                                navController.navigate("add_edit_note/${note.id}") // ✅ Correct navigation
                            }
                        },
                        onLongClick = {
                            if (!selectedNotes.contains(note.id)) selectedNotes.add(note.id)
                        },
                        isSelected = isSelected,
                        onPinClick = { viewModel.togglePin(note) },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

        }
    }
}
