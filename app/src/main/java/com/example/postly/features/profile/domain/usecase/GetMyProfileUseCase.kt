package com.example.postly.features.profile.domain.usecase

import com.example.postly.core.domain.Result
import com.example.postly.core.data.local.CurrentUserCache
import com.example.postly.core.domain.model.User
import com.example.postly.core.network.AppError
import com.example.postly.features.profile.domain.model.UserProfile
import com.example.postly.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
    private val currentUserCache: CurrentUserCache
) {
    suspend operator fun invoke(): Result<UserProfile, AppError> {
        val result = repository.getMyProfile()
        if (result is Result.Success) {
            val profile = result.data
            currentUserCache.save(
                User(
                    profile.id,
                    profile.name,
                    profile.username,
                    profile.email,
                    profile.photo,
                    profile.followers,
                    profile.following
                )
            )
        }
        return result
    }
}