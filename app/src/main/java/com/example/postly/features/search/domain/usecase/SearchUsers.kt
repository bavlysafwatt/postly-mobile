package com.example.postly.features.search.domain.usecase

import com.example.postly.features.search.domain.repository.SearchRepository
import javax.inject.Inject

class SearchUsers @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String) =
        repository.searchUsers(query)
}