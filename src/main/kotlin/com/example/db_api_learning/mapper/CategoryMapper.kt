package com.example.db_api_learning.mapper

import com.example.db_api_learning.dto.request.CategoryRequestCreate
import com.example.db_api_learning.dto.response.CategoryResForm
import com.example.db_api_learning.dto.response.Status
import com.example.db_api_learning.model.Category

fun CategoryRequestCreate.toCategory(): Category {
    return Category(
        name = name
    )
}

fun Category.toCategoryResForm(): CategoryResForm {
    return CategoryResForm(
        status = Status(
            errorCode = 0,
            errorMessage = "Success hz"
        ),
        category = Category(
            id = id,
            name = name
        )
    )
}