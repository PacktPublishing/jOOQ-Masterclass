package com.classicmodels.repository

import com.classicmodels.entity.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly=true)
interface EmployeeRepository : JpaRepository<com.classicmodels.entity.Employee, Long>, ClassicModelsRepository {
    
    fun findByJobTitle(jobTitle: String): MutableList<Employee>
}