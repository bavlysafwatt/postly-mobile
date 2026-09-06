package com.example.postly.features.profile.data.dto

import com.example.postly.core.data.dto.PostDto
import com.google.gson.annotations.SerializedName

data class BookmarkEntryDto(
    @SerializedName("_id")
    val id: String? = null,
    val post: PostDto? = null
)

data class BookmarksEnvelope(
    val results: Int = 0,
    val bookmarks: List<BookmarkEntryDto> = emptyList()
)