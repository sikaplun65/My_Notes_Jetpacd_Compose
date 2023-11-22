package com.example.mynotes.domain.repository

import kotlinx.coroutines.flow.Flow

interface ItemRepository<T> {
    fun getAllItems(): Flow<List<T>>
    suspend fun getItemById(id: Int): T?
    suspend fun insertItem(item: T)
    suspend fun deleteItem(item: T)
}