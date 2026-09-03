package com.example.postly.features.feed.domain.usecase

import androidx.paging.PagingData
import com.example.postly.core.domain.model.Post
import com.example.postly.features.feed.domain.repository.FeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFeedUseCase @Inject constructor(
    private val repository: FeedRepository
) {
    operator fun invoke(): Flow<PagingData<Post>> = repository.getFeedPager()
}