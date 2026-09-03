package com.example.postly.features.feed.domain.model

import com.example.postly.core.domain.model.Post

data class PostOverride(
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val likeCount: Int
)

fun Post.applyOverride(override: PostOverride?): Post {
    if (override == null) return this
    return copy(
        isLiked = override.isLiked,
        isBookmarked = override.isBookmarked,
        likeCount = override.likeCount
    )
}