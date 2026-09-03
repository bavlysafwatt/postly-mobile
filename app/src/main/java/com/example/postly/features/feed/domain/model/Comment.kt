package com.example.postly.features.feed.domain.model

import com.example.postly.core.domain.model.UserSummary

data class Comment(
    val id: String,
    val content: String,
    val user: UserSummary?,
)