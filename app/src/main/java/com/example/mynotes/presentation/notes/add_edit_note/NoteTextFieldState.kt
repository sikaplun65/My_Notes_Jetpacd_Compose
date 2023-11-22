package com.example.mynotes.presentation.notes.add_edit_note

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    var isHintVisible: Boolean = true,
    val noteImportance: Boolean = false,
    val timeStamp: Long = 0
)

data class FilteredCategoriesState(
    val categories: List<String> = emptyList()
)