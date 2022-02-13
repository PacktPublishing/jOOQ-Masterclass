package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleCustomer;
import com.classicmodels.pojo.SimpleManager;
import com.classicmodels.pojo.SimpleOffice;
import com.classicmodels.pojo.SimpleProductLine;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.key;
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

    // if you get, ORA-40478: output value too large (maximum: 4000)
    // then you should set MAX_STRING_SIZE to EXTENTED instead of STANDARD
    
    public void oneToOneToJsonToPojo() {
        
        List<SimpleCustomer> result1 = ctx.select(
                jsonObject(
                        key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                        key("phone").value(CUSTOMER.PHONE),
                        key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                        key("details").value(select(
                                jsonObject(key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                        key("state").value(CUSTOMERDETAIL.STATE),                                        
                                        key("city").value(CUSTOMERDETAIL.CITY)))
                                .from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetchInto(SimpleCustomer.class);

        System.out.println("Example 1.1 (one-to-one):\n" + result1);

        List<SimpleCustomer> result2 = ctx.select(jsonObject(
                key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                key("phone").value(CUSTOMER.PHONE),
                key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                key("details").value(
                        jsonObject(
                                key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                key("state").value(CUSTOMERDETAIL.STATE),
                                key("city").value(CUSTOMERDETAIL.CITY)))))
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetchInto(SimpleCustomer.class);

        System.out.println("Example 1.2 (one-to-one):\n" + result2);
        
        List<SimpleCustomer> result3 = ctx.select(CUSTOMER.CUSTOMER_NAME, 
                CUSTOMER.PHONE, CUSTOMER.CREDIT_LIMIT,
                select(CUSTOMERDETAIL.ADDRESS_LINE_FIRST.as("addressLineFirst"),
                        CUSTOMERDETAIL.STATE.as("state"), CUSTOMERDETAIL.CITY.as("city"))
                        .from(CUSTOMERDETAIL)
                        .where(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                        .forJSON().path().withoutArrayWrapper().asField("details"))
                .from(CUSTOMER)
                .fetchInto(SimpleCustomer.class);

        System.out.println("Example 1.3 (one-to-one):\n" + result3);
    }
    
    public void oneToManyToJsonToPojo() {                  
        
        List<SimpleProductLine> result1 = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(select(jsonArrayAgg(
                                jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                        key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                        key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK)))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE)))))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetchInto(SimpleProductLine.class);
        
        System.out.println("Example 2.1 (one-to-many):\n" + result1);
        
        List<SimpleProductLine> result2 = ctx.select(
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
                .fetchInto(SimpleProductLine.class);

        System.out.println("Example 2.2 (one-to-many):\n" + result2);
        
        List<SimpleProductLine> result3 = ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetchInto(SimpleProductLine.class);

        System.out.println("Example 2.3 (one-to-many):\n" + result3);
    }
    
    public void manyToManyToJsonToPojoManagersOffices() {

        List<SimpleManager> result1 = ctx.select(
                jsonObject(
                        key("managerId").value(MANAGER.MANAGER_ID),
                        key("managerName").value(MANAGER.MANAGER_NAME),
                        key("offices").value(select(jsonArrayAgg(jsonObject(
                                key("officeCode").value(OFFICE.OFFICE_CODE), 
                                key("state").value(OFFICE.STATE), 
                                key("city").value(OFFICE.CITY))))
                                .from(OFFICE)
                                .join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                                .where(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.eq(MANAGER.MANAGER_ID)))))
                .from(MANAGER)
                .fetchInto(SimpleManager.class);
        
        // trivial display         
        System.out.println("\nExample 3.1 (many-to-many):");
        for (SimpleManager sm : result1) {

            System.out.println("\nManager:");
            System.out.println("===========================");
            System.out.println(sm);
            System.out.println(sm.getOffices());
        }
       
        List<SimpleManager> result2 = ctx.select(
                jsonObject(
                        key("managerId").value(MANAGER.MANAGER_ID.as("MANAGER_ID")),
                        key("managerName").value(MANAGER.MANAGER_NAME.as("MANAGER_NAME")),
                        key("offices").value(jsonArrayAgg(
                                jsonObject(key("officeCode").value(field("OFFICE_CODE")),
                                        key("state").value(field("STATE")),
                                        key("city").value(field("CITY"))))
                                .orderBy(field("OFFICE_CODE")))))
                .from(MANAGER)
                .join(select(OFFICE.OFFICE_CODE.as("OFFICE_CODE"),
                        OFFICE.CITY.as("CITY"), OFFICE.STATE.as("STATE"),
                        OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("MANAGERS_MANAGER_ID"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)))
                .on(MANAGER.MANAGER_ID.eq(field("MANAGERS_MANAGER_ID", Long.class)))
                .groupBy(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                .orderBy(MANAGER.MANAGER_ID)
                .fetchInto(SimpleManager.class);

        // trivial display         
        System.out.println("\nExample 3.2 (many-to-many):");
        for (SimpleManager sm : result2) {

            System.out.println("\nManager:");
            System.out.println("===========================");
            System.out.println(sm);
            System.out.println(sm.getOffices());
        }
    }

    public void manyToManyToJsonToPojoOfficesManagers() {
        
        List<SimpleOffice> result1 = ctx.select(
                jsonObject(
                        key("officeCode").value(OFFICE.OFFICE_CODE),
                        key("state").value(OFFICE.STATE),
                        key("city").value(OFFICE.CITY),
                        key("managers").value(select(jsonArrayAgg(jsonObject(
                                key("managerId").value(MANAGER.MANAGER_ID), 
                                key("managerName").value(MANAGER.MANAGER_NAME))))
                                .from(MANAGER)
                                .join(OFFICE_HAS_MANAGER)
                                .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                                .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)))))
                .from(OFFICE)
                .fetchInto(SimpleOffice.class);
        
        // trivial display         
        System.out.println("\nExample 4.1 (many-to-many):");
        for (SimpleOffice so : result1) {

            System.out.println("\nOffice:");
            System.out.println("===========================");
            System.out.println(so);
            System.out.println(so.getManagers());
        }

        List<SimpleOffice> result2 = ctx.select(
                jsonObject(
                        key("officeCode").value(OFFICE.OFFICE_CODE),
                        key("state").value(OFFICE.STATE),
                        key("city").value(OFFICE.CITY),
                        key("managers").value(jsonArrayAgg(
                                jsonObject(key("managerId").value(field("MANAGER_ID")),
                                        key("managerName").value(field("MANAGER_NAME"))))
                        .orderBy(field("MANAGER_ID")))))
                .from(OFFICE)
                .join(select(MANAGER.MANAGER_ID.as("MANAGER_ID"),
                        MANAGER.MANAGER_NAME.as("MANAGER_NAME"),
                        MANAGER.MANAGER_DETAIL.as("DETAILS"),
                        OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE.as("OFFICES_OFFICE_CODE"))
                        .from(MANAGER).join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)))
                .on(OFFICE.OFFICE_CODE.eq(field("OFFICES_OFFICE_CODE", String.class)))
                .groupBy(OFFICE.OFFICE_CODE, OFFICE.STATE, OFFICE.CITY)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetchInto(SimpleOffice.class);

        // trivial display         
        System.out.println("\nExample 4.2 (many-to-many):");
        for (SimpleOffice so : result2) {

            System.out.println("\nOffice:");
            System.out.println("===========================");
            System.out.println(so);
            System.out.println(so.getManagers());
        }                
    }
}
