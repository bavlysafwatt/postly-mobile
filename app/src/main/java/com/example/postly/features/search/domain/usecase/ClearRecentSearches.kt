package com.example.postly.features.search.domain.usecase

import com.example.postly.features.search.domain.repository.SearchRepository
import javax.inject.Inject

class ClearRecentSearches @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke() {
        repository.clearRecentSearches()
    }
}