package com.example.db_api_learning.dto.response

import com.example.db_api_learning.model.SaleDetail
import java.io.Serializable
import java.time.LocalDateTime

data class SaleResponse( //for Get all in detail Product
    val id: Long?,
    var status: Short? = 1,
    var saleDetail: List<SaleDetailRes>,
    var createdAt: LocalDateTime? = null,
): Serializable

////For create Order Response
data class SaleCreateResForm(
    val status: Status?,
    val order: OrderDetailCreateRes,
)
data class OrderDetailCreateRes(
    val id: Long?, //sale ID
    var status: Short? = 1,
    var orderDetail: List<SaleDetailCreateRes>,
//    var orderDetail: List<SaleDetail>,
    var createdAt: LocalDateTime? = null,
)
