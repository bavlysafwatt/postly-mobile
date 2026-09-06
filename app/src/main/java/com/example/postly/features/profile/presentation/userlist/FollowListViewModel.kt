package com.example.postly.features.profile.presentation.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.core.domain.Result
import com.example.postly.core.data.local.CurrentUserCache
import com.example.postly.core.domain.model.UserSummary
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.profile.domain.usecase.GetFollowersUseCase
import com.example.postly.features.profile.domain.usecase.GetFollowingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowListViewModel @Inject constructor(
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getFollowingUseCase: GetFollowingUseCase,
    private val currentUserCache: CurrentUserCache
) : ViewModel() {

    private val _uiState = MutableStateFlow(FollowListUiState())
    val uiState: StateFlow<FollowListUiState> = _uiState.asStateFlow()

    private var loadedKey: String? = null

    fun load(userId: String, mode: FollowListMode) {
        val key = "$userId:$mode"
        if (loadedKey == key) return
        loadedKey = key

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val currentUser = currentUserCache.observeUser().first()
            val effectiveUserId = if (userId == currentUser?.id) null else userId
            val result = when (mode) {
                FollowListMode.FOLLOWERS -> getFollowersUseCase(effectiveUserId)
                FollowListMode.FOLLOWING -> getFollowingUseCase(effectiveUserId)
            }
            when (result) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        users = result.data,
                        isLoading = false
                    )
                }

                is Result.Failure -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.error.toUserMessage()
                    )
                }
            }
        }
    }
}