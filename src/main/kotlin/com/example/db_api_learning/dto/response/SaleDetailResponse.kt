package com.example.db_api_learning.dto.response

import java.math.BigDecimal

data class SaleDetailRes( //for Get all in detail Product
    val id: Long, //saleDetail ID
    val saleId: Long,
    var product: ProductResponse,
    var qty: Int,
    var unitPrice: BigDecimal,
    var totalPrice:BigDecimal,
)
data class SaleDetailCreateRes( //for Create Order Response
    val id: Long, //saleDetail ID
    val saleId: Long,
    var productId: Long,
    var productName: String,
    var qty: Int,
    var unitPrice: BigDecimal,
    var totalPrice:BigDecimal,
)