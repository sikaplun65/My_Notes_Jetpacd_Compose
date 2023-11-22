package com.example.mynotes.presentation.notes.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.model.Note
import com.example.mynotes.domain.note_use_cases.NoteUseCases
import com.example.mynotes.util.InvalidItemException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _noteCategory = mutableStateOf(NoteTextFieldState(hint = "Введите категорию"))
    val noteCategory: State<NoteTextFieldState> = _noteCategory

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Введите заголовок"))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Введите данные"))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteImportance = mutableStateOf(NoteTextFieldState(noteImportance = false))
    val noteImportance: State<NoteTextFieldState> = _noteImportance

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    var filteredCategories: List<String> = mutableListOf()
        private set
    var timeStamp: Long = 0
        private set

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id

                        _noteCategory.value = noteCategory.value.copy(
                            text = note.categoryNote,
                            isHintVisible = false
                        )
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = _noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteImportance.value = _noteImportance.value.copy(
                            noteImportance = note.noteImportance
                        )

                        timeStamp = note.timeStamp
                    }
                }
            }
            if (currentNoteId == null) {
                timeStamp = System.currentTimeMillis()
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.ChangeCategoryFocus -> {
                _noteCategory.value = noteCategory.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteCategory.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredCategory -> {
                _noteCategory.value = noteCategory.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.FilteredCategories -> {
                viewModelScope.launch {
                    filteredCategories =
                        event.categories.filter { it.contains(event.value, ignoreCase = true) }
                }
            }

            AddEditNoteEvent.ChangeNoteImportance -> {
                _noteImportance.value = noteImportance.value.copy(
                    noteImportance = !_noteImportance.value.noteImportance
                )
            }

            AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                id = currentNoteId,
                                categoryNote =
                                _noteCategory.value.text.ifEmpty { "Без категории" },
                                title =
                                _noteTitle.value.text.ifEmpty {
                                    if (_noteContent.value.text.isNotEmpty()) {
                                        _noteContent.value.text.substring(0, 15) + "..."
                                    } else {
                                        "Без заголовка"
                                    }
                                },
                                content =
                                _noteContent.value.text.ifEmpty { "Пустая заметка" },
                                noteImportance = _noteImportance.value.noteImportance,
                                timeStamp = timeStamp
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidItemException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Не удалось сохранить заметку"
                            )
                        )
                    }
                }
            }

            else -> {}
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}