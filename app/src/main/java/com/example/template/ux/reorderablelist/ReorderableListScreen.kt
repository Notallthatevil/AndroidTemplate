package com.example.template.ux.reorderablelist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun ReorderableListScreen(navController: NavController, viewModel: ReorderableListViewModel = hiltViewModel()) {
    ReorderableListContent(viewModel.uiState, navController::popBackStack)
}

@Composable
fun ReorderableListContent(uiState: ReorderableListUiState, onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.REORDERABLE_LIST.title, onBack = onBack) }) { paddingValues ->
        val list by uiState.listFlow.collectAsState()
        val state = rememberReorderableLazyListState(onMove = uiState.onMove, canDragOver = uiState.canDragOver)
        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .padding(paddingValues)
                .reorderable(state)
                .fillMaxSize()
        ) {
            items(items = list, key = { it.id }) { item ->
                ReorderableItem(reorderableState = state, key = item.id) { isDragging ->
                    val elevation by animateDpAsState(if (isDragging) 16.dp else 0.dp)
                    Box(
                        modifier = Modifier
                            .shadow(elevation)
                            .background(if (isDragging) MaterialTheme.colors.onPrimary else MaterialTheme.colors.background)
                            .detectReorderAfterLongPress(state) // remove to prevent selection
                    ) {
                        ListItem(text = { Text(item.value) })
                    }
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun ReorderableListContentPreview() {
    AppTheme { ReorderableListContent(ReorderableListUiState()) }
}