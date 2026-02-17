package com.example.db_api_learning.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
@Table(name = "products")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "product_seq", sequenceName = "products_seq", allocationSize = 50, initialValue = 6)
    var id: Long = 0,
    var name: String = "",
    var price: BigDecimal = BigDecimal.ZERO,
    var stock: Int = 0,
    var unitId: Int = 0 ,
    var categoryId: Int = 0,
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
)