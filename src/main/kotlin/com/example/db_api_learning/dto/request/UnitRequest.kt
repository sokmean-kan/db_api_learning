package com.example.db_api_learning.dto.request

import jakarta.validation.constraints.NotBlank

data class UnitRequestCreate(
    @field:NotBlank(message = "Name cannot be null.")
    val name: String?
)