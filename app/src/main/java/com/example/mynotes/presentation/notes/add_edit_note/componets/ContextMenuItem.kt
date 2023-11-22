package com.example.mynotes.presentation.notes.add_edit_note.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ContextMenuItem(
    modifier: Modifier = Modifier,
    category: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(text = category)
    }
}

@Preview
@Composable
fun PreviewItem(){
    ContextMenuItem(category = "Category") {
        
    }
}