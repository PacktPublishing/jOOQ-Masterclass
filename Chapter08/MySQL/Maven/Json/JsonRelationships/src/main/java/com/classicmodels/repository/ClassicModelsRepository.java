package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.Record1;
import org.jooq.Result;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.jsonValue;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.key;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.name;
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

    public void oneToOneToJson() {

        Result<Record1<JSON>> result11 = ctx.select(
                jsonObject(
                        // or, jsonEntry("customerName", CUSTOMER.CUSTOMER_NAME)
                        key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                        key("phone").value(CUSTOMER.PHONE),
                        key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                        key("details").value(select(
                                jsonObject(CUSTOMERDETAIL.CITY,
                                        CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE))
                                .from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetch();

        System.out.println("Example 1.1.1 (one-to-one):\n" + result11.formatJSON());

        // as a JSON String
        String result12 = ctx.select(
                jsonArrayAgg(
                        jsonObject(
                                // or, jsonEntry("customerName", CUSTOMER.CUSTOMER_NAME)
                                key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                                key("phone").value(CUSTOMER.PHONE),
                                key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                                key("details").value(select(
                                        jsonObject(CUSTOMERDETAIL.CITY,
                                                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE))
                                        .from(CUSTOMERDETAIL)
                                        .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))))
                        .orderBy(CUSTOMER.CREDIT_LIMIT))
                .from(CUSTOMER)
                .fetchSingleInto(String.class);

        System.out.println("Example 1.1.2 (one-to-one):\n" + result12);

        Result<Record1<JSON>> result2 = ctx.select(
                jsonObject(
                        key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                        key("phone").value(CUSTOMER.PHONE),
                        key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                        key("details").value(select(
                                jsonObject(key("city").value(CUSTOMERDETAIL.CITY),
                                        key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                        key("state").value(CUSTOMERDETAIL.STATE)))
                                .from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetch();

        System.out.println("Example 1.2 (one-to-one):\n" + result2.formatJSON());

        // same thing as above via JOIN
        Result<Record1<JSON>> result3 = ctx.select(jsonArrayAgg(jsonObject(
                key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                key("phone").value(CUSTOMER.PHONE),
                key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                key("details").value(
                        jsonObject(key("city").value(CUSTOMERDETAIL.CITY),
                                key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                key("state").value(CUSTOMERDETAIL.STATE)))))
                .orderBy(CUSTOMER.CREDIT_LIMIT))
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetch();

        System.out.println("Example 1.3 (one-to-one):\n" + result3.formatJSON());
    }

    public void oneToOneToJsonLimit() {

        Result<Record1<JSON>> result1 = ctx.select(
                jsonObject(
                        key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                        key("phone").value(CUSTOMER.PHONE),
                        key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                        key("details").value(select(
                                jsonObject(key("city").value(CUSTOMERDETAIL.CITY),
                                        key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                        key("state").value(CUSTOMERDETAIL.STATE)))
                                .from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CUSTOMER_NAME)
                .limit(2)
                .fetch();

        System.out.println("Example 2.1 (one-to-one and limit):\n" + result1.formatJSON());

        // limit join() via groupBy()
        Result<Record1<JSON>> result2 = ctx.select(jsonArrayAgg(jsonObject(
                key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                key("phone").value(CUSTOMER.PHONE),
                key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                key("details").value(
                        jsonObject(key("city").value(CUSTOMERDETAIL.CITY),
                                key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                key("state").value(CUSTOMERDETAIL.STATE))))))
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .groupBy(CUSTOMER.CUSTOMER_NUMBER)
                .orderBy(CUSTOMER.CUSTOMER_NAME)
                .limit(2)
                .fetch();

        System.out.println("Example 2.2 (one-to-one and limit):\n" + result2.formatJSON());

        // limit your result before aggregating via subquery
        Result<Record1<JSON>> result3 = ctx.select(jsonArrayAgg(jsonObject(
                key("customerName").value(field("customerName")),
                key("phone").value(field("phone")),
                key("creditLimit").value(field("creditLimit")),
                key("details").value(
                        jsonObject(key("city").value(field("city")),
                                key("addressLineFirst").value(field("addressFirstLine")),
                                key("state").value(field("state"))))))
                .orderBy(field("creditLimit")))
                .from(select(CUSTOMER.CUSTOMER_NAME.as("customerName"),
                        CUSTOMER.PHONE.as("phone"), CUSTOMER.CREDIT_LIMIT.as("creditLimit"),
                        CUSTOMERDETAIL.CITY.as("city"),
                        CUSTOMERDETAIL.ADDRESS_LINE_FIRST.as("addressFirstLine"),
                        CUSTOMERDETAIL.STATE.as("state"))
                        .from(CUSTOMER)
                        .join(CUSTOMERDETAIL)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                        .orderBy(CUSTOMER.CUSTOMER_NAME)
                        .limit(2))
                .fetch();

        System.out.println("Example 2.3 (one-to-one and limit):\n" + result3.formatJSON());
    }

    public void oneToManyToJson() {

        Result<Record1<JSON>> result31 = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(select(jsonArrayAgg(
                                // or, jsonObject(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK))
                                jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                        key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                        key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK)))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE)))))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Example 3.1.1 (one-to-many):\n" + result31.formatJSON());

        // as a JSON String
        String result32 = ctx.select(
                jsonArrayAgg(
                        jsonObject(
                                key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                                key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                                key("products").value(select(jsonArrayAgg(
                                        // or, jsonObject(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK))
                                        jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                                key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                                key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK)))
                                        .orderBy(PRODUCT.QUANTITY_IN_STOCK))
                                        .from(PRODUCT)
                                        .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE)))))
                        .orderBy(PRODUCTLINE.PRODUCT_LINE))
                .from(PRODUCTLINE)
                .fetchSingleInto(String.class);

        System.out.println("Example 3.1.2 (one-to-many):\n" + result32);

        // same thing as above via JOIN
        Result<Record1<JSON>> result2 = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(jsonArrayAgg(
                                jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                        key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                        key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK)))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK))))
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .groupBy(PRODUCTLINE.PRODUCT_LINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Example 3.2 (one-to-many):\n" + result2.formatJSON());
    }

    public void oneToManyToJsonLimit() {

        // limit 'one' in one-to-many
        Result<Record1<JSON>> result1 = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(jsonArrayAgg(
                                jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                        key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                        key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK)))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK))))
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .groupBy(PRODUCTLINE.PRODUCT_LINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(2)
                .fetch();

        System.out.println("Example 4.1 (one-to-many and limit):\n" + result1.formatJSON());

        // limit 'many' in one-to-many
        Result<Record1<JSON>> result2 = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(jsonArrayAgg(
                                jsonObject(key("productName").value(field("productName")),
                                        key("productVendor").value(field("productVendor")),
                                        key("quantityInStock").value(field("quantityInStock"))))
                                .orderBy(field("quantityInStock")))))
                .from(PRODUCTLINE,
                        lateral(select(PRODUCT.PRODUCT_NAME.as("productName"),
                                PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                                PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                                .limit(2)))
                .groupBy(PRODUCTLINE.PRODUCT_LINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Example 4.2 (one-to-many and limit):\n" + result2.formatJSON());

        // limit 'one' and 'many' in one-to-many
        Result<Record1<JSON>> result3 = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(jsonArrayAgg(
                                jsonObject(key("productName").value(field("productName")),
                                        key("productVendor").value(field("productVendor")),
                                        key("quantityInStock").value(field("quantityInStock"))))
                                .orderBy(field("quantityInStock")))))
                .from(PRODUCTLINE,
                        lateral(select(PRODUCT.PRODUCT_NAME.as("productName"),
                                PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                                PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                                .limit(2))) // limit 'many'
                .groupBy(PRODUCTLINE.PRODUCT_LINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(3) // limit 'one'
                .fetch();

        System.out.println("Example 4.3 (one-to-many and limit):\n" + result3.formatJSON());
    }

    public void manyToManyToJsonManagersOffices() {

        Result<Record1<JSON>> result51 = ctx.select(
                jsonObject(
                        key("managerId").value(MANAGER.MANAGER_ID),
                        key("managerName").value(MANAGER.MANAGER_NAME),
                        key("mobilePhone").value(jsonValue(MANAGER.MANAGER_DETAIL, "$.phoneNumber[*].number[1]")),
                        key("offices").value(select(jsonArrayAgg(jsonObject(
                                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)))
                                .from(OFFICE)
                                .join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                                .where(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.eq(MANAGER.MANAGER_ID)))))
                .from(MANAGER)
                .fetch();

        System.out.println("Example 5.1 (many-to-many):\n" + result51.formatJSON());

        // as a JSON String
        String result52 = ctx.select(
                jsonArrayAgg(
                        jsonObject(
                                key("managerId").value(MANAGER.MANAGER_ID),
                                key("managerName").value(MANAGER.MANAGER_NAME),
                                key("mobilePhone").value(jsonValue(MANAGER.MANAGER_DETAIL, "$.phoneNumber[*].number[1]")),
                                key("offices").value(select(jsonArrayAgg(jsonObject(
                                        OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)))
                                        .from(OFFICE)
                                        .join(OFFICE_HAS_MANAGER)
                                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                                        .where(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.eq(MANAGER.MANAGER_ID))))))
                .from(MANAGER)
                .fetchSingleInto(String.class);

        System.out.println("Example 5.2 (many-to-many):\n" + result52);

        Result<Record1<JSON>> result2 = ctx.select(
                jsonObject(
                        key("managerId").value(MANAGER.MANAGER_ID),
                        key("managerName").value(MANAGER.MANAGER_NAME),
                        key("mobilePhone").value(jsonValue(MANAGER.MANAGER_DETAIL, "$.phoneNumber[*].number[1]")),
                        key("offices").value(jsonArrayAgg(
                                jsonObject(key("officeCode").value(field("officeCode")),
                                        key("state").value(field("state")),
                                        key("city").value(field("city"))))
                                .orderBy(field("officeCode")))))
                .from(MANAGER)
                .join(select(OFFICE.OFFICE_CODE.as("officeCode"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                        OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)))
                .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                .groupBy(MANAGER.MANAGER_ID)
                .orderBy(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("Example 5.2 (many-to-many):\n" + result2.formatJSON());
    }

    public void manyToManyToJsonOfficesManagers() {

        Result<Record1<JSON>> result1 = ctx.select(
                jsonObject(
                        key("officeCode").value(OFFICE.OFFICE_CODE),
                        key("state").value(OFFICE.STATE),
                        key("city").value(OFFICE.CITY),
                        key("managers").value(select(jsonArrayAgg(jsonObject(
                                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                                jsonValue(MANAGER.MANAGER_DETAIL, "$.phoneNumber[*].number[1]"))))
                                .from(MANAGER)
                                .join(OFFICE_HAS_MANAGER)
                                .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                                .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)))))
                .from(OFFICE)
                .fetch();

        System.out.println("Example 6.1 (many-to-many):\n" + result1.formatJSON());

        Result<Record1<JSON>> result2 = ctx.select(
                jsonObject(
                        key("officeCode").value(OFFICE.OFFICE_CODE),
                        key("state").value(OFFICE.STATE),
                        key("city").value(OFFICE.CITY),
                        key("managers").value(jsonArrayAgg(
                                jsonObject(key("managerId").value(field("managerId")),
                                        key("managerName").value(field("managerName")),
                                        key("mobilePhone").value(field("mobilePhone"))))
                                .orderBy(field("managerId")))))
                .from(OFFICE)
                .join(select(MANAGER.MANAGER_ID.as("managerId"),
                        MANAGER.MANAGER_NAME.as("managerName"),
                        jsonValue(MANAGER.MANAGER_DETAIL, "$.phoneNumber[*].number[1]").as("mobilePhone"),
                        OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE.as("offices_office_code"))
                        .from(MANAGER).join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)))
                .on(OFFICE.OFFICE_CODE.eq(field(name("offices_office_code"), String.class)))
                .groupBy(OFFICE.OFFICE_CODE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch();

        System.out.println("Example 6.2 (many-to-many):\n" + result2.formatJSON());
    }

    public void manyToManyToJsonManagersOfficesLimit() {

        Result<Record1<JSON>> result = ctx.select(
                jsonObject(
                        key("managerId").value(MANAGER.MANAGER_ID),
                        key("managerName").value(MANAGER.MANAGER_NAME),
                        key("mobilePhone").value(jsonValue(MANAGER.MANAGER_DETAIL, "$.phoneNumber[*].number[1]")),
                        key("offices").value(jsonArrayAgg(
                                jsonObject(key("officeCode").value(field("officeCode")),
                                        key("state").value(field("state")),
                                        key("city").value(field("city"))))
                                .orderBy(field("officeCode")))))
                .from(MANAGER)
                .join(select(OFFICE.OFFICE_CODE.as("officeCode"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                        OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .groupBy(OFFICE.OFFICE_CODE)
                        .orderBy(OFFICE.OFFICE_CODE)
                        .limit(5) // limit the number of offices                        
                )
                .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                .groupBy(MANAGER.MANAGER_ID)
                .orderBy(MANAGER.MANAGER_ID)
                .limit(2) // limit the number of managers
                .fetch();

        System.out.println("Example 7 (many-to-many and limit):\n" + result.formatJSON());
    }
}
