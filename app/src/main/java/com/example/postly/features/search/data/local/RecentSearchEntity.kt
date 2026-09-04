package com.example.postly.features.search.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.postly.features.search.domain.model.RecentSearch

@Entity(tableName = "recent_searches")
data class RecentSearchEntity(
    @PrimaryKey
    val userId: String,
    val username: String,
    val name: String,
    val photo: String?,
    val searchedAt: Long
)

fun RecentSearchEntity.toDomain(): RecentSearch =
    RecentSearch(
        userId = userId,
        username = username,
        name = name,
        photo = photo,
        searchedAt = searchedAt
    )