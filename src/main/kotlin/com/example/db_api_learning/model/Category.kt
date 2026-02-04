package com.example.db_api_learning.model

import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "categories_seq", allocationSize = 20, initialValue = 5)
    var id: Int = 0,
    var name: String? = null
)