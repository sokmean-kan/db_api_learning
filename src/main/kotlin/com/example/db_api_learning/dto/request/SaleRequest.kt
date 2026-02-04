package com.example.db_api_learning.dto.request

import java.math.BigDecimal

data class CreateSaleRequest(
    var status: Short?,
    val items: List<SaleItemRequest>
)
data class SaleItemRequest(
    val productId: Long,
    val qty: Int,
    val unitPrice: BigDecimal?
)