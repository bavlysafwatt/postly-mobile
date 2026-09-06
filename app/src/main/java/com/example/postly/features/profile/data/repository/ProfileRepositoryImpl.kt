package com.example.postly.features.profile.data.repository

import android.content.Context
import android.net.Uri
import com.example.postly.core.data.dto.toDomain
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.map
import com.example.postly.core.domain.model.Post
import com.example.postly.core.domain.model.UserSummary
import com.example.postly.core.network.AppError
import com.example.postly.core.network.safeApiCall
import com.example.postly.core.network.safeApiCallUnit
import com.example.postly.features.profile.data.dto.toDomain
import com.example.postly.features.profile.data.remote.ProfileApi
import com.example.postly.features.profile.domain.model.PagedPosts
import com.example.postly.features.profile.domain.model.UserProfile
import com.example.postly.features.profile.domain.repository.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    @ApplicationContext private val context: Context
) : ProfileRepository {

    override suspend fun getMyProfile(): Result<UserProfile, AppError> =
        safeApiCall { api.getMe() }.map { it.user.toDomain() }

    override suspend fun getUserProfile(userId: String): Result<UserProfile, AppError> =
        safeApiCall { api.getUser(userId) }.map { it.user.toDomain() }

    override suspend fun updateMe(
        name: String,
        email: String,
        photoUri: Uri?
    ): Result<UserProfile, AppError> {
        val photoPart = photoUri?.let { uriToMultipart(it) }
        val result = safeApiCall {
            api.updateMe(
                name = name.toPlainRequestBody(),
                email = email.toPlainRequestBody(),
                photo = photoPart
            )
        }
        return result.map { it.user.toDomain() }
    }

    override suspend fun followUser(userId: String): Result<Unit, AppError> =
        safeApiCall { api.followUser(userId) }.map { }

    override suspend fun unfollowUser(userId: String): Result<Unit, AppError> =
        safeApiCallUnit { api.unfollowUser(userId) }

    override suspend fun getFollowers(userId: String?): Result<List<UserSummary>, AppError> {
        val result = if (userId == null) safeApiCall { api.getMyFollowers() } else safeApiCall {
            api.getFollowersByUser(userId)
        }
        return result.map { envelope -> envelope.followers.mapNotNull { it.follower?.toDomain() } }
    }

    override suspend fun getFollowing(userId: String?): Result<List<UserSummary>, AppError> {
        val result = if (userId == null) safeApiCall { api.getMyFollowing() } else safeApiCall {
            api.getFollowingByUser(userId)
        }
        return result.map { envelope -> envelope.following.mapNotNull { it.following?.toDomain() } }
    }

    override suspend fun getUserPosts(
        userId: String?,
        page: Int,
        limit: Int
    ): Result<PagedPosts, AppError> {
        val result = if (userId == null) safeApiCall {
            api.getMyPosts(
                page,
                limit
            )
        } else safeApiCall { api.getUserPosts(userId, page, limit) }
        return result.map { envelope ->
            PagedPosts(
                posts = envelope.posts.map { it.toDomain() },
                hasNextPage = envelope.hasNextPage,
                totalResults = envelope.totalResults
            )
        }
    }

    override suspend fun getMyBookmarks(): Result<List<Post>, AppError> =
        safeApiCall { api.getMyBookmarks() }.map { envelope -> envelope.bookmarks.mapNotNull { it.post?.toDomain() } }

    private fun String.toPlainRequestBody() = toRequestBody("text/plain".toMediaTypeOrNull())

    private fun uriToMultipart(uri: Uri): MultipartBody.Part? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val bytes = inputStream.use { it.readBytes() }
        val mimeType = context.contentResolver.getType(uri) ?: "image/*"
        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        return MultipartBody.Part.createFormData("photo", fileName, requestBody)
    }
}