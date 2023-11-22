package com.example.mynotes.presentation.notes

import com.example.mynotes.domain.model.Note
import com.example.mynotes.util.ItemOrder
import com.example.mynotes.util.OrderType

data class NotesScreenState(
    val notes: List<Note> = emptyList(),
    val categories: List<String> = emptyList(),
    val categoryNote: String = "",
    val searchQuery: String = "",
    val noteOrder: ItemOrder = ItemOrder.Date(OrderType.Descending),
    var isModalBottomSheetVisible:Boolean = false,
    val isOrderSectionVisible: Boolean = false,
    val error: String? = null,
    var isSearchBarVisible: Boolean = false,
)
