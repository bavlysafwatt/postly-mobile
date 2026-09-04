package com.example.postly.features.search.domain.usecase

import com.example.postly.features.search.domain.model.RecentSearch
import com.example.postly.features.search.domain.repository.SearchRepository
import javax.inject.Inject

class SaveRecentSearch @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(search: RecentSearch) {
        repository.saveRecentSearch(search)
    }
}