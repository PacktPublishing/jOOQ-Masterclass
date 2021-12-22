package com.classicmodels.repository

import com.classicmodels.pojo.EmployeeNoCntr
import jooq.generated.tables.references.*
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.jooq.conf.RenderNameCase
import org.jooq.impl.DSL.firstValue

@Repository
class ClassicModelsRepositoryImpl(private val ctx: DSLContext) : ClassicModelsRepository {

    override fun findEmployeesAndLeastSalary(): MutableList<EmployeeNoCntr> {

       (ctx.configuration().settings()::setRenderNameCase)(RenderNameCase.UPPER) // this is default anyway

       return ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                firstValue(EMPLOYEE.FIRST_NAME)
                        .over().orderBy(EMPLOYEE.SALARY).`as`("least_salary"))
                .from(EMPLOYEE)
                .fetchInto(EmployeeNoCntr::class.java)
    }

}