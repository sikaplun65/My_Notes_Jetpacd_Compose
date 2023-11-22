package com.example.mynotes.util

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}
