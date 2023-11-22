package com.example.mynotes.presentation.notes

import com.example.mynotes.domain.model.Note
import com.example.mynotes.util.ItemOrder

sealed class NotesEvent{
    data class GetAllNotesByOrder(val noteOrder: ItemOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    data class BottomSheetItemClicked(val value: String) : NotesEvent()
    data class OnSearchQueryChanged(val query: String): NotesEvent()
    object GetCategoriesList: NotesEvent()
    object ToggleOrderSection: NotesEvent()
    object OnSearchIconClicked: NotesEvent()
    object OnCloseSearchIconClicked: NotesEvent()
    object BottomSheetShow: NotesEvent()
    object OnCloseBottomSheet: NotesEvent()
}
