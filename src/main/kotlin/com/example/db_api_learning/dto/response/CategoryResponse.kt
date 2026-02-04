package com.example.db_api_learning.dto.response

import com.example.db_api_learning.model.Category

//data class CategoryResponse(
//    var status: Status? = null,
//    var data: List<Category>? = null
//)

data class CategoryResForm(
    var status: Status?,
    var category: Category?,
)