package com.classicmodels.repository

import com.classicmodels.pojo.EmployeeNoCntr

interface ClassicModelsRepository {

    fun findEmployeesAndLeastSalary(): MutableList<EmployeeNoCntr>
}