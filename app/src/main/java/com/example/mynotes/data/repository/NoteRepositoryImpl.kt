package com.example.mynotes.data.repository


import com.example.mynotes.data.data_sourse.NoteDao
import com.example.mynotes.domain.model.Note
import com.example.mynotes.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
): ItemRepository<Note> {
    override fun getAllItems(): Flow<List<Note>> = dao.getAllNotes()

    override suspend fun getItemById(id: Int): Note? = dao.getNote(id)

    override suspend fun deleteItem(item: Note) {
        dao.deleteNote(item)
    }

    override suspend fun insertItem(item: Note) {
        dao.insertNote(item)
    }
}