package com.example.mynotes.presentation.notes.navigation

sealed class Screens(val route: String){
    object NotesScreen: Screens("notes_screen")
    object AddEditNoteScreen: Screens("add_edit_note_screen")
}
