package com.classicmodels.repository;

import java.math.BigInteger;
import static jooq.generated.Sequences.CUSTOMER_SEQ;
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
import static org.jooq.impl.DSL.defaultValue;
import static org.jooq.impl.DSL.default_;
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
    insert into SYSTEM."SALE" (
      "FISCAL_YEAR", "SALE", "EMPLOYEE_NUMBER"
    ) 
    values 
      (?, ?, ?)
    */
    public void returnOneId() {

        // Record1<Long>
        var insertedId = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                .values(BigInteger.valueOf(2004), 2311.42, 1370L)
                .returningResult(SALE.SALE_ID)
                .fetchOne();

        System.out.println("EXAMPLE 1 (inserted id): \n" + insertedId); // as Long, insertedId.value1()
    }

    // EXAMPLE 2
    /*
    declare i0 dbms_sql.number_table;
    i1 dbms_sql.number_table;
    i2 dbms_sql.number_table;
    o0 dbms_sql.number_table;
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
    forall i in 1..i0.count insert into SYSTEM."SALE" (
      "FISCAL_YEAR", "SALE", "EMPLOYEE_NUMBER"
    ) 
    values 
      (
        i0(i), 
        i1(i), 
        i2(i)
      ) returning "SYSTEM"."SALE"."SALE_ID" bulk collect into o0;
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
        var insertedIds = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                .values(BigInteger.valueOf(2004), 2311.42, 1370L)
                .values(BigInteger.valueOf(2003), 900.21, 1504L)
                .values(BigInteger.valueOf(2005), 1232.2, 1166L)
                .returningResult(SALE.SALE_ID)
                .fetch();

        System.out.println("EXAMPLE 2 (inserted ids): \n" + insertedIds);
    }

    // EXAMPLE 3
    public void insertReturningOfCustomerInCustomerDetail() {

        /*
        insert into "SYSTEM"."CUSTOMER" (
          "CUSTOMER_NAME", "CONTACT_FIRST_NAME", 
          "CONTACT_LAST_NAME", "PHONE"
        ) 
        values 
          (?, ?, ?, ?)        
        */
        ctx.insertInto(CUSTOMER,
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE)
                .values("Ltd. AirRoads", "Kyle", "Doyle", "+ 44 321 321")
                .execute();

        /*
        insert into "SYSTEM"."CUSTOMERDETAIL" (
          "CUSTOMER_NUMBER", "ADDRESS_LINE_FIRST", 
          "ADDRESS_LINE_SECOND", "CITY", "STATE", 
          "POSTAL_CODE", "COUNTRY"
        ) 
        values 
          (
            "SYSTEM"."CUSTOMER_SEQ".currval, 
            ?, ?, ?, ?, ?, ?
          )        
        */
        System.out.println("EXAMPLE 3.1 (affected rows): "
                + ctx.insertInto(CUSTOMERDETAIL)
                        .values(CUSTOMER_SEQ.currval(),
                                "No. 14 Avenue", default_(), "Los Angeles", default_(), default_(), "USA")
                        .execute());
        
        /*
        insert into "SYSTEM"."CUSTOMER" (
          "CUSTOMER_NAME", "CONTACT_FIRST_NAME", 
          "CONTACT_LAST_NAME", "PHONE"
        ) 
        values 
          (?, ?, ?, ?)        
        
        insert into "SYSTEM"."CUSTOMERDETAIL" (
          "CUSTOMER_NUMBER", "ADDRESS_LINE_FIRST", 
          "ADDRESS_LINE_SECOND", "CITY", "STATE", 
          "POSTAL_CODE", "COUNTRY"
        ) 
        values 
          (?, ?, ?, ?, ?, ?, ?)        
        */
        System.out.println("EXAMPLE 3.2 (affected rows): "
                + ctx.insertInto(CUSTOMERDETAIL)
                        .values(ctx.insertInto(CUSTOMER,
                                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE)
                                .values("Ltd. AirRoads", "Kyle", "Doyle", "+ 44 321 321")
                                .returningResult(CUSTOMER.CUSTOMER_NUMBER).fetchOne().value1(),
                                "No. 14 Avenue", default_(), "Los Angeles", default_(), default_(), "USA")
                        .execute()
        );       
    }

    // EXAMPLE 4
    /*
    insert into SYSTEM."MANAGER" ("MANAGER_NAME") 
    values 
      (
        (
          select 
            (
              "SYSTEM"."EMPLOYEE"."FIRST_NAME" || ? || "SYSTEM"."EMPLOYEE"."LAST_NAME"
            ) 
          from 
            "SYSTEM"."EMPLOYEE" 
          where 
            "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = ?
        )
      )    
     */
    public void insertEmployeeInManagerReturningId() {

        var inserted = ctx.insertInto(MANAGER, MANAGER.MANAGER_NAME)
                .values(select(concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1165L)).asField())
                .returningResult(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("EXAMPLE 4 (inserted ids): \n" + inserted);
    }

    // EXAMPLE 5
    /*
    insert into SYSTEM."MANAGER" ("MANAGER_ID", "MANAGER_NAME") 
    values 
      (?, ?)    
     */
    public void insertNewManagerReturningId() {

        var inserted = ctx.insertInto(MANAGER, MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                .values(default_(MANAGER.MANAGER_ID), val("Karl Frum"))
                .returningResult(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("EXAMPLE 5 (inserted id): \n" + inserted);
    }

    // EXAMPLE 6
    /*
    declare i0 dbms_sql.varchar2_table;
    i1 dbms_sql.varchar2_table;
    o0 dbms_sql.varchar2_table;
    o1 dbms_sql.date_table;
    c0 sys_refcursor;
    c1 sys_refcursor;
    begin i0(1) := ?;
    i0(2) := ?;
    i1(1) := ?;
    i1(2) := ?;
    forall i in 1..i0.count insert into SYSTEM."PRODUCTLINE" (
      "PRODUCT_LINE", "TEXT_DESCRIPTION"
    ) 
    values 
      (
        i0(i), 
        i1(i)
      ) returning "SYSTEM"."PRODUCTLINE"."PRODUCT_LINE", 
      "SYSTEM"."PRODUCTLINE"."CREATED_ON" bulk collect into o0, 
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

        // delete if exists
        ctx.deleteFrom(PRODUCTLINE).where(PRODUCTLINE.PRODUCT_LINE.eq("Electric Vans")).execute();
        ctx.deleteFrom(PRODUCTLINE).where(PRODUCTLINE.PRODUCT_LINE.eq("Turbo N Cars")).execute();
        
        // Result<Record2<String, LocalDate>>
        var inserted = ctx.insertInto(PRODUCTLINE, PRODUCTLINE.PRODUCT_LINE, 
                PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CODE)
                .values("Electric Vans", "This new line of electric vans ...", 983423L)
                .values("Turbo N Cars", "This new line of turbo N cars ...", 193384L)                
                .returningResult(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CREATED_ON)
                .fetch();

        System.out.println("EXAMPLE 6 (inserted ids and employee numbers): \n" + inserted);
    }

    // EXAMPLE 7
    /*
    insert into SYSTEM."DEPARTMENT" (
      "NAME", "PHONE", "CODE", "OFFICE_CODE"
    ) 
    values 
      (?, ?, ?, ?)    
    */
    public void insertReturningAndSerialInDepartment() {
        
        // Record1<Integer>, DEPARTMENT_DEPARTMENT_ID_SEQ - this is the sequence created automatically 
        var inserted = ctx.insertInto(DEPARTMENT, DEPARTMENT.NAME, 
                DEPARTMENT.PHONE, DEPARTMENT.CODE, DEPARTMENT.OFFICE_CODE)
                .values("Marketing", "+2 311 312", 5432, "5")
                .returningResult(DEPARTMENT.DEPARTMENT_ID)
                .fetchOne();
        
        System.out.println("EXAMPLE 7 (inserted id): \n" + inserted);
    }
}