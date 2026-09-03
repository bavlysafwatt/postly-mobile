package com.example.postly.features.feed.presentation.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.postly.core.components.EmptyState
import com.example.postly.core.components.ErrorState
import com.example.postly.core.components.PostCard
import com.example.postly.core.components.PostCardShimmer
import com.example.postly.core.network.PagingAppException
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.feed.domain.model.applyOverride

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    onPostClick: (String) -> Unit,
    onCreatePost: () -> Unit
) {
    val lazyPagingItems = viewModel.feed.collectAsLazyPagingItems()
    val overrides by viewModel.overrides.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        topBar = { FeedTopBar(onCreatePost = onCreatePost) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        val refreshState = lazyPagingItems.loadState.refresh
        val isInitialLoading = refreshState is LoadState.Loading && lazyPagingItems.itemCount == 0
        val isRefreshing = refreshState is LoadState.Loading && lazyPagingItems.itemCount > 0

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { lazyPagingItems.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isInitialLoading -> LazyColumn { items(2) { PostCardShimmer() } }

                refreshState is LoadState.Error && lazyPagingItems.itemCount == 0 -> {
                    ErrorState(
                        message = (refreshState.error as? PagingAppException)?.appError?.toUserMessage()
                            ?: "Something went wrong",
                        onRetry = { lazyPagingItems.retry() }
                    )
                }

                lazyPagingItems.itemCount == 0 -> {
                    EmptyState(
                        title = "No posts yet",
                        subtitle = "Be the first to share something",
                        icon = Icons.Rounded.PhotoCamera
                    )
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = lazyPagingItems.itemKey { it.id }) { index ->
                            lazyPagingItems[index]?.let { post ->
                                PostCard(
                                    post = post.applyOverride(overrides[post.id]),
                                    onClick = { onPostClick(post.id) },
                                    onLikeToggle = { viewModel.toggleLike(post) },
                                    onBookmarkToggle = { viewModel.toggleBookmark(post) }
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
                                            modifier = Modifier.size(28.dp),
                                            strokeWidth = 2.5.dp
                                        )
                                    }
                                }

                                is LoadState.Error -> {
                                    TextButton(
                                        onClick = { lazyPagingItems.retry() },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Retry")
                                    }
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

@Composable
private fun FeedTopBar(onCreatePost: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1.4f))
        Text(
            text = "POSTLY",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (2).sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onCreatePost),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.Add,
                contentDescription = "Create post",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}