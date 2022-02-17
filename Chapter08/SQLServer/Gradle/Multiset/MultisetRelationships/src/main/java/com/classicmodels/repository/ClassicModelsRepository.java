package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;
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

        // Result<Record4<String, String, BigDecimal, Result<Record2<String, String>>>>
        var result = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                multiset(
                        select(CUSTOMERDETAIL.CUSTOMER_NUMBER, CUSTOMERDETAIL.POSTAL_CODE)
                                .from(CUSTOMERDETAIL)
                                .where(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER)).limit(1)
                ).as("customer_details"))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CUSTOMER_NAME)
                .fetch();

        System.out.println("One-to-one:\n" + result);
        System.out.println("One-to-one (JSON): " + result.formatJSON());
        System.out.println("One-to-one (XML): " + result.formatXML());
    }

    public void oneToMany() {

        // Result<Record3<String, String, Result<Record2<String, Integer>>>>
        var result = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                multiset(
                        select(PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                ).as("products"))
                .from(PRODUCTLINE)
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
                multiset(
                        select( // or, selectDistinct if duplicates are involved
                                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE)
                                .from(OFFICE)
                                .join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                                .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                ).as("offices"))
                .from(MANAGER)
                .orderBy(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("Many-to-many:\n" + result1);
        System.out.println("Many-to-many (JSON): " + result1.formatJSON());
        System.out.println("Many-to-many (XML): " + result1.formatXML());
        
        //  jOOQ’s type safe implicit to-one join feature
        var result2 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                multiset(
                        select(
                                OFFICE_HAS_MANAGER.office().OFFICE_CODE, 
                                OFFICE_HAS_MANAGER.office().CITY, 
                                OFFICE_HAS_MANAGER.office().STATE)
                                .from(OFFICE_HAS_MANAGER)                                
                                .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                ).as("offices"))
                .from(MANAGER)
                .orderBy(MANAGER.MANAGER_ID)
                .fetch();
        
        System.out.println("Many-to-many:\n" + result2);
        System.out.println("Many-to-many (JSON): " + result2.formatJSON());
        System.out.println("Many-to-many (XML): " + result2.formatXML());

        // Result<Record4<String, String, String, Result<Record2<Long, String>>>>
        var result3 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE,
                multiset(
                        select(
                                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                                .from(MANAGER)
                                .join(OFFICE_HAS_MANAGER)
                                .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                                .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                ).as("managers"))
                .from(OFFICE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch();

        System.out.println("Many-to-many:\n" + result3);
        System.out.println("Many-to-many (JSON): " + result3.formatJSON());
        System.out.println("Many-to-many (XML): " + result3.formatXML());
        
        //  jOOQ’s type safe implicit to-one join feature
        var result4 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE,
                multiset(
                        select(
                                OFFICE_HAS_MANAGER.manager().MANAGER_ID, 
                                OFFICE_HAS_MANAGER.manager().MANAGER_NAME)
                                .from(OFFICE_HAS_MANAGER)
                                .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                ).as("managers"))
                .from(OFFICE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch();
        
        System.out.println("Many-to-many:\n" + result4);
        System.out.println("Many-to-many (JSON): " + result4.formatJSON());
        System.out.println("Many-to-many (XML): " + result4.formatXML());
    }
}
