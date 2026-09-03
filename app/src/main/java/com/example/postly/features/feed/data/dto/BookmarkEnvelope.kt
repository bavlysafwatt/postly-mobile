package com.example.postly.features.feed.data.dto

data class BookmarkEnvelope(val bookmark: BookmarkDto)
data class BookmarkDto(val id: String? = null)

data class BookmarkRequest(val post: String)