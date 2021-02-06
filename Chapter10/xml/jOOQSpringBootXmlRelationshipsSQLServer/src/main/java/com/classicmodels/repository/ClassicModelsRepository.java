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
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.XML;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void oneToOneToXml() {

        Result<Record1<XML>> result1 = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT, CUSTOMERDETAIL.CITY,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .forXML().auto().root("customers")
                .fetch();

        System.out.println("Example 1.1 (one-to-one):\n" + result1.formatXML());

        Result<Record1<XML>> result2 = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT, CUSTOMERDETAIL.CITY,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                CUSTOMERDETAIL.STATE)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .forXML().path().root("customers")
                .fetch();

        System.out.println("Example 1.2 (one-to-one):\n" + result2.formatXML());
    }

    public void oneToManyToXml() {

        Result<Record1<XML>> result1 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE,
                PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME.as("product.product_name"),
                PRODUCT.PRODUCT_VENDOR.as("product.product_vendor"),
                PRODUCT.QUANTITY_IN_STOCK.as("product.quantity_in_stock"))
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .forXML().path().root("productlines")
                .fetch();

        System.out.println("Example 2.1 (one-to-many):\n" + result1.formatXML());

        Result<Record1<XML>> result2 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .forXML().auto().root("productlines")
                .fetch();

        System.out.println("Example 2.2 (one-to-many):\n" + result2.formatXML());

        var result31 = ctx.select( // Result<Record3<String, String, Object>>
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        // .limit(5) // limit products
                        .forXML().path().asField("products"))
                .from(PRODUCTLINE)
                // .limit(2) // limit product lines
                // .forXML().path() // add this to return Result<Record1<XML>>
                .fetch();

        System.out.println("Example 2.3.1 (one-to-many):\n" + result31.formatXML());

        // SQL Server serve XML in slices, so you have to join all slices into the resulted String
        // or fetch a single slice by using LIMIT (check SQL Server docs for details)
        String result30 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE.as("productLine"),
                PRODUCTLINE.TEXT_DESCRIPTION.as("textDescription"),
                select(PRODUCT.PRODUCT_NAME.as("productName"),
                        PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                        PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .limit(1) // limit products
                        .forXML().path("product").asField("products"))
                .from(PRODUCTLINE)
                .limit(1) // limit product lines
                .forXML().path("productline").root("productlines")
                .fetchSingleInto(String.class);

        System.out.println("Example 2.3.0 (one-to-many):\n" + result30);
        
        String result32 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE.as("productLine"),
                PRODUCTLINE.TEXT_DESCRIPTION.as("textDescription"),
                select(PRODUCT.PRODUCT_NAME.as("productName"),
                        PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                        PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        // .limit(2) // limit products
                        .forXML().path("product").asField("products"))
                .from(PRODUCTLINE)
                //.limit(2) // limit product lines
                .forXML().path("productline").root("productlines")
                .fetchInto(String.class).stream().collect(Collectors.joining());

        System.out.println("Example 2.3.2 (one-to-many):\n" + result32);

        String result33 = StringUtils.join(ctx.select(
                PRODUCTLINE.PRODUCT_LINE.as("productLine"),
                PRODUCTLINE.TEXT_DESCRIPTION.as("textDescription"),
                select(PRODUCT.PRODUCT_NAME.as("productName"),
                        PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                        PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        // .limit(2) // limit products
                        .forXML().path("product").asField("products"))
                .from(PRODUCTLINE)
                //.limit(2) // limit product lines
                .forXML().path("productline").root("productlines")
                .fetchInto(String.class), "");

        System.out.println("Example 2.3.3 (one-to-many):\n" + result33);
    }

    public void manyToManyToXmlManagersOffices() {

        Result<Record1<XML>> result1 = ctx.select(
                MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"),
                select(OFFICE.OFFICE_CODE.as("officeCode"), OFFICE.CITY, OFFICE.STATE)
                        .from(OFFICE)
                        .join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .forXML().path().asField("offices"))
                .from(MANAGER)
                .forXML().path()
                .fetch();

        System.out.println("Example 3.1 (many-to-many):\n" + result1.formatXML());

        Result<Record1<XML>> result2 = ctx.select(
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
                .forXML().path().root("offices")
                .fetch();

        System.out.println("Example 3.2 (many-to-many):\n" + result2.formatXML());

        Result<Record1<XML>> result3 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                field("office_code"), field("city"), field("state"))
                .from(MANAGER)
                .join(select(OFFICE.OFFICE_CODE.as("office_code"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                        OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("office"))
                .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                .orderBy(MANAGER.MANAGER_ID)
                .forXML().auto().root("offices")
                .fetch();

        System.out.println("Example 3.3 (many-to-many):\n" + result3.formatXML());
    }

    public void manyToManyToXmlOfficesManagers() {

        Result<Record1<XML>> result1 = ctx.select(
                OFFICE.OFFICE_CODE.as("officeCode"), OFFICE.CITY, OFFICE.STATE,
                select(MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"))
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .forXML().path().asField("managers"))
                .from(OFFICE)
                .forXML().path()
                .fetch();

        System.out.println("Example 4.1 (many-to-many):\n" + result1.formatXML());

        Result<Record1<XML>> result2 = ctx.select(
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
                .forXML().path().root("managers")
                .fetch();

        System.out.println("Example 4.2 (many-to-many):\n" + result2.formatXML());

        Result<Record1<XML>> result3 = ctx.select(
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
                .forXML().auto().root("managers")
                .fetch();

        System.out.println("Example 4.3 (many-to-many):\n" + result3.formatXML());
    }
}
