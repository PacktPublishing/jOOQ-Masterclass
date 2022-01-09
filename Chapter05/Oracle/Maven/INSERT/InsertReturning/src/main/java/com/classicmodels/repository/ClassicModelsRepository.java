package com.classicmodels.repository;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.default_;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.val;
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
    insert into CLASSICMODELS."SALE" (
      "FISCAL_YEAR", "SALE", "EMPLOYEE_NUMBER", 
      "REVENUE_GROWTH", "FISCAL_MONTH"
    ) 
    values 
      (?, ?, ?, ?, ?)    
    */
    public void returnOneId() {

        // Record1<Long>
         var insertedId = ctx.insertInto(SALE, 
                SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.REVENUE_GROWTH, SALE.FISCAL_MONTH)
                .values(2004, 2311.42, 1370L, 10.12, 1)
                .returningResult(SALE.SALE_ID) // or, returningResult() to return whole fields
                .fetchOne(); 


        System.out.println("EXAMPLE 1 (inserted id): \n" + insertedId); // as Long, insertedId.value1()
    }

    // EXAMPLE 2
    /*
    declare i0 DBMS_SQL.NUMBER_TABLE;
    i1 DBMS_SQL.NUMBER_TABLE;
    i2 DBMS_SQL.NUMBER_TABLE;
    i3 DBMS_SQL.NUMBER_TABLE;
    i4 DBMS_SQL.NUMBER_TABLE;
    o0 DBMS_SQL.NUMBER_TABLE;
    c0 sys_refcursor;
    begin i0(1) := ?;
    i0(2) := ?;
    i0(3) := ?;
    i1(1) := ?;
    i1(2) := ?;
    i1(3) := ?;
    i2(1) := ?;
    i2(2) := ?;
    i2(3) := ?;
    i3(1) := ?;
    i3(2) := ?;
    i3(3) := ?;
    i4(1) := ?;
    i4(2) := ?;
    i4(3) := ?;
    forall i in 1..i0.count insert into CLASSICMODELS."SALE" (
      "FISCAL_YEAR", "SALE", "EMPLOYEE_NUMBER", 
      "REVENUE_GROWTH", "FISCAL_MONTH"
    ) 
    values 
      (
        i0(i), 
        i1(i), 
        i2(i), 
        i3(i), 
        i4(i)
      ) returning "CLASSICMODELS"."SALE"."SALE_ID" bulk collect into o0;
    ? := sql % rowcount;
    open c0 for 
    select 
      * 
    from 
      table(o0);
    ? := c0;
    end;    
     */
    public void returnMultipleIds() {

        // Result<Record1<Long>>
         var insertedIds = ctx.insertInto(SALE, 
                SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.REVENUE_GROWTH, SALE.FISCAL_MONTH)
                .values(2004, 2311.42, 1370L, 12.50, 1)
                .values(2003, 900.21, 1504L, 23.99, 2)
                .values(2005, 1232.2, 1166L, 14.65, 3)
                .returningResult(SALE.SALE_ID) // or, returningResult() to return whole fields
                .fetch();


        System.out.println("EXAMPLE 2 (inserted ids): \n" + insertedIds);
    }

    // EXAMPLE 3
    /*
    insert into "CLASSICMODELS"."CUSTOMERDETAIL" (
      "CUSTOMER_NUMBER", "ADDRESS_LINE_FIRST", 
      "ADDRESS_LINE_SECOND", "CITY", "STATE", 
      "POSTAL_CODE", "COUNTRY"
    ) 
    values 
      (
        ?, ?, default, ?, default, default, ?
      )    
    */
    public void insertReturningOfCustomerInCustomerDetail() {

        // Note: passing explicit "null" instead of default_() produces implementation specific behaviour
        
        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(CUSTOMERDETAIL)
                        .values(ctx.insertInto(CUSTOMER)
                                .values(default_(),
                                        UUID.randomUUID().toString(), // random customer_name
                                        "Kyle", "Doyle", "+ 44 321 321", 
                                        default_(), default_(), default_())
                                .returningResult(CUSTOMER.CUSTOMER_NUMBER).fetchOne().value1(),
                                UUID.randomUUID().toString(), // random address_line_first
                                default_(), "Los Angeles", default_(), default_(), "USA")
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    insert into CLASSICMODELS."MANAGER" ("MANAGER_NAME") 
    values 
      (
        (
          select 
            (
              "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME" || ? || "CLASSICMODELS"."EMPLOYEE"."LAST_NAME"
            ) 
          from 
            "CLASSICMODELS"."EMPLOYEE" 
          where 
            "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = ?
        )
      )    
     */
    public void insertEmployeeInManagerReturningId() {

        // Result<Record1<Long>>
        var inserted = ctx.insertInto(MANAGER, MANAGER.MANAGER_NAME)
                .values(select(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1165L)).asField())
                .returningResult(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("EXAMPLE 4 (inserted ids): \n" + inserted);
    }

    // EXAMPLE 5
    /*
    insert into CLASSICMODELS."MANAGER" ("MANAGER_ID", "MANAGER_NAME") 
    values 
      (?, ?)    
     */
    public void insertNewManagerReturningId() {

        // Result<Record1<Long>>
        var inserted = ctx.insertInto(MANAGER, MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                .values(default_(MANAGER.MANAGER_ID), val("Karl Frum"))
                .returningResult(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("EXAMPLE 5 (inserted id): \n" + inserted);
    }

    // EXAMPLE 6
    /*
    declare i0 DBMS_SQL.VARCHAR2_TABLE;
    i1 DBMS_SQL.VARCHAR2_TABLE;
    i2 DBMS_SQL.NUMBER_TABLE;
    o0 DBMS_SQL.VARCHAR2_TABLE;
    o1 DBMS_SQL.DATE_TABLE;
    c0 sys_refcursor;
    c1 sys_refcursor;
    begin i0(1) := ?;
    i0(2) := ?;
    i1(1) := ?;
    i1(2) := ?;
    i2(1) := ?;
    i2(2) := ?;
    forall i in 1..i0.count insert into CLASSICMODELS."PRODUCTLINE" (
      "PRODUCT_LINE", "TEXT_DESCRIPTION", 
      "CODE"
    ) 
    values 
      (
        i0(i), 
        i1(i), 
        i2(i)
      ) returning "CLASSICMODELS"."PRODUCTLINE"."PRODUCT_LINE", 
      "CLASSICMODELS"."PRODUCTLINE"."CREATED_ON" bulk collect into o0, 
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
    public void insertAndReturnMultipleColsProductline() {
         
        // Result<Record2<String, LocalDate>>
        var inserted = ctx.insertInto(PRODUCTLINE, 
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CODE)
                .values(UUID.randomUUID().toString(), // random product_line
                        "This new line of electric vans ...", 983423L)
                .values(UUID.randomUUID().toString(), // random product_line 
                        "This new line of turbo N cars ...", 193384L)         
                .returningResult(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CREATED_ON)
                .fetch();

        System.out.println("EXAMPLE 6 (inserted product lines and created on): \n" + inserted);
    }   
    
    // EXAMPLE 7
    /*
    insert into CLASSICMODELS."DEPARTMENT" (
      "NAME", "PHONE", "CODE", "OFFICE_CODE"
    ) 
    values 
      (?, ?, ?, ?)    
    */
    public void insertReturningAndSerialInDepartment() {
        
        // Record1<Long>
        var inserted = ctx.insertInto(DEPARTMENT, DEPARTMENT.NAME, 
                DEPARTMENT.PHONE, DEPARTMENT.CODE, DEPARTMENT.OFFICE_CODE)
                .values("Marketing", "+2 311 312", 
                        ThreadLocalRandom.current().nextInt(10000, 20000), // random code
                        "5")                
                .returningResult(DEPARTMENT.DEPARTMENT_ID)
                .fetchOne();
        
        System.out.println("EXAMPLE 7 (inserted id): \n" + inserted);
    }
}