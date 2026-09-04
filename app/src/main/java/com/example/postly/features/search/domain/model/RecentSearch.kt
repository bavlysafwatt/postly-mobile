package com.example.postly.features.search.domain.model

data class RecentSearch(
    val userId: String,
    val username: String,
    val name: String,
    val photo: String?,
    val searchedAt: Long
)