package com.example.db_api_learning.config

import com.example.db_api_learning.service.TokenBlacklistService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration(
    private val authenticationProvider: AuthenticationProvider,
    private val tokenBlacklistService: TokenBlacklistService

) {
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        response: HttpServletResponse,
    ): DefaultSecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    // 1. Public endpoints (Login, Refresh, Error)
                    .requestMatchers("/api/auth/login", "/api/auth/refresh", "/error").permitAll()

                    .requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                    // Units access for both  ADMIN
                    .requestMatchers("/api/units/**").hasRole("ADMIN")

                    // Admin-only management of users (GET, DELETE, etc.)
                    .requestMatchers("/api/user/**").hasRole("ADMIN")

                    // 5. Everything else requires authentication
                    .anyRequest().fullyAuthenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .logout { logout ->
                logout.logoutUrl("/api/auth/logout")
                    .addLogoutHandler { request, _, _ ->
                        val authHeader = request.getHeader("Authorization")
                        println("--- LOGOUT TRIGGERED ---") // view in console to see logout success or not
                        if (authHeader != null && authHeader.startsWith("Bearer ")) {
                            val jwt = authHeader.substring(7)
                            println("DEBUG: Blacklisting token: ${jwt.take(10)}...")
                            tokenBlacklistService.blacklistToken(jwt)
                        } else {
                            println("DEBUG: Logout failed - No Bearer token found in header")
                        }
                    }
                    .logoutSuccessHandler { _, response, _ ->
                        response.status = 200
                        println("response status=> ${response.status}")
                        sendErrorResponses(response,"Logout successfully")
                    }
            }
        return http.build()
    }

    private fun sendErrorResponses(response: HttpServletResponse, message: String) {
        response.status = 200
        response.contentType = "application/json;charset=UTF-8"
        val json = """
                {"status": ${response.status},"message": "$message"}
            """.trimIndent()

        response.writer.write(json)
        response.writer.flush()
    }

}