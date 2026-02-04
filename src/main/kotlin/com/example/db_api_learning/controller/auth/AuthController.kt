package com.example.db_api_learning.controller.auth

import com.example.db_api_learning.exception.ResourceNotFoundException
import com.example.db_api_learning.service.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/auth"])
class AuthController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/login")
    fun authentication(@RequestBody authRequest: AuthenticationRequest): AuthenticationResponse =
        authenticationService.authentication(authRequest)

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @RequestBody request: RefreshTokenRequest
    ): RefreshTokenResponse =
        authenticationService.refreshAccessToken(request.token)
            ?.mapToTokenResponse()
            ?: throw ResourceNotFoundException("Invalid refresh token!")

    private fun String.mapToTokenResponse(): RefreshTokenResponse=
        RefreshTokenResponse(
            token = this
        )

}