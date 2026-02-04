package com.example.db_api_learning.dto.response

import com.example.db_api_learning.model.Role

data class UserRes(
    val id: Long,
    val email: String,
    val username: String,
    val  role: Role,
)
