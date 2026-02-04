package com.example.db_api_learning.mapper

import com.example.db_api_learning.dto.request.CreateProductRequest
import com.example.db_api_learning.dto.response.ProductResForm
import com.example.db_api_learning.dto.response.ProductResponse
import com.example.db_api_learning.dto.response.Status
import com.example.db_api_learning.model.Category
import com.example.db_api_learning.model.Product
import com.example.db_api_learning.model.Unit


fun Product.toProductResponse(unit: Unit?, category: Category?): ProductResponse {
    return ProductResponse(
        id = this.id,
        name = this.name,
        price = this.price,
        stock = this.stock,
        unit = unit,
        category = category,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Product.toProductResForm(): ProductResForm {
    return ProductResForm(
        status = Status(
            errorCode = 0,
            errorMessage = "Success",
        ),
        product = ProductResponse(
            id = id,
            name = name,
            price = price,
            stock = stock,
            unit = Unit(id = unitId),
            category = Category(id = categoryId),
            createdAt = createdAt,
            updatedAt = updatedAt,
        ),
    )
}

fun CreateProductRequest.toProduct(): Product{
    return Product(
        name = name,
        price = price?:0.0.toBigDecimal(),
        stock = stock?:0,
        unitId = unitId?:0,
        categoryId =  categoryId?:0,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}