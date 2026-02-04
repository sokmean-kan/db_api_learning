package com.example.db_api_learning.service

import com.example.db_api_learning.exception.ResourceNotFoundException
import com.example.db_api_learning.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

typealias ApplicationUser = com.example.db_api_learning.model.User

@Service
class CustomUserDetailService (
    private val userRepository: UserRepository
): UserDetailsService{
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByEmail(username)
            ?.mapToUserDetails()
            ?:throw ResourceNotFoundException("User not found")

    private fun ApplicationUser.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.email)
            .password(this.password)
            .roles(this.role.name)
            .build()
}