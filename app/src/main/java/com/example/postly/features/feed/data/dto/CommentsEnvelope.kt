package com.example.postly.features.feed.data.dto

data class CommentsEnvelope(
    val page: Int,
    val totalPages: Int,
    val totalResults: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val comments: List<CommentDto>
)