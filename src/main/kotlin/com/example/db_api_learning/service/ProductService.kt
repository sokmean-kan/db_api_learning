package com.example.db_api_learning.service

import com.example.db_api_learning.component.sortByMulti
import com.example.db_api_learning.component.updateToProduct
import com.example.db_api_learning.dto.request.CreateProductRequest
import com.example.db_api_learning.dto.response.*
import com.example.db_api_learning.exception.ResourceNotFoundException
import com.example.db_api_learning.mapper.*
import com.example.db_api_learning.model.Product
import com.example.db_api_learning.repository.CategoryRepository
import com.example.db_api_learning.repository.ProductRepository
import com.example.db_api_learning.repository.UnitRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.collections.listOf

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val unitRepository: UnitRepository,
    private val categoryRepository: CategoryRepository,
    ) {
    fun getAllProducts(
        currentPage: Int,
        size:Int,
        sortBy: List<String>?,
        unitId: Int?,
        categoryId: Int?,
    ): ApiResponse<ProductResponse> {
    val order = sortBy?.sortByMulti()?: listOf(Sort.Order.asc("id"))
        val sort = Sort.by(order)
        val pageable = PageRequest.of(currentPage-1, size, sort)
//        val result = productRepository.findAll(pageable)
        val result: Page<Product> = when (unitId) {
            null -> when(categoryId){
                null-> productRepository.findAll(pageable)
                else -> productRepository.findAllByCategoryId(categoryId,pageable)
            }
            else -> when(categoryId){
                null -> productRepository.findAllByUnitId(unitId,pageable)
                else -> productRepository.findAllByUnitIdAndCategoryId(unitId,categoryId,pageable)
            }

        }

val listProduct = result.content.map { product ->
    val unit = unitRepository.findById(product.unitId).orElseThrow {
        IllegalArgumentException("Unit not found with id ${product.unitId}")
    }
    val category = categoryRepository.findById(product.categoryId).orElseThrow {
        ResourceNotFoundException("not found")
    }
    product.toProductResponse(unit = unit, category = category)
}

        return ApiResponse(
            Status(
                errorCode = 0,
                errorMessage = "Sucsess hz",
                    ),
            DataResponse(
                content = listProduct,
                pagination = PaginationResponse(
                    currentPage = currentPage,
                    pageSize = result.size,
                    totalElements = result.totalElements,
                    totalPage = result.totalPages,
                )
            )
        )

    }
    fun createProduct(productRequest: CreateProductRequest): ProductResForm {

        if (productRepository.existsByName(productRequest.name)) { //check name duplicated
            throw ResourceNotFoundException(
                "Product with name ${productRequest.name} already exists"
            )
        }

        val product = productRequest.toProduct()
        val unit = unitRepository.findById(productRequest.unitId?:0)
            .orElseThrow { ResourceNotFoundException("Unit not found") }
//
        val category = categoryRepository.findById(productRequest.categoryId?:0)
            .orElseThrow { ResourceNotFoundException("Category not found") }
        val savedPerson = productRepository.save(product)
        val result = savedPerson.toProductResForm()
        result.product?.unit = unit
        result.product?.category = category

        return result
    }

    fun updateProduct(productRequest: CreateProductRequest, findId:Int): ProductResForm {
        val existingProduct = productRepository.findById(findId)
            .orElseThrow{ ResourceNotFoundException("Product not found") }

        if (productRepository.existsByNameAndIdNot(productRequest.name,findId)) { //check name duplicated except product id = findId
            throw ResourceNotFoundException(
                "Product with name ${productRequest.name} already exists"
            )
        }
        val unitName = unitRepository.findById(productRequest.unitId?:0)
            .orElseThrow { ResourceNotFoundException("Unit not found") }

        val categoryName = categoryRepository.findById(productRequest.categoryId?:0)
            .orElseThrow { ResourceNotFoundException("Category not found") }
        //////////////////build function
        existingProduct.updateToProduct(productRequest)
        //////////////////Or directly
//        existingProduct.name = productRequest.name  // or => productRequest.name.let{existingProduct.name = it}
//        existingProduct.stock = productRequest.stock?:0
//        existingProduct.price = productRequest.price?:0.toBigDecimal()
//        existingProduct.unitId = productRequest.unitId?:0
//        existingProduct.categoryId = productRequest.categoryId?:0

        ///////////////////Or create new and replace id(NOT recommend)
//        val updateProduct = productRequest.toProduct()
//        updateProduct.id = existingProduct.id
//        updateProduct.createdAt = existingProduct.createdAt

        val savedPerson = productRepository.save(existingProduct)
        val result = savedPerson.toProductResForm()
        result.product?.unit = unitName
        result.product?.category = categoryName
        return result

    }

    fun findProductByLowStock(minStock:Int): List<Product> {
        return productRepository.findByStockLessThan(minStock)
    }

}