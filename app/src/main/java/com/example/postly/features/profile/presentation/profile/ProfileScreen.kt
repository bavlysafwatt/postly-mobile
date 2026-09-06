package com.example.postly.features.profile.presentation.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.postly.core.components.EmptyState
import com.example.postly.core.components.ErrorState
import com.example.postly.features.profile.presentation.profile.components.PostGridRow
import com.example.postly.features.profile.presentation.profile.components.ProfileHeader
import com.example.postly.features.profile.presentation.profile.components.ProfileTabRow
import com.example.postly.features.profile.presentation.profile.components.ShimmerGridRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: String?,
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToFollowers: (String) -> Unit,
    onNavigateToFollowing: (String) -> Unit,
    onPostClick: (String) -> Unit
) {
    LaunchedEffect(userId) { viewModel.load(userId) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { viewModel.errorEvent.collect { snackbarHostState.showSnackbar(it) } }

    LaunchedEffect(listState, uiState.selectedTab) {
        if (uiState.selectedTab != ProfileTab.POSTS) return@LaunchedEffect
        snapshotFlow { listState.layoutInfo }.collect { layoutInfo ->
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@collect
            if (lastVisible >= layoutInfo.totalItemsCount - 4) {
                viewModel.loadMorePostsIfNeeded()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(uiState.profile?.username?.let { "@$it" } ?: "Profile",
                        fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    if (!uiState.isOwnProfile) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (uiState.isOwnProfile) {
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                        }
                    }
                },
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoadingProfile && uiState.profile != null,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoadingProfile && uiState.profile == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                uiState.profile == null && uiState.profileError != null -> {
                    ErrorState(message = uiState.profileError!!, onRetry = { viewModel.refresh() })
                }

                uiState.profile != null -> {
                    val profile = uiState.profile!!
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        item {
                            ProfileHeader(
                                profile = profile,
                                isOwnProfile = uiState.isOwnProfile,
                                isFollowActionInProgress = uiState.isFollowActionInProgress,
                                postsCount = uiState.postsTotalCount,
                                onEditProfile = onNavigateToEditProfile,
                                onToggleFollow = viewModel::toggleFollow,
                                onFollowersClick = { onNavigateToFollowers(profile.id) },
                                onFollowingClick = { onNavigateToFollowing(profile.id) }
                            )
                        }

                        if (uiState.isOwnProfile) {
                            item {
                                ProfileTabRow(
                                    selectedTab = uiState.selectedTab,
                                    onTabSelected = viewModel::selectTab
                                )
                            }
                        }

                        val gridItems =
                            if (uiState.selectedTab == ProfileTab.POSTS) uiState.posts else uiState.bookmarks
                        val isLoadingGrid =
                            if (uiState.selectedTab == ProfileTab.POSTS) uiState.isLoadingPosts else uiState.isLoadingBookmarks
                        val gridError =
                            if (uiState.selectedTab == ProfileTab.POSTS) uiState.postsError else uiState.bookmarksError

                        when {
                            isLoadingGrid && gridItems.isEmpty() -> {
                                items(4) { ShimmerGridRow() }
                            }

                            gridError != null && gridItems.isEmpty() -> {
                                item {
                                    ErrorState(
                                        message = gridError,
                                        onRetry = {
                                            if (uiState.selectedTab == ProfileTab.POSTS) viewModel.loadPosts(
                                                reset = true
                                            )
                                        }
                                    )
                                }
                            }

                            gridItems.isEmpty() -> {
                                item {
                                    EmptyState(
                                        title = if (uiState.selectedTab == ProfileTab.POSTS) "No posts yet" else "No bookmarks yet",
                                        icon = if (uiState.selectedTab == ProfileTab.POSTS) Icons.Rounded.PhotoCamera else Icons.Rounded.Bookmarks
                                    )
                                }
                            }

                            else -> {
                                items(gridItems.chunked(3)) { row ->
                                    PostGridRow(
                                        row = row,
                                        onPostClick = onPostClick
                                    )
                                }
                                if (uiState.selectedTab == ProfileTab.POSTS && uiState.isLoadingMorePosts) {
                                    item {
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
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}