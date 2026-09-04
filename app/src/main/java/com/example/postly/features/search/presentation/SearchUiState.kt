package com.example.postly.features.search.presentation

import com.example.postly.core.domain.model.UserSummary
import com.example.postly.features.search.domain.model.RecentSearch

sealed interface SearchUiState {
    data object Empty : SearchUiState

    data class Recent(
        val searches: List<RecentSearch>
    ) : SearchUiState

    data object Loading : SearchUiState

    data class Results(
        val users: List<UserSummary>
    ) : SearchUiState

    data class NoResults(
        val query: String
    ) : SearchUiState

    data class Error(
        val message: String
    ) : SearchUiState
}