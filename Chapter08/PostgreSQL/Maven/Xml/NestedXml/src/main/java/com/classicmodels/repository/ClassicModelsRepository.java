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
import static org.jooq.impl.DSL.field;
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

        System.out.println("Example 2.1 (one-to-many):\n" + result1.formatXML());
    }

    public void xmlCustomerPaymentBankTransactionCustomerdetail() {

        Result<Record1<XML>> result1 = ctx.select(
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

        System.out.println("Example 2.1:\n" + result1.formatXML());

        String result2 = ctx.select(
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

        System.out.println("Example 2.2:\n" + result2);
    }

    public void xmlOfficeManagerDepartmentEmployeeSale() {

        Result<Record1<XML>> result = ctx.select(
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

        System.out.println("Example 3:\n" + result.formatXML());
    }
}
