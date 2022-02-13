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
import org.jooq.XMLFormat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlforest;
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

    public void arrayToXML() {         

        Result<Record1<XML>> result = ctx.select(
                xmlelement("department",
                        xmlelement("id", DEPARTMENT.DEPARTMENT_ID),
                        xmlelement("topic", field("COLUMN_VALUE"))))
                .from(DEPARTMENT, lateral(select(field("COLUMN_VALUE"))
                        .from(table(DEPARTMENT.TOPIC))))
                .orderBy(DEPARTMENT.DEPARTMENT_ID)
                .fetch();

        System.out.println("Example (array):\n" + result.formatXML());
    }

    public void UDTToXML() {

        Result<Record1<XML>> result = ctx.select(
                xmlelement("REPORT",
                        xmlelement("MANAGER", MANAGER.MANAGER_NAME),
                        xmlelement("EVALUATION", MANAGER.MANAGER_EVALUATION)))
                .from(MANAGER)
                .fetch();

        System.out.println("Example (UDT):\n" + result.formatXML());
    }

    public void oneToOneToXml() {

        Result<Record1<XML>> result1 = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT, CUSTOMERDETAIL.CITY,
                CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                CUSTOMERDETAIL.STATE)
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

        Result<Record1<XML>> result3 = ctx.select(
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

        System.out.println("Example 1.3 (one-to-one):\n" + result3.formatXML());

        // same thing as above via JOIN        
        Result<Record1<XML>> result4 = ctx.select(
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

        System.out.println("Example 1.4 (one-to-one):\n" + result4.formatXML());
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
        
        Result<Record1<XML>> result32 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE.as("productLine"),
                PRODUCTLINE.TEXT_DESCRIPTION.as("textDescription"),
                select(PRODUCT.PRODUCT_NAME.as("productName"),
                        PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                        PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .forXML().path().asField("products"))
                .from(PRODUCTLINE)
                .forXML().path("productline").root("productlines")
                .fetch();

        System.out.println("Example 2.3.2 (one-to-many):\n" + result32.formatXML());
        
        String result331 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE.as("productLine"),
                PRODUCTLINE.TEXT_DESCRIPTION.as("textDescription"),
                select(PRODUCT.PRODUCT_NAME.as("productName"),
                        PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                        PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        // .limit(5) // limit products
                        .forXML().path("product").asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                // .limit(2) // limit product lines
                .forXML().path("productline").root("productlines")
                .fetchSingleInto(String.class);

        System.out.println("Example 2.3.3.1 (one-to-many):\n" + result331);
        
        String result332 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE.as("productLine"),
                PRODUCTLINE.TEXT_DESCRIPTION.as("textDescription"),
                select(PRODUCT.PRODUCT_NAME.as("productName"),
                        PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                        PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        // .limit(5) // limit products
                        .forXML().path("product").asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                // .limit(2) // limit product lines
                .forXML().path("productline").root("productlines")
                .fetch()
                .formatXML(XMLFormat.DEFAULT_FOR_RECORDS);

        System.out.println("Example 2.3.3.2 (one-to-many):\n" + result332);

        Result<Record1<XML>> result41 = ctx.select(
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

        System.out.println("Example 2.4.1 (one-to-many):\n" + result41.formatXML());

        String result42 = ctx.select(
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

        System.out.println("Example 2.4.2 (one-to-many):\n" + result42);

        Result<Record1<XML>> result5 = ctx.select(
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

        System.out.println("Example 2.5 (one-to-many):\n" + result5.formatXML());
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

        System.out.println("Example 3.1 (many-to-many):\n" + result1.formatXML());

        Result<Record1<XML>> result2 = ctx.select(
                MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"),
                select(OFFICE.OFFICE_CODE.as("officeCode"), OFFICE.CITY.as("city"), OFFICE.STATE.as("state"))
                        .from(OFFICE)
                        .join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .forXML().path().asField("offices"))
                .from(MANAGER)
                .forXML().path()
                .fetch();

        System.out.println("Example 3.2 (many-to-many):\n" + result2.formatXML());

        Result<Record1<XML>> result3 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                field(name("office_code")), field(name("city")), field(name("state")))
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

        System.out.println("Example 3.3 (many-to-many):\n" + result3.formatXML());

        Result<Record1<XML>> result4 = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                field(name("office_code")), field(name("city")), field(name("state")))
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

        System.out.println("Example 3.4 (many-to-many):\n" + result4.formatXML());

        Result<Record1<XML>> result5 = ctx.select(
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
                .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                .groupBy(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                .orderBy(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("Example 3.5 (many-to-many):\n" + result5.formatXML());
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

        System.out.println("Example 4.1 (many-to-many):\n" + result1.formatXML());

        Result<Record1<XML>> result2 = ctx.select(
                OFFICE.OFFICE_CODE.as("officeCode"), OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                select(MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"))
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .forXML().path().asField("managers"))
                .from(OFFICE)
                .forXML().path()
                .fetch();

        System.out.println("Example 4.2 (many-to-many):\n" + result2.formatXML());

        Result<Record1<XML>> result3 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE,
                field(name("manager_id")), field(name("manager_name")))
                .from(OFFICE)
                .join(select(MANAGER.MANAGER_ID.as("manager_id"),
                        MANAGER.MANAGER_NAME.as("manager_name"),
                        OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE.as("offices_office_code"))
                        .from(MANAGER).join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("managers"))
                .on(OFFICE.OFFICE_CODE.eq(field(name("offices_office_code"), String.class)))
                .orderBy(OFFICE.OFFICE_CODE)
                .forXML().path().root("managers")
                .fetch();

        System.out.println("Example 4.3 (many-to-many):\n" + result3.formatXML());

        Result<Record1<XML>> result4 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE,
                field(name("manager_id")), field(name("manager_name")))
                .from(OFFICE)
                .join(select(MANAGER.MANAGER_ID.as("manager_id"),
                        MANAGER.MANAGER_NAME.as("manager_name"),
                        OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE.as("offices_office_code"))
                        .from(MANAGER).join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("managers"))
                .on(OFFICE.OFFICE_CODE.eq(field(name("offices_office_code"), String.class)))
                .orderBy(OFFICE.OFFICE_CODE)
                .forXML().auto().root("managers")
                .fetch();

        System.out.println("Example 4.4 (many-to-many):\n" + result4.formatXML());

        Result<Record1<XML>> result5 = ctx.select(
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
                .groupBy(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch();

        System.out.println("Example 4.5 (many-to-many):\n" + result5.formatXML());
    }
}
