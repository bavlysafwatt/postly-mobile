package com.example.postly.features.search.domain.usecase

import com.example.postly.features.search.domain.repository.SearchRepository
import javax.inject.Inject

class RemoveRecentSearch @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(userId: String) {
        repository.removeRecentSearch(userId)
    }
}