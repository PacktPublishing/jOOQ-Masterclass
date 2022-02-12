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
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.XML;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.unnest;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.DSL.xmlforest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void arrayToXML() {

        Result<Record1<XML>> result = ctx.select(
                xmlelement("departments",
                        xmlagg(xmlelement("department",
                                xmlforest(DEPARTMENT.DEPARTMENT_ID, field("r").as("topic"))))))
                .from(DEPARTMENT, lateral(select(field("topic"))
                        .from(unnest(DEPARTMENT.TOPIC).as("t", "topic"))
                ).as("r")).groupBy(field("r"))
                .fetch();

        System.out.println("Example (array):\n" + result.formatXML());
    }

    public void UDTToXML() {

        Result<Record1<XML>> result = ctx.select(
                xmlelement("data",
                        xmlelement("mananger", MANAGER.MANAGER_NAME),
                        xmlelement("evaluation", MANAGER.MANAGER_EVALUATION)))
                .from(MANAGER)
                .fetch();

        System.out.println("Example (UDT):\n" + result.formatXML());
    }

    public void oneToOneToXml() {

        Result<Record1<XML>> result1 = ctx.select(
                xmlelement("customer",
                        xmlelement("customerName", CUSTOMER.CUSTOMER_NAME),
                        xmlelement("phone", CUSTOMER.PHONE),
                        xmlelement("creditLimit", CUSTOMER.CREDIT_LIMIT),
                        xmlelement("details", field(select(xmlforest(
                                CUSTOMERDETAIL.CITY.as("city"),
                                CUSTOMERDETAIL.ADDRESS_LINE_FIRST.as("addressLineFirst"),
                                CUSTOMERDETAIL.STATE.as("state")))
                                .from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER
                                        .eq(CUSTOMER.CUSTOMER_NUMBER))))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetch();

        System.out.println("Example 1.1 (one-to-one):\n" + result1.formatXML());

        // same thing as above via JOIN        
        Result<Record1<XML>> result2 = ctx.select(
                xmlelement("customer",
                        xmlelement("customerName", CUSTOMER.CUSTOMER_NAME),
                        xmlelement("phone", CUSTOMER.PHONE),
                        xmlelement("creditLimit", CUSTOMER.CREDIT_LIMIT),
                        xmlelement("details", xmlforest(
                                CUSTOMERDETAIL.CITY.as("city"),
                                CUSTOMERDETAIL.ADDRESS_LINE_FIRST.as("addressLineFirst"),
                                CUSTOMERDETAIL.STATE.as("state")))))
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetch();

        System.out.println("Example 1.2 (one-to-one):\n" + result2.formatXML());
    }

    public void oneToManyToXml() {

        Result<Record1<XML>> result11 = ctx.select(
                xmlelement("productLine",
                        xmlelement("productLine", PRODUCTLINE.PRODUCT_LINE),
                        xmlelement("textDescription", PRODUCTLINE.TEXT_DESCRIPTION),
                        xmlelement("products", field(select(xmlagg(
                                xmlelement("product", // optionally, each product wrapped in <product/>
                                        xmlforest(
                                                PRODUCT.PRODUCT_NAME.as("productName"),
                                                PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                                                PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock")))))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))))))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Example 2.1.1 (one-to-many):\n" + result11.formatXML());

        String result12 = ctx.select(
                xmlelement("productlines", xmlagg(
                        xmlelement("productLine",
                                xmlelement("productLine", PRODUCTLINE.PRODUCT_LINE),
                                xmlelement("textDescription", PRODUCTLINE.TEXT_DESCRIPTION),
                                xmlelement("products", field(select(xmlagg(
                                        xmlelement("product", // optionally, each product wrapped in <product/>
                                                xmlforest(
                                                        PRODUCT.PRODUCT_NAME.as("productName"),
                                                        PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                                                        PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock")))))
                                        .from(PRODUCT)
                                        .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))))))
                        .orderBy(PRODUCTLINE.PRODUCT_LINE)))
                .from(PRODUCTLINE)
                .fetchSingleInto(String.class);

        System.out.println("Example 2.1.2 (one-to-many):\n" + result12);

        Result<Record1<XML>> result2 = ctx.select(
                xmlelement("productLine",
                        xmlelement("productLine", PRODUCTLINE.PRODUCT_LINE),
                        xmlelement("textDescription", PRODUCTLINE.TEXT_DESCRIPTION),
                        xmlelement("products", xmlagg(
                                xmlelement("product", // optionally, each product wrapped in <product/>
                                        xmlforest(
                                                PRODUCT.PRODUCT_NAME.as("productName"),
                                                PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                                                PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock")))))))
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .groupBy(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Example 2.2 (one-to-many):\n" + result2.formatXML());
    }

    public void oneToManyToXmlLimit() {

        // limit 'one' in one-to-many
        Result<Record1<XML>> result1 = ctx.select(
                xmlelement("productLine",
                        xmlelement("productLine", PRODUCTLINE.PRODUCT_LINE),
                        xmlelement("textDescription", PRODUCTLINE.TEXT_DESCRIPTION),
                        xmlelement("products", field(select(xmlagg(
                                xmlelement("product", // optionally, each product wrapped in <product/>
                                        xmlforest(
                                                PRODUCT.PRODUCT_NAME.as("productName"),
                                                PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                                                PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock")))))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))))))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(2)
                .fetch();

        System.out.println("Example 3.1 (one-to-many):\n" + result1.formatXML());

        // limit 'many' in one-to-many
        Result<Record1<XML>> result2 = ctx.select(
                xmlelement("productLine",
                        xmlelement("productLine", PRODUCTLINE.PRODUCT_LINE),
                        xmlelement("textDescription", PRODUCTLINE.TEXT_DESCRIPTION),
                        xmlelement("products", field(xmlagg(
                                xmlelement("product", // optionally, each product wrapped in <product/>
                                        xmlforest(
                                                field(name("productName")),
                                                field(name("productVendor")),
                                                field(name("quantityInStock")))))))))
                .from(PRODUCTLINE,
                        lateral(select(PRODUCT.PRODUCT_NAME.as("productName"),
                                PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                                PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                                .limit(2)).asTable("t"))
                .groupBy(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Example 3.2 (one-to-many):\n" + result2.formatXML());

        // limit 'one' and 'many' in one-to-many
        Result<Record1<XML>> result3 = ctx.select(
                xmlelement("productLine",
                        xmlelement("productLine", PRODUCTLINE.PRODUCT_LINE),
                        xmlelement("textDescription", PRODUCTLINE.TEXT_DESCRIPTION),
                        xmlelement("products", field(xmlagg(
                                xmlelement("product", // optionally, each product wrapped in <product/>
                                        xmlforest(
                                                field(name("productName")),
                                                field(name("productVendor")),
                                                field(name("quantityInStock")))))))))
                .from(PRODUCTLINE,
                        lateral(select(PRODUCT.PRODUCT_NAME.as("productName"),
                                PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                                PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                                .limit(3)).asTable("t")) // limit 'many'
                .groupBy(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(2) // limit 'one'
                .fetch();

        System.out.println("Example 3.3 (one-to-many):\n" + result3.formatXML());
    }

    public void manyToManyToXmlManagersOffices() {

        Result<Record1<XML>> result1 = ctx.select(
                xmlelement("managers",
                        xmlelement("managerId", MANAGER.MANAGER_ID),
                        xmlelement("managerName", MANAGER.MANAGER_NAME),
                        xmlelement("offices", field(select(xmlagg(xmlelement("office",
                                xmlforest(OFFICE.OFFICE_CODE.as("officeCode"), OFFICE.CITY, OFFICE.COUNTRY))))
                                .from(OFFICE)
                                .join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                                .where(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.eq(MANAGER.MANAGER_ID))))))
                .from(MANAGER)
                .fetch();

        System.out.println("Example 4.1 (many-to-many):\n" + result1.formatXML());

        Result<Record1<XML>> result2 = ctx.select(
                xmlelement("managers",
                        xmlelement("managerId", MANAGER.MANAGER_ID),
                        xmlelement("managerName", MANAGER.MANAGER_NAME),
                        xmlelement("offices", xmlagg(
                                xmlelement("office", // optionally, each office is wrapped in <office/>
                                        xmlforest(field(name("officeCode")),
                                                field(name("state")),
                                                field(name("city"))))))))
                .from(MANAGER)
                .join(select(OFFICE.OFFICE_CODE.as("officeCode"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                        OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("t"))
                .on(MANAGER.MANAGER_ID.eq(field("managers_manager_id", Long.class)))
                .groupBy(MANAGER.MANAGER_ID)
                .orderBy(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("Example 4.2 (many-to-many):\n" + result2.formatXML());
    }

    public void manyToManyToXmlOfficesManagers() {

        Result<Record1<XML>> result1 = ctx.select(
                xmlelement("offices",
                        xmlelement("officeCode", OFFICE.OFFICE_CODE),
                        xmlelement("state", OFFICE.STATE),
                        xmlelement("city", OFFICE.CITY),
                        xmlelement("managers", field(select(xmlagg(xmlelement("manager",
                                xmlforest(MANAGER.MANAGER_ID.as("managerId"),
                                        MANAGER.MANAGER_NAME.as("managerName")))))
                                .from(MANAGER)
                                .join(OFFICE_HAS_MANAGER)
                                .on(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.eq(MANAGER.MANAGER_ID))
                                .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))))))
                .from(OFFICE)
                .fetch();

        System.out.println("Example 5.1 (many-to-many):\n" + result1.formatXML());

        Result<Record1<XML>> result2 = ctx.select(
                xmlelement("offices",
                        xmlelement("officeCode", OFFICE.OFFICE_CODE),
                        xmlelement("state", OFFICE.STATE),
                        xmlelement("city", OFFICE.CITY),
                        xmlelement("managers", xmlagg(
                                xmlelement("manager", // optionally, each manager is wrapped in <manager/>
                                        xmlforest(field(name("managerId")),
                                                field(name("managerName"))))))))
                .from(OFFICE)
                .join(select(MANAGER.MANAGER_ID.as("managerId"),
                        MANAGER.MANAGER_NAME.as("managerName"),
                        MANAGER.MANAGER_DETAIL.as("details"),
                        OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE.as("offices_office_code"))
                        .from(MANAGER).join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("t"))
                .on(OFFICE.OFFICE_CODE.eq(field(name("offices_office_code"), String.class)))
                .groupBy(OFFICE.OFFICE_CODE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch();

        System.out.println("Example 5.2 (many-to-many):\n" + result2.formatXML());
    }

    public void manyToManyToJsonManagersOfficesLimit() {

        Result<Record1<XML>> result = ctx.select(
                xmlelement("managers",
                        xmlelement("managerId", MANAGER.MANAGER_ID),
                        xmlelement("managerName", MANAGER.MANAGER_NAME),
                        xmlelement("offices", xmlagg(
                                xmlelement("office", // optionally, each office is wrapped in <office/>
                                        xmlforest(field(name("officeCode")),
                                                field(name("state")),
                                                field(name("city"))))))))
                .from(MANAGER)
                .join(select(OFFICE.OFFICE_CODE.as("officeCode"),
                        OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                        OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .limit(5) // limit offices
                        .asTable("t"))
                .on(MANAGER.MANAGER_ID.eq(field("managers_manager_id", Long.class)))
                .groupBy(MANAGER.MANAGER_ID)
                .orderBy(MANAGER.MANAGER_ID)
                .limit(1) // limit managers
                .fetch();

        System.out.println("Example 6 (many-to-many):\n" + result.formatXML());
    }
}
