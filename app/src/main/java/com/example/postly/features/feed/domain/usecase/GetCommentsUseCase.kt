package com.example.postly.features.feed.domain.usecase

import androidx.paging.PagingData
import com.example.postly.features.feed.domain.model.Comment
import com.example.postly.features.feed.domain.repository.FeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: FeedRepository
) {
    operator fun invoke(postId: String): Flow<PagingData<Comment>> =
        repository.getCommentsPager(postId)
}