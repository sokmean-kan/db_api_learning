package com.example.db_api_learning.dto.response

data class ErrorResponse(
    val status: Status,
    val data: List<FieldError> = emptyList(),
)

data class FieldError(
    val field: String,
    val message: String
)


