package com.example.mynotes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val categoryNote: String,
    val title: String,
    val content: String,
    val timeStamp: Long,
    val noteImportance: Boolean
)
