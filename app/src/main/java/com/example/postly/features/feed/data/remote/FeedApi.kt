package com.example.postly.features.feed.data.remote


import com.example.postly.core.data.dto.BaseResponse
import com.example.postly.features.feed.data.dto.BookmarkEnvelope
import com.example.postly.features.feed.data.dto.BookmarkRequest
import com.example.postly.features.feed.data.dto.CommentEnvelope
import com.example.postly.features.feed.data.dto.CommentsEnvelope
import com.example.postly.features.feed.data.dto.CreateCommentRequest
import com.example.postly.features.feed.data.dto.LikeEnvelope
import com.example.postly.features.feed.data.dto.PostEnvelope
import com.example.postly.features.feed.data.dto.PostsEnvelope
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedApi {

    @GET("api/v1/posts")
    suspend fun getFeed(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<BaseResponse<PostsEnvelope>>

    @GET("api/v1/posts/{id}")
    suspend fun getPost(@Path("id") postId: String): Response<BaseResponse<PostEnvelope>>

    @Multipart
    @POST("api/v1/posts")
    suspend fun createPost(
        @Part("content") content: RequestBody,
        @Part photos: List<MultipartBody.Part>
    ): Response<BaseResponse<PostEnvelope>>

    @Multipart
    @PATCH("api/v1/posts/{id}")
    suspend fun updatePost(
        @Path("id") postId: String,
        @Part("content") content: RequestBody,
        @Part photos: List<MultipartBody.Part>?
    ): Response<BaseResponse<PostEnvelope>>

    @DELETE("api/v1/posts/{id}")
    suspend fun deletePost(@Path("id") postId: String): Response<BaseResponse<Unit?>>

    @POST("api/v1/posts/{id}/like")
    suspend fun likePost(@Path("id") postId: String): Response<BaseResponse<LikeEnvelope>>

    @DELETE("api/v1/posts/{id}/unlike")
    suspend fun unlikePost(@Path("id") postId: String): Response<BaseResponse<Unit?>>

    @POST("api/v1/posts/{id}/comment")
    suspend fun addComment(
        @Path("id") postId: String,
        @Body request: CreateCommentRequest
    ): Response<BaseResponse<CommentEnvelope>>

    @GET("api/v1/posts/{id}/comments")
    suspend fun getComments(
        @Path("id") postId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<BaseResponse<CommentsEnvelope>>

    @POST("api/v1/bookmarks")
    suspend fun bookmarkPost(@Body request: BookmarkRequest): Response<BaseResponse<BookmarkEnvelope>>

    @DELETE("api/v1/bookmarks")
    suspend fun unbookmarkPost(@Body request: BookmarkRequest): Response<BaseResponse<Unit?>>
}