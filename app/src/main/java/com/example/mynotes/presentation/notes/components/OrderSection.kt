package com.example.mynotes.presentation.notes.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mynotes.presentation.notes.NotesScreenViewModel
import com.example.mynotes.util.ItemOrder
import com.example.mynotes.util.OrderType

@Composable
fun OrderSection(
    noteOrder: ItemOrder = ItemOrder.Date(
        OrderType.Descending
    ),
    onOrderChange: (ItemOrder) -> Unit,
    toggleBottomSheetShow: Boolean,
    onToggleBottomSheetShowChanged: () -> Unit
) {
    Column (
        modifier = Modifier.padding(top = 60.dp)
    ){
        Row {
            Column {
                DefaultRadioButton(
                    text = "от старых к новым",
                    selected = noteOrder.orderType is OrderType.Ascending,
                    onSelect = {
                        onOrderChange(noteOrder.copy(OrderType.Ascending))
                    }
                )
                DefaultRadioButton(
                    text = "от новых к старым",
                    selected = noteOrder.orderType is OrderType.Descending,
                    onSelect = {
                        onOrderChange(noteOrder.copy(OrderType.Descending))
                    }
                )
            }
            Column {
                DefaultRadioButton(
                    text = "по дате",
                    selected = noteOrder is ItemOrder.Date,
                    onSelect = {
                        onOrderChange(ItemOrder.Date(noteOrder.orderType))
                    }
                )
                DefaultRadioButton(
                    text = "по важности",
                    selected = noteOrder is ItemOrder.NoteImportance,
                    onSelect = {
                        onOrderChange(ItemOrder.NoteImportance(noteOrder.orderType))
                    }
                )
            }
        }
        DefaultRadioButton(
            text = "Показать заметки по категории",
            selected = toggleBottomSheetShow,
            onSelect = {
                onToggleBottomSheetShowChanged()
            }
        )
        HorizontalDivider()
    }
}