package com.example.mynotes.presentation.main_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.EventRepeat
import androidx.compose.material.icons.outlined.Note
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.mynotes.presentation.notes.navigation.NotesScreensNavigation
import com.example.mynotes.presentation.reminders.RemindersScreen
import com.example.mynotes.presentation.shopping.ShoppingScreen
import com.example.mynotes.presentation.tasks.TasksScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavController) {
    val tabItems = getTabItems()

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val pagerState = rememberPagerState {
        tabItems.size
    }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = {
                        Text(text = item.title)
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedTabIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            val selectedTabItem = tabItems[index]
            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTabItem.title) {
                    "Заметки" -> NotesScreensNavigation(navController)
                    "Покупки" -> ShoppingScreen()
                    "Задачи" -> TasksScreen()
                    "Напоминания" -> RemindersScreen()
                }
            }
        }
    }
}

private fun getTabItems(): List<TabItem> {
    return listOf(
        TabItem(
            title = "Заметки",
            unselectedIcon = Icons.Outlined.Note,
            selectedIcon = Icons.Filled.Note
        ),
        TabItem(
            title = "Покупки",
            unselectedIcon = Icons.Outlined.ShoppingCart,
            selectedIcon = Icons.Filled.ShoppingCart
        ),
        TabItem(
            title = "Задачи",
            unselectedIcon = Icons.Outlined.Task,
            selectedIcon = Icons.Filled.Task
        ),
        TabItem(
            title = "Напоминания",
            unselectedIcon = Icons.Outlined.EventRepeat,
            selectedIcon = Icons.Filled.EventRepeat
        )
    )
}