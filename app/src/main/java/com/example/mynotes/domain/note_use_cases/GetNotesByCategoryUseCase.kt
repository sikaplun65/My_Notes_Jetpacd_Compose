package com.example.mynotes.domain.note_use_cases

import com.example.mynotes.domain.model.Note
import com.example.mynotes.domain.repository.ItemRepository
import com.example.mynotes.util.ItemOrder
import com.example.mynotes.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesByCategoryUseCase(
    private val repository: ItemRepository<Note>
) {
    operator fun invoke(
        noteOrder: ItemOrder = ItemOrder.Date(orderType = OrderType.Descending),
        category: String
    ): Flow<List<Note>> =
        repository.getAllItems()
            .map { listNotes ->
                when (noteOrder.orderType) {
                    is OrderType.Ascending -> {
                        when (noteOrder) {
                            is ItemOrder.Date -> listNotes.sortedBy { note -> note.timeStamp }
                            is ItemOrder.NoteImportance -> listNotes.sortedBy { note -> note.noteImportance }
                        }
                    }

                    is OrderType.Descending -> {
                        when (noteOrder) {
                            is ItemOrder.Date -> listNotes.sortedByDescending { note -> note.timeStamp }
                            is ItemOrder.NoteImportance -> listNotes.sortedByDescending { note -> note.noteImportance }
                        }
                    }
                }
                listNotes
                    .filter { note ->
                        note.categoryNote.contains(category) &&
                                note.categoryNote.length == category.length
                    }
            }
}