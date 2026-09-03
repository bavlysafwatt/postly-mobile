package com.example.postly.features.feed.domain.usecase

import android.net.Uri
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.model.Post
import com.example.postly.core.network.AppError
import com.example.postly.features.feed.domain.repository.FeedRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(content: String, photoUris: List<Uri>): Result<Post, AppError> =
        repository.createPost(content, photoUris)
}