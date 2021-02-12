package com.classicmodels.repository;

import static jooq.generated.Sequences.CUSTOMER_SEQ;
import static jooq.generated.Sequences.MANAGER_SEQ;
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
    insert into "public"."sale" 
      ("fiscal_year", "sale", "employee_number")
    values
      (?, ?, ?) 
    returning "public"."sale"."sale_id"
     */
    public void returnOneId() {

        // Record1<Long>
        var insertedId = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                .values(2004, 2311.42, 1370L)
                .returningResult(SALE.SALE_ID)
                .fetchOne();

        System.out.println("EXAMPLE 1 (inserted id): \n" + insertedId); // as Long, insertedId.value1()
    }

    // EXAMPLE 2
    /*
    insert into "public"."sale" 
      ("fiscal_year", "sale", "employee_number")
    values
      (?, ?, ?),(?, ?, ?),(?, ?, ?) 
    returning "public"."sale"."sale_id"
     */
    public void returnMultipleIds() {

        // Result<Record1<Long>>
        var insertedIds = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                .values(2004, 2311.42, 1370L)
                .values(2003, 900.21, 1504L)
                .values(2005, 1232.2, 1166L)
                .returningResult(SALE.SALE_ID)
                .fetch();

        System.out.println("EXAMPLE 2 (inserted ids): \n" + insertedIds);
    }

    // EXAMPLE 3
    /*
    insert into "public"."customer" (
      "customer_name","contact_first_name","contact_last_name","phone")
    values
      (?, ?, ?, ?) 
    returning "public"."customer"."customer_number"
    
    insert into "public"."customerdetail" (
      "customer_number","address_line_first","address_line_second","city",
      "state","postal_code","country")
    values
      (?, ?, ?, ?, ?, ?, ?)
     */
    public void insertReturningOfCustomerInCustomerDetail() {

        ctx.insertInto(CUSTOMER,
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE)
                .values("Ltd. AirRoads", "Kyle", "Doyle", "+ 44 321 321")
                .execute();

        // passing explicit "null" instead of default_() produces implementation specific behaviour
        System.out.println("EXAMPLE 3.1 (affected rows): "
                + ctx.insertInto(CUSTOMERDETAIL)
                        .values(CUSTOMER_SEQ.currval(),
                                "No. 14 Avenue", null, "Los Angeles", null, null, "USA")
                        .execute());
        
        // passing explicit "null" instead of default_() produces implementation specific behaviour
        System.out.println("EXAMPLE 3.2 (affected rows): "
                + ctx.insertInto(CUSTOMERDETAIL)
                        .values(ctx.insertInto(CUSTOMER,
                                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE)
                                .values("Ltd. AirRoads", "Kyle", "Doyle", "+ 44 321 321")
                                .returningResult(CUSTOMER.CUSTOMER_NUMBER).fetchOne().value1(),
                                "No. 14 Avenue", null, "Los Angeles", null, null, "USA")
                        .execute()
        );       
    }

    // EXAMPLE 4
    /*
    insert into "public"."manager" 
      ("manager_name")
    values
      (
        (
          select
           (
             "public"."employee"."first_name" || ? || "public"."employee"."last_name"
           )
          from
             "public"."employee"
          where
             "public"."employee"."employee_number" = ?
        )
      ) returning "public"."manager"."manager_id"
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
    insert into "public"."manager" ("manager_id", "manager_name") 
    values 
      (
        nextval('"public"."manager_seq"'), 
        ?
      ) returning "public"."manager"."manager_id"   
     */
    public void insertNewManagerReturningId() {

        var inserted = ctx.insertInto(MANAGER, MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                .values(MANAGER_SEQ.nextval(), val("Karl Frum"))
                .returningResult(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("EXAMPLE 5 (inserted id): \n" + inserted);
    }

    // EXAMPLE 6
    /*
    insert into "public"."productline" (
      "product_line", "text_description", 
      "code"
    ) 
    values 
      (?, ?, ?), 
      (?, ?, ?) on conflict do nothing returning "public"."productline"."product_line", 
      "public"."productline"."created_on"   
     */
    public void insertAndReturnMultipleColsProductline() {

        // Result<Record3<String, LocalDate, Long>>
        var inserted = ctx.insertInto(PRODUCTLINE, 
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CODE)
                .values("Electric Vans", "This new line of electric vans ...", 983423L)
                .values("Turbo N Cars", "This new line of turbo N cars ...", 193384L)
                .onDuplicateKeyIgnore()
                .returningResult(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CREATED_ON)
                .fetch();

        System.out.println("EXAMPLE 6 (inserted ids and employee numbers): \n" + inserted);
    }

    // EXAMPLE 7
    /*
    insert into "public"."productline" (
      "product_line", "text_description", 
      "code"
    ) 
    values 
      (?, ?, ?), 
      (?, ?, ?) on conflict do nothing returning "public"."productline"."product_line", 
      "public"."productline"."code", 
      "public"."productline"."text_description", 
      "public"."productline"."html_description", 
      "public"."productline"."image", 
      "public"."productline"."created_on"    
     */
    public void insertAndReturnAllColsProductline() {

        // Result<Record2<String, LocalDate>>
        var inserted = ctx.insertInto(PRODUCTLINE, 
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CODE)
                .values("Master Vans", "This new line of master vans ...", 983423L)
                .values("Cool Cars", "This new line of cool cars ...", 193384L)
                .onDuplicateKeyIgnore()
                .returning()
                .fetch();

        System.out.println("EXAMPLE 7 (inserted ids and employee numbers): \n" + inserted);
    }
    
    // EXAMPLE 8
    /*
    insert into "public"."department" 
      ("name", "phone", "code", "office_code")
    values
      (?, ?, ?, ?) 
    returning 
      "public"."department"."department_id"
    */
    public void insertReturningAndSerialInDepartment() {
        
        // Record1<Integer>, DEPARTMENT_DEPARTMENT_ID_SEQ - this is the sequence created automatically 
        var inserted = ctx.insertInto(DEPARTMENT, DEPARTMENT.NAME, 
                DEPARTMENT.PHONE, DEPARTMENT.CODE, DEPARTMENT.OFFICE_CODE)
                .values("Marketing", "+2 311 312", Short.valueOf("5432"), "5")
                .returningResult(DEPARTMENT.DEPARTMENT_ID)
                .fetchOne();
        
        System.out.println("EXAMPLE 8 (inserted id): \n" + inserted);
    }
}
