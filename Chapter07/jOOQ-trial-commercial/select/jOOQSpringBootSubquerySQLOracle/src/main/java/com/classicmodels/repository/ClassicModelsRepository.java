package com.classicmodels.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import static jooq.generated.tables.Customer.CUSTOMER;
import jooq.generated.tables.Employee;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Name;
import org.jooq.SelectQuery;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.primaryKey;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.VARCHAR;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    /*
    select       
      "SYSTEM"."EMPLOYEE"."LAST_NAME", 
      "SYSTEM"."EMPLOYEE"."FIRST_NAME"
    from 
      "SYSTEM"."EMPLOYEE" 
    where 
      "SYSTEM"."EMPLOYEE"."OFFICE_CODE" in (
        select 
          "SYSTEM"."OFFICE"."OFFICE_CODE" 
        from 
          "SYSTEM"."OFFICE" 
        where 
          "SYSTEM"."OFFICE"."CITY" like ?
      )
     */
    public void findlEmployeeInOfficeStartingS() {

        System.out.println("EXAMPLE 1.1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.OFFICE_CODE.in(
                                select(OFFICE.OFFICE_CODE).from(OFFICE).where(OFFICE.CITY.like("S%"))))
                        .fetch()
        );

        // same query expressed via SelectQuery API
        SelectQuery sqInner = ctx.selectQuery();
        sqInner.addSelect(OFFICE.OFFICE_CODE);
        sqInner.addFrom(OFFICE);
        sqInner.addConditions(OFFICE.CITY.like("S%"));

        SelectQuery sqOuter = ctx.selectQuery();   
        sqOuter.addSelect(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME);
        sqOuter.addFrom(EMPLOYEE);        
        sqOuter.addConditions(EMPLOYEE.OFFICE_CODE.in(sqInner));

        System.out.println("EXAMPLE 1.2\n"
                + sqOuter.fetch()
        );
    }

    // EXAMPLE 2
    /*
    select 
      count(*), 
      "SYSTEM"."OFFICE"."OFFICE_CODE", 
      "SYSTEM"."OFFICE"."STATE" 
    from 
      "SYSTEM"."EMPLOYEE" 
      join "SYSTEM"."OFFICE" on "SYSTEM"."EMPLOYEE"."OFFICE_CODE" = "SYSTEM"."OFFICE"."OFFICE_CODE" 
    group by 
      "SYSTEM"."OFFICE"."OFFICE_CODE", 
      "SYSTEM"."OFFICE"."STATE" 
    having 
      "SYSTEM"."OFFICE"."OFFICE_CODE" in (
        select 
          "SYSTEM"."OFFICE"."OFFICE_CODE" 
        from 
          "SYSTEM"."OFFICE" 
        where 
          "SYSTEM"."OFFICE"."STATE" <> ?
      )    
     */
    public void findEmployeeInOfficeNotMA() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(count(), OFFICE.OFFICE_CODE, OFFICE.STATE)
                        .from(EMPLOYEE)
                        .join(OFFICE)
                        .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .groupBy(OFFICE.OFFICE_CODE, OFFICE.STATE)
                        .having(OFFICE.OFFICE_CODE.in(
                                select(OFFICE.OFFICE_CODE).from(OFFICE).where(OFFICE.STATE.ne("MA"))))
                        .fetch()
        );
    }

    // EXAMPLE 3
    /*
    select 
      "SYSTEM"."SALE"."SALE_ID", 
      "SYSTEM"."SALE"."SALE" 
    from 
      "SYSTEM"."SALE", 
      (
        select 
          avg("SYSTEM"."SALE"."SALE") "avgs", 
          "SYSTEM"."SALE"."EMPLOYEE_NUMBER" "sen" 
        from 
          "SYSTEM"."SALE" 
        group by 
          "SYSTEM"."SALE"."EMPLOYEE_NUMBER"
      ) "saleTable" 
    where 
      (
        "SYSTEM"."SALE"."EMPLOYEE_NUMBER" = "saleTable"."sen" 
        and "SYSTEM"."SALE"."SALE" < "saleTable"."avgs"
      )    
     */
    public void findSaleLtAvg() {

        // Table<Record2<BigDecimal, Long>>
        var saleTable = select(avg(SALE.SALE_).as("avgs"), SALE.EMPLOYEE_NUMBER.as("sen"))
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER)
                .asTable("saleTable"); // derived table
        
        // or, like this (consider this alternative for every usage of asTable())
        /*
        var saleTable = table(select(avg(SALE.SALE_).as("avgs"), SALE.EMPLOYEE_NUMBER.as("sen"))
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER))
                .as("saleTable"); // derived table
        */

        System.out.println("EXAMPLE 3\n"
                + ctx.select(SALE.SALE_ID, SALE.SALE_)
                        .from(SALE, saleTable)
                        .where(SALE.EMPLOYEE_NUMBER.eq(saleTable.field("sen").coerce(Long.class))
                                .and(SALE.SALE_.lt(saleTable.field("avgs").coerce(Double.class))))
                        .fetch()
        );

        /* same query with fields extracted as local variables
        Field<BigDecimal> avgs = avg(SALE.SALE_).as("avgs");
        Field<Long> sen = SALE.EMPLOYEE_NUMBER.as("sen");

        // Table<Record2<BigDecimal, Long>>
        var saleTable = select(avgs, sen)
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER)
                .asTable("saleTable"); // derived table

        System.out.println("EXAMPLE 3\n"
                + ctx.select(SALE.SALE_ID, SALE.SALE_)
                        .from(SALE, saleTable)
                        .where(SALE.EMPLOYEE_NUMBER.eq(sen)
                                .and(SALE.SALE_.lt(avgs.coerce(Double.class))))
                        .fetch()
        );
         */
        
        /* same query with fields extracted as local variables and no derived table
        Field<BigDecimal> avgs = avg(SALE.SALE_).as("avgs");
        Field<Long> sen = SALE.EMPLOYEE_NUMBER.as("sen");

        System.out.println("EXAMPLE 3\n"
                + ctx.select(SALE.SALE_ID, SALE.SALE_)
                        .from(SALE, select(avgs, sen)
                                .from(SALE)
                                .groupBy(SALE.EMPLOYEE_NUMBER))
                        .where(SALE.EMPLOYEE_NUMBER.eq(sen)
                                .and(SALE.SALE_.lt(avgs.coerce(Double.class))))
                        .fetch()
        );
         */
    }

    // EXAMPLE 4
    /*    
    select 
      "e1"."FIRST_NAME", 
      "e1"."LAST_NAME", 
      "e1"."OFFICE_CODE" 
    from 
      "SYSTEM"."EMPLOYEE" "e1", 
      (
        select 
          avg("e2"."SALARY") "avgsal", 
          "e2"."OFFICE_CODE" 
        from 
          "SYSTEM"."EMPLOYEE" "e2" 
        group by 
          "e2"."OFFICE_CODE"
      ) "e3" 
    where 
      (
        "e1"."OFFICE_CODE" = ? 
        and "e1"."SALARY" >= "e3"."avgsal"
      )    
     */
    public void findEmployeesWithSalaryGeAvgPerOffice() {

        Employee e1 = EMPLOYEE.as("e1");
        Employee e2 = EMPLOYEE.as("e2");

        // Table<?>
        var e3 = select(avg(e2.SALARY).as("avgsal"), e2.OFFICE_CODE)
                .from(e2)
                .groupBy(e2.OFFICE_CODE)
                .asTable("e3");

        System.out.println("EXAMPLE 4\n"
                + ctx.select(e1.FIRST_NAME, e1.LAST_NAME, e1.OFFICE_CODE).from(e1, e3)
                        .where(e1.OFFICE_CODE.eq(e3.field("office_code", String.class))
                                .and(e1.SALARY.ge(e3.field("avgsal", BigInteger.class))))
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    select 
      "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER", 
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."LAST_NAME", 
      "saleTable"."ss" 
    from 
      (
        select 
          "SYSTEM"."SALE"."EMPLOYEE_NUMBER" "sen", 
          "SYSTEM"."SALE"."SALE" "ss" 
        from 
          "SYSTEM"."SALE"
      ) "saleTable", 
      "SYSTEM"."EMPLOYEE" 
    where 
      "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "saleTable"."sen"    
     */
    public void findEmployeeAndSale() {

        // Table<?>
        var saleTable = select(SALE.EMPLOYEE_NUMBER.as("sen"), SALE.SALE_.as("ss"))
                .from(SALE)
                .asTable("saleTable");

        System.out.println("EXAMPLE 5\n"
                + ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME,
                        EMPLOYEE.LAST_NAME, saleTable.field("ss"))
                        .from(saleTable, EMPLOYEE)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER
                                .eq(saleTable.field("sen").coerce(Long.class)))
                        .fetch()
        );
    }

    // EXAMPLE 6
    /*
    select 
      "saleTable"."sen", 
      "saleTable"."ss" 
    from 
      (
        select 
          "SYSTEM"."SALE"."EMPLOYEE_NUMBER" "sen", 
          "SYSTEM"."SALE"."SALE" "ss" 
        from 
          "SYSTEM"."SALE"
      ) "saleTable" 
    order by 
      "saleTable"."ss"    
     */
    public void findSale() {

        // Table<?>
        var saleTable = select(SALE.EMPLOYEE_NUMBER.as("sen"), SALE.SALE_.as("ss"))
                .from(SALE)
                .asTable("saleTable");

        System.out.println("EXAMPLE 6\n"
                + ctx.select(saleTable.fields())
                        .from(saleTable)
                        .orderBy(saleTable.field("ss"))
                        .fetch()
        );
    }

    // EXAMPLE 7
    /*
    select 
      "saleTable"."sen", 
      "saleTable"."sales", 
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."LAST_NAME" 
    from 
      (
        select 
          "SYSTEM"."SALE"."EMPLOYEE_NUMBER" "sen", 
          count(*) "sales" 
        from 
          "SYSTEM"."SALE" 
        group by 
          "SYSTEM"."SALE"."EMPLOYEE_NUMBER"
      ) "saleTable" 
      join "SYSTEM"."EMPLOYEE" on "saleTable"."sen" = "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" 
    order by 
      "saleTable"."sales" desc   
     */
    public void employeesAndNumberOfSales() {

        // Table<?>
        var salesTable = select(SALE.EMPLOYEE_NUMBER.as("sen"), count().as("sales"))
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER).asTable("saleTable");

        System.out.println("EXAMPLE 7\n"
                + ctx.select(salesTable.field("sen"), salesTable.field("sales"),
                        EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(salesTable)
                        .innerJoin(EMPLOYEE).on(salesTable.field("sen").coerce(Long.class)
                        .eq(EMPLOYEE.EMPLOYEE_NUMBER))
                        .orderBy(salesTable.field("sales").desc())
                        .fetch()
        );
    }

    // EXAMPLE 8    
    /*
    select 
      "SYSTEM"."SALE"."SALE_ID", 
      "SYSTEM"."SALE"."SALE" 
    from 
      "SYSTEM"."SALE", 
      (
        select 
          "saleTable"."avgs", 
          "saleTable"."sen" 
        from 
          (
            select 
              avg("SYSTEM"."SALE"."SALE") "avgs", 
              "SYSTEM"."SALE"."EMPLOYEE_NUMBER" "sen" 
            from 
              "SYSTEM"."SALE" 
            group by 
              "SYSTEM"."SALE"."EMPLOYEE_NUMBER"
          ) "saleTable" 
        where 
          "saleTable"."avgs" > (
            select 
              avg("SYSTEM"."SALE"."SALE") 
            from 
              "SYSTEM"."SALE"
          )
      ) "saleTable2" 
    where 
      (
        "SYSTEM"."SALE"."EMPLOYEE_NUMBER" = "saleTable2"."sen" 
        and "SYSTEM"."SALE"."SALE" < "saleTable2"."avgs"
      )    
     */
    public void findSaleLtAvgAvg() {

        // Table<?>
        var saleTable = select(avg(SALE.SALE_).as("avgs"), SALE.EMPLOYEE_NUMBER.as("sen"))
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER)
                .asTable("saleTable");

        // Table<?>
        var saleTable2 = select()
                .from(saleTable)
                .where(saleTable.field("avgs").coerce(BigDecimal.class)
                        .gt(select(avg(SALE.SALE_)).from(SALE)))
                .asTable("saleTable2");

        System.out.println("EXAMPLE 8\n"
                + ctx.select(SALE.SALE_ID, SALE.SALE_)
                        .from(SALE, saleTable2)
                        .where(SALE.EMPLOYEE_NUMBER.eq(saleTable2.field("sen").coerce(Long.class))
                                .and(SALE.SALE_.lt(saleTable2.field("avgs").coerce(Double.class))))
                        .fetch()
        );
    }

    // EXAMPLE 9
    /*
    create view "payment_view" as 
    select 
      "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER", 
      "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
      "SYSTEM"."PAYMENT"."PAYMENT_DATE", 
      "SYSTEM"."PAYMENT"."INVOICE_AMOUNT", 
      "SYSTEM"."PAYMENT"."CACHING_DATE" 
    from 
      "SYSTEM"."PAYMENT" 
    where 
      "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER" = (
        select 
          "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER" 
        from 
          "SYSTEM"."CUSTOMER" 
        where 
          "SYSTEM"."CUSTOMER"."CUSTOMER_NAME" = 'Signal Gift Stores'
      )   
    
    select * from "payment_view"
     */
    @Transactional
    public void findPaymentForCustomerSignalGiftStores() {

        ctx.createView(name("payment_view")).as(ctx.selectFrom(PAYMENT)
                .where(PAYMENT.CUSTOMER_NUMBER.eq(
                        select(CUSTOMER.CUSTOMER_NUMBER).from(CUSTOMER)
                                .where(CUSTOMER.CUSTOMER_NAME.eq("Signal Gift Stores"))
                ))).execute();

        System.out.println("EXAMPLE 9\n"
                + ctx.selectFrom(table(name("payment_view")))
                        .fetch()
        );

        // clean up
        ctx.dropView(name("payment_view")).execute();
    }

    // EXAMPLE 10
    /*
    insert into "SYSTEM"."ORDER" (
      "COMMENTS", "CUSTOMER_NUMBER", "ORDER_DATE", 
      "REQUIRED_DATE", "SHIPPED_DATE", 
      "STATUS"
    ) 
    select 
      "SYSTEM"."ORDER"."COMMENTS", 
      "SYSTEM"."ORDER"."CUSTOMER_NUMBER", 
      "SYSTEM"."ORDER"."ORDER_DATE", 
      "SYSTEM"."ORDER"."REQUIRED_DATE", 
      "SYSTEM"."ORDER"."SHIPPED_DATE", 
      "SYSTEM"."ORDER"."STATUS" 
    from 
      "SYSTEM"."ORDER" fetch next ? rows only    
     */
    @Transactional
    public void insertIntoOrder() {

        System.out.println("EXAMPLE 10 (rows affected):"
                + ctx.insertInto(ORDER, ORDER.COMMENTS, ORDER.CUSTOMER_NUMBER,
                        ORDER.ORDER_DATE, ORDER.REQUIRED_DATE, ORDER.SHIPPED_DATE, ORDER.STATUS)
                        .select(select(ORDER.COMMENTS, ORDER.CUSTOMER_NUMBER,
                                ORDER.ORDER_DATE, ORDER.REQUIRED_DATE, ORDER.SHIPPED_DATE, ORDER.STATUS)
                                .from(ORDER)
                                .limit(5)
                        ).execute()
        );
    }

    // EXAMPLE 11
    /*
    insert into "SYSTEM"."MANAGER" ("MANAGER_ID", "MANAGER_NAME") 
    select 
      * 
    from 
      "manager_temporary"    
     */
    @Transactional
    public void insertAnotherTableInManager() {

        // create a temporary table identical to MANAGER         
        Name tn = name("manager_temporary");
        Name mi = name("manager_id");
        Name mn = name("manager_name");
        
        ctx.createTemporaryTable(tn)
                .column(mi, BIGINT.nullable(false))
                .column(mn, VARCHAR(50).nullable(false))
                .constraints(
                        primaryKey(mi)
                )
                .execute();

        // insert some data into the temporary table 
        ctx.insertInto(table(tn))
                .values(Math.random() * 100000, "John Malon")
                .execute();

        // insert into MANAGER the data from the temporary table via SELECT 
        System.out.println("EXAMPLE 11 (rows affected):"
                + ctx.insertInto(MANAGER)
                        .select(select(table(tn).field(mi, Long.class),
                                table(tn).field(mn, String.class))
                                .from(table(tn)))                        
                        .execute()        
        );
        
        ctx.dropTemporaryTableIfExists(tn)
                .execute();
    }

    // EXAMPLE 12
    /*
    update 
      "SYSTEM"."EMPLOYEE" 
    set 
      "SYSTEM"."EMPLOYEE"."SALARY" = ("SYSTEM"."EMPLOYEE"."SALARY" * ?) 
    where 
      "SYSTEM"."EMPLOYEE"."JOB_TITLE" in (
        select 
          "SYSTEM"."EMPLOYEE"."JOB_TITLE" 
        from 
          "SYSTEM"."EMPLOYEE" 
        where 
          "SYSTEM"."EMPLOYEE"."JOB_TITLE" like ?
      )    
     */
    @Transactional
    public void updateEmployeeSalaryByJobTitle() {

        System.out.println("EXAMPLE 12 (rows affected):"
                + ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.mul(0.25))
                        .where(EMPLOYEE.JOB_TITLE.in(select(EMPLOYEE.JOB_TITLE).from(EMPLOYEE)
                                .where(EMPLOYEE.JOB_TITLE.like("Sale%"))))
                        .execute()
        );
    }

    // EXAMPLE 13
    /*
    delete from 
      "SYSTEM"."PAYMENT" 
    where 
      "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" in (
        select 
          "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" 
        from 
          "SYSTEM"."PAYMENT" 
        where 
          "SYSTEM"."PAYMENT"."CACHING_DATE" is not null
      )    
     */
    @Transactional
    public void deletePaymentWithCachingDateNotNull() {

        System.out.println("EXAMPLE 13 (rows affected):"
                + ctx.deleteFrom(PAYMENT)
                        .where(PAYMENT.INVOICE_AMOUNT.in(select(PAYMENT.INVOICE_AMOUNT).from(PAYMENT)
                                .where(PAYMENT.CACHING_DATE.isNotNull())))
                        .execute()
        );
    }
}