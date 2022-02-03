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
import org.jooq.JSONFormat;
import org.jooq.Record1;
import org.jooq.Result;
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
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK,
                        select(ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH)
                                .from(ORDERDETAIL)
                                .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                                .orderBy(ORDERDETAIL.QUANTITY_ORDERED)
                                // .limit(3) // limit 'orderdetail'
                                .forJSON().path().asField("orderdetails"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        // .limit(2) // limit 'product'
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                // .limit(2) // limit 'productline'
                .forJSON().path().withoutArrayWrapper()
                .fetch();

        System.out.println("Example 1.1:\n" + result1.formatJSON());
        
        // same thing as above but mapping to String
        String result2 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK,
                        select(ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH)
                                .from(ORDERDETAIL)
                                .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                                .orderBy(ORDERDETAIL.QUANTITY_ORDERED)
                                // .limit(3) // limit 'orderdetail'
                                .forJSON().path().asField("orderdetails"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        // .limit(2) // limit 'product'
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                // .limit(2) // limit 'productline'
                .forJSON().path().withoutArrayWrapper()
                .fetch()
                .formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);

        System.out.println("Example 1.2:\n" + result2);

        // same thing as above but mapping to POJO
        List<SimpleProductLine> result3 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE.as("productLine"), PRODUCTLINE.TEXT_DESCRIPTION.as("textDescription"),
                select(PRODUCT.PRODUCT_NAME.as("productName"), PRODUCT.PRODUCT_VENDOR.as("productVendor"),
                        PRODUCT.QUANTITY_IN_STOCK.as("quantityInStock"),
                        select(ORDERDETAIL.QUANTITY_ORDERED.as("quantityOrdered"),
                                ORDERDETAIL.PRICE_EACH.as("priceEach"))
                                .from(ORDERDETAIL)
                                .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                                .orderBy(ORDERDETAIL.QUANTITY_ORDERED)
                                .limit(3)
                                .forJSON().path().asField("orderdetail"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                        .limit(2)
                        .forJSON().path().asField("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(1)
                .forJSON().path().withoutArrayWrapper()
                .fetchInto(SimpleProductLine.class);

        System.out.println("Example 1.3:\n" + result3);
    }

    public void jsonCustomerPaymentBankTransactionCustomerdetail() {

        Result<Record1<JSON>> result1 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT,
                select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE,
                        select(BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.TRANSFER_AMOUNT)
                                .from(BANK_TRANSACTION)
                                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER)))
                                .orderBy(BANK_TRANSACTION.TRANSFER_AMOUNT)
                                // .limit(3) // limit 'transactions'
                                .forJSON().path().asField("transactions"))
                        .from(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                        .orderBy(PAYMENT.CACHING_DATE)
                        // .limit(2) // limit 'payments'
                        .forJSON().path().asField("payments"),
                select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE)
                        .from(CUSTOMERDETAIL)
                        .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                        .forJSON().path().asField("details"))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                // .limit(2) // limit 'customers'
                .forJSON().path()
                .fetch();

        System.out.println("Example 2.1:\n" + result1.formatJSON());

        // same thing as above but mapping to String
        String result2 = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT,
                select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE,
                        select(BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.TRANSFER_AMOUNT)
                                .from(BANK_TRANSACTION)
                                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER)))
                                .orderBy(BANK_TRANSACTION.TRANSFER_AMOUNT)
                                // .limit(3) // limit 'transactions'
                                .forJSON().path().asField("transactions"))
                        .from(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                        .orderBy(PAYMENT.CACHING_DATE)
                        // .limit(2) // limit 'payments'
                        .forJSON().path().asField("payments"),
                select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE)
                        .from(CUSTOMERDETAIL)
                        .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                        .forJSON().path().asField("details"))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                // .limit(2) // limit 'customers'
                .forJSON().path()
                .fetch()
                .formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);

        System.out.println("Example 2.2:\n" + result2);
        
        // same thing as above but mapping to POJO
        List<SimpleCustomer> result3 = ctx.select(
                CUSTOMER.CUSTOMER_NAME.as("customerName"), CUSTOMER.CREDIT_LIMIT.as("creditLimit"),
                select(PAYMENT.CUSTOMER_NUMBER.as("customerNumber"), PAYMENT.INVOICE_AMOUNT.as("invoiceAmount"),
                        PAYMENT.CACHING_DATE.as("cachingDate"),
                        select(BANK_TRANSACTION.BANK_NAME.as("bankName"),
                                BANK_TRANSACTION.TRANSFER_AMOUNT.as("transferAmount"))
                                .from(BANK_TRANSACTION)
                                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER)))
                                .orderBy(BANK_TRANSACTION.TRANSFER_AMOUNT)
                                .limit(4) // limit 'transactions'
                                .forJSON().path().asField("transactions"))
                        .from(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                        .orderBy(PAYMENT.CACHING_DATE)
                        .limit(3) // limit 'payments'
                        .forJSON().path().asField("payments"),
                select(CUSTOMERDETAIL.CITY.as("city"),
                        CUSTOMERDETAIL.ADDRESS_LINE_FIRST.as("addressLineFirst"),
                        CUSTOMERDETAIL.STATE.as("state"))
                        .from(CUSTOMERDETAIL)
                        .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                        .forJSON().path().asField("details"))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CUSTOMER_NAME)
                .limit(1) // limit 'customers'
                .forJSON().path().withoutArrayWrapper()
                .fetchInto(SimpleCustomer.class);

        System.out.println("Example 2.3:\n" + result3);
    }

    public void jsonOfficeManagerDepartmentEmployeeSale() {

        Result<Record1<JSON>> result1 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                        select(SALE.FISCAL_YEAR, SALE.SALE_)
                                .from(SALE)
                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))
                                .forJSON().path().asField("sales"))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .forJSON().path().asField("employees"),
                select(DEPARTMENT.NAME, DEPARTMENT.PHONE)
                        .from(DEPARTMENT)
                        .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .forJSON().path().asField("departments"),
                select(MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"))
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .forJSON().path().asField("managers"))
                .from(OFFICE)
                .forJSON().path()
                .fetch();

        System.out.println("Example 3.1:\n" + result1.formatJSON());
        
        // same thing as above but mapping to String
        String result2 = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                        select(SALE.FISCAL_YEAR, SALE.SALE_)
                                .from(SALE)
                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))
                                .forJSON().path().asField("sales"))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .forJSON().path().asField("employees"),
                select(DEPARTMENT.NAME, DEPARTMENT.PHONE)
                        .from(DEPARTMENT)
                        .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .forJSON().path().asField("departments"),
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

        System.out.println("Example 3.2:\n" + result2);
        
        // same thing as above but mapping to POJO
        List<SimpleOffice> result3 = ctx.select(
                OFFICE.OFFICE_CODE.as("officeCode"), OFFICE.CITY.as("officeCity"), OFFICE.COUNTRY.as("officeCountry"),
                select(EMPLOYEE.FIRST_NAME.as("employeeFirstName"), EMPLOYEE.LAST_NAME.as("employeeLastName"), EMPLOYEE.SALARY.as("employeeSalary"),
                        select(SALE.FISCAL_YEAR.as("fiscalYear"), SALE.SALE_.as("sale"))
                                .from(SALE)
                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))
                                .forJSON().path().asField("sales"))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .forJSON().path().asField("employees"),
                select(DEPARTMENT.NAME.as("departmentName"), DEPARTMENT.PHONE.as("departmentPhone"))
                        .from(DEPARTMENT)
                        .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .forJSON().path().asField("departments"),
                select(MANAGER.MANAGER_ID.as("managerId"), MANAGER.MANAGER_NAME.as("managerName"))
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .forJSON().path().asField("managers"))
                .from(OFFICE)
                .limit(1) // limit offices
                .forJSON().path().withoutArrayWrapper()
                .fetchInto(SimpleOffice.class);

        System.out.println("Example 3.3:\n" + result3);
    }
}
