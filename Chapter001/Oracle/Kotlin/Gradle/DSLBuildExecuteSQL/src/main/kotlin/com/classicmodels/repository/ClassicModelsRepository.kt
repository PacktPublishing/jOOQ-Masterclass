package com.classicmodels.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import com.classicmodels.pojo.Office

import org.jooq.impl.DSL.field;
import org.jooq.impl.DSL.table;

@Repository
class ClassicModelsRepository(private val ctx: DSLContext) {

    /* Using jOOQ DSL to build and execute SQL */
    fun findOfficesInTerritory(territory: String): MutableList<Office> {        
        
        return ctx.selectFrom(table("office"))
                .where(field("territory").eq(territory))
                .fetchInto(Office::class.java)
    }
}