package com.example.postly.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.model.UserSummary
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.search.domain.model.RecentSearch
import com.example.postly.features.search.domain.usecase.ClearRecentSearches
import com.example.postly.features.search.domain.usecase.GetRecentSearches
import com.example.postly.features.search.domain.usecase.RemoveRecentSearch
import com.example.postly.features.search.domain.usecase.SaveRecentSearch
import com.example.postly.features.search.domain.usecase.SearchUsers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUsers: SearchUsers,
    getRecentSearches: GetRecentSearches,
    private val saveRecentSearch: SaveRecentSearch,
    private val removeRecentSearchUseCase: RemoveRecentSearch,
    private val clearRecentSearches: ClearRecentSearches
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Empty)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val recentSearches = getRecentSearches()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            recentSearches.collect { searches ->
                if (_query.value.isBlank()) {
                    _uiState.value = SearchUiState.Recent(searches)
                }
            }
        }
    }

    fun onQueryChange(value: String) {
        _query.value = value
        searchJob?.cancel()

        if (value.isBlank()) {
            viewModelScope.launch {
                recentSearches.first().let { searches ->
                    _uiState.value = SearchUiState.Recent(searches)
                }
            }
            return
        }

        _uiState.value = SearchUiState.Loading

        searchJob = viewModelScope.launch {
            delay(400)

            val result = searchUsers(value.trim())

            if (_query.value != value) return@launch

            when (result) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        _uiState.value = SearchUiState.NoResults(value.trim())
                    } else {
                        _uiState.value = SearchUiState.Results(result.data)
                    }
                }

                is Result.Failure -> {
                    _uiState.value = SearchUiState.Error(result.error.toUserMessage())
                }
            }
        }
    }

    fun saveAndOpenUser(
        user: UserSummary,
        onNavigate: (String) -> Unit
    ) {
        viewModelScope.launch {
            saveRecentSearch(
                RecentSearch(
                    userId = user.id,
                    username = user.username,
                    name = user.name,
                    photo = user.photo,
                    searchedAt = System.currentTimeMillis()
                )
            )

            onNavigate(user.id)
        }
    }

    fun openRecentSearch(
        search: RecentSearch,
        onNavigate: (String) -> Unit
    ) {
        viewModelScope.launch {
            saveRecentSearch(
                search.copy(
                    searchedAt = System.currentTimeMillis()
                )
            )

            onNavigate(search.userId)
        }
    }

    fun removeRecentSearch(userId: String) {
        viewModelScope.launch {
            removeRecentSearchUseCase(userId)
        }
    }

    fun clearAllRecentSearches() {
        viewModelScope.launch {
            clearRecentSearches()
        }
    }

    fun retry() {
        val currentQuery = _query.value
        if (currentQuery.isNotBlank()) {
            onQueryChange(currentQuery)
        }
    }
}