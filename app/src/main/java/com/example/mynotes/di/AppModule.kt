package com.example.mynotes.di

import android.app.Application
import androidx.room.Room
import com.example.mynotes.data.data_sourse.NotebookDatabase
import com.example.mynotes.data.repository.NoteRepositoryImpl
import com.example.mynotes.domain.model.Note
import com.example.mynotes.domain.note_use_cases.AddNoteUseCase
import com.example.mynotes.domain.note_use_cases.DeleteNoteUseCase
import com.example.mynotes.domain.note_use_cases.GetAllNotesUseCase
import com.example.mynotes.domain.note_use_cases.GetCategoriesUseCase
import com.example.mynotes.domain.note_use_cases.GetNoteUseCase
import com.example.mynotes.domain.note_use_cases.GetNotesByCategoryUseCase
import com.example.mynotes.domain.note_use_cases.GetNotesBySearchUseCase
import com.example.mynotes.domain.note_use_cases.NoteUseCases
import com.example.mynotes.domain.repository.ItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNotebookDatabase(app: Application): NotebookDatabase =
        Room.databaseBuilder(
            app,
            NotebookDatabase::class.java,
            NotebookDatabase.DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideNoteRepositoryImpl(db: NotebookDatabase): ItemRepository<Note> =
        NoteRepositoryImpl(db.noteDao)

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: ItemRepository<Note>): NoteUseCases =
        NoteUseCases(
            getAllNotes = GetAllNotesUseCase(repository),
            getNotesByCategory = GetNotesByCategoryUseCase(repository),
            addNote = AddNoteUseCase(repository),
            deleteNote = DeleteNoteUseCase(repository),
            getNote = GetNoteUseCase(repository),
            searchNote = GetNotesBySearchUseCase(repository),
            getCategories = GetCategoriesUseCase(repository)
        )
}