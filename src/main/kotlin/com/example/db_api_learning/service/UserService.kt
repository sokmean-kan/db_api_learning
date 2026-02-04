package com.example.db_api_learning.service

import com.example.db_api_learning.component.sortByMulti
import com.example.db_api_learning.dto.response.ApiResponse
import com.example.db_api_learning.dto.response.DataResponse
import com.example.db_api_learning.dto.response.PaginationResponse
import com.example.db_api_learning.dto.response.Status
import com.example.db_api_learning.dto.response.UserRes
import com.example.db_api_learning.exception.ResourceNotFoundException
import com.example.db_api_learning.mapper.toUserRes
import com.example.db_api_learning.model.User
import com.example.db_api_learning.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun createUser(user: User): User {
        val found = userRepository.findByEmail(user.email)
        if (found != null) {
            throw ResourceNotFoundException("Email already exist.")
        }else null
        val rawPassword = user.password
        val encodedPassword = passwordEncoder.encode(rawPassword)
        user.password = encodedPassword!!
        return userRepository.save(user)
    }

    fun getAllUsers(
        currentPage: Int,
        pageSize: Int,
        sortBy: List<String>?
    ): ApiResponse<UserRes> {
        val order = sortBy?.sortByMulti()?: listOf(Sort.Order.asc("id"))
        val sort = Sort.by(order)
        val pageable = PageRequest.of(currentPage - 1, pageSize, sort)
        val users = userRepository.findAll(pageable)
        val userRes = users.content.map { user -> user.toUserRes() }
        return ApiResponse(
            Status(
                errorCode = 0,
                errorMessage = "success hz",
            ),
            DataResponse(
                content = userRes,
                pagination = PaginationResponse(
                    currentPage = currentPage,
                    pageSize = pageSize,
                    totalElements = users.totalElements,
                    totalPage = users.totalPages,
                )
            )
        )
    }
}