package com.example.db_api_learning.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq",sequenceName = "users_seq", allocationSize = 50, initialValue = 3)
    val id: Long = 0,
    var name: String = "",
    var email: String = "",
    var password: String = "",
    @Enumerated(EnumType.STRING)
    var role: Role
)

enum class Role {
    USER, ADMIN
}