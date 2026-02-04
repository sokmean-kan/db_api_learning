package com.example.db_api_learning.repository

import com.example.db_api_learning.model.SaleDetail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface SaleDetailRepository : JpaRepository<SaleDetail, Long> {
    fun findBySaleId(saleId: Long): List<SaleDetail>
}
