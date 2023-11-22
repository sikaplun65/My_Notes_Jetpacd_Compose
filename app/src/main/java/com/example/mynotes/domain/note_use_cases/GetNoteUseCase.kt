package com.example.mynotes.domain.note_use_cases

import com.example.mynotes.domain.model.Note
import com.example.mynotes.domain.repository.ItemRepository

class GetNoteUseCase(
    private val repository: ItemRepository<Note>
) {
    suspend operator fun invoke(id: Int): Note? = repository.getItemById(id)
}