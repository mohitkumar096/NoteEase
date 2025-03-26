package com.mohit.noteease.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.noteease.viewmodel.NoteViewModel
import com.mohit.noteease.data.local.Note
import com.mohit.noteease.data.local.NoteDao
import com.mohit.noteease.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: NoteViewModel,
    noteId: Int?
) {
    val isDarkMode = isSystemInDarkTheme()

    val note by viewModel.selectedNote.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(0xFFFFF59D.toInt()) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val window = (context as? Activity)?.window

    val animatedColor by animateColorAsState(
        targetValue = Color(selectedColor),
        label = "BackgroundColor"
    )

    fun darkenColor(color: Color, factor: Float = 0.8f): Color {
        return color.copy(alpha = 1f - (1f - factor)).compositeOver(Color.Black)
    }

    val darkerColor = darkenColor(animatedColor)


    LaunchedEffect(noteId) {
        if (noteId == null || noteId == 0) {
            viewModel.clearSelectedNote()
            title = ""
            content = ""
            selectedColor = 0xFFE6E6FA.toInt() // default color
        } else {
            viewModel.loadNote(noteId)
        }
    }







    LaunchedEffect(note) {
        note?.let {
            title = it.title
            content = it.content
            selectedColor = it.color
        }
    }


    fun saveNote() {
        if (title.isNotEmpty() || content.isNotEmpty()) {
            val updatedNote = Note(
                id = note?.id ?: 0,
                title = title,
                content = content,
                color = selectedColor,
                isPinned = note?.isPinned ?: false
            )
            viewModel.addOrUpdateNote(updatedNote)
        }
    }


    DisposableEffect(darkerColor) {
        val window = (context as? Activity)?.window

        // Save Original Colors
        val originalStatusBarColor = window?.statusBarColor
        val originalNavBarColor = window?.navigationBarColor

        val solidColor = if (isDarkMode) Color.Black.toArgb() else Color.White.toArgb()
        window?.statusBarColor = darkerColor.toArgb()
        window?.navigationBarColor = solidColor
        WindowCompat.setDecorFitsSystemWindows(window!!, true)

        // Cleanup when leaving screen
        onDispose {
            window?.statusBarColor = solidColor
            window?.navigationBarColor = solidColor
        }
    }


    BackHandler {
        saveNote()
        navController.popBackStack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        saveNote()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkerColor),
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(animatedColor)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = LocalTextStyle.current.copy(fontSize = 22.sp),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            if (title.isEmpty()) Text("Title", fontSize = 22.sp, color = Color.Gray)
                            innerTextField()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                BasicTextField(
                    value = content,
                    onValueChange = { content = it },
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 4.dp)
                        ) {
                            if (content.isEmpty()) Text("Note", fontSize = 16.sp, color = Color.Gray)
                            innerTextField()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(modifier = Modifier.background(darkerColor).padding(6.dp).align(alignment = Alignment.CenterHorizontally)){
                Column {
                    Text("Pick a Color", fontSize = 14.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(0xFFE6E6FA, 0xFFADD8E6, 0xFF90EE90, 0xFFFFF9C4, 0xFFFFB6C1).forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(color))
                                    .border(1.dp, if (selectedColor == color.toInt()) Color.Black else Color.Transparent,
                                        RoundedCornerShape(12.dp),
                                    )
                                    .clickable { selectedColor = color.toInt() }
                            )
                        }
                    }
                }
            }
        }
    }


    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Note") },
            text = { Text("Are you sure you want to delete this note?") },
            confirmButton = {
                TextButton(onClick = {
                    note?.let { viewModel.deleteNote(it) }
                    navController.popBackStack()
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

