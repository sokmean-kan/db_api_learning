package com.example.db_api_learning.repository

import com.example.db_api_learning.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Int>{
    fun existsByName(name: String): Boolean
    fun existsByNameAndIdNot(name:String, id:Int): Boolean
}