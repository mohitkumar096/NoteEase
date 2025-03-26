package com.mohit.noteease.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mohit.noteease.ui.screens.AboutScreen
import com.mohit.noteease.ui.screens.AddEditNoteScreen
import com.mohit.noteease.ui.screens.HelpFeedbackScreen
import com.mohit.noteease.ui.screens.NoteListScreen
import com.mohit.noteease.ui.screens.NoteDetailScreen
import com.mohit.noteease.ui.screens.PinnedNotesScreen
import com.mohit.noteease.viewmodel.NoteViewModel

// navigation/Navigation.kt
sealed class Screen(val route: String) {
    object NoteList : Screen("note_list")
    object PinnedNotes : Screen("pinned_notes")
    object AddEditNote : Screen("add_edit_note?noteId={noteId}") {
        fun createRoute(noteId: Int) = "add_edit_note?noteId=$noteId"
    }
    object About : Screen("about")
    object HelpFeedback : Screen("help_feedback")
}





@Composable
fun AppNavigation(navController: NavHostController, viewModel: NoteViewModel, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.NoteList.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.NoteList.route) {
            NoteListScreen(navController, viewModel)
        }
        composable("add_edit_note/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            AddEditNoteScreen(navController, viewModel, noteId)
        }

        composable(Screen.HelpFeedback.route) {
            HelpFeedbackScreen(navController)
        }

        composable(Screen.About.route) {
            AboutScreen(navController)
        }
        composable(Screen.PinnedNotes.route) {
            PinnedNotesScreen(navController, viewModel)
        }



    }
}


