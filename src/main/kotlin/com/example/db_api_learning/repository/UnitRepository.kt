package com.example.db_api_learning.repository

import com.example.db_api_learning.model.Unit
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UnitRepository : JpaRepository<Unit, Int> {
    fun existsByName(name: String): Boolean
    fun existsByNameAndIdNot(name: String, id: Int): Boolean

    //    @Query(value = "SELECT u FROM Unit u WHERE u.name LIKE CONCAT('%', :name, '%')")
    @Query(
        value = "SELECT * From units Where name Like CONCAT('%', :nameSearch, '%')",
        nativeQuery = true
    )
    fun getAllUnitsName(@Param("nameSearch") name: String, page: Pageable): Page<Unit>  //nameSearch refer to name >>no need @Param if the same name(param and query above)
}