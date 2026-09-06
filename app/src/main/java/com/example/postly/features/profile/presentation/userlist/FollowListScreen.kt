package com.example.postly.features.profile.presentation.userlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.postly.core.components.EmptyState
import com.example.postly.core.components.ErrorState
import com.example.postly.core.components.ShimmerBone
import com.example.postly.features.profile.presentation.userlist.components.FollowRow
import com.example.postly.features.profile.presentation.userlist.components.FollowRowShimmer
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowListScreen(
    userId: String,
    mode: FollowListMode,
    viewModel: FollowListViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onUserClick: (String) -> Unit
) {
    LaunchedEffect(userId, mode) { viewModel.load(userId, mode) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (mode == FollowListMode.FOLLOWERS) "Followers" else "Following",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            when {
                uiState.isLoading -> LazyColumn { items(8) { FollowRowShimmer() } }
                uiState.errorMessage != null -> ErrorState(message = uiState.errorMessage!!)
                uiState.users.isEmpty() -> EmptyState(
                    title = if (mode == FollowListMode.FOLLOWERS) "No followers yet" else "Not following anyone yet",
                    icon = Icons.Rounded.PersonOutline
                )

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.users, key = { it.id }) { user ->
                            FollowRow(user = user, onClick = { onUserClick(user.id) })
                        }
                    }
                }
            }
        }
    }
}