package com.example.db_api_learning.component

import org.springframework.data.domain.Sort

fun List<String>.sortByMulti(): List<Sort.Order> {
    val result = this.mapNotNull {
        val parts = it.split(",")
        if (parts.size == 2) {
            val property = parts[0].trim()
            val direction = parts[1].trim().uppercase()
            when (direction) {
                "ASC" -> Sort.Order.asc(property)
                "DESC" -> Sort.Order.desc(property)
                else -> null
            }
        }else null
    }
    return result
}

