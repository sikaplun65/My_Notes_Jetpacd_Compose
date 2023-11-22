package com.example.mynotes.presentation.notes.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetContent(
    onItemClicked:(String) -> Unit,
    categoryList: List<String>
) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp)) {
        items(categoryList){ category ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClicked(category) }
                    .padding(vertical = 4.dp, horizontal = 16.dp)
            ){
                Text(text = category)
            }
        }
    }
    Spacer(modifier = Modifier.height(30.dp))
}