package com.example.db_api_learning.service

import com.example.db_api_learning.component.sortByMulti
import com.example.db_api_learning.dto.request.CategoryRequestCreate
import com.example.db_api_learning.dto.response.ApiResponse
import com.example.db_api_learning.dto.response.CategoryResForm
import com.example.db_api_learning.dto.response.DataResponse
import com.example.db_api_learning.dto.response.PaginationResponse
import com.example.db_api_learning.dto.response.Status
import com.example.db_api_learning.exception.ResourceNotFoundException
import com.example.db_api_learning.mapper.toCategory
import com.example.db_api_learning.mapper.toCategoryResForm
import com.example.db_api_learning.model.Category
import com.example.db_api_learning.repository.CategoryRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    fun getAllCategories(
        currentPage: Int,
        pageSize: Int,
        sortBy: List<String>?
    ): ApiResponse<Category> {
        val order = sortBy?.sortByMulti()?: listOf(Sort.Order.asc("id"))
        val sort = Sort.by(order)
        val pageable = PageRequest.of(currentPage-1, pageSize,sort)
        val result = categoryRepository.findAll(pageable)
        return ApiResponse(
            Status(
                errorCode = 0,
                errorMessage = "Success hz"
            ),
            DataResponse(
                content = result.content,
                pagination = PaginationResponse(
                    currentPage = currentPage,
                    pageSize = pageSize,
                    totalElements = result.totalElements,
                    totalPage = result.totalPages,
                )
            )
        )

    }

    fun createCategory(category: CategoryRequestCreate): CategoryResForm {

        val newCategory = category.toCategory()
        if (categoryRepository.existsByName(newCategory.name ?: "")) {
            throw ResourceNotFoundException("Unit: ${newCategory.name} already exists")
        }
        val data = categoryRepository.save(newCategory)
        return data.toCategoryResForm()

    }

    fun updateCategory(category: CategoryRequestCreate, findId:Int): CategoryResForm {
        val existingCategory = categoryRepository.findById(findId)
            .orElseThrow { ResourceNotFoundException("Category not found with id $findId") }
        if (categoryRepository.existsByNameAndIdNot(category.name ?:"",findId)) {
            throw ResourceNotFoundException("Unit: ${category.name} already exists")
        }

        category.name.let { existingCategory.name = it } //or existingCategory.name = category.name
        val newCategory = categoryRepository.save(existingCategory)
        return newCategory.toCategoryResForm()
    }

}