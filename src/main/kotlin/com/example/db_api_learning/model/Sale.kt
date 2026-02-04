package com.example.db_api_learning.model

import jakarta.persistence.*
import java.time.LocalDateTime
import org.hibernate.annotations.CreationTimestamp

@Entity
@Table(name = "sales")
class Sale(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sale_seq", sequenceName = "sales_seq", allocationSize = 50, initialValue = 3)
    var id: Long = 0,
    var status: Short = 0,
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,

)