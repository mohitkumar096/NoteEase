package com.mohit.noteease.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.noteease.data.local.Note
import com.mohit.noteease.data.local.NoteDao
import com.mohit.noteease.data.repository.NoteRepository
import com.mohit.noteease.ui.components.NoteItem
import com.mohit.noteease.ui.components.SearchBar
import com.mohit.noteease.ui.navigation.Screen
import com.mohit.noteease.viewmodel.NoteViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import com.mohit.noteease.ui.navigation.DrawerScreen
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(navController: NavController, viewModel: NoteViewModel) {
    val notes by viewModel.notes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // Track selected notes
    val selectedNotes = remember { mutableStateListOf<Int>() }
    var isSelectionMode by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(selectedNotes.size) {
        isSelectionMode = selectedNotes.isNotEmpty()
    }


    BackHandler(enabled = isSelectionMode) {
        selectedNotes.clear()
        isSelectionMode = false
    }

    ModalNavigationDrawer(
        drawerContent = {
            DrawerScreen(navController) { coroutineScope.launch { drawerState.close() } }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { newQuery ->
                        searchQuery = newQuery
                        viewModel.updateSearchQuery(newQuery)
                    },
                    onDrawerClick = { coroutineScope.launch { drawerState.open() } }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.clearSelectedNote()
                        navController.navigate("add_edit_note/0")
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note")
                }
            },
            bottomBar = {
                if (isSelectionMode) {
                    SelectionOptions(
                        selectedNotes = selectedNotes,
                        onDelete = {
                            selectedNotes.forEach { noteId ->
                                val noteToDelete = notes.find { it.id == noteId }
                                noteToDelete?.let { viewModel.deleteNote(it) }
                            }
                            selectedNotes.clear()
                        },
                        onCopy = { viewModel.copyNotes(selectedNotes) },
                        onShare = { coroutineScope.launch { viewModel.shareNotes(selectedNotes) } },
                        onCopyToDocs = { coroutineScope.launch { viewModel.copyToGoogleDocs(selectedNotes) } }
                    )
                }
            }
        ) { paddingValues ->
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(notes.size) { index ->
                    val note = notes[index]
                    val isSelected = selectedNotes.contains(note.id)

                    NoteItem(
                        note = note,
                        onNoteClick = {
                            if (isSelectionMode) {
                                if (isSelected) selectedNotes.remove(note.id) else selectedNotes.add(note.id)
                            } else {
                                navController.navigate("add_edit_note/${note.id}")
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


@Composable
fun SelectionOptions(
    selectedNotes: List<Int>,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onCopyToDocs: () -> Unit
) {
    var Context =LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Notes?") },
            text = { Text("Are you sure you want to delete ${selectedNotes.size} selected notes? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onDelete()
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    BottomAppBar(
        containerColor = Color(0xCC1289ED)
    ) {
        IconButton(onClick = { showDialog = true }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
        }
        IconButton(onClick = onCopy) {
            Icon(Icons.Default.ContentCopy, contentDescription = "Make a Copy")
        }
        IconButton(onClick = {Toast.makeText(Context, "Upload is Coming Soon", Toast.LENGTH_SHORT).show()}) {
            Icon(Icons.Default.CloudUpload, contentDescription = "Copy to Google Docs")
        }
        IconButton(onClick = {Toast.makeText(Context, "Share is Coming Soon", Toast.LENGTH_SHORT).show()}) {
            Icon(Icons.Default.Share, contentDescription = "Share")
        }
    }
}










