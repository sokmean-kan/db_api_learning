package com.example.db_api_learning.repository

import com.example.db_api_learning.model.LoginAttempt
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LoginAttemptRepository : JpaRepository<LoginAttempt, Long> {
    fun findByEmail(email: String): LoginAttempt?
    fun deleteByEmail(email: String)
}