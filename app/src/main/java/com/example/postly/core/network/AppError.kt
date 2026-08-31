package com.example.postly.core.network

sealed interface AppError {
    data object NoInternet : AppError
    data object Timeout : AppError
    data object Unauthorized : AppError
    data object Forbidden : AppError // 403
    data object NotFound : AppError // 404
    data object RateLimited : AppError // 429
    data object Server : AppError // 5xx
    data class Api(val message: String) : AppError // server message worth showing verbatim
    data class Unknown(val message: String? = null) : AppError
}

fun AppError.toUserMessage(): String = when (this) {
    AppError.NoInternet -> "No internet connection, check your connection and try again"
    AppError.Timeout -> "Request timed out, please try again later"
    AppError.Unauthorized -> "Your session has expired. Please log in again."
    AppError.Forbidden -> "You don't have permission to do that, please contact support if you think this is a mistake"
    AppError.NotFound -> "Not found, please check the URL or contact support if you think this is a mistake"
    AppError.RateLimited -> "Too many requests — please try again in a bit"
    AppError.Server -> "Something went wrong on the server, please try again later"
    is AppError.Api -> message
    is AppError.Unknown -> message ?: "Something went wrong"
}