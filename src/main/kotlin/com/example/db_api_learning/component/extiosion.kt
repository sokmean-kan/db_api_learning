package com.example.db_api_learning.component

import com.example.db_api_learning.dto.request.CreateProductRequest
import com.example.db_api_learning.dto.request.UnitRequestCreate
import com.example.db_api_learning.model.Product
import com.example.db_api_learning.model.Unit

fun Product.updateToProduct(request: CreateProductRequest): Product {
    return this.apply {
        request.name.let { this.name = it }
        request.price?.let { this.price = it }
        request.stock?.let { this.stock = it }
        request.categoryId?.let { this.categoryId = it }
        request.unitId?.let { this.unitId = it }
//        this.updatedAt = LocalDateTime.now()
    }
}

fun Unit.updateToUnit(request: UnitRequestCreate): Unit {
    return this.apply {
        this.name = request.name
    }
}
