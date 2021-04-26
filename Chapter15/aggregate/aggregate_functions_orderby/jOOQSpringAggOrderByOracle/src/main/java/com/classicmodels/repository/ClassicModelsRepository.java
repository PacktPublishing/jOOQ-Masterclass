package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.udt.records.SalaryarrRecord;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.collect;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonEntry;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlelement;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // JSON_ARRAYAGG()
    public void jsonArrayAggSale() {

        String result = ctx.select(jsonArrayAgg(jsonObject(
                jsonEntry("id", SALE.SALE_ID),
                jsonEntry("sale", SALE.SALE_)))
                .orderBy(SALE.SALE_).as("json_result"))
                .from(SALE)
                .limit(5)
                .fetchSingleInto(String.class);

        System.out.println("Result:\n" + result);
    }

    // XML_AGG()
    public void xmlAggCustomer() {

        String result = ctx.select(xmlagg(
                xmlelement("name", CUSTOMER.CUSTOMER_NAME))
                .orderBy(CUSTOMER.CUSTOMER_NAME.desc()))
                .from(CUSTOMER)
                .fetchSingleInto(String.class);

        System.out.println("Result:\n" + result);
    }

    // COLLECT()
    public void collectSale() {

        var result1 = ctx.select(collect(EMPLOYEE.SALARY, SalaryarrRecord.class)
                .orderBy(EMPLOYEE.SALARY.asc(), EMPLOYEE.JOB_TITLE.desc()))
                .from(EMPLOYEE)
                .fetch();

        System.out.println("Result (1):\n" + result1);

        var result2 = ctx.select(collect(EMPLOYEE.SALARY,
                SQLDataType.INTEGER.asArrayDataType(SalaryarrRecord.class))
                .orderBy(EMPLOYEE.SALARY.asc(), EMPLOYEE.JOB_TITLE.desc()))
                .from(EMPLOYEE)
                .fetch();

        System.out.println("Result (2):\n" + result2);
    }
    
    // GROUP_CONCAT()
    public void groupConcatEmployee() {

        ctx.select(groupConcat(concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME))
                .orderBy(EMPLOYEE.SALARY.desc()).separator("; ").as("names_of_employees"))
                .from(EMPLOYEE)
                .fetch();
    }
}
