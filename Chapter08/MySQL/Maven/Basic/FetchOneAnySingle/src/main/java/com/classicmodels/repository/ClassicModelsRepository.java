package com.classicmodels.repository;

import static com.classicmodels.converter.YearMonthConverter.INTEGER_YEARMONTH_CONVERTER;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Objects;
import java.util.Optional;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.pojos.Employee;
import jooq.generated.tables.pojos.Sale;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void fetchOneEmployee() {

        EmployeeRecord result1 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOne();

        System.out.println("Example 1.1.1\n" + result1);

        if (result1 != null) {
            System.out.println("Example 1.1.2\n"
                    + "Employee number: " + result1.value1() + " Email:" + result1.value5());
        }

        // this is not prone to NPE
        Employee result2 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOneInto(Employee.class);

        System.out.println("Example 1.2.1\n" + result2);

        if (result2 != null) {
            System.out.println("Example 1.2.2\n"
                    + "Employee number: " + result2.getEmployeeNumber() + " Email:" + result2.getEmail());
        }

        // pay attention, since this is prone to NPE if the specified employee number doesn't exist in the db
        Employee result3 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOne().into(Employee.class);

        System.out.println("Example 1.3\n" + result3);

        var result4 = ctx.select(EMPLOYEE.EMAIL) // Record1<String>
                .from(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOne();
        System.out.println("Example 1.4\n" + result4);

        // pay attention, since this is prone to NPE if the specified employee number doesn't exist in the db
        String result5 = ctx.select(EMPLOYEE.EMAIL)
                .from(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOne().value1(); // or, into(String.class)
        System.out.println("Example 1.5\n" + result5);

        String result6 = ctx.select(EMPLOYEE.EMAIL)
                .from(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)                
                .fetchOneInto(String.class);
        System.out.println("Example 1.6\n" + result6);
                
        Optional<EmployeeRecord> result71 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOptional();
        System.out.println("Example 1.7.1\n" + result71);

        Optional<Employee> result72 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOptionalInto(Employee.class);
        System.out.println("Example 1.7.2\n" + result72);

        String result73 = Objects.requireNonNullElseGet(
                ctx.select(EMPLOYEE.EMAIL)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(13700L)) // one or none (null)                
                        .fetchOneInto(String.class), () -> "-");
        System.out.println("Example 1.7.3\n" + result73);

        Optional<String> result74 = Optional.ofNullable(
                ctx.select(EMPLOYEE.EMAIL)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)                
                        .fetchOneInto(String.class));
        System.out.println("Example 1.7.4\n" + result74);

        YearMonth result8 = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NUMBER.eq(112L)) // one or none (null)
                .fetchOne(CUSTOMER.FIRST_BUY_DATE, INTEGER_YEARMONTH_CONVERTER);
        System.out.println("Example 1.8\n" + result8);

        EmployeeRecord result9 = ctx.fetchOne(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L));
        System.out.println("Example 1.9 \n" + result9);

        Employee result10 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOneInto(Employee.class);
        System.out.println("Example 1.10\n" + result10);

        Employee result11 = ctx.select(EMPLOYEE.EMAIL, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOneInto(Employee.class);
        System.out.println("Example 1.11\n" + result11);

        EmployeeRecord result12 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOneInto(EmployeeRecord.class); // like ...fetchOne();
        System.out.println("Example 1.12\n" + result12);

        EmployeeRecord result13 = ctx.select(EMPLOYEE.EMAIL, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOneInto(EMPLOYEE);
        System.out.println("Example 1.13\n" + result13);

        EmployeeRecord result14 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOneInto(EMPLOYEE);
        System.out.println("Example 1.14\n" + result14);

        // Avoid fetching more data than needed
        String result15 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOne(EMPLOYEE.EMAIL);
        System.out.println("Example 1.15 (avoid) \n" + result15);

        // Avoid fetching more data than needed
        String result16 = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.JOB_TITLE, EMPLOYEE.EMAIL)
                .from(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchOne(EMPLOYEE.EMAIL);
        System.out.println("Example 1.16 (avoid) \n" + result16);
    }

    public void fetchSingleEmployee() {

        EmployeeRecord result1 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // must be exactly one
                .fetchSingle();
        System.out.println("Example 2.1\n" + result1);
        
        Employee result2 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // must be exactly one
                .fetchSingleInto(Employee.class);
        System.out.println("Example 2.2\n" + result2);

        var result3 = ctx.select(EMPLOYEE.EMAIL) // Record1<String>
                .from(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // must be exactly one
                .fetchSingle();
        System.out.println("Example 2.3\n" + result3);

        String result4 = ctx.select(EMPLOYEE.EMAIL)
                .from(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // must be exactly one
                .fetchSingle().value1(); // or, into(String.class)
        System.out.println("Example 2.4\n" + result4);

        String result5 = ctx.select(EMPLOYEE.EMAIL)
                .from(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // must be exactly one
                .fetchSingleInto(String.class);
        System.out.println("Example 2.5\n" + result5);

        YearMonth result6 = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NUMBER.eq(112L)) // must be exactly one
                .fetchSingle(CUSTOMER.FIRST_BUY_DATE, INTEGER_YEARMONTH_CONVERTER);
        System.out.println("Example 2.6\n" + result6);

        var result7 = ctx.fetchSingle(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)); // EmployeeRecord
        System.out.println("Example 2.7 \n" + result7);

        Employee result8 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // must be exactly one
                .fetchSingleInto(Employee.class);
        System.out.println("Example 2.8\n" + result8);

        Employee result9 = ctx.select(EMPLOYEE.EMAIL, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // must be exactly one
                .fetchSingleInto(Employee.class);
        System.out.println("Example 2.9\n" + result9);

        EmployeeRecord result10 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // must be exactly one
                .fetchSingleInto(EmployeeRecord.class); // like ...fetchSingle();
        System.out.println("Example 2.10\n" + result10);

        EmployeeRecord result11 = ctx.select(EMPLOYEE.EMAIL, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // must be exactly one
                .fetchSingleInto(EMPLOYEE);
        System.out.println("Example 2.11\n" + result11);

        EmployeeRecord result12 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // one or none (null)
                .fetchSingleInto(EMPLOYEE);
        System.out.println("Example 2.12\n" + result12);
        
        // Avoid fetching more data than needed
        String result13 = ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // must be exactly one
                .fetchSingle(EMPLOYEE.EMAIL);
        System.out.println("Example 2.13 (avoid) \n" + result13);

        // Avoid fetching more data than needed
        String result14 = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.JOB_TITLE, EMPLOYEE.EMAIL)
                .from(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L)) // must be exactly one
                .fetchSingle(EMPLOYEE.EMAIL);
        System.out.println("Example 2.14 (avoid) \n" + result14);
    }

    public void fetchAnySale() {

        SaleRecord result1 = ctx.selectFrom(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)
                .fetchAny();

        System.out.println("Example 1.1.1\n" + result1);

        if (result1 != null) {
            System.out.println("Example 1.1.2\n"
                    + "Fiscal year: " + result1.value2() + " Trend:" + result1.value8());
        }

        // this is not prone to NPE
        Sale result2 = ctx.selectFrom(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)
                .fetchAnyInto(Sale.class);

        System.out.println("Example 1.2.1\n" + result2);

        if (result2 != null) {
            System.out.println("Example 1.2.2\n"
                    + "Fiscal year: " + result1.value2() + " Trend:" + result1.value8());
        }

        // pay attention, since this is prone to NPE if the specified employee number doesn't exist
        Sale result3 = ctx.selectFrom(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)
                .fetchAny().into(Sale.class);

        System.out.println("Example 1.3\n" + result3);

        var result4 = ctx.select(SALE.TREND) // Record1<String>
                .from(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)
                .fetchAny();
        System.out.println("Example 1.4\n" + result4);

        // pay attention, since this is prone to NPE if the specified employee number doesn't exist
        String result5 = ctx.select(SALE.TREND)
                .from(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)
                .fetchAny().value1(); // or, into(String.class)
        System.out.println("Example 1.5\n" + result5);

        String result6 = ctx.select(SALE.TREND)
                .from(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)                
                .fetchAnyInto(String.class);
        System.out.println("Example 1.6\n" + result6);

        String result7 = Objects.requireNonNullElseGet(
                ctx.select(SALE.TREND)
                        .from(SALE)
                        .where(SALE.EMPLOYEE_NUMBER.eq(13700L)) // many or none (null)                
                        .fetchAnyInto(String.class), () -> "-");
        System.out.println("Example 1.7\n" + result7);

        Optional<String> result8 = Optional.ofNullable(
                ctx.select(SALE.TREND)
                        .from(SALE)
                        .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)                
                        .fetchAnyInto(String.class));
        System.out.println("Example 1.8\n" + result8);

        YearMonth result9 = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CREDIT_LIMIT.ge(BigDecimal.ZERO)) // many or none (null)
                .fetchAny(CUSTOMER.FIRST_BUY_DATE, INTEGER_YEARMONTH_CONVERTER);
        System.out.println("Example 1.9\n" + result9);

        SaleRecord result10 = ctx.fetchAny(SALE, SALE.EMPLOYEE_NUMBER.eq(1370L));
        System.out.println("Example 1.10 \n" + result10);

        Sale result11 = ctx.selectFrom(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)
                .fetchAnyInto(Sale.class);
        System.out.println("Example 1.11\n" + result11);

        Sale result12 = ctx.select(SALE.FISCAL_YEAR, SALE.TREND, SALE.VAT)
                .from(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)
                .fetchAnyInto(Sale.class);
        System.out.println("Example 1.12\n" + result12);

        SaleRecord result13 = ctx.selectFrom(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)
                .fetchAnyInto(SaleRecord.class); 
        System.out.println("Example 1.13\n" + result13);

        SaleRecord result14 = ctx.select(SALE.FISCAL_YEAR, SALE.TREND, SALE.VAT)
                .from(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)
                .fetchAnyInto(SALE);
        System.out.println("Example 1.14\n" + result14);

        SaleRecord result15 = ctx.selectFrom(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)
                .fetchAnyInto(SALE);
        System.out.println("Example 1.15\n" + result15);

        // Avoid fetching more data than needed
        String result16 = ctx.selectFrom(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)
                .fetchAny(SALE.TREND);
        System.out.println("Example 1.16 (avoid) \n" + result16);

        // Avoid fetching more data than needed
        String result17 = ctx.select(SALE.FISCAL_YEAR, SALE.TREND, SALE.VAT)
                .from(SALE)
                .where(SALE.EMPLOYEE_NUMBER.eq(1370L)) // many or none (null)
                .fetchAny(SALE.TREND);
        System.out.println("Example 1.17 (avoid) \n" + result17);
    }
}
