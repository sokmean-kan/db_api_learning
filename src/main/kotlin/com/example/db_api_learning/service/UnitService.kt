package com.example.db_api_learning.service

import com.example.db_api_learning.component.sortByMulti
import com.example.db_api_learning.component.updateToUnit
import com.example.db_api_learning.dto.request.UnitRequestCreate
import com.example.db_api_learning.dto.response.ApiResponse
import com.example.db_api_learning.dto.response.DataResponse
import com.example.db_api_learning.dto.response.PaginationResponse
import com.example.db_api_learning.dto.response.Status
import com.example.db_api_learning.dto.response.UnitResForm
import com.example.db_api_learning.exception.ResourceNotFoundException
import com.example.db_api_learning.mapper.toUnit
import com.example.db_api_learning.mapper.toUnitResForm
import com.example.db_api_learning.model.Unit
import com.example.db_api_learning.repository.UnitRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UnitService(private val unitRepository: UnitRepository) {
    fun getAll(
        currentPage: Int,
        pageSize: Int,
        sortBy: List<String>?
    ): ApiResponse<Unit> {
        val order = sortBy?.sortByMulti()?: listOf(Sort.Order.asc("id"))
        val sort = Sort.by(order)
        val pageable = PageRequest.of(currentPage - 1, pageSize, sort)
        val result = unitRepository.findAll(pageable)

        return ApiResponse(
            Status(
                errorCode = 0,
                errorMessage = "Success hz bro",
            ),
            DataResponse(
                content = result.content,
                pagination = PaginationResponse(
                    currentPage = currentPage,
                    pageSize = result.size,
                    totalElements = result.totalElements,
                    totalPage = result.totalPages,
                )
            )
        )
    }

    fun createUnit(unit: UnitRequestCreate): UnitResForm {
        val result = unit.toUnit()

        if (unitRepository.existsByName(result.name ?: "")) {
            throw ResourceNotFoundException("Unit: ${result.name} already exists")
        }
        val data = unitRepository.save(result)
        return data.toUnitResForm()
    }
///////////////Update my flow/////////
//    fun updateUnit(unit: UnitRequestCreate, requestId: Int): UnitResForm {
//
//        if (unitRepository.existsById(requestId)) {
//
//            if(unitRepository.existsByNameAndIdNot(unit.name?:"",requestId)) {
//                throw ResourceNotFoundException("Unit: ${unit.name} already exists")
//            }
//
//            val result = unit.toUnit().apply { id = requestId } //generate new id then replace
//            val data = unitRepository.save(result)
//            return data.toUnitResForm()
//        } else throw ResourceNotFoundException("Resource not found 01")
//    }

    ///////////replace exist unit name (any data if more data to update) with request name////////////////
//    fun updateUnit(unitRequest: UnitRequestCreate, requestId: Int): UnitResForm {
//        val existingUnit = unitRepository.findById(requestId)
//            .orElseThrow { ResourceNotFoundException("Resource not found 02") }
//
//        if(unitRepository.existsByNameAndIdNot(unitRequest.name?:"",requestId)) {
//            throw ResourceNotFoundException("Unit: ${unitRequest.name} already exists")
//        }
//        // Update fields from request DTO
//        existingUnit.name = unitRequest.name
////           More data...
//        val saved = unitRepository.save(existingUnit)
//        return saved.toUnitResForm()}

    //////////replace id that generated with the existing id, then save(easier understand)
    fun updateUnit(unitRequest: UnitRequestCreate, requestId: Int): UnitResForm {
        val existingUnit = unitRepository.findById(requestId)
            .orElseThrow { ResourceNotFoundException("Resource not found 03") }

        if (unitRepository.existsByNameAndIdNot(unitRequest.name ?: "", requestId)) {
            throw ResourceNotFoundException("Unit: ${unitRequest.name} already exists")
        }
        existingUnit.updateToUnit(unitRequest) //build a extension fun to reduce two line of code below
//        val updatedUnit = unitRequest.toUnit()
//        updatedUnit.id = existingUnit.id
        val savedUnit = unitRepository.save(existingUnit)
        return savedUnit.toUnitResForm()
    }

//    @Transactional ///////////Hibernate detects changes automatically
//    fun updateUnit(request: UnitRequestCreate, id: Int): UnitResForm {
//        val unit = unitRepository.findById(id).orElseThrow()
//        unit.name = request.name
//        // No need to call save()
//        return unit.toUnitResForm()
//    }

    fun getUnitSearch(name: String, currentPage: Int, pageSize: Int, sortBy: List<String>?): ApiResponse<Unit> {
        val order = sortBy?.sortByMulti() ?: listOf(Sort.Order.asc("id"))
        val sort = Sort.by(order)
        val pageable = PageRequest.of(currentPage - 1, pageSize, sort)
        val result = unitRepository.getAllUnitsName(name, pageable)

        if (result.content.isEmpty()) {
            throw ResourceNotFoundException("Resource not found")
        }

        return ApiResponse(
            Status(
                errorCode = 0,
                errorMessage = "Success hz bro",
            ),
            DataResponse(
                content = result.content,
                pagination = PaginationResponse(
                    currentPage = currentPage,
                    pageSize = result.size,
                    totalElements = result.totalElements,
                    totalPage = result.totalPages,
                )
            )
        )
    }

}