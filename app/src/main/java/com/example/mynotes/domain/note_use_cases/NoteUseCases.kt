package com.example.mynotes.domain.note_use_cases

data class NoteUseCases(
    val getAllNotes: GetAllNotesUseCase,
    val getNotesByCategory: GetNotesByCategoryUseCase,
    val addNote: AddNoteUseCase,
    val getNote: GetNoteUseCase,
    val deleteNote: DeleteNoteUseCase,
    val searchNote: GetNotesBySearchUseCase,
    val getCategories: GetCategoriesUseCase
)
