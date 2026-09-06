package com.example.postly.features.profile.domain.usecase

import com.example.postly.core.domain.Result
import com.example.postly.core.domain.model.UserSummary
import com.example.postly.core.network.AppError
import com.example.postly.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetFollowersUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String?): Result<List<UserSummary>, AppError> =
        repository.getFollowers(userId)
}