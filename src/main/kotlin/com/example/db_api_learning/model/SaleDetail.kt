package com.example.db_api_learning.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

@Entity
@Table(name = "sale_details")
class SaleDetail(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sale_detail_seq", sequenceName = "sale_details_seq", allocationSize = 50, initialValue = 5)
    val id: Long = 0,
    var saleId: Long = 0,
    var productId: Long = 0,
    var qty: Int = 0,
    var unitPrice: BigDecimal = 0.toBigDecimal(),
    var totalPrice: BigDecimal = 0.toBigDecimal(),
)