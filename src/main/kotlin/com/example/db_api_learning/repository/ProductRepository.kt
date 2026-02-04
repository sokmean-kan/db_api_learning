package com.example.db_api_learning.repository

import com.example.db_api_learning.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Int>, JpaSpecificationExecutor<Product> {
    fun findByCategoryId(categoryId: Int): List<Product>

    // Check for low stock
    fun findByStockLessThan(threshold: Int): List<Product>
    fun existsByName(name: String): Boolean
    fun existsByNameAndIdNot(name: String, id: Int): Boolean
    fun findAllByUnitIdAndCategoryId(unitId: Int?,categoryId: Int?, pageable: Pageable): Page<Product>
    fun findAllByCategoryId(categoryId: Int?,pageable: Pageable): Page<Product>
    fun findAllByUnitId(unitId: Int?,pageable: Pageable): Page<Product>

}