package com.example.postly.features.search.data.remote

import com.example.postly.core.data.dto.BaseResponse
import com.example.postly.features.search.data.dto.SearchUsersEnvelope
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("api/v1/users/search")
    suspend fun searchUsers(
        @Query("query") query: String
    ): Response<BaseResponse<SearchUsersEnvelope>>
}