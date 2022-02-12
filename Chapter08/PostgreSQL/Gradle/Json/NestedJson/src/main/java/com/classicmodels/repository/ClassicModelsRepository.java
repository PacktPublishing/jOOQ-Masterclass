package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleCustomer;
import com.classicmodels.pojo.SimpleOffice;
import com.classicmodels.pojo.SimpleProductLine;
import java.util.List;
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
import org.jooq.JSON;
import org.jooq.Record1;
import org.jooq.Result;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonEntry;
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

    public void jsonProductlineProductOrderdetail() {

        Result<Record1<JSON>> result1 = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(select(jsonArrayAgg(
                                jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                        key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                        key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK),
                                        key("orderdetail")
                                                .value(select(jsonArrayAgg(
                                                        jsonObject(
                                                                key("quantityOrdered").value(ORDERDETAIL.QUANTITY_ORDERED),
                                                                key("priceEach").value(ORDERDETAIL.PRICE_EACH)))
                                                        .orderBy(ORDERDETAIL.QUANTITY_ORDERED))
                                                        .from(ORDERDETAIL)
                                                        .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID)))))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE)))))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Example 1.1:\n" + result1.formatJSON());

        // the same thing but using jsonEntry and mapping to POJO
        List<SimpleProductLine> result2 = ctx.select(
                jsonObject(
                        jsonEntry("productLine", PRODUCTLINE.PRODUCT_LINE),
                        jsonEntry("textDescription", PRODUCTLINE.TEXT_DESCRIPTION),
                        jsonEntry("products", select(jsonArrayAgg(
                                jsonObject(jsonEntry("productName", PRODUCT.PRODUCT_NAME),
                                        jsonEntry("productVendor", PRODUCT.PRODUCT_VENDOR),
                                        jsonEntry("quantityInStock", PRODUCT.QUANTITY_IN_STOCK),
                                        jsonEntry("orderdetail",
                                                select(jsonArrayAgg(
                                                        jsonObject(
                                                                jsonEntry("quantityOrdered", ORDERDETAIL.QUANTITY_ORDERED),
                                                                jsonEntry("priceEach", ORDERDETAIL.PRICE_EACH)))
                                                        .orderBy(ORDERDETAIL.QUANTITY_ORDERED))
                                                        .from(ORDERDETAIL)
                                                        .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID)))))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE)))))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetchInto(SimpleProductLine.class);

        System.out.println("Example 1.2:\n" + result2);
    }

    public void jsonCustomerPaymentBankTransactionCustomerdetail() {

        Result<Record1<JSON>> result1 = ctx.select(
                jsonObject(
                        jsonEntry("customerName", CUSTOMER.CUSTOMER_NAME),
                        jsonEntry("creditLimit", CUSTOMER.CREDIT_LIMIT),
                        jsonEntry("payments", select(jsonArrayAgg(
                                jsonObject(jsonEntry("customerNumber", PAYMENT.CUSTOMER_NUMBER),
                                        jsonEntry("invoiceAmount", PAYMENT.INVOICE_AMOUNT),
                                        jsonEntry("cachingDate", PAYMENT.CACHING_DATE),
                                        jsonEntry("transactions", select(jsonArrayAgg(
                                                jsonObject(
                                                        jsonEntry("bankName", BANK_TRANSACTION.BANK_NAME),
                                                        jsonEntry("transferAmount", BANK_TRANSACTION.TRANSFER_AMOUNT)))
                                                .orderBy(BANK_TRANSACTION.TRANSFER_AMOUNT))
                                                .from(BANK_TRANSACTION)
                                                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER))))))
                                .orderBy(PAYMENT.CACHING_DATE))
                                .from(PAYMENT)
                                .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))),
                        jsonEntry("details", select(
                                jsonObject(jsonEntry("city", CUSTOMERDETAIL.CITY),
                                        jsonEntry("addressLineFirst", CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                        jsonEntry("state", CUSTOMERDETAIL.STATE)))
                                .from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetch();

        System.out.println("Example 2.1:\n" + result1.formatJSON());

        // the same thing but using key().value() and mapping to POJO
        List<SimpleCustomer> result2 = ctx.select(
                jsonObject(
                        key("customerName").value(CUSTOMER.CUSTOMER_NAME),
                        key("creditLimit").value(CUSTOMER.CREDIT_LIMIT),
                        key("payments").value(select(jsonArrayAgg(
                                jsonObject(key("customerNumber").value(PAYMENT.CUSTOMER_NUMBER),
                                        key("invoiceAmount").value(PAYMENT.INVOICE_AMOUNT),
                                        key("cachingDate").value(PAYMENT.CACHING_DATE),
                                        key("transactions")
                                                .value(select(jsonArrayAgg(
                                                        jsonObject(
                                                                key("bankName").value(BANK_TRANSACTION.BANK_NAME),
                                                                key("transferAmount").value(BANK_TRANSACTION.TRANSFER_AMOUNT)))
                                                        .orderBy(BANK_TRANSACTION.TRANSFER_AMOUNT))
                                                        .from(BANK_TRANSACTION)
                                                        .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                                                .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER))))))
                                .orderBy(PAYMENT.CACHING_DATE))
                                .from(PAYMENT)
                                .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))),
                        key("details").value(select(
                                jsonObject(key("city").value(CUSTOMERDETAIL.CITY),
                                        key("addressLineFirst").value(CUSTOMERDETAIL.ADDRESS_LINE_FIRST),
                                        key("state").value(CUSTOMERDETAIL.STATE)))
                                .from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .fetchInto(SimpleCustomer.class);

        System.out.println("Example 2.2:\n" + result2);
    }

    public void jsonOfficeManagerDepartmentEmployeeSale() {

        Result<Record1<JSON>> result1 = ctx.select(
                jsonObject(
                        jsonEntry("officeCode", OFFICE.OFFICE_CODE),
                        jsonEntry("officeCity", OFFICE.CITY),
                        jsonEntry("officeCountry", OFFICE.COUNTRY),
                        jsonEntry("departments", select(jsonArrayAgg(
                                jsonObject(jsonEntry("departmentName", DEPARTMENT.NAME),
                                        jsonEntry("departmentPhone", DEPARTMENT.PHONE))))
                                .from(DEPARTMENT)
                                .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))),
                        jsonEntry("employees", select(jsonArrayAgg(
                                jsonObject(jsonEntry("employeeFirstName", EMPLOYEE.FIRST_NAME),
                                        jsonEntry("employeeLastName", EMPLOYEE.LAST_NAME),
                                        jsonEntry("employeeSalary", EMPLOYEE.SALARY),
                                        jsonEntry("sales", select(jsonArrayAgg(
                                                jsonObject(jsonEntry("fiscalYear", SALE.FISCAL_YEAR),
                                                        jsonEntry("sale", SALE.SALE_)))
                                                .orderBy(SALE.FISCAL_YEAR))
                                                .from(SALE)
                                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))))))
                                .from(EMPLOYEE)
                                .where(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))),
                        jsonEntry("managers", select(jsonArrayAgg(jsonObject(
                                jsonEntry("managerId", MANAGER.MANAGER_ID),
                                jsonEntry("managerName", MANAGER.MANAGER_NAME)))
                                .orderBy(MANAGER.MANAGER_ID))
                                .from(MANAGER)
                                .join(OFFICE_HAS_MANAGER)
                                .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                                .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)))))
                .from(OFFICE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch();

        System.out.println("Example 3.1:\n" + result1.formatJSON());

        // the same thing but using key().value() and mapping to POJO
        List<SimpleOffice> result2 = ctx.select(
                jsonObject(
                        key("officeCode").value(OFFICE.OFFICE_CODE),
                        key("officeCity").value(OFFICE.CITY),
                        key("officeCountry").value(OFFICE.COUNTRY),
                        key("departments").value(select(jsonArrayAgg(
                                jsonObject(key("departmentName").value(DEPARTMENT.NAME),
                                        key("departmentPhone").value(DEPARTMENT.PHONE))))
                                .from(DEPARTMENT)
                                .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))),
                        key("employees").value(select(jsonArrayAgg(
                                jsonObject(key("employeeFirstName").value(EMPLOYEE.FIRST_NAME),
                                        key("employeeLastName").value(EMPLOYEE.LAST_NAME),
                                        key("employeeSalary").value(EMPLOYEE.SALARY),
                                        key("sales").value(select(jsonArrayAgg(
                                                jsonObject(key("fiscalYear").value(SALE.FISCAL_YEAR),
                                                        key("sale").value(SALE.SALE_)))
                                                .orderBy(SALE.FISCAL_YEAR))
                                                .from(SALE)
                                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))))))
                                .from(EMPLOYEE)
                                .where(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))),
                        key("managers").value(select(jsonArrayAgg(jsonObject(
                                key("managerId").value(MANAGER.MANAGER_ID),
                                key("managerName").value(MANAGER.MANAGER_NAME)))
                                .orderBy(MANAGER.MANAGER_ID))
                                .from(MANAGER)
                                .join(OFFICE_HAS_MANAGER)
                                .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                                .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)))))
                .from(OFFICE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetchInto(SimpleOffice.class);

        System.out.println("Example 3.2:\n" + result2);
    }
}