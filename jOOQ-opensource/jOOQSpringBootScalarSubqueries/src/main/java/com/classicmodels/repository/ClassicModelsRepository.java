package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.Tables.MANAGER;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Payment.PAYMENT;
import jooq.generated.tables.Sale;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.primaryKey;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectCount;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.VARCHAR;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext create;

    public ClassicModelsRepository(DSLContext create) {
        this.create = create;
    }

    public Object[][] findOfficesAndEmployeesNr() {

        return create.select(OFFICE.OFFICE_CODE, OFFICE.ADDRESS_LINE_FIRST,
                (selectCount().from(EMPLOYEE)
                        .where(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))).asField("employeesNr"))
                .from(OFFICE)
                .fetchArrays();
    }

    public Object[][] findSalariesGeAvgPlus25000() {

        return create.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.SALARY.ge(
                        select(avg(EMPLOYEE.SALARY).plus(25000)).from(EMPLOYEE).asField()
                ))
                .fetchArrays();
    }

    public int insertEmployee() {

        return create.insertInto(EMPLOYEE)
                .values(select(max(EMPLOYEE.EMPLOYEE_NUMBER.plus(1))).from(EMPLOYEE),
                        "Mark", "Janel", "x4443", "markjanel@classicmodelcars.com", "1",
                        select(avg(EMPLOYEE.SALARY)).from(EMPLOYEE),
                        1002L, "VP Of Engineering")
                .execute();
    }

    public int insertAnotherTableDataInManager() {

        /* create a temporary table identical to MANAGER */
        create.createTemporaryTable("managerTemp")
                .column("manager_id", BIGINT.nullable(false))
                .column("manager_name", VARCHAR(50).nullable(false))
                .constraints(
                        primaryKey("manager_id")
                )
                .execute();

        /* insert some data into the temporary table */
        create.insertInto(table("managerTemp"))
                .values(Math.random() * 1000, "John Malon")
                .execute();

        /* insert into MANAGER the data from the temporary table via SELECT */
        return create.insertInto(MANAGER)
                .select(select().from(table("managerTemp")))
                .onDuplicateKeyIgnore()
                .execute();
    }

    public int deletePaymentsOfAtelierGraphique() {

        return create.deleteFrom(PAYMENT)
                .where(PAYMENT.CUSTOMER_NUMBER.eq(
                        select(CUSTOMER.CUSTOMER_NUMBER).from(CUSTOMER)
                                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier graphique"))
                )).execute();
    }

    public Object[][] createPaymentViewForCustomerSignalGiftStores() {

        create.createView("paymentView").as(create.selectFrom(PAYMENT)
                .where(PAYMENT.CUSTOMER_NUMBER.eq(
                        select(CUSTOMER.CUSTOMER_NUMBER).from(CUSTOMER)
                                .where(CUSTOMER.CUSTOMER_NAME.eq("Signal Gift Stores"))
                ))).execute();

        return create.selectFrom(table("paymentView")).fetchArrays();
    }

    public Object[] findBaseSalary() {

        return create.select(EMPLOYEE.SALARY.plus(
                select(avg(EMPLOYEE.SALARY)).from(EMPLOYEE).asField()).as("baseSalary"))
                .from(EMPLOYEE)
                .fetchArray("baseSalary");
    }

    public Object[][] findUnpaidPayments() {

        return create.select(PAYMENT.INVOICE_AMOUNT, PAYMENT.PAYMENT_DATE, PAYMENT.CACHING_DATE,
                case_()
                        .when(PAYMENT.CACHING_DATE.isNull(),
                                select(CUSTOMER.CREDIT_LIMIT)
                                        .from(CUSTOMER)
                                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))
                        .else_(BigDecimal.valueOf(0.0)))
                .from(PAYMENT)
                .orderBy(PAYMENT.CACHING_DATE)
                .fetchArrays();
    }

    public Object[][] minAndRoundMinInvoiceAmount() {

        return create.select(min(PAYMENT.INVOICE_AMOUNT),
                round(select(min(PAYMENT.INVOICE_AMOUNT))
                        .from(PAYMENT).asField(), 0).as("round_min"))
                .from(PAYMENT)
                .fetchArrays();
    }

    public Object[][] maxSalePerFiscalYearAndEmployee() {
        
        Sale s1 = SALE.as("s1");
        Sale s2 = SALE.as("s2");             
             
        return create.select(s1.SALE_, s1.FISCAL_YEAR, s1.EMPLOYEE_NUMBER)
                .from(s1)
                .where(s1.SALE_.eq(select(max(s2.SALE_))
                .from(s2)
                .where(s2.EMPLOYEE_NUMBER.eq(s1.EMPLOYEE_NUMBER)
                        .and(s2.FISCAL_YEAR.eq(s1.FISCAL_YEAR)))))
                .orderBy(s1.FISCAL_YEAR)
                .fetchArrays();         
        
        // of course, it is simpler to rely on groupBy and not on a nested select 
        /*
        return create.select(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, max(SALE.SALE_))
                .from(SALE)
                .groupBy(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER)
                .orderBy(SALE.FISCAL_YEAR)
                .fetchArrays();
        */
    }
}
