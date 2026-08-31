package com.example.postly.core.data.dto

data class BaseResponse<T>(
    val status: String,
    val message: String? = null,
    val data: T? = null
)