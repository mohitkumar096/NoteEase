package com.mohit.noteease.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.mohit.noteease.viewmodel.NoteViewModel
import com.mohit.noteease.data.local.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: NoteViewModel,
    noteId: Int?
) {
    val note by viewModel.selectedNote.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(0xFFE6E6FA.toInt()) } // Default: Lavender
    var showDeleteDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val isDarkMode = isSystemInDarkTheme()
    val window = (context as? Activity)?.window

    val animatedColor by animateColorAsState(
        targetValue = Color(selectedColor),
        label = "BackgroundColor"
    )

    // Function to determine app bar and system color based on background color
    fun getSystemColor(backgroundColor: Int): Color {
        return when (backgroundColor) {
            0xFFE6E6FA.toInt() -> Color(0xFF7979e4)        // Lavender -> White
            0xFFADD8E6.toInt() -> Color(0xFF3897b7)        // Light Blue -> Blue
            0xFF90EE90.toInt() -> Color(0xFF18a558)  // Light Green -> Dark Green
            0xFFFFF9C4.toInt() -> Color(0xFFd6ad60)  // Light Yellow -> Dark Yellow
            0xFFFFB6C1.toInt() -> Color(0xFFe10022)         // Light Pink -> Red
            else -> Color.White                      // Default to White
        }
    }

    val systemColor = getSystemColor(selectedColor)
    val gradient = Brush.verticalGradient(
        colors = listOf(animatedColor, systemColor.copy(alpha = 0.7f))
    )

    LaunchedEffect(noteId) {
        if (noteId == null || noteId == 0) {
            viewModel.clearSelectedNote()
            title = ""
            content = ""
            selectedColor = 0xFFE6E6FA.toInt() // Default: Lavender
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

    DisposableEffect(animatedColor) {
        window?.statusBarColor = systemColor.toArgb()
        window?.navigationBarColor = systemColor.toArgb()
        WindowCompat.setDecorFitsSystemWindows(window!!, true)

        val solidColor = if (isDarkMode) Color.Black.toArgb() else Color.White.toArgb()
        WindowCompat.setDecorFitsSystemWindows(window!!, true)

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
                title = {Text("Note Details", modifier = Modifier.padding(horizontal = 8.dp))},
                navigationIcon = {
                    IconButton(onClick = {
                        saveNote()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

                },

                actions = {
                    val iconclr: Color
                    if (systemColor == Color(0xFFe10022)){
                        iconclr = Color.White
                    }
                    else{
                        iconclr = Color(0xFFe10022)
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = iconclr)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = systemColor),
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(animatedColor)
                .padding(horizontal = 14.dp, vertical = 2.dp)
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

            Box(
                modifier = Modifier
                    .background(gradient)
                    .padding(4.dp, bottom = 0.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text("Pick a Color", fontSize = 14.sp, color = systemColor,
                        modifier = Modifier.background(animatedColor).padding(horizontal = 4.dp))
                    Spacer(modifier = Modifier.height(7.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(0xFFE6E6FA, 0xFFADD8E6, 0xFF90EE90, 0xFFFFF9C4, 0xFFFFB6C1).forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(Color(color))
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
            title = { Text("Delete Note", color = Color.Black) },
            text = { Text("Are you sure you want to delete this note?", color = Color.Black) },
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
                    Text("Cancel", color = Color.Blue)
                }
            },
            containerColor = animatedColor // Dark background color
        )

    }
}
