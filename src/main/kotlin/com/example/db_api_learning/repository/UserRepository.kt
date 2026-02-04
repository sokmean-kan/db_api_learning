package com.example.db_api_learning.repository

import com.example.db_api_learning.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun findByPassword(password: String): User?

}