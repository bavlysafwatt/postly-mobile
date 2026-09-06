package com.example.postly.features.profile.domain.model

import com.example.postly.core.domain.model.Post

data class PagedPosts(
    val posts: List<Post>,
    val hasNextPage: Boolean,
    val totalResults: Int
)