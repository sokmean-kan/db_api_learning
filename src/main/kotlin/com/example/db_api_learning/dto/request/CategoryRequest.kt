package com.example.db_api_learning.dto.request

import jakarta.validation.constraints.NotBlank

data class CategoryRequestCreate (
    @field:NotBlank(message = "Name is required")
    var name : String?
)