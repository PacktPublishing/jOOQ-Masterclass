package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleBank;
import com.classicmodels.pojo.SimpleCustomer;
import com.classicmodels.pojo.SimpleCustomerDetail;
import com.classicmodels.pojo.SimpleDepartment;
import com.classicmodels.pojo.SimpleEmployee;
import com.classicmodels.pojo.SimpleManager;
import com.classicmodels.pojo.SimpleOffice;
import com.classicmodels.pojo.SimpleOrderdetail;
import com.classicmodels.pojo.SimplePayment;
import com.classicmodels.pojo.SimpleProduct;
import com.classicmodels.pojo.SimpleProductLine;
import com.classicmodels.pojo.SimpleSale;
import com.classicmodels.pojo.java16records.RecordBank;
import com.classicmodels.pojo.java16records.RecordCustomer;
import com.classicmodels.pojo.java16records.RecordCustomerDetail;
import com.classicmodels.pojo.java16records.RecordDepartment;
import com.classicmodels.pojo.java16records.RecordEmployee;
import com.classicmodels.pojo.java16records.RecordManager;
import com.classicmodels.pojo.java16records.RecordOffice;
import com.classicmodels.pojo.java16records.RecordOrderdetail;
import com.classicmodels.pojo.java16records.RecordPayment;
import com.classicmodels.pojo.java16records.RecordProduct;
import com.classicmodels.pojo.java16records.RecordProductLine;
import com.classicmodels.pojo.java16records.RecordSale;
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
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.multiset;
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

    public void multisetProductlineProductOrderdetail() {

        var result = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                multiset(
                        select(
                                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK,
                                multiset(select(ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH)
                                        .from(ORDERDETAIL)
                                        .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                                        .orderBy(ORDERDETAIL.QUANTITY_ORDERED)))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                ).as("products"))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Result:\n" + result);
    }

    public void multisetProductlineProductOrderdetailMapping() {

        List<SimpleProductLine> resultPojo = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                multiset(
                        select(
                                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK,
                                multiset(select(ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH)
                                        .from(ORDERDETAIL)
                                        .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                                        .orderBy(ORDERDETAIL.QUANTITY_ORDERED))
                                        .convertFrom(r -> r.map(mapping(SimpleOrderdetail::new))))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE)))
                        .as("products").convertFrom(r -> r.map(mapping(SimpleProduct::new))))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch(mapping(SimpleProductLine::new));

        System.out.println("Result (POJO):\n" + resultPojo);
        
        List<RecordProductLine> resultRecord = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                multiset(
                        select(
                                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK,
                                multiset(select(ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH)
                                        .from(ORDERDETAIL)
                                        .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                                        .orderBy(ORDERDETAIL.QUANTITY_ORDERED))
                                        .convertFrom(r -> r.map(mapping(RecordOrderdetail::new))))
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE)))
                        .as("products").convertFrom(r -> r.map(mapping(RecordProduct::new))))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch(mapping(RecordProductLine::new));

        System.out.println("Result (Record):\n" + resultRecord);
    }

    public void multisetCustomerPaymentBankTransactionCustomerdetail() {

        // Result<Record4<String, BigDecimal, Result<Record4<Long, BigDecimal, LocalDateTime, 
        //        Result<Record2<String, BigDecimal>>>>, Result<Record3<String, String, String>>>>
        var result = ctx.select(
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT,
                multiset(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE,
                        multiset(select(BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.TRANSFER_AMOUNT)
                                .from(BANK_TRANSACTION)
                                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER)))
                                .orderBy(BANK_TRANSACTION.TRANSFER_AMOUNT)))
                        .from(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))).as("payments"),
                multiset(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE)
                        .from(CUSTOMERDETAIL)
                        .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))).as("customer_details"))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT.desc())
                .fetch();

        System.out.println("Result:\n" + result);
    }

    public void multisetCustomerPaymentBankTransactionCustomerdetailMapping() {

        List<SimpleCustomer> resultPojo = ctx.select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT,
                multiset(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE,
                        multiset(select(BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.TRANSFER_AMOUNT)
                                .from(BANK_TRANSACTION)
                                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER)))
                                .orderBy(BANK_TRANSACTION.TRANSFER_AMOUNT))
                                .convertFrom(r -> r.map(mapping(SimpleBank::new))))
                        .from(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))
                        .as("payments").convertFrom(r -> r.map(mapping(SimplePayment::new))),
                multiset(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE)
                        .from(CUSTOMERDETAIL)
                        .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))
                        .as("customer_details").convertFrom(r -> r.map(mapping(SimpleCustomerDetail::new))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT.desc())
                .fetch(mapping(SimpleCustomer::new));

        System.out.println("Result (POJO):\n" + resultPojo);
        
        List<RecordCustomer> resultRecord = ctx.select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT,
                multiset(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE,
                        multiset(select(BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.TRANSFER_AMOUNT)
                                .from(BANK_TRANSACTION)
                                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER)))
                                .orderBy(BANK_TRANSACTION.TRANSFER_AMOUNT))
                                .convertFrom(r -> r.map(mapping(RecordBank::new))))
                        .from(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))
                        .as("payments").convertFrom(r -> r.map(mapping(RecordPayment::new))),
                multiset(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.STATE)
                        .from(CUSTOMERDETAIL)
                        .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))
                        .as("customer_details").convertFrom(r -> r.map(mapping(RecordCustomerDetail::new))))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CREDIT_LIMIT.desc())
                .fetch(mapping(RecordCustomer::new));

        System.out.println("Result (Record):\n" + resultRecord);
    }

    public void multisetOfficeManagerDepartmentEmployeeSale() {

        var result = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                multiset(select(DEPARTMENT.NAME, DEPARTMENT.PHONE)
                        .from(DEPARTMENT)
                        .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))).as("departments"),
                multiset(select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                        multiset(select(SALE.FISCAL_YEAR, SALE.SALE_)
                                .from(SALE)
                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))
                                .orderBy(SALE.FISCAL_YEAR)))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))).as("employees"),
                multiset(select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .orderBy(MANAGER.MANAGER_ID)).as("managers"))
                .from(OFFICE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch();

        System.out.println("Result:\n" + result);
    }

    public void multisetOfficeManagerDepartmentEmployeeSaleMapping() {

        List<SimpleOffice> resultPojo = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                multiset(select(DEPARTMENT.NAME, DEPARTMENT.PHONE)
                        .from(DEPARTMENT)
                        .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE)))
                        .as("departments").convertFrom(r -> r.map(mapping(SimpleDepartment::new))),
                multiset(select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                        multiset(select(SALE.FISCAL_YEAR, SALE.SALE_)
                                .from(SALE)
                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))
                                .orderBy(SALE.FISCAL_YEAR))
                                .convertFrom(r -> r.map(mapping(SimpleSale::new))))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE)))
                        .as("employees").convertFrom(r -> r.map(mapping(SimpleEmployee::new))),
                multiset(select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .orderBy(MANAGER.MANAGER_ID))
                        .as("managers").convertFrom(r -> r.map(mapping(SimpleManager::new))))
                .from(OFFICE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch(mapping(SimpleOffice::new));

        System.out.println("Result (POJO):\n" + resultPojo);
        
        List<RecordOffice> resultRecord = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                multiset(select(DEPARTMENT.NAME, DEPARTMENT.PHONE)
                        .from(DEPARTMENT)
                        .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE)))
                        .as("departments").convertFrom(r -> r.map(mapping(RecordDepartment::new))),
                multiset(select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                        multiset(select(SALE.FISCAL_YEAR, SALE.SALE_)
                                .from(SALE)
                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))
                                .orderBy(SALE.FISCAL_YEAR))
                                .convertFrom(r -> r.map(mapping(RecordSale::new))))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE)))
                        .as("employees").convertFrom(r -> r.map(mapping(RecordEmployee::new))),
                multiset(select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                        .from(MANAGER)
                        .join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .orderBy(MANAGER.MANAGER_ID))
                        .as("managers").convertFrom(r -> r.map(mapping(RecordManager::new))))
                .from(OFFICE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch(mapping(RecordOffice::new));

        System.out.println("Result (Record):\n" + resultRecord);
    }
}
