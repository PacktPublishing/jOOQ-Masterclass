package com.classicmodels.repository;

import java.util.Arrays;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.arrayAgg;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonEntry;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlelement;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // ARRAY_AGG()
    public void arrayAggEmployee() {

        String[] names1 = ctx.select(
                arrayAgg(EMPLOYEE.FIRST_NAME).orderBy(EMPLOYEE.FIRST_NAME.desc(), EMPLOYEE.LAST_NAME.desc()))
                .from(EMPLOYEE)
                .fetch().get(0).value1();

        // fetch as a Java array
        String[] names2 = ctx.select(EMPLOYEE.FIRST_NAME)
                .from(EMPLOYEE)
                .orderBy(EMPLOYEE.FIRST_NAME.desc(), EMPLOYEE.LAST_NAME.desc())
                .fetchArray(EMPLOYEE.FIRST_NAME);

        System.out.println("Java array (1):\n" + Arrays.toString(names1));
        System.out.println("Java array (2):\n" + Arrays.toString(names2));
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

    // XMLAGG()
    public void xmlAggCustomer() {

        String result = ctx.select(xmlagg(
                xmlelement("name", CUSTOMER.CUSTOMER_NAME))
                .orderBy(CUSTOMER.CUSTOMER_NAME.desc()))
                .from(CUSTOMER)
                .fetchSingleInto(String.class);

        System.out.println("Result:\n" + result);
    }

    // GROUP_CONCAT()
    public void groupConcatEmployee() {

        ctx.select(groupConcat(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME))
                .orderBy(EMPLOYEE.SALARY.desc()).separator("; ").as("names_of_employees"))
                .from(EMPLOYEE)
                .fetch();
    }
}
