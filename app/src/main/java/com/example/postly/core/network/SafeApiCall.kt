package com.example.postly.core.network

import com.example.postly.core.data.dto.BaseResponse
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import com.example.postly.core.domain.Result

private val errorGson = Gson()

suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<BaseResponse<T>>
): Result<T, AppError> = try {
    val response = apiCall()
    if (response.isSuccessful) {
        val body = response.body()
        if (body?.data != null) {
            Result.Success(body.data)
        } else {
            Result.Failure(AppError.Api(body?.message ?: "No data returned"))
        }
    } else {
        Result.Failure(parseApiError(response))
    }
} catch (e: SocketTimeoutException) {
    Result.Failure(AppError.Timeout)
} catch (e: IOException) {
    Result.Failure(AppError.NoInternet)
} catch (e: Exception) {
    Result.Failure(AppError.Unknown(e.message))
}

suspend fun safeApiCallUnit(
    apiCall: suspend () -> Response<BaseResponse<Unit?>>
): Result<Unit, AppError> = try {
    val response = apiCall()
    if (response.isSuccessful) {
        Result.Success(Unit)
    } else {
        Result.Failure(parseApiError(response))
    }
} catch (e: SocketTimeoutException) {
    Result.Failure(AppError.Timeout)
} catch (e: IOException) {
    Result.Failure(AppError.NoInternet)
} catch (e: Exception) {
    Result.Failure(AppError.Unknown(e.message))
}

private fun <T> parseApiError(response: Response<BaseResponse<T>>): AppError {
    if (response.code() == 429) return AppError.RateLimited

    val message = runCatching {
        response.errorBody()?.charStream()?.use {
            errorGson.fromJson(it, BaseResponse::class.java)?.message
        }
    }.getOrNull() ?: "Something went wrong"

    return when (response.code()) {
        401 -> AppError.Unauthorized
        403 -> AppError.Forbidden
        404 -> AppError.Api(message)
        in 500..599 -> AppError.Server
        else -> AppError.Api(message)
    }
}