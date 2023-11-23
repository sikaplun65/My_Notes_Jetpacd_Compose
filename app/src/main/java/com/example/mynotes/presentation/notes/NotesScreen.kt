package com.example.mynotes.presentation.notes

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mynotes.presentation.notes.components.BottomSheetContent
import com.example.mynotes.presentation.notes.components.NoteItem
import com.example.mynotes.presentation.notes.components.NotesScreenAppBar
import com.example.mynotes.presentation.notes.components.OrderSection
import com.example.mynotes.presentation.notes.components.SearchAppBar
import com.example.mynotes.presentation.notes.navigation.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun NotesScreen(
    navController: NavHostController,
    viewModel: NotesScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val isOrderSectionVisibleCurrently = remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var shouldBottomSheetShow by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        if (state.searchQuery.isNotEmpty()) {
            viewModel.onEvent(NotesEvent.OnSearchQueryChanged(query = state.searchQuery))
        }
    }

    LaunchedEffect(key1 = scrollBehavior.state.collapsedFraction == 1f){
        if (state.isOrderSectionVisible) {
            viewModel.onEvent(NotesEvent.ToggleOrderSection)
        }
    }

    LaunchedEffect(key1 = scrollBehavior.state.collapsedFraction == 0f){
        if (isOrderSectionVisibleCurrently.value) {
            viewModel.onEvent(NotesEvent.ToggleOrderSection)
        }
    }

    if (shouldBottomSheetShow) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { shouldBottomSheetShow = false },
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(
                        text = "Выберите категорию",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                }
            },
            content = {
                BottomSheetContent(
                    onItemClicked = { category ->
                        viewModel.onEvent(NotesEvent.BottomSheetItemClicked(category))
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) shouldBottomSheetShow = false
                        }
                    },
                    categoryList = viewModel.state.value.categories
                )
            }
        )

    }

    Crossfade(targetState = state.isSearchBarVisible, label = "") { isVisible ->
        if (isVisible) {
            Column {
                SearchAppBar(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth(),
                    value = state.searchQuery,
                    onInputValueChange = { newValue ->
                        viewModel.onEvent(NotesEvent.OnSearchQueryChanged(newValue))
                    },
                    onCloseIconClicked = { viewModel.onEvent(NotesEvent.OnCloseSearchIconClicked) },
                    onSearchIconClicked = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.notes) { note ->
                        NoteItem(
                            note = note,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        Screens.AddEditNoteScreen.route +
                                                "?noteId=${note.id}"
                                    )
                                },
                            onDeleteClick = {
                                showDeleteConfirmationSnackbar(
                                    scope = scope,
                                    deleteNote = {
                                        viewModel.onEvent(NotesEvent.DeleteNote(note))
                                    },
                                    snackbarHostState = snackbarHostState
                                )
                                state.isSearchBarVisible = !state.isSearchBarVisible
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        } else {
            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                },
                topBar = {
                    NotesScreenAppBar(
                        scrollBehavior = scrollBehavior,
                        onSearchIconClicked = {
                            viewModel.onEvent(NotesEvent.OnSearchIconClicked)
                            scope.launch {
                                delay(500)
                                focusRequester.requestFocus()
                            }
                        },
                        onIconSortedClicked = {
                            viewModel.onEvent(NotesEvent.ToggleOrderSection)
                            isOrderSectionVisibleCurrently.value = !isOrderSectionVisibleCurrently.value
                        },
                        isSortSectionVisible = state.isOrderSectionVisible
                    )

                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Screens.AddEditNoteScreen.route)
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
                    }
                }
            ) { innerPadding ->
                val topPadding =
                    if (state.isOrderSectionVisible) 4.dp
                    else innerPadding.calculateTopPadding() + 4.dp

                Column(
                    modifier = Modifier
                ) {
                    AnimatedVisibility(
                        visible = state.isOrderSectionVisible,
                        enter = slideInVertically()
                                + expandVertically(expandFrom = Alignment.Top)
                                + fadeIn(),
                        exit = slideOutVertically()
                                + shrinkVertically()
                                + fadeOut()
                                + scaleOut(),
                    ) {
                        OrderSection(
                            noteOrder = state.noteOrder,
                            onOrderChange = {
                                viewModel.onEvent(NotesEvent.GetAllNotesByOrder(it))
                                viewModel.onEvent(NotesEvent.ToggleOrderSection)
                            },
                            shouldBottomSheetShow,
                            onToggleBottomSheetShowChanged = {
                                viewModel.onEvent(NotesEvent.GetCategoriesList)
                                viewModel.onEvent(NotesEvent.ToggleOrderSection)
                                shouldBottomSheetShow = !shouldBottomSheetShow
                            }
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .padding(top = topPadding)
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        contentPadding = PaddingValues(start = 8.dp, end = 8.dp)
                    ) {
                        items(state.notes) { note ->
                            NoteItem(
                                note = note,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(
                                            Screens.AddEditNoteScreen.route +
                                                    "?noteId=${note.id}"
                                        )
                                    },
                                onDeleteClick = {
                                    showDeleteConfirmationSnackbar(
                                        scope = scope,
                                        deleteNote = {
                                            viewModel.onEvent(NotesEvent.DeleteNote(note))
                                        },
                                        snackbarHostState = snackbarHostState
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

private fun showDeleteConfirmationSnackbar(
    scope: CoroutineScope,
    deleteNote: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    scope.launch {
        val result = snackbarHostState
            .showSnackbar(
                message = "Заметка будет удалена!!!",
                actionLabel = "Отменить",
                duration = SnackbarDuration.Short
            )
        when (result) {
            SnackbarResult.ActionPerformed -> {
                return@launch
            }

            SnackbarResult.Dismissed -> {
                deleteNote()
            }
        }
    }
}
