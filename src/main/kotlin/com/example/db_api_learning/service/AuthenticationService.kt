package com.example.db_api_learning.service

import com.example.db_api_learning.config.JwtProperties
import com.example.db_api_learning.controller.auth.AuthenticationRequest
import com.example.db_api_learning.controller.auth.AuthenticationResponse
import com.example.db_api_learning.controller.auth.TokenResponse
import com.example.db_api_learning.dto.response.Status
import com.example.db_api_learning.mapper.toUserRes
import com.example.db_api_learning.repository.RefreshTokenRepository
import com.example.db_api_learning.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailService: CustomUserDetailService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository,
){
    fun authentication(authRequest: AuthenticationRequest): AuthenticationResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password
            )
        )
        val userOriginal = userRepository.findByEmail(authRequest.email)
        val user =userDetailService.loadUserByUsername(authRequest.email)
        val accessToken = generateAccessToken(user)
        val refreshToken = generateRefreshToken(user)
        refreshTokenRepository.save(refreshToken,user)
        return AuthenticationResponse(
            Status(
                errorCode = 0,
                errorMessage = "Login successfully",
            ),
            user = userOriginal!!.toUserRes(),
            token = TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
            )
        )
    }
    fun refreshAccessToken(token: String): String? {
        val extractedEmail = tokenService.extractEmail(token)
        return extractedEmail?.let { email->
            val currentUserDetails = userDetailService.loadUserByUsername(email)
            val refreshTokenUserDetails = refreshTokenRepository.findUserDetailsByToken(token)

            if (!tokenService.isExpired(token) && currentUserDetails.username == refreshTokenUserDetails?.username)
                generateAccessToken(currentUserDetails)
            else null
        }
    }
    private fun generateAccessToken(user: UserDetails) = tokenService.generate(
        userDetails = user,
        expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
    )

    private fun generateRefreshToken(user: UserDetails) = tokenService.generate(
        userDetails = user,
        expirationDate = Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)
    )

    fun logout(authRequest: AuthenticationRequest) {

    }

}
