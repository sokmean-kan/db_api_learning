package com.example.db_api_learning.service

import com.example.db_api_learning.config.JwtProperties
import com.example.db_api_learning.controller.auth.AuthenticationRequest
import com.example.db_api_learning.controller.auth.AuthenticationResponse
import com.example.db_api_learning.controller.auth.TokenResponse
import com.example.db_api_learning.dto.response.Status
import com.example.db_api_learning.exception.ResourceNotFoundException
import com.example.db_api_learning.mapper.toUserRes
import com.example.db_api_learning.model.User
import com.example.db_api_learning.repository.RefreshTokenRepository
import com.example.db_api_learning.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

//@Service
//class AuthenticationService(
//    private val authManager: AuthenticationManager,
//    private val userDetailService: CustomUserDetailService,
//    private val tokenService: TokenService,
//    private val jwtProperties: JwtProperties,
//    private val refreshTokenRepository: RefreshTokenRepository,
//    private val userRepository: UserRepository,
//){
//    fun authentication(authRequest: AuthenticationRequest): AuthenticationResponse {
//
//        try {
//            authManager.authenticate(
//                UsernamePasswordAuthenticationToken(
//                    authRequest.email,
//                    authRequest.password
//                )
//            )
//            val userOriginal = userRepository.findByEmail(authRequest.email)
//            val user = userDetailService.loadUserByUsername(authRequest.email)
//            val accessToken = generateAccessToken(user)
//            val refreshToken = generateRefreshToken(user)
//            refreshTokenRepository.save(refreshToken, user)
//            return AuthenticationResponse(
//                Status(
//                    errorCode = 0,
//                    errorMessage = "Login successfully",
//                ),
//                user = userOriginal!!.toUserRes(),
//                token = TokenResponse(
//                    accessToken = accessToken,
//                    refreshToken = refreshToken
//                )
//            )
//        }catch (ex: BadCredentialsException) {
//            throw ResourceNotFoundException("Invalid username or password")
//        }
//    }
//    fun refreshAccessToken(token: String): String? {
//        val extractedEmail = tokenService.extractEmail(token)
//        return extractedEmail?.let { email->
//            val currentUserDetails = userDetailService.loadUserByUsername(email)
//            val refreshTokenUserDetails = refreshTokenRepository.findUserDetailsByToken(token)
//
//            if (!tokenService.isExpired(token) && currentUserDetails.username == refreshTokenUserDetails?.username)
//                generateAccessToken(currentUserDetails)
//            else null
//        }
//    }
//    private fun generateAccessToken(user: UserDetails) = tokenService.generate(
//        userDetails = user,
//        expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
//    )
//
//    private fun generateRefreshToken(user: UserDetails) = tokenService.generate(
//        userDetails = user,
//        expirationDate = Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)
//    )
//
//}


//========================================add login attempts===========================
@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailService: CustomUserDetailService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository,
    private val loginAttemptService: LoginAttemptService  // ADD THIS
){
    fun authentication(authRequest: AuthenticationRequest): AuthenticationResponse {
        // Clear expired lock if any
        loginAttemptService.clearExpiredLock(authRequest.email)

        // Check if account is blocked
        if (loginAttemptService.isBlocked(authRequest.email)) {
            val blockedUntil = loginAttemptService.getBlockedUntil(authRequest.email)
            throw ResourceNotFoundException("Account is temporarily locked until $blockedUntil due to too many failed login attempts")
        }


        try {
            // Try to authenticate
            authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    authRequest.email,
                    authRequest.password
                )
            )
        } catch (e: Exception) {
            // Authentication failed - record the attempt
            loginAttemptService.loginFailed(authRequest.email)

            val remaining = loginAttemptService.getRemainingAttempts(authRequest.email)

            if (remaining > 0) {
                throw ResourceNotFoundException("Invalid credentials. You have $remaining attempt(s) remaining.")
            } else {
                throw ResourceNotFoundException("Account locked due to too many failed attempts. Please try again in 15 minutes.")
            }
        }

        // Authentication successful - clear any previous failed attempts
        loginAttemptService.loginSucceeded(authRequest.email)

        // Continue with token generation
        val userOriginal = userRepository.findByEmail(authRequest.email)
        val user = userDetailService.loadUserByUsername(authRequest.email)
        val accessToken = generateAccessToken(user)
        val refreshToken = generateRefreshToken(user)
        refreshTokenRepository.save(refreshToken, user)

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

    // ... rest of your existing code stays the same
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

}
