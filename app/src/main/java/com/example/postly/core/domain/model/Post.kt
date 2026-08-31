package com.example.postly.core.domain.model

data class Post(
    val id: String,
    val content: String,
    val author: UserSummary?,
    val photos: List<String>,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val createdAt: String
)