package com.example.mynotes.data.data_sourse

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynotes.domain.model.Note

@Database(entities = [Note::class], version = 1,exportSchema = false)
abstract class NotebookDatabase: RoomDatabase(){
    abstract val noteDao: NoteDao
    companion object{
        const val DATABASE_NAME = "notebook_db"
    }
}