package com.classicmodels.repository;

import java.util.stream.Collectors;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.JSONFormat;
import org.jooq.Record1;
import org.jooq.Result;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.key;
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

        Result<Record1<JSON>> result1 = ctx.select(
                jsonObject(
                        // or, jsonEntry("customerName", CUSTOMER.CUSTOMER_NAME)
                        key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                        key("phone").value(CUSTOMER.PHONE),
                        key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                        key("details").value(select(
                                // or, jsonObject(CUSTOMERDETAIL.CITY,
                                //                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE)
                                jsonObject(key("city").value(CUSTOMERDETAIL.CITY),
                                        key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                        key("state").value(CUSTOMERDETAIL.STATE)))
                                .from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetch();

        System.out.println("Example 1.1 (one-to-one):\n" + result1.formatJSON());

        // same thing as above via JOIN
        Result<Record1<JSON>> result2 = ctx.select(jsonObject(
                key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                key("phone").value(CUSTOMER.PHONE),
                key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                key("details").value(
                        jsonObject(key("city").value(CUSTOMERDETAIL.CITY),
                                key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                key("state").value(CUSTOMERDETAIL.STATE)))))
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetch();

        System.out.println("Example 1.2 (one-to-one):\n" + result2.formatJSON());

        Result<Record1<JSON>> result31 = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT, CUSTOMERDETAIL.CITY,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                CUSTOMERDETAIL.STATE)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .forJSON().path().root("data")
                .fetch();

        System.out.println("Example 1.3.1 (one-to-one):\n" + result31.formatJSON());

        String result32 = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT, CUSTOMERDETAIL.CITY,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                CUSTOMERDETAIL.STATE)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(10)
                .forJSON().path()
                .fetchSingleInto(String.class);

        System.out.println("Example 1.3.2 (one-to-one):\n" + result32);

        Result<Record1<JSON>> result4 = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT, CUSTOMERDETAIL.CITY,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                CUSTOMERDETAIL.STATE)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .forJSON().auto().root("data")
                .fetch();

        System.out.println("Example 1.4 (one-to-one):\n" + result4.formatJSON());
    }

    public void oneToManyToJson() {

        Result<Record1<JSON>> result1 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE,
                PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME.as("product.product_name"),
                PRODUCT.PRODUCT_VENDOR.as("product.product_vendor"),
                PRODUCT.QUANTITY_IN_STOCK.as("product.quantity_in_stock"))
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .forJSON().path().root("data")
                .fetch();

        System.out.println("Example 2.1 (one-to-many):\n" + result1.formatJSON());

        Result<Record1<JSON>> result2 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .forJSON().auto().root("data")
                .fetch();

        System.out.println("Example 2.2 (one-to-many):\n" + result2.formatJSON());

        var result31 = ctx.select( // Result<Record3<String, String, Object>>
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        // .limit(5) // limit products
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                // .limit(2) // limit product lines
                // .forJSON().path() // add this if you need a return of type Result<Record1<JSON>>  
                .fetch();

        System.out.println("Example 2.3.1 (one-to-many):\n" + result31.formatJSON());

        Result<Record1<JSON>> result32 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE.as("productLine"),
                PRODUCTLINE.TEXT_DESCRIPTION.as("textDescription"),
                select(PRODUCT.PRODUCT_NAME.as("productName"),
                        PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                        PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        // .limit(5) // limit products
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                // .limit(2) // limit product lines
                .forJSON().path()
                .fetch();

        System.out.println("Example 2.3.2 (one-to-many):\n" + result32.formatJSON());
        
        String result33 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE.as("productLine"),
                PRODUCTLINE.TEXT_DESCRIPTION.as("textDescription"),
                select(PRODUCT.PRODUCT_NAME.as("productName"),
                        PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                        PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        //.limit(2) // limit products
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                //.limit(2) // limit product lines
                .forJSON().path()
                .fetch()
                .formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);                

        System.out.println("Example 2.3.3 (one-to-many):\n" + result33);

        String result34 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE.as("productLine"),
                PRODUCTLINE.TEXT_DESCRIPTION.as("textDescription"),
                select(PRODUCT.PRODUCT_NAME.as("productName"),
                        PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                        PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        // .limit(2) // limit products
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                // .limit(2) // limit product lines
                .forJSON().path()
                .fetchInto(String.class).stream().collect(Collectors.joining());

        System.out.println("Example 2.3.4 (one-to-many):\n" + result34);
    }

    public void manyToManyToJsonManagersOffices() {

        Result<Record1<JSON>> result31 = ctx.select(
                OFFICE.OFFICE_CODE.as("officeCode"), OFFICE.CITY, OFFICE.STATE,
                select(MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"))
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .forJSON().path().asField("managers"))
                .from(OFFICE)
                .forJSON().path()
                .fetch();

        System.out.println("Example 3.1.1 (many-to-many):\n" + result31.formatJSON());

        String result32 = ctx.select(
                OFFICE.OFFICE_CODE.as("officeCode"), OFFICE.CITY, OFFICE.STATE,
                select(MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"))
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .forJSON().path().asField("managers"))
                .from(OFFICE)
                .forJSON().path()
                .fetch()
                .formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);

        System.out.println("Example 3.1.2 (many-to-many):\n" + result32);

        Result<Record1<JSON>> result2 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                field("office_code"), field("city"), field("state"))
                .from(MANAGER)
                .join(select(OFFICE.OFFICE_CODE.as("office_code"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                        OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("offices"))
                .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                .orderBy(MANAGER.MANAGER_ID)
                .forJSON().path().root("data")
                .fetch();

        System.out.println("Example 3.2 (many-to-many):\n" + result2.formatJSON());

        Result<Record1<JSON>> result3 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                field("office_code"), field("city"), field("state"))
                .from(MANAGER)
                .join(select(OFFICE.OFFICE_CODE.as("office_code"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                        OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("offices"))
                .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                .orderBy(MANAGER.MANAGER_ID)
                .forJSON().auto().root("data")
                .fetch();

        System.out.println("Example 3.3 (many-to-many):\n" + result3.formatJSON());

        Result<Record1<JSON>> result4 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                select(field("office_code"), field("city"), field("state"))
                        .from(select(OFFICE.OFFICE_CODE.as("office_code"),
                                OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                                OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                                .from(OFFICE).join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("offices"))
                        .where(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                        .forJSON().path().asField("offices"))
                .from(MANAGER)
                .forJSON().path()
                .fetch();

        System.out.println("Example 3.4 (many-to-many):\n" + result4.formatJSON());
    }

    public void manyToManyToJsonOfficesManagers() {

        Result<Record1<JSON>> result41 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE)
                        .from(OFFICE)
                        .join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .forJSON().path().asField("offices"))
                .from(MANAGER)
                .forJSON().path()
                .fetch();

        System.out.println("Example 4.1.1 (many-to-many):\n" + result41.formatJSON());

        String result42 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE)
                        .from(OFFICE)
                        .join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .forJSON().path().asField("offices"))
                .from(MANAGER)
                .forJSON().path()
                .fetch()
                .formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);

        System.out.println("Example 4.1.2 (many-to-many):\n" + result42);

        Result<Record1<JSON>> result2 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE,
                field("manager_id"), field("manager_name"))
                .from(OFFICE)
                .join(select(MANAGER.MANAGER_ID.as("manager_id"),
                        MANAGER.MANAGER_NAME.as("manager_name"),
                        OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE.as("offices_office_code"))
                        .from(MANAGER).join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("managers"))
                .on(OFFICE.OFFICE_CODE.eq(field("offices_office_code", String.class)))
                .orderBy(OFFICE.OFFICE_CODE)
                .forJSON().path().root("data")
                .fetch();

        System.out.println("Example 4.2 (many-to-many):\n" + result2.formatJSON());

        Result<Record1<JSON>> result3 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE,
                field("manager_id"), field("manager_name"))
                .from(OFFICE)
                .join(select(MANAGER.MANAGER_ID.as("manager_id"),
                        MANAGER.MANAGER_NAME.as("manager_name"),
                        OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE.as("offices_office_code"))
                        .from(MANAGER).join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("managers"))
                .on(OFFICE.OFFICE_CODE.eq(field("offices_office_code", String.class)))
                .orderBy(OFFICE.OFFICE_CODE)
                .forJSON().auto().root("data")
                .fetch();

        System.out.println("Example 4.3 (many-to-many):\n" + result3.formatJSON());

        Result<Record1<JSON>> result4 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                select(field("manager_id"), field("manager_name"))
                        .from(select(MANAGER.MANAGER_ID.as("manager_id"),
                                MANAGER.MANAGER_NAME.as("manager_name"),
                                OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE.as("offices_office_code"))
                                .from(MANAGER).join(OFFICE_HAS_MANAGER)
                                .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("managers"))
                        .where(OFFICE.OFFICE_CODE.eq(field(name("offices_office_code"), String.class)))
                        .forJSON().path().asField("managers"))
                .from(OFFICE)
                .forJSON().path()
                .fetch();

        System.out.println("Example 4.4 (many-to-many):\n" + result4.formatJSON());
    }
}
