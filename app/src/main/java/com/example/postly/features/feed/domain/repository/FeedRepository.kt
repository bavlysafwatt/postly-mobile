package com.example.postly.features.feed.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.model.Post
import com.example.postly.core.network.AppError
import com.example.postly.features.feed.domain.model.Comment
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun getFeedPager(): Flow<PagingData<Post>>
    fun getCommentsPager(postId: String): Flow<PagingData<Comment>>
    suspend fun getPost(postId: String): Result<Post, AppError>
    suspend fun createPost(content: String, photoUris: List<Uri>): Result<Post, AppError>
    suspend fun updatePost(
        postId: String,
        content: String,
        photoUris: List<Uri>?
    ): Result<Post, AppError>

    suspend fun deletePost(postId: String): Result<Unit, AppError>
    suspend fun likePost(postId: String): Result<Unit, AppError>
    suspend fun unlikePost(postId: String): Result<Unit, AppError>
    suspend fun bookmarkPost(postId: String): Result<Unit, AppError>
    suspend fun unbookmarkPost(postId: String): Result<Unit, AppError>
    suspend fun addComment(postId: String, content: String): Result<Comment, AppError>
}