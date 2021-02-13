package com.classicmodels.repository;

import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.XML;
import org.jooq.XMLFormat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
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

    public void xmlProductlineProductOrderdetail() {

        Result<Record1<XML>> result1 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK,
                        select(ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH)
                                .from(ORDERDETAIL)
                                .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                                .orderBy(ORDERDETAIL.QUANTITY_ORDERED)
                                // .limit(3) // limit 'orderdetail'
                                .forXML().path().asField("orderdetails"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        // .limit(2) // limit 'product'
                        .forXML().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                // .limit(2) // limit 'productline'
                .forXML().path()
                .fetch();

        System.out.println("Example 1.1:\n" + result1.formatXML());
        
        String result2 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK,
                        select(ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH)
                                .from(ORDERDETAIL)
                                .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                                .orderBy(ORDERDETAIL.QUANTITY_ORDERED)
                                // .limit(3) // limit 'orderdetail'
                                .forXML().path().asField("orderdetails"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        // .limit(2) // limit 'product'
                        .forXML().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                // .limit(2) // limit 'productline'
                .forXML().path("productline")
                .fetch()
                .formatXML(XMLFormat.DEFAULT_FOR_RECORDS);

        System.out.println("Example 1.2:\n" + result2);

        Result<Record1<XML>> result3 = ctx.select(
                xmlelement("productLine",
                        xmlelement("productLine", PRODUCTLINE.PRODUCT_LINE),
                        xmlelement("textDescription", PRODUCTLINE.TEXT_DESCRIPTION),
                        xmlelement("products", field(select(xmlagg(
                                xmlelement("product", // optionally, each product wrapped in <product/>
                                        xmlforest(
                                                PRODUCT.PRODUCT_NAME.as("productName"),
                                                PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                                                PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"),
                                                field(select(xmlagg(
                                                        xmlelement("detail", // optionally, each order detail wrapped in <detail/>
                                                                xmlforest(
                                                                        ORDERDETAIL.QUANTITY_ORDERED.as("quantityOrdered"),
                                                                        ORDERDETAIL.PRICE_EACH.as("priceEach")))))
                                                        .from(ORDERDETAIL)
                                                        .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))).as("orderdetail")))))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))))))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Example 1.3 (one-to-many):\n" + result3.formatXML());
    }

    public void xmlCustomerPaymentBankTransactionCustomerdetail() {

        Result<Record1<XML>> result1 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT,
                select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE,
                        select(BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.TRANSFER_AMOUNT)
                                .from(BANK_TRANSACTION)
                                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER)))
                                .orderBy(BANK_TRANSACTION.TRANSFER_AMOUNT)
                                // .limit(3) // limit 'transactions'
                                .forXML().path().asField("transactions"))
                        .from(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                        .orderBy(PAYMENT.CACHING_DATE)
                        // .limit(2) // limit 'payments'
                        .forXML().path().asField("payments"),
                select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE)
                        .from(CUSTOMERDETAIL)
                        .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                        .forXML().path().asField("details"))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                // .limit(2) // limit 'customers'
                .forXML().path()
                .fetch();

        System.out.println("Example 2.1:\n" + result1.formatXML());

        String result2 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT,
                select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE,
                        select(BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.TRANSFER_AMOUNT)
                                .from(BANK_TRANSACTION)
                                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER)))
                                .orderBy(BANK_TRANSACTION.TRANSFER_AMOUNT)
                                // .limit(3) // limit 'transactions'
                                .forXML().path().asField("transactions"))
                        .from(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                        .orderBy(PAYMENT.CACHING_DATE)
                        // .limit(2) // limit 'payments'
                        .forXML().path().asField("payments"),
                select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE)
                        .from(CUSTOMERDETAIL)
                        .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                        .forXML().path().asField("details"))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                // .limit(2) // limit 'customers'
                .forXML().path("customer")
                .fetch()
                .formatXML(XMLFormat.DEFAULT_FOR_RECORDS);

        System.out.println("Example 2.2:\n" + result2);

        Result<Record1<XML>> result3 = ctx.select(
                xmlelement("customer",
                        xmlelement("customerName", CUSTOMER.CUSTOMER_NAME),
                        xmlelement("creditLimit", CUSTOMER.CREDIT_LIMIT),
                        xmlelement("payments", field(select(xmlagg(
                                xmlelement("payment", // optionally, each payment wrapped in <payment/>
                                        xmlforest(PAYMENT.CUSTOMER_NUMBER.as("customerNumber"),
                                                PAYMENT.INVOICE_AMOUNT.as("invoiceAmount"),
                                                PAYMENT.CACHING_DATE.as("cachingDate"),
                                                field(select(xmlagg(
                                                        xmlelement("transaction", // optionally, each transaction wrapped in <transaction/>
                                                                xmlforest(
                                                                        BANK_TRANSACTION.BANK_NAME.as("bankName"),
                                                                        BANK_TRANSACTION.TRANSFER_AMOUNT.as("transferAmount")))))
                                                        .from(BANK_TRANSACTION)
                                                        .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                                                .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER)))).as("transactions")))))
                                .from(PAYMENT)
                                .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))),
                        xmlelement("details", field(select(xmlagg(
                                xmlforest(CUSTOMERDETAIL.ADDRESS_LINE_FIRST.as("addressLineFirst"),
                                        CUSTOMERDETAIL.STATE.as("state"))))
                                .from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetch();

        System.out.println("Example 2.3:\n" + result3.formatXML());

        String result4 = ctx.select(
                xmlagg(
                        xmlelement("customer",
                                xmlelement("customerName", CUSTOMER.CUSTOMER_NAME),
                                xmlelement("creditLimit", CUSTOMER.CREDIT_LIMIT),
                                xmlelement("payments", field(select(xmlagg(
                                        xmlelement("payment", // optionally, each payment wrapped in <payment/>
                                                xmlforest(PAYMENT.CUSTOMER_NUMBER.as("paymentNumber"),
                                                        PAYMENT.INVOICE_AMOUNT.as("invoiceAmount"),
                                                        PAYMENT.CACHING_DATE.as("cachingDate"),
                                                        field(select(xmlagg(
                                                                xmlelement("transaction", // optionally, each transaction wrapped in <transaction/>
                                                                        xmlforest(
                                                                                BANK_TRANSACTION.BANK_NAME.as("bankName"),
                                                                                BANK_TRANSACTION.TRANSFER_AMOUNT.as("transferAmount")))))
                                                                .from(BANK_TRANSACTION)
                                                                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                                                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER)))).as("transactions")))))
                                        .from(PAYMENT)
                                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))),
                                xmlelement("details", field(select(xmlagg(
                                        xmlforest(CUSTOMERDETAIL.ADDRESS_LINE_FIRST.as("addressLineFirst"),
                                                CUSTOMERDETAIL.STATE.as("state"))))
                                        .from(CUSTOMERDETAIL)
                                        .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))))))
                        .orderBy(CUSTOMER.CREDIT_LIMIT))
                .from(CUSTOMER)
                .fetchSingleInto(String.class);

        System.out.println("Example 2.4:\n" + result4);
    }

    public void xmlOfficeManagerDepartmentEmployeeSale() {

        Result<Record1<XML>> result1 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                        select(SALE.FISCAL_YEAR, SALE.SALE_)
                                .from(SALE)
                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))
                                .forXML().path().asField("sales"))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .forXML().path().asField("employees"),
                select(DEPARTMENT.NAME, DEPARTMENT.PHONE)
                        .from(DEPARTMENT)
                        .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .forXML().path().asField("departments"),
                select(MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"))
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .forXML().path().asField("managers"))
                .from(OFFICE)
                .forXML().path()
                .fetch();

        System.out.println("Example 3.1:\n" + result1.formatXML());
        
        String result2 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                        select(SALE.FISCAL_YEAR, SALE.SALE_)
                                .from(SALE)
                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))
                                .forXML().path().asField("sales"))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .forXML().path().asField("employees"),
                select(DEPARTMENT.NAME, DEPARTMENT.PHONE)
                        .from(DEPARTMENT)
                        .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .forXML().path().asField("departments"),
                select(MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"))
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .forXML().path().asField("managers"))
                .from(OFFICE)
                .forXML().path("office")
                .fetch()
                .formatXML(XMLFormat.DEFAULT_FOR_RECORDS);

        System.out.println("Example 3.2:\n" + result2);

        Result<Record1<XML>> result3 = ctx.select(
                xmlelement("offices",
                        xmlelement("officeCode", OFFICE.OFFICE_CODE),
                        xmlelement("officeCity", OFFICE.CITY),
                        xmlelement("officeCountry", OFFICE.COUNTRY),
                        xmlelement("departments", field(select(xmlagg(
                                xmlelement("department", // optionally, each department wrapped in <department/>
                                        xmlforest(DEPARTMENT.NAME.as("departmentName"),
                                                DEPARTMENT.PHONE.as("departmentPhone")))))
                                .from(DEPARTMENT)
                                .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE)))),
                        xmlelement("employees", field(select(xmlagg(
                                xmlelement("employee", // optionally, each employee wrapped in <employee/>
                                        xmlforest(EMPLOYEE.FIRST_NAME.as("employeeFirstName"),
                                                EMPLOYEE.LAST_NAME.as("employeeLastName"),
                                                EMPLOYEE.SALARY.as("employeeSalary"),
                                                field(select(xmlagg(
                                                        xmlelement("sale", // optionally, each sale wrapped in <sale/>
                                                                xmlforest(SALE.FISCAL_YEAR.as("fiscalYear"),
                                                                        SALE.SALE_.as("sale")))))
                                                        .from(SALE)
                                                        .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER)))
                                                        .as("sales")))))
                                .from(EMPLOYEE)
                                .where(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE)))),
                        xmlelement("managers", field(select(xmlagg(xmlforest(
                                MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName")))
                                .orderBy(MANAGER.MANAGER_ID))
                                .from(MANAGER)
                                .join(OFFICE_HAS_MANAGER)
                                .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                                .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))))))
                .from(OFFICE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch();

        System.out.println("Example 3.3:\n" + result3.formatXML());
    }
}
