package com.classicmodels.repository

import com.classicmodels.pojo.Order
import com.classicmodels.pojo.CustomerAndOrder
import com.classicmodels.pojo.Office
import java.time.LocalDate
import jooq.generated.tables.references.*
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class ClassicModelsRepository(private val ctx: DSLContext) {

    /* Using jOOQ to build the typesafe SQL and to execute it */
    fun findOfficesInTerritory(territory: String): MutableList<Office> {        
        
        return ctx.selectFrom(OFFICE)
                  .where(OFFICE.TERRITORY.eq(territory))
                  .fetchInto(Office::class.java)
    }

    fun findOrdersByRequiredDate(startDate: LocalDate, endDate: LocalDate): MutableList<Order> {        

        /* Using jOOQ to build the typesafe SQL and to execute it */
        return ctx.selectFrom(ORDER)
                  .where(ORDER.REQUIRED_DATE.between(startDate, endDate))                
                  .fetchInto(Order::class.java)
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