package com.example.postly.core.data.dto
import com.example.postly.core.domain.model.Post

data class PostDto(
    val id: String? = null,
    val content: String = "",
    val author: UserSummaryDto? = null,
    val photos: List<String> = emptyList(),
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val createdAt: String? = null
)

fun PostDto.toDomain(): Post = Post(
    id = id.orEmpty(),
    content = content,
    author = author?.toDomain(),
    photos = photos,
    likeCount = likeCount,
    commentCount = commentCount,
    isLiked = isLiked,
    isBookmarked = isBookmarked,
    createdAt = createdAt.orEmpty()
)