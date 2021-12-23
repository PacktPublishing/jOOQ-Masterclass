package com.classicmodels.repository

import com.classicmodels.pojo.CustomerAndOrder
import jooq.generated.tables.references.*
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class ClassicModelsRepository(private val ctx: DSLContext) {   

    fun findCustomersAndOrders(): MutableList<CustomerAndOrder> {        

        /* Using jOOQ to build the typesafe SQL and to execute it */
        return ctx.select(CUSTOMER.CUSTOMER_NAME, ORDER.ORDER_DATE)
                  .from(ORDER)
                  .innerJoin(CUSTOMER).using(CUSTOMER.CUSTOMER_NUMBER)
                  .orderBy(ORDER.ORDER_DATE.desc())
                  .fetchInto(CustomerAndOrder::class.java)
    }
}