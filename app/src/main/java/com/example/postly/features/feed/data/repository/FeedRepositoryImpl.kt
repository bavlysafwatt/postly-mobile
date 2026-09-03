package com.example.postly.features.feed.data.repository

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.postly.core.common.Constants
import com.example.postly.core.data.dto.toDomain
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.map
import com.example.postly.core.domain.model.Post
import com.example.postly.core.network.AppError
import com.example.postly.core.network.safeApiCall
import com.example.postly.core.network.safeApiCallUnit
import com.example.postly.features.feed.data.dto.BookmarkRequest
import com.example.postly.features.feed.data.dto.CreateCommentRequest
import com.example.postly.features.feed.data.dto.toDomain
import com.example.postly.features.feed.data.paging.CommentsPagingSource
import com.example.postly.features.feed.data.paging.FeedPagingSource
import com.example.postly.features.feed.data.remote.FeedApi
import com.example.postly.features.feed.domain.model.Comment
import com.example.postly.features.feed.domain.repository.FeedRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val api: FeedApi,
    @ApplicationContext private val context: Context
) : FeedRepository {

    override fun getFeedPager(): Flow<PagingData<Post>> =
        Pager(PagingConfig(pageSize = Constants.DEFAULT_PAGE_SIZE)) { FeedPagingSource(api) }.flow

    override fun getCommentsPager(postId: String): Flow<PagingData<Comment>> =
        Pager(PagingConfig(pageSize = Constants.DEFAULT_PAGE_SIZE)) {
            CommentsPagingSource(
                api,
                postId
            )
        }.flow

    override suspend fun getPost(postId: String): Result<Post, AppError> =
        safeApiCall { api.getPost(postId) }.map { it.post.toDomain() }

    override suspend fun createPost(content: String, photoUris: List<Uri>): Result<Post, AppError> {
        val photoParts = photoUris.mapNotNull { uriToMultipart(it) }
        val result = safeApiCall {
            api.createPost(content = content.toPlainRequestBody(), photos = photoParts)
        }
        return result.map { it.post.toDomain() }
    }

    override suspend fun updatePost(
        postId: String,
        content: String,
        photoUris: List<Uri>?
    ): Result<Post, AppError> {
        val photoParts = photoUris?.mapNotNull { uriToMultipart(it) }
        val result = safeApiCall {
            api.updatePost(
                postId = postId,
                content = content.toPlainRequestBody(),
                photos = photoParts
            )
        }
        return result.map { it.post.toDomain() }
    }

    override suspend fun deletePost(postId: String): Result<Unit, AppError> =
        safeApiCallUnit { api.deletePost(postId) }

    override suspend fun likePost(postId: String): Result<Unit, AppError> =
        safeApiCall { api.likePost(postId) }.map { }

    override suspend fun unlikePost(postId: String): Result<Unit, AppError> =
        safeApiCallUnit { api.unlikePost(postId) }

    override suspend fun bookmarkPost(postId: String): Result<Unit, AppError> =
        safeApiCall { api.bookmarkPost(BookmarkRequest(postId)) }.map { }

    override suspend fun unbookmarkPost(postId: String): Result<Unit, AppError> =
        safeApiCallUnit { api.unbookmarkPost(BookmarkRequest(postId)) }

    override suspend fun addComment(postId: String, content: String): Result<Comment, AppError> =
        safeApiCall {
            api.addComment(
                postId,
                CreateCommentRequest(content)
            )
        }.map { it.comment.toDomain() }

    private fun String.toPlainRequestBody() = toRequestBody("text/plain".toMediaTypeOrNull())

    private fun uriToMultipart(uri: Uri): MultipartBody.Part? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val bytes = inputStream.use { it.readBytes() }
        val mimeType = context.contentResolver.getType(uri) ?: "image/*"
        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        return MultipartBody.Part.createFormData("photos", fileName, requestBody)
    }
}