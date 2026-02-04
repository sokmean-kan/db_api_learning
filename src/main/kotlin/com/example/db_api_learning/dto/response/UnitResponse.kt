package com.example.db_api_learning.dto.response

import com.example.db_api_learning.model.Unit

data class UnitResponse(
    val id: Int?,
    val name: String?,
)

data class UnitResForm(
    val status: Status,
    val unit: Unit,
)