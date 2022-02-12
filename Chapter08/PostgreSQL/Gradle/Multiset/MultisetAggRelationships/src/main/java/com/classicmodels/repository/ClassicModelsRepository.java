package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.multisetAgg;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.unnest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void oneToOne() {

        // Result<Record4<String, String, BigDecimal, Result<Record3<String, String, String>>>>
        var result = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                multisetAgg(CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                        CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.CITY).as("customer_details"))
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .groupBy(CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT)
                .orderBy(CUSTOMER.CUSTOMER_NAME)
                .fetch();

        System.out.println("One-to-one:\n" + result);
        System.out.println("One-to-one (JSON): " + result.formatJSON());
        System.out.println("One-to-one (XML): " + result.formatXML());
    }

    public void oneToMany() {

        // Result<Record3<String, String, Result<Record3<String, String, Integer>>>>
        var result = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                multisetAgg(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR,
                        PRODUCT.QUANTITY_IN_STOCK).as("products"))
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .groupBy(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("One-to-many:\n" + result);
        System.out.println("One-to-many (JSON): " + result.formatJSON());
        System.out.println("One-to-many (XML): " + result.formatXML());
    }

    public void manyToMany() {

        // Result<Record3<Long, String, Result<Record3<String, String, String>>>>
        var result1 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                multisetAgg(
                        field(name("t", "officeCode"), String.class), 
                        field(name("t", "city"), String.class), 
                        field(name("t", "state"), String.class)).as("offices"))
                .from(MANAGER, lateral(select(OFFICE.OFFICE_CODE.as("officeCode"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))).asTable("t"))
                .groupBy(MANAGER.MANAGER_ID)
                .orderBy(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("Many-to-many:\n" + result1);
        System.out.println("Many-to-many (JSON): " + result1.formatJSON());
        System.out.println("Many-to-many (XML): " + result1.formatXML());

        // Result<Record4<String, String, String, Result<Record2<Long, String>>>>        
        var result2 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE,
                multisetAgg(
                        field(name("t", "managerId"), Long.class), 
                        field(name("t", "managerName"), String.class)).as("managers"))
                .from(OFFICE, lateral(select(MANAGER.MANAGER_ID.as("managerId"),
                        MANAGER.MANAGER_NAME.as("managerName"))
                        .from(MANAGER).join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))).asTable("t"))
                .groupBy(OFFICE.OFFICE_CODE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch();

        System.out.println("Many-to-many:\n" + result2);
        System.out.println("Many-to-many (JSON): " + result2.formatJSON());
        System.out.println("Many-to-many (XML): " + result2.formatXML());
    }

    public void multisetArray() {

        // Result<Record2<Integer, Result<Record1<String>>>>
        var result = ctx.select(DEPARTMENT.DEPARTMENT_ID,
                multisetAgg(field(name("t", "topic"), String.class)).as("topic"))
                        .from(DEPARTMENT, unnest(DEPARTMENT.TOPIC).as("t", "topic"))
                .groupBy(DEPARTMENT.DEPARTMENT_ID)
                .fetch();

        System.out.println("Multiset Array:\n" + result);
    }
}
