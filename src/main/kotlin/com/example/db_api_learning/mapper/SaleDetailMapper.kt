package com.example.db_api_learning.mapper

import com.example.db_api_learning.dto.response.ProductResponse
import com.example.db_api_learning.dto.response.SaleDetailCreateRes
import com.example.db_api_learning.dto.response.SaleDetailRes
import com.example.db_api_learning.model.Product
import com.example.db_api_learning.model.SaleDetail
import java.math.BigDecimal
//For get all detail
fun SaleDetail.toSaleResponse() = SaleDetailRes(
    id = this.id,
    saleId = this.saleId,
    product = ProductResponse(null, "", BigDecimal.ZERO, 0, null, null, null, null), // Will be populated by service
    qty = this.qty,
    unitPrice = this.unitPrice,
    totalPrice = this.totalPrice
)

//For create Order Response
fun SaleDetail.toSaleDetailCreateRes(product : Product) = SaleDetailCreateRes(
    id = this.id,
    saleId = this.saleId,
    productId = this.productId,
    productName = product.name,
    qty = this.qty,
    unitPrice = this.unitPrice,
    totalPrice = this.totalPrice
)