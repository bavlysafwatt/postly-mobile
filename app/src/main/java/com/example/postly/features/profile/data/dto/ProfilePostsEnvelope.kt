package com.example.postly.features.profile.data.dto

import com.example.postly.core.data.dto.PostDto

data class ProfilePostsEnvelope(
    val page: Int,
    val totalPages: Int,
    val totalResults: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val posts: List<PostDto>
)