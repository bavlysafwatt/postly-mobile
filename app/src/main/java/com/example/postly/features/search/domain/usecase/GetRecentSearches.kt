package com.example.postly.features.search.domain.usecase

import com.example.postly.features.search.domain.repository.SearchRepository
import javax.inject.Inject

class GetRecentSearches @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke() =
        repository.observeRecentSearches()
}