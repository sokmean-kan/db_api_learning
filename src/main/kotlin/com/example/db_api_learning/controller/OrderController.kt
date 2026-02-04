package com.example.db_api_learning.controller

import com.example.db_api_learning.dto.request.CreateSaleRequest
import com.example.db_api_learning.dto.request.GetPaginationRequest
import com.example.db_api_learning.dto.response.ApiResponse
import com.example.db_api_learning.dto.response.SaleCreateResForm
import com.example.db_api_learning.dto.response.SaleResponse
import com.example.db_api_learning.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/sale")
class OrderController(private val orderService: OrderService) {
    @PostMapping("/list")
    fun getAllSales(@RequestBody request: GetPaginationRequest): ResponseEntity<ApiResponse<SaleResponse>> {
        val sales = orderService.getAllOrders(request.currentPage, request.pageSize,request.sortOrder)
        return ResponseEntity.ok(sales)
    }

    @PostMapping("/add")
    fun createSale(@RequestBody request: CreateSaleRequest): ResponseEntity<SaleCreateResForm> {
        val sale = orderService.createOrder(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(sale)
    }


}