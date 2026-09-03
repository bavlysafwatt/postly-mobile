package com.example.postly.core.network

class PagingAppException(val appError: AppError) : Exception(appError.toUserMessage())