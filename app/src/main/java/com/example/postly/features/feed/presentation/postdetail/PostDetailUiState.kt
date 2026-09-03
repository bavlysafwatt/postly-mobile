package com.example.postly.features.feed.presentation.postdetail

import com.example.postly.core.domain.model.Post

data class PostDetailUiState(
    val post: Post? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val commentText: String = "",
    val isSubmittingComment: Boolean = false,
    val isCurrentUserAuthor: Boolean = false,
    val postDeleted: Boolean = false
)