package com.example.postly.features.profile.presentation.userlist

import com.example.postly.core.domain.model.UserSummary

enum class FollowListMode { FOLLOWERS, FOLLOWING }

data class FollowListUiState(
    val users: List<UserSummary> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)