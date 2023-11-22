package com.example.mynotes.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.note_use_cases.NoteUseCases
import com.example.mynotes.util.ItemOrder
import com.example.mynotes.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesScreenViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesScreenState())
    val state: State<NotesScreenState> = _state

    private var currentOrder = state.value.noteOrder

    private var getNotesJob: Job? = null

    private var getCategoriesJob: Job? = null

    var currentCategoryList = mutableListOf<String>()
        private set

    init {
        getNotes(ItemOrder.Date(OrderType.Descending))
        getAllCategories()
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                }
                getAllCategories()
            }

            is NotesEvent.GetAllNotesByOrder -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }

            NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            is NotesEvent.OnSearchQueryChanged -> {
                _state.value = _state.value.copy(searchQuery = event.query)
                getNotesBySearch(query = event.query)
            }

            is NotesEvent.BottomSheetItemClicked -> {
                if (state.value.categoryNote == event.value) {
                    return
                }
                getNotesByCategory(
                    noteOrder = ItemOrder.Date(OrderType.Descending),// Todo(изменить на текущий noteOrder)
                    category = event.value
                )
            }

            is NotesEvent.OnSearchIconClicked -> {
                _state.value = state.value.copy(
                    isSearchBarVisible = !state.value.isSearchBarVisible
                )
            }

            is NotesEvent.OnCloseSearchIconClicked -> {
                _state.value = state.value.copy(
                    isSearchBarVisible = !state.value.isSearchBarVisible
                )
                getNotes(noteOrder = ItemOrder.Date(OrderType.Descending))
            }

            is NotesEvent.BottomSheetShow -> {
                _state.value = state.value.copy(
                    isModalBottomSheetVisible = !state.value.isModalBottomSheetVisible
                )

            }

            is NotesEvent.OnCloseBottomSheet -> {} // Todo(удалить)
            is NotesEvent.GetCategoriesList -> {
                getAllCategories()
            }
        }
    }

    private fun getNotesBySearch(query: String) {
        if (query.isEmpty()) {
            return
        }
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.searchNote(query)
            .onEach { notes ->
                delay(1500)
                _state.value = state.value.copy(
                    notes = notes,
                    searchQuery = query
                )
            }.launchIn(viewModelScope)
    }

    private fun getNotes(noteOrder: ItemOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getAllNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }.launchIn(viewModelScope)
    }

    private fun getNotesByCategory(noteOrder: ItemOrder, category: String) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotesByCategory(noteOrder, category)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    categoryNote = category,
                    noteOrder = noteOrder
                )
            }.launchIn(viewModelScope)
    }

    private fun getAllCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = noteUseCases.getCategories
            .onEach { categories ->
                _state.value = state.value.copy(
                    categories = categories
                )
                currentCategoryList = categories.toMutableList()
            }.launchIn(viewModelScope)

    }


}