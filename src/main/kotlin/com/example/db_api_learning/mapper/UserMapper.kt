package com.example.db_api_learning.mapper

import com.example.db_api_learning.dto.response.UserRes
import com.example.db_api_learning.model.User

fun User.toUserRes(): UserRes {
    return UserRes(
        id = this.id,
        email = this.email,
        username = this.name,
        role = this.role,

    )
}