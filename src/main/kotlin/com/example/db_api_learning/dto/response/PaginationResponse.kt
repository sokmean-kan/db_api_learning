package com.example.db_api_learning.dto.response

data class ApiResponse<T>(
    val status: Status,
    val data: DataResponse<T>?
)
data class Status(
    val errorCode: Int?= null,
    val errorMessage: String? = null
)
data class DataResponse<T>(
    val content: List<T>,
    val pagination: PaginationResponse
)

data class PaginationResponse(
    val currentPage: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPage: Int
)