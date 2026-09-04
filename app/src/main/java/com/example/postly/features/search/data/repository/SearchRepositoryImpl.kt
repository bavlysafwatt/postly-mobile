package com.example.postly.features.search.data.repository

import com.example.postly.core.data.dto.toDomain
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.map
import com.example.postly.core.domain.model.UserSummary
import com.example.postly.core.network.AppError
import com.example.postly.core.network.safeApiCall
import com.example.postly.features.search.data.local.RecentSearchDao
import com.example.postly.features.search.data.local.RecentSearchEntity
import com.example.postly.features.search.data.local.toDomain
import com.example.postly.features.search.data.remote.SearchApi
import com.example.postly.features.search.domain.model.RecentSearch
import com.example.postly.features.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: SearchApi,
    private val dao: RecentSearchDao
) : SearchRepository {

    override suspend fun searchUsers(
        query: String
    ): Result<List<UserSummary>, AppError> {
        return safeApiCall {
            api.searchUsers(query)
        }.map { envelope ->
            envelope.users
                .take(5)
                .map { it.toDomain() }
        }
    }

    override fun observeRecentSearches(): Flow<List<RecentSearch>> {
        return dao.observeRecentSearches()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun saveRecentSearch(search: RecentSearch) {
        dao.upsert(
            RecentSearchEntity(
                userId = search.userId,
                username = search.username,
                name = search.name,
                photo = search.photo,
                searchedAt = search.searchedAt
            )
        )

        dao.deleteOlderThan10()
    }

    override suspend fun removeRecentSearch(userId: String) {
        dao.delete(userId)
    }

    override suspend fun clearRecentSearches() {
        dao.clearAll()
    }
}