package com.example.db_api_learning.exception


class ResourceNotFoundException(message: String? = "Data not found") : RuntimeException(message)
