package com.example.mynotes.domain.note_use_cases

import com.example.mynotes.domain.model.Note
import com.example.mynotes.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesBySearchUseCase(private val repository: ItemRepository<Note>) {
    operator fun invoke(
        str: String
    ): Flow<List<Note>> {

        val regex = Regex(str, RegexOption.IGNORE_CASE)

        return repository.getAllItems()
            .map { listNote ->
                listNote
                    .filter { note ->
                        regex.containsMatchIn(note.title) || regex.containsMatchIn(note.content)
                    }
            }
    }

}




