package com.example.db_api_learning.controller.auth

import com.example.db_api_learning.dto.response.Status
import com.example.db_api_learning.dto.response.UserRes
import jakarta.servlet.http.HttpServletResponse
import java.io.Serializable

data class AuthenticationResponse(
    val status: Status,
    val user: UserRes,
    val token: TokenResponse
): Serializable

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)


