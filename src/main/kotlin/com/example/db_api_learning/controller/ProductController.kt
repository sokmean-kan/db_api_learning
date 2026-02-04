package com.example.db_api_learning.controller
import com.example.db_api_learning.dto.request.GetPaginationRequest
import com.example.db_api_learning.dto.request.CreateProductRequest
import com.example.db_api_learning.dto.response.ApiResponse
import com.example.db_api_learning.dto.response.ProductResForm
import com.example.db_api_learning.dto.response.ProductResponse
import com.example.db_api_learning.model.Product
import com.example.db_api_learning.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {
    @PostMapping("/list")
    fun getAllProducts(@RequestBody request: GetPaginationRequest): ResponseEntity<ApiResponse<ProductResponse>> =
        ResponseEntity.ok(productService.getAllProducts(request.currentPage, request.pageSize,request.sortOrder, request.filter?.unitId, request.filter?.categoryId))


    @PostMapping("/add")
    fun createProduct(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<ProductResForm> =
        ResponseEntity.ok(productService.createProduct(request))

    @PostMapping("/update/{id}")
    fun updateProduct(@Valid @RequestBody request: CreateProductRequest, @PathVariable id: Int): ResponseEntity<ProductResForm> =
        ResponseEntity.ok(productService.updateProduct(request,id))

    data class RequestData(
        val minStock: Int,
    )
    @PostMapping("/lowStock")
    fun getProductLowStock(@RequestBody request: RequestData): List<Product> {
        return productService.findProductByLowStock(request.minStock)

    }

}
