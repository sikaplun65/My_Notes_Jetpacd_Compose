package com.example.mynotes.domain.note_use_cases

import com.example.mynotes.domain.model.Note
import com.example.mynotes.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
class GetCategoriesUseCase(
    private val repository: ItemRepository<Note>
) : Flow<List<String>> {
    override suspend fun collect(collector: FlowCollector<List<String>>) {
        repository.getAllItems()
            .collect { notes ->
                val uniqueCategories = notes
                    .map { it.categoryNote }
                    .distinct()
                collector.emit(uniqueCategories)
            }
    }
}