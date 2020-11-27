package com.classicmodels.repository;

import com.classicmodels.pojo.OfficePart;
import java.math.BigDecimal;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.OfficeRecord;
import org.jooq.DSLContext;
import org.jooq.UpdateQuery;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.avg;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    /*
    update 
      "SYSTEM"."OFFICE" 
    set 
      "SYSTEM"."OFFICE"."CITY" = ?, 
      "SYSTEM"."OFFICE"."COUNTRY" = ? 
    where 
      "SYSTEM"."OFFICE"."OFFICE_CODE" = ?    
     */
    public void updateOffice() {

        System.out.println("EXAMPLE 1.1 (affected rows): "
                + ctx.update(OFFICE)
                        .set(OFFICE.CITY, "Banesti")
                        .set(OFFICE.COUNTRY, "Romania")
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .execute()
        );
        
        UpdateQuery uq = ctx.updateQuery(OFFICE);
        uq.addValue(OFFICE.CITY, "Craiova");
        uq.addValue(OFFICE.COUNTRY, "Romania");
        uq.addConditions(OFFICE.OFFICE_CODE.eq("1"));
        // uq.execute();
        System.out.println("EXAMPLE 1.2 (query): " + uq.getSQL());
    }

    // EXAMPLE 2
    /*
    update 
      "SYSTEM"."OFFICE" 
    set 
      ("CITY", "COUNTRY") = (
        select 
          ?, 
          ? 
        from 
          dual
      ) 
    where 
      "SYSTEM"."OFFICE"."OFFICE_CODE" = ?   
     */
    public void updateRowOffice() {

        System.out.println("EXAMPLE 2 (affected rows): "
                + ctx.update(OFFICE)
                        .set(row(OFFICE.CITY, OFFICE.COUNTRY),
                                row("Hamburg", "Germany"))
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    update 
      "SYSTEM"."CUSTOMER" 
    set 
      "SYSTEM"."CUSTOMER"."CREDIT_LIMIT" = (
        select 
          max(
            "SYSTEM"."PAYMENT"."INVOICE_AMOUNT"
          ) 
        from 
          "SYSTEM"."PAYMENT" 
        where 
          "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER" = "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER"
      ) 
    where 
      "SYSTEM"."CUSTOMER"."CREDIT_LIMIT" > ?   
     */
    public void updateCustomerCreditLimitAsMaxPaymentInvoice() {

        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.update(CUSTOMER)
                        .set(CUSTOMER.CREDIT_LIMIT,
                                select(max(PAYMENT.INVOICE_AMOUNT)).from(PAYMENT)
                                        .where(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)))
                        .where(CUSTOMER.CREDIT_LIMIT.gt(BigDecimal.ZERO))
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    update 
      "SYSTEM"."EMPLOYEE" 
    set 
      "SYSTEM"."EMPLOYEE"."SALARY" = (
        "SYSTEM"."EMPLOYEE"."SALARY" + (
          select 
            (
              count("SYSTEM"."SALE"."SALE") * ?
            ) 
          from 
            "SYSTEM"."SALE" 
          where 
            "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "SYSTEM"."SALE"."EMPLOYEE_NUMBER"
        )
      )    
     */
    public void updateEmployeeSalaryBySaleCount() {

        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(
                                select(count(SALE.SALE_).multiply(5.75)).from(SALE)
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)).asField()))
                        .execute()
        );
    }

    // EXAMPLE 5    
    public void updateNewRecordOffice() {

        /* approach 1 */
        OfficeRecord or = new OfficeRecord();
        or.setCity("Constanta");
        or.setCountry("Romania");

        /*
        update 
          "SYSTEM"."OFFICE" 
        set 
          "SYSTEM"."OFFICE"."CITY" = ?, 
          "SYSTEM"."OFFICE"."COUNTRY" = ? 
        where 
          "SYSTEM"."OFFICE"."OFFICE_CODE" = ?       
         */
        System.out.println("EXAMPLE 5.1 (affected rows): "
                + ctx.update(OFFICE)
                        .set(or)
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .execute()
        );

        /* approach 2 */
        /*
        update 
          "SYSTEM"."OFFICE" 
        set 
          "SYSTEM"."OFFICE"."OFFICE_CODE" = ?, 
          "SYSTEM"."OFFICE"."CITY" = ?, 
          "SYSTEM"."OFFICE"."COUNTRY" = ? 
        where 
          "SYSTEM"."OFFICE"."OFFICE_CODE" = ?        
         */
        System.out.println("EXAMPLE 5.2 (affected rows): "
                + ctx.newRecord(OFFICE)
                        .value1("1") // the ID is present in the WHERE clause
                        .value2("Parma")
                        .value7("Italy")
                        .update()
        );

        /* approach 3 */
        OfficePart op = new OfficePart("Madrid", "Spain");
        OfficeRecord orFromOp = new OfficeRecord();

        orFromOp.from(op);

        /*
        update 
          "SYSTEM"."OFFICE" 
        set 
          "SYSTEM"."OFFICE"."CITY" = ?, 
          "SYSTEM"."OFFICE"."COUNTRY" = ? 
        where 
          "SYSTEM"."OFFICE"."OFFICE_CODE" = ?        
         */
        System.out.println("EXAMPLE 5.3 (affected rows): "
                + ctx.update(OFFICE)
                        .set(orFromOp)
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .execute()
        );
    }

    // EXAMPLE 6
    /*
    update 
      "SYSTEM"."OFFICE" 
    set 
      (
        "ADDRESS_LINE_FIRST", "ADDRESS_LINE_SECOND", 
        "PHONE"
      ) = (
        select 
          "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
          "SYSTEM"."EMPLOYEE"."LAST_NAME", 
          ? 
        from 
          "SYSTEM"."EMPLOYEE" 
        where 
          "SYSTEM"."EMPLOYEE"."JOB_TITLE" = ?
      )    
     */
    public void updateOfficeAddressAsPresidentName() {

        System.out.println("EXAMPLE 6 (affected rows): "
                + ctx.update(OFFICE)
                        .set(row(OFFICE.ADDRESS_LINE_FIRST, OFFICE.ADDRESS_LINE_SECOND, OFFICE.PHONE),
                                select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, val("+40 0721 456 322"))
                                        .from(EMPLOYEE)
                                        .where(EMPLOYEE.JOB_TITLE.eq("President")))
                        .execute()
        );
    }  

    // EXAMPLE 7
    /*
    declare o0 dbms_sql.varchar2_table;
    o1 dbms_sql.varchar2_table;
    c0 sys_refcursor;
    c1 sys_refcursor;
    begin 
    update 
      "SYSTEM"."OFFICE" 
    set 
      "SYSTEM"."OFFICE"."CITY" = ?, 
      "SYSTEM"."OFFICE"."COUNTRY" = ? 
    where 
      "SYSTEM"."OFFICE"."OFFICE_CODE" = ? returning "SYSTEM"."OFFICE"."CITY", 
      "SYSTEM"."OFFICE"."COUNTRY" bulk collect into o0, 
      o1;
    ? := sql % rowcount;
    open c0 for 
    select 
      * 
    from 
      table(o0);
    open c1 for 
    select 
      * 
    from 
      table(o1);
    ? := c0;
    ? := c1;
    end;   
     */
    public void updateOfficeReturning() {

        System.out.println("EXAMPLE 7: \n"
                + ctx.update(OFFICE)
                        .set(OFFICE.CITY, "Paris")
                        .set(OFFICE.COUNTRY, "France")
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .returningResult(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetchOne()
        );
    }

    // EXAMPLE 8
    /*
    declare o0 dbms_sql.number_table;
    c0 sys_refcursor;
    begin 
    update 
      "SYSTEM"."EMPLOYEE" 
    set 
      "SYSTEM"."EMPLOYEE"."SALARY" = (
        "SYSTEM"."EMPLOYEE"."SALARY" + (
          select 
            avg("SYSTEM"."SALE"."SALE") 
          from 
            "SYSTEM"."SALE" 
          where 
            "SYSTEM"."SALE"."EMPLOYEE_NUMBER" = "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER"
        )
      ) 
    where 
      "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = ? returning "SYSTEM"."EMPLOYEE"."SALARY" bulk collect into o0;
    ? := sql % rowcount;
    open c0 for 
    select 
      * 
    from 
      table(o0);
    ? := c0;
    end;
    
    update 
      "SYSTEM"."CUSTOMER" 
    set 
      "SYSTEM"."CUSTOMER"."CREDIT_LIMIT" = (
        "SYSTEM"."CUSTOMER"."CREDIT_LIMIT" + ?
      ) 
    where 
      "SYSTEM"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER" = ?   
     */
    public void updateEmployeeSalaryAsAvgSaleAndCustomersCreditAsDoubleSalary() {

        System.out.println("EXAMPLE 8 (affected rows): "
                + ctx.update(CUSTOMER)
                        .set(CUSTOMER.CREDIT_LIMIT, CUSTOMER.CREDIT_LIMIT.plus(
                                ctx.update(EMPLOYEE)
                                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(
                                                select(avg(SALE.SALE_)).from(SALE)
                                                        .where(SALE.EMPLOYEE_NUMBER
                                                                .eq(EMPLOYEE.EMPLOYEE_NUMBER)).asField()))
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1504L))
                                        .returningResult(EMPLOYEE.SALARY.coerce(BigDecimal.class))
                                        .fetchOne().value1().multiply(BigDecimal.valueOf(2))))
                        .where(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER.eq(1504L))
                        .execute()
        );
    }
}