package com.example.db_api_learning.dto.request

data class GetPaginationRequest(
    val currentPage: Int,
    val pageSize: Int,
    val sortOrder: List<String>? = listOf("id,asc","name,acs"),
    val search: String? = "",
    val categoryId: Long?,
    val filter: FilterRequest?
)
data class FilterRequest(
    val unitId: Int?,
    val categoryId: Int?,
)

