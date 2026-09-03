package com.example.postly.features.feed.data.dto

import com.example.postly.core.data.dto.UserSummaryDto
import com.example.postly.core.data.dto.toDomain
import com.example.postly.features.feed.domain.model.Comment
import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("_id")
    val id: String? = null,
    val content: String = "",
    val user: UserSummaryDto? = null
)

fun CommentDto.toDomain(): Comment = Comment(
    id = id.orEmpty(),
    content = content,
    user = user?.toDomain()
)