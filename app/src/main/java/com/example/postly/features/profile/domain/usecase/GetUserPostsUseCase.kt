package com.example.postly.features.profile.domain.usecase

import com.example.postly.core.common.Constants
import com.example.postly.core.domain.Result
import com.example.postly.core.network.AppError
import com.example.postly.features.profile.domain.model.PagedPosts
import com.example.postly.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetUserPostsUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String?, page: Int): Result<PagedPosts, AppError> =
        repository.getUserPosts(userId, page, Constants.DEFAULT_PAGE_SIZE)
}