package com.example.mynotes.presentation.notes.add_edit_note

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mynotes.presentation.notes.NotesScreenViewModel
import com.example.mynotes.presentation.notes.add_edit_note.componets.TransparentHintTextField
import com.example.mynotes.util.getDate
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    viewModelNotesScreen: NotesScreenViewModel = hiltViewModel()
) {
    val categoryState = viewModel.noteCategory.value
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val importanceState = viewModel.noteImportance.value.noteImportance
    val timeStamp = viewModel.timeStamp

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    val categories = viewModelNotesScreen.currentCategoryList
    val keyboardController = LocalSoftwareKeyboardController.current
//    val filteredCategories = viewModel.filteredCategories

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }

                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = it.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.SaveAlt, contentDescription = "save note")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = getDate(timeStamp)
                )

                Column {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(
                                if (importanceState) Color.Red
                                else Color.Transparent
                            )
                            .border(
                                width = 3.dp,
                                color = Color.Black,
                                shape = CircleShape
                            )
                            .clickable {
                                viewModel.onEvent(AddEditNoteEvent.ChangeNoteImportance)
                            }
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Важно",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { expanded = !expanded },
                ) {
                    Icon(
                        imageVector =if (!expanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                        contentDescription = ""
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    TransparentHintTextField(
                        text = categoryState.text,
                        hint = categoryState.hint,
                        onValueChange = {
                            viewModel.onEvent(AddEditNoteEvent.EnteredCategory(it))
                        },
                        onFocusChange = { focusState ->
                            viewModel.onEvent(AddEditNoteEvent.ChangeCategoryFocus(focusState))
                        },
                        isHintVisible = categoryState.isHintVisible,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    if (expanded) {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background),
                            offset = DpOffset(x = 0.dp, y = 10.dp)
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(text = category) },
                                    onClick = {
                                        viewModel.onEvent(AddEditNoteEvent.EnteredCategory(category))
                                        viewModel.noteCategory.value.isHintVisible = false
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}


/*
Box(
modifier = Modifier
.fillMaxWidth(1f)
) {
    ExposedDropdownMenuBox(
        expanded = isDropdownVisible,
        onExpandedChange = {isDropdownVisible = !isDropdownVisible}
    ) {
        TextField(
            value = categoryState.text,
            onValueChange = {
                selectedText = it
                viewModel.onEvent(AddEditNoteEvent.EnteredCategory(it))
            },
            label = {categoryState.hint},
            trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownVisible)},
            modifier = Modifier.menuAnchor()
        )
    }
    val filterOptions =
        categories.filter { it.contains(selectedText, ignoreCase = true) }

    if (selectedText.isNotEmpty()){
        DropdownMenu(
            expanded = !isDropdownVisible,
            onDismissRequest = {
                isDropdownVisible = false
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            filterOptions.forEach { category ->
                DropdownMenuItem(
                    text = { Text(text = category) },
                    onClick = {
                        selectedText = category
                        viewModel.onEvent(AddEditNoteEvent.EnteredCategory(category))
                        isDropdownVisible = false
                        selectedText = ""
                    }
                )
                HorizontalDivider()
            }
        }
    }
}*/
