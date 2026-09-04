package com.example.postly.features.search.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.postly.core.components.ErrorState
import com.example.postly.features.search.presentation.components.PostlySearchField
import com.example.postly.features.search.presentation.components.RecentSearchContent
import com.example.postly.features.search.presentation.components.SearchLoading
import com.example.postly.features.search.presentation.components.SearchResults

@Composable
fun SearchScreen(
    onUserClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        PostlySearchField(
            value = query,
            onValueChange = viewModel::onQueryChange,
            onClear = {
                viewModel.onQueryChange("")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val currentState = state) {
            SearchUiState.Empty -> Unit

            is SearchUiState.Recent -> {
                RecentSearchContent(
                    searches = currentState.searches,
                    onSearchClick = { search ->
                        viewModel.openRecentSearch(
                            search = search,
                            onNavigate = onUserClick
                        )
                    },
                    onRemove = viewModel::removeRecentSearch,
                    onClearAll = viewModel::clearAllRecentSearches
                )
            }

            SearchUiState.Loading -> {
                SearchLoading()
            }

            is SearchUiState.Results -> {
                SearchResults(
                    users = currentState.users,
                    onUserClick = { user ->
                        viewModel.saveAndOpenUser(
                            user = user,
                            onNavigate = onUserClick
                        )
                    }
                )
            }

            is SearchUiState.NoResults -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No users found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "No results for \"${currentState.query}\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            is SearchUiState.Error -> {
                ErrorState(
                    message = currentState.message,
                    onRetry = viewModel::retry
                )
            }
        }
    }
}

