package com.example.db_api_learning.controller.auth

data class AuthenticationRequest(
    val email: String,
    val password: String
)
