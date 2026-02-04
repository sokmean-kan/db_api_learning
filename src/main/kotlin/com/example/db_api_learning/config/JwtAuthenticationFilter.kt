package com.example.db_api_learning.config

import com.example.db_api_learning.service.CustomUserDetailService
import com.example.db_api_learning.service.TokenBlacklistService
import com.example.db_api_learning.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

//@Component
//class JwtAuthenticationFilter(
//    private val userDetailService: CustomUserDetailService,
//    private val tokenService: TokenService
//) : OncePerRequestFilter() {
//    override fun doFilterInternal(
//        request: HttpServletRequest,
//        response: HttpServletResponse,
//        filterChain: FilterChain
//    ) {
//        val authHeader: String? = request.getHeader("Authorization")
//        if (authHeader.doseNotContainBearerToken()) {
//            filterChain.doFilter(request, response)
//            return
//        }
//
//        val jwtToken = authHeader!!.extractTokenValue()
//        val email = tokenService.extractEmail(jwtToken)
//
//        if (email != null && SecurityContextHolder.getContext().authentication == null) {
//            val foundUser = userDetailService.loadUserByUsername(email)
//
//            if (tokenService.isValid(jwtToken, foundUser)) {
//                updateContext(foundUser, request)
//            }
//            filterChain.doFilter(request, response)
//        }
//    }
//
//    private fun updateContext(
//        foundUser: UserDetails,
//        request: HttpServletRequest
//    ) {
//        val authToken = UsernamePasswordAuthenticationToken(foundUser, null, foundUser.authorities)
//        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
//        SecurityContextHolder.getContext().authentication = authToken
//    }
//
//    private fun String?.doseNotContainBearerToken(): Boolean =
//        this == null || !this.startsWith("Bearer ")
//
//    private fun String.extractTokenValue(): String =
//        this.substringAfter("Bearer ")
//
//}
//////////////////////

@Component
class JwtAuthenticationFilter(
    private val userDetailService: CustomUserDetailService,
    private val tokenService: TokenService,
    private val tokenBlacklistService: TokenBlacklistService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader.doseNotContainBearerToken()) {
            filterChain.doFilter(request, response)
            return
        }
        ////////////check if token was logout(in blacklist)
        val jwt = authHeader.substring("Bearer ".length)
        if (tokenBlacklistService.isBlacklisted(jwt)) {
            println("DEBUG: Rejected blacklisted token")
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return sendErrorResponse(response, "Token is invalidated. Please login again.")
        }
        /////////////
        val jwtToken = authHeader.extractTokenValue() //or substringAfter("Bearer ").trim()

        try {
            val email = tokenService.extractEmail(jwtToken)

            if (email != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailService.loadUserByUsername(email)

                if (tokenService.isValid(jwtToken, userDetails)) {
                    updateContext(userDetails, request)
                }
                // else → invalid → will be caught below as exception or just no auth set
            }

            filterChain.doFilter(request, response)

        } catch (e: Exception) {
            sendErrorResponse(response, "Invalid or expired token")
        }
    }

    private fun updateContext(userDetails: UserDetails, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.authorities
        )
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
    }
    private fun String?.doseNotContainBearerToken(): Boolean =
        this == null || !this.startsWith("Bearer ")

    private fun String.extractTokenValue(): String =
        this.substringAfter("Bearer ")

}

private fun sendErrorResponse(response: HttpServletResponse, message: String) {
    try {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json;charset=UTF-8"

        val json = """
                {
                    "status": 401,
                    "error": "Unauthorized",
                    "message": "$message"
                }
            """.trimIndent()

        response.writer.write(json)
        response.writer.flush()
    } catch (ignored: Exception) {
        // fallback - make sure we don't crash the filter
        response.status = HttpServletResponse.SC_UNAUTHORIZED
    }
}



