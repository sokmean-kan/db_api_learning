package com.example.db_api_learning.controller

import com.example.db_api_learning.dto.request.CategoryRequestCreate
import com.example.db_api_learning.dto.request.GetPaginationRequest
import com.example.db_api_learning.dto.response.ApiResponse
import com.example.db_api_learning.dto.response.CategoryResForm
import com.example.db_api_learning.model.Category
import com.example.db_api_learning.service.CategoryService
import jakarta.annotation.security.RolesAllowed
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@PreAuthorize("hasRole('ADMIN')")
//@RolesAllowed("ADMIN")
//@Secured("ROLE_ADMIN")
@RequestMapping("/api/categories")
class CategoryController(private val categoryService: CategoryService) {
    @PostMapping
    fun getAllCategories(@RequestBody request: GetPaginationRequest): ResponseEntity<ApiResponse<Category>> {
        val result = categoryService.getAllCategories(currentPage = request.currentPage, pageSize = request.pageSize, sortBy = request.sortOrder)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/create")
    fun createCategory(@Valid @RequestBody request: CategoryRequestCreate): ResponseEntity<CategoryResForm> {
        val result = categoryService.createCategory(request)
        return ResponseEntity.ok(result)
    }


    @PostMapping("/update/{id}")
    fun updateCategory(@PathVariable("id") id:Int, @Valid @RequestBody request: CategoryRequestCreate): ResponseEntity<CategoryResForm> {
        val result = categoryService.updateCategory(request, id)
        return ResponseEntity.ok(result)
    }
}