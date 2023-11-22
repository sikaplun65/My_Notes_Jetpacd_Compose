package com.example.mynotes.presentation.notes.components


import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreenAppBar(
    onSearchIconClicked: () -> Unit,
    onIconSortedClicked: () -> Unit,
    isSortSectionVisible: Boolean,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val textScale = if (screenWidth < 350)  0.35f else 0.6f

    val density = LocalDensity.current.density
    val scaledTextSize = with(LocalDensity.current) {
        (MaterialTheme.typography.bodySmall.fontSize.value * density * textScale).sp
    }

    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            AnimatedVisibility(
                visible = isSortSectionVisible,
                enter = slideInHorizontally() + expandHorizontally(expandFrom = Alignment.End)
                        + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth })
                        + shrinkHorizontally() + fadeOut(),
            ) {
                Text(
                    text = "Сортировать",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
//                    fontWeight = FontWeight.Bold,
                    fontSize = scaledTextSize
                )
            }

//            Row {
//                Box {
//                    this@Row.AnimatedVisibility(
//                        isSortSectionVisible,
//                        enter = slideInHorizontally() + expandHorizontally(expandFrom = Alignment.End)
//                                + fadeIn(),
//                        exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth })
//                                + shrinkHorizontally() + fadeOut(),
//                    ) {
//                        Text(
//                            modifier = Modifier.padding(end = 10.dp),
//                            text = "Сортировать",
//                            fontSize = scaledTextSize
//                        )
//                    }
//                }
//
//                Text(
//                    text = "Заметки",
//                    fontSize = scaledTextSize
//                )
//
//            }
        },
        actions = {
            IconButton(onClick = { onSearchIconClicked() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search")
            }
            IconButton(onClick = { onIconSortedClicked() }) {
                Icon(imageVector = Icons.Default.Sort, contentDescription = "sort")
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
    HorizontalDivider()
}