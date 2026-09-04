package com.example.postly.features.search.domain.repository

import com.example.postly.core.domain.Result
import com.example.postly.core.domain.model.UserSummary
import com.example.postly.core.network.AppError
import com.example.postly.features.search.domain.model.RecentSearch
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchUsers(query: String): Result<List<UserSummary>, AppError>
    fun observeRecentSearches(): Flow<List<RecentSearch>>
    suspend fun saveRecentSearch(search: RecentSearch)
    suspend fun removeRecentSearch(userId: String)
    suspend fun clearRecentSearches()
}