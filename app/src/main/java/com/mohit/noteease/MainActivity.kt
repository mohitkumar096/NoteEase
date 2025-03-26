package com.mohit.noteease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.mohit.noteease.data.local.NoteDatabase
import com.mohit.noteease.data.repository.NoteRepository
import com.mohit.noteease.ui.navigation.AppNavigation
import com.mohit.noteease.ui.theme.NoteEaseTheme
import com.mohit.noteease.viewmodel.NoteViewModel
import com.mohit.noteease.viewmodel.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ✅ Initialize Database & Repository
        val database = NoteDatabase.getDatabase(this)
        val repository = NoteRepository(database.noteDao())

        // ✅ Create ViewModel using Factory
        val viewModelFactory = NoteViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]

        setContent {
            NoteEaseTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppNavigation(navController, viewModel, innerPadding) // ✅ Now passing `innerPadding`
                }
            }
        }


    }
}
