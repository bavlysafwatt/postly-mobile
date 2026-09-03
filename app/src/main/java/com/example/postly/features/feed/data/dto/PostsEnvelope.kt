package com.example.postly.features.feed.data.dto

import com.example.postly.core.data.dto.PostDto

data class PostsEnvelope(
    val page: Int,
    val totalPages: Int,
    val totalResults: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val posts: List<PostDto>
)