package com.example.mynotes.domain.note_use_cases

import com.example.mynotes.domain.model.Note
import com.example.mynotes.domain.repository.ItemRepository

class AddNoteUseCase(
    private val repository: ItemRepository<Note>
) {
    suspend operator fun invoke(note: Note){
        repository.insertItem(note)
    }
}