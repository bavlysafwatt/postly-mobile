package com.example.postly.core.data.dto

data class PaginationMeta(
    val page: Int,
    val totalPages: Int,
    val totalResults: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean
)