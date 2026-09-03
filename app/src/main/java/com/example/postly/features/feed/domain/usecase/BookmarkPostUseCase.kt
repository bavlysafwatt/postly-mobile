package com.example.postly.features.feed.domain.usecase

import com.example.postly.core.domain.Result
import com.example.postly.core.network.AppError
import com.example.postly.features.feed.domain.repository.FeedRepository
import javax.inject.Inject

class BookmarkPostUseCase @Inject constructor(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(postId: String): Result<Unit, AppError> =
        repository.bookmarkPost(postId)
}