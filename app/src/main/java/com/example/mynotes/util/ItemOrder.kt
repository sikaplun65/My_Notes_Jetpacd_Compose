package com.example.mynotes.util

sealed class ItemOrder(
    val orderType: OrderType
){
    class Date(orderType: OrderType) : ItemOrder(orderType)
    class NoteImportance(orderType: OrderType) : ItemOrder(orderType)
    fun copy(orderType: OrderType): ItemOrder =
        when (this) {
            is Date -> Date(orderType)
            is NoteImportance -> NoteImportance(orderType)
        }
}
