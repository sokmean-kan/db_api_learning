package com.example.db_api_learning.dto.response
import com.example.db_api_learning.model.Category
import com.example.db_api_learning.model.Unit
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductResponse(
    val id: Long?,
    var name: String,
    var price: BigDecimal,
    var stock: Int,
    var unit: Unit?,
    var category: Category?,
    val createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
): Serializable


data class ProductResForm(
    val status: Status,
    val product: ProductResponse? = null,
)






