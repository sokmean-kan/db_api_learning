package com.example.db_api_learning.controller

import com.example.db_api_learning.dto.request.GetPaginationRequest
import com.example.db_api_learning.dto.request.UnitRequestCreate
import com.example.db_api_learning.dto.response.ApiResponse
import com.example.db_api_learning.dto.response.UnitResForm
import com.example.db_api_learning.model.Unit
import com.example.db_api_learning.service.UnitService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/units"])
class UnitController(private val unitService: UnitService) {

    @GetMapping("/list")
    fun getAllUnit(@RequestBody request: GetPaginationRequest): ResponseEntity<ApiResponse<Unit>> {
        val result =  unitService.getAll(request.currentPage,request.pageSize,request.sortOrder)
        return ResponseEntity.ok(result)
    }
    @PostMapping(value =["/search"])
    fun searchUnit(@RequestParam ("name") name: String, @RequestBody request: GetPaginationRequest): ResponseEntity<ApiResponse<Unit>>{
        val result =  unitService.getUnitSearch(name, request.currentPage,request.pageSize,request.sortOrder)
        return ResponseEntity.ok(result)
    }

    @PostMapping(value = ["/add"])
    fun addUnit(@Valid @RequestBody unit: UnitRequestCreate): ResponseEntity<UnitResForm> = ResponseEntity.ok(unitService.createUnit(unit))

    @PostMapping(value = ["/update/{id}"])
    fun updateUnit(
        @RequestBody unit: UnitRequestCreate,
        @PathVariable("id") id: Int ): ResponseEntity<UnitResForm> = ResponseEntity.ok(unitService.updateUnit(unit, id))


}


