package com.example.db_api_learning.model

import jakarta.persistence.*
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "login_attempts")
data class LoginAttempt(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "login_attempt_seq")
    @SequenceGenerator(name = "login_attempt_seq", sequenceName = "login_attempts_seq", allocationSize = 50)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    var email: String,

    @Column(nullable = false)
    var attempts: Int = 0,

    @Column(name = "locked_until")
    val lockedUntil: LocalDateTime? = null,

    @Column(name = "last_attempt", nullable = false)
    var lastAttempt: LocalDateTime = LocalDateTime.now()
)