package com.example.db_api_learning.mapper

import com.example.db_api_learning.dto.response.OrderDetailCreateRes
import com.example.db_api_learning.dto.response.SaleDetailCreateRes
import com.example.db_api_learning.dto.response.SaleResponse
import com.example.db_api_learning.model.Sale
import com.example.db_api_learning.model.SaleDetail

fun Sale.toResponse(saleDetails: List<SaleDetail>) = SaleResponse(
    id = this.id,
    status = this.status,
    saleDetail = saleDetails.map { it.toSaleResponse() },
    createdAt = this.createdAt
)
//fun Sale.toCreateOrderResponse(saleDetails: List<SaleDetail>) = OrderDetailCreateRes(
fun Sale.toCreateOrderResponse(saleDetails: List<SaleDetailCreateRes>) = OrderDetailCreateRes(
        id = this.id,
        status = this.status,
        orderDetail = saleDetails.map { it},
        createdAt = this.createdAt
)


