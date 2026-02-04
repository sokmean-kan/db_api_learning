package com.example.db_api_learning.controller

import com.example.db_api_learning.dto.request.GetPaginationRequest
import com.example.db_api_learning.dto.response.ApiResponse
import com.example.db_api_learning.dto.response.UserRes
import com.example.db_api_learning.model.User
import com.example.db_api_learning.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/user"])
class UserController(private val userService: UserService) {
    @PostMapping
    fun createUSer(@RequestBody user: User): User {
        return userService.createUser(user)
    }

    @PostMapping("/list")
    fun listUsers(@RequestBody request: GetPaginationRequest): ApiResponse<UserRes> {
        return userService.getAllUsers(request.currentPage, request.pageSize, sortBy = request.sortOrder)
    }

}