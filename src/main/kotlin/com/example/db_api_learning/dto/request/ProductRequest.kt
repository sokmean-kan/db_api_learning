package com.example.db_api_learning.dto.request

import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreateProductRequest(
    @field:NotBlank(message = "Product name cannot be blank")
    @field:Size(max = 255, message = "Name is too long")
    var name: String,

    @field:NotNull(message = "Price cannot be null")
    @field:DecimalMin(value = "0.0", inclusive = true , message = "Price must be at least 0") //default inclusive
//    @field:DecimalMin(value = "0.0", inclusive = false , message = "Price must be greater than 0")
    @field:Digits(integer = 10, fraction = 2, message = "Price must have max 10 digits and 2 decimal places")
    var price: BigDecimal?,   //use (?) for check request if empty string it will consider that is null

    @field:Min(value = 0, message = "Stock cannot be negative")
    var stock: Int? = 0,

    @field:NotNull(message = "Unit ID cannot be null")
    var unitId: Int?,

    @field:NotNull(message = "Category ID cannot be null")
    var categoryId: Int?,

    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
)





