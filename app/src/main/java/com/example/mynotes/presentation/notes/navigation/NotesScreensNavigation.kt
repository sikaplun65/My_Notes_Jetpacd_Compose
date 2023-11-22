package com.example.mynotes.presentation.notes.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mynotes.presentation.notes.NotesScreen
import com.example.mynotes.presentation.notes.add_edit_note.AddEditNoteScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesScreensNavigation(
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController as NavHostController,
            startDestination = Screens.NotesScreen.route
        ) {
            composable(route = Screens.NotesScreen.route) {
                NotesScreen(navController = navController)
            }
            composable(
                route = Screens.AddEditNoteScreen.route + "?noteId={noteId}",
                arguments = listOf(
                    navArgument(
                        name = "noteId"
                    ){
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { backStackEntry ->
                AddEditNoteScreen(navController = navController)
            }
        }
    }
}