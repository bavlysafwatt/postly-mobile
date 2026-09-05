package com.example.postly.features.notifications.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.postly.core.components.EmptyState
import com.example.postly.core.components.ErrorState
import com.example.postly.core.network.PagingAppException
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.notifications.presentation.components.NotificationRow
import com.example.postly.features.notifications.presentation.components.NotificationRowShimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = hiltViewModel(),
    onNavigateToPostDetail: (String) -> Unit,
    onNavigateToUserProfile: (String) -> Unit
) {
    val lazyPagingItems = viewModel.notifications.collectAsLazyPagingItems()
    val readOverlay by viewModel.readOverlay.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NotificationNavigationEvent.ToPostDetail -> onNavigateToPostDetail(event.postId)
                is NotificationNavigationEvent.ToUserProfile -> onNavigateToUserProfile(event.userId)
            }
        }
    }
    LaunchedEffect(Unit) { viewModel.refreshEvent.collect { lazyPagingItems.refresh() } }
    LaunchedEffect(Unit) { viewModel.errorEvent.collect { snackbarHostState.showSnackbar(it) } }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Notifications", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = { viewModel.markAllAsRead() }) {
                        Text("Mark all read")
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        val refreshState = lazyPagingItems.loadState.refresh
        val isInitialLoading = refreshState is LoadState.Loading && lazyPagingItems.itemCount == 0

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            when {
                isInitialLoading -> {
                    LazyColumn { items(8) { NotificationRowShimmer() } }
                }

                refreshState is LoadState.Error && lazyPagingItems.itemCount == 0 -> {
                    ErrorState(
                        message = (refreshState.error as? PagingAppException)?.appError?.toUserMessage()
                            ?: "Something went wrong",
                        onRetry = { lazyPagingItems.retry() }
                    )
                }

                lazyPagingItems.itemCount == 0 -> {
                    EmptyState(
                        title = "No notifications yet",
                        subtitle = "Likes, comments, and new followers will show up here",
                        icon = Icons.Rounded.Notifications
                    )
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = lazyPagingItems.itemKey { it.id }) { index ->
                            lazyPagingItems[index]?.let { notification ->
                                val isRead =
                                    notification.isRead || readOverlay.contains(notification.id)
                                NotificationRow(
                                    notification = notification,
                                    isRead = isRead,
                                    onClick = { viewModel.onNotificationClick(notification) }
                                )
                            }
                        }

                        item {
                            when (lazyPagingItems.loadState.append) {
                                is LoadState.Loading -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            strokeWidth = 2.dp
                                        )
                                    }
                                }

                                is LoadState.Error -> {
                                    TextButton(
                                        onClick = { lazyPagingItems.retry() },
                                        modifier = Modifier.fillMaxWidth()
                                    ) { Text("Retry") }
                                }

                                else -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}