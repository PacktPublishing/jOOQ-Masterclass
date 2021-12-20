package com.classicmodels.repository

import jooq.generated.tables.pojos.JooqOffice
import jooq.generated.tables.pojos.JooqOrder
import com.classicmodels.pojo.CustomerAndOrder
import java.time.LocalDate
import jooq.generated.tables.references.*
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class ClassicModelsRepository(private val ctx: DSLContext) {

    /* Using jOOQ to build the typesafe SQL and to execute it */
    fun findOfficesInTerritory(territory: String): MutableList<JooqOffice> {        
        
        return ctx.selectFrom(OFFICE)
                  .where(OFFICE.TERRITORY.eq(territory))
                  .fetchInto(JooqOffice::class.java)
    }

    fun findOrdersByRequiredDate(startDate: LocalDate, endDate: LocalDate): MutableList<JooqOrder> {        

        /* Using jOOQ to build the typesafe SQL and to execute it */
        return ctx.selectFrom(ORDER)
                  .where(ORDER.REQUIRED_DATE.between(startDate, endDate))                
                  .fetchInto(JooqOrder::class.java)
    }

    fun findCustomersAndOrders(): MutableList<CustomerAndOrder> {        

        /* Using jOOQ to build the typesafe SQL and to execute it */
        return ctx.select(CUSTOMER.CUSTOMER_NAME, ORDER.ORDER_DATE)
                  .from(ORDER)
                  .innerJoin(CUSTOMER).using(CUSTOMER.CUSTOMER_NUMBER)
                  .orderBy(ORDER.ORDER_DATE.desc())
                  .fetchInto(CustomerAndOrder::class.java)
    }
}