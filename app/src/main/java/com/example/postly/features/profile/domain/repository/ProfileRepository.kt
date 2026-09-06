package com.example.postly.features.profile.domain.repository

import android.net.Uri
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.model.Post
import com.example.postly.core.domain.model.UserSummary
import com.example.postly.core.network.AppError
import com.example.postly.features.profile.domain.model.PagedPosts
import com.example.postly.features.profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getMyProfile(): Result<UserProfile, AppError>
    suspend fun getUserProfile(userId: String): Result<UserProfile, AppError>
    suspend fun updateMe(name: String, email: String, photoUri: Uri?): Result<UserProfile, AppError>
    suspend fun followUser(userId: String): Result<Unit, AppError>
    suspend fun unfollowUser(userId: String): Result<Unit, AppError>
    suspend fun getFollowers(userId: String?): Result<List<UserSummary>, AppError>
    suspend fun getFollowing(userId: String?): Result<List<UserSummary>, AppError>
    suspend fun getUserPosts(userId: String?, page: Int, limit: Int): Result<PagedPosts, AppError>
    suspend fun getMyBookmarks(): Result<List<Post>, AppError>
}