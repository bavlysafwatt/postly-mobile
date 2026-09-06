package com.example.postly.features.profile.data.remote

import com.example.postly.core.data.dto.BaseResponse
import com.example.postly.features.profile.data.dto.BookmarksEnvelope
import com.example.postly.features.profile.data.dto.FollowEnvelope
import com.example.postly.features.profile.data.dto.FollowersEnvelope
import com.example.postly.features.profile.data.dto.FollowingEnvelope
import com.example.postly.features.profile.data.dto.ProfilePostsEnvelope
import com.example.postly.features.profile.data.dto.UserProfileEnvelope
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileApi {

    @GET("api/v1/users/me")
    suspend fun getMe(): Response<BaseResponse<UserProfileEnvelope>>

    @GET("api/v1/users/{id}")
    suspend fun getUser(@Path("id") userId: String): Response<BaseResponse<UserProfileEnvelope>>

    @Multipart
    @PATCH("api/v1/users/update-me")
    suspend fun updateMe(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part photo: MultipartBody.Part?
    ): Response<BaseResponse<UserProfileEnvelope>>

    @POST("api/v1/users/{id}/follow")
    suspend fun followUser(@Path("id") userId: String): Response<BaseResponse<FollowEnvelope>>

    @DELETE("api/v1/users/{id}/unfollow")
    suspend fun unfollowUser(@Path("id") userId: String): Response<BaseResponse<Unit?>>

    @GET("api/v1/users/me/followers")
    suspend fun getMyFollowers(): Response<BaseResponse<FollowersEnvelope>>

    @GET("api/v1/users/me/following")
    suspend fun getMyFollowing(): Response<BaseResponse<FollowingEnvelope>>

    @GET("api/v1/users/{id}/followers")
    suspend fun getFollowersByUser(@Path("id") userId: String): Response<BaseResponse<FollowersEnvelope>>

    @GET("api/v1/users/{id}/following")
    suspend fun getFollowingByUser(@Path("id") userId: String): Response<BaseResponse<FollowingEnvelope>>

    @GET("api/v1/users/me/posts")
    suspend fun getMyPosts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<BaseResponse<ProfilePostsEnvelope>>

    @GET("api/v1/users/{id}/posts")
    suspend fun getUserPosts(
        @Path("id") userId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<BaseResponse<ProfilePostsEnvelope>>

    @GET("api/v1/bookmarks")
    suspend fun getMyBookmarks(): Response<BaseResponse<BookmarksEnvelope>>
}