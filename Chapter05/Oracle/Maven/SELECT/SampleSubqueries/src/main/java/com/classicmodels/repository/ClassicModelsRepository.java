package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.DailyActivity.DAILY_ACTIVITY;
import static jooq.generated.tables.Department.DEPARTMENT;
import jooq.generated.tables.Employee;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.primaryKey;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.DATE;
import static org.jooq.impl.SQLDataType.FLOAT;
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
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME"
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    where 
      "CLASSICMODELS"."EMPLOYEE"."OFFICE_CODE" in (
        select 
          "CLASSICMODELS"."OFFICE"."OFFICE_CODE" 
        from 
          "CLASSICMODELS"."OFFICE" 
        where 
          "CLASSICMODELS"."OFFICE"."CITY" like ?
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
      "CLASSICMODELS"."OFFICE"."OFFICE_CODE", 
      "CLASSICMODELS"."OFFICE"."STATE" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
      join "CLASSICMODELS"."OFFICE" on "CLASSICMODELS"."EMPLOYEE"."OFFICE_CODE" = "CLASSICMODELS"."OFFICE"."OFFICE_CODE" 
    group by 
      "CLASSICMODELS"."OFFICE"."OFFICE_CODE", 
      "CLASSICMODELS"."OFFICE"."STATE" 
    having 
      "CLASSICMODELS"."OFFICE"."OFFICE_CODE" in (
        select 
          "CLASSICMODELS"."OFFICE"."OFFICE_CODE" 
        from 
          "CLASSICMODELS"."OFFICE" 
        where 
          "CLASSICMODELS"."OFFICE"."STATE" <> ?
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
      "CLASSICMODELS"."SALE"."SALE_ID", 
      "CLASSICMODELS"."SALE"."SALE" 
    from 
      "CLASSICMODELS"."SALE", 
      (
        select 
          avg("CLASSICMODELS"."SALE"."SALE") "avgs", 
          "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" "sen" 
        from 
          "CLASSICMODELS"."SALE" 
        group by 
          "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
      ) "saleTable" 
    where 
      (
        "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" = "saleTable"."sen" 
        and "CLASSICMODELS"."SALE"."SALE" < "saleTable"."avgs"
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
                        .where(SALE.EMPLOYEE_NUMBER.eq(saleTable.field("sen", Long.class))
                                .and(SALE.SALE_.lt(saleTable.field("avgs", Double.class))))
                        .fetch()
        );

        /* same query with fields extracted as local variables
        Field<Double> avgs = avg(SALE.SALE_).coerce(Double.class).as("avgs");
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
                                .and(SALE.SALE_.lt(avgs)))
                        .fetch()
        );
         */
        
        /* same query with fields extracted as local variables and no derived table
        Field<Double> avgs = avg(SALE.SALE_).coerce(Double.class).as("avgs");
        Field<Long> sen = SALE.EMPLOYEE_NUMBER.as("sen");

        System.out.println("EXAMPLE 3\n"
                + ctx.select(SALE.SALE_ID, SALE.SALE_)
                        .from(SALE, select(avgs, sen)
                                .from(SALE)
                                .groupBy(SALE.EMPLOYEE_NUMBER))
                        .where(SALE.EMPLOYEE_NUMBER.eq(sen)
                                .and(SALE.SALE_.lt(avgs)))
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
      "CLASSICMODELS"."EMPLOYEE" "e1", 
      (
        select 
          avg("e2"."SALARY") "avgsal", 
          "e2"."OFFICE_CODE" 
        from 
          "CLASSICMODELS"."EMPLOYEE" "e2" 
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
                                .and(e1.SALARY.ge(e3.field("avgsal", Integer.class))))
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    select 
      "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER", 
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
      "saleTable"."ss" 
    from 
      (
        select 
          "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" "sen", 
          "CLASSICMODELS"."SALE"."SALE" "ss" 
        from 
          "CLASSICMODELS"."SALE"
      ) "saleTable", 
      "CLASSICMODELS"."EMPLOYEE" 
    where 
      "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "saleTable"."sen"    
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
          "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" "sen", 
          "CLASSICMODELS"."SALE"."SALE" "ss" 
        from 
          "CLASSICMODELS"."SALE"
      ) "saleTable" 
    order by 
      "saleTable"."ss"    
     */
    public void orderSales() {

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
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME" 
    from 
      (
        select 
          "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" "sen", 
          count(*) "sales" 
        from 
          "CLASSICMODELS"."SALE" 
        group by 
          "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
      ) "saleTable" 
      join "CLASSICMODELS"."EMPLOYEE" on "saleTable"."sen" = "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" 
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
                        .innerJoin(EMPLOYEE).on(salesTable.field("sen", Long.class)
                        .eq(EMPLOYEE.EMPLOYEE_NUMBER))
                        .orderBy(salesTable.field("sales").desc())
                        .fetch()
        );
    }

    // EXAMPLE 8    
    /*
    select 
      "CLASSICMODELS"."SALE"."SALE_ID", 
      "CLASSICMODELS"."SALE"."SALE" 
    from 
      "CLASSICMODELS"."SALE", 
      (
        select 
          "saleTable"."avgs", 
          "saleTable"."sen" 
        from 
          (
            select 
              avg("CLASSICMODELS"."SALE"."SALE") "avgs", 
              "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" "sen" 
            from 
              "CLASSICMODELS"."SALE" 
            group by 
              "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
          ) "saleTable" 
        where 
          "saleTable"."avgs" > (
            select 
              avg("CLASSICMODELS"."SALE"."SALE") 
            from 
              "CLASSICMODELS"."SALE"
          )
      ) "saleTable2" 
    where 
      (
        "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" = "saleTable2"."sen" 
        and "CLASSICMODELS"."SALE"."SALE" < "saleTable2"."avgs"
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
                .where(saleTable.field("avgs", BigDecimal.class)
                        .gt(select(avg(SALE.SALE_)).from(SALE)))
                .asTable("saleTable2");

        System.out.println("EXAMPLE 8\n"
                + ctx.select(SALE.SALE_ID, SALE.SALE_)
                        .from(SALE, saleTable2)
                        .where(SALE.EMPLOYEE_NUMBER.eq(saleTable2.field("sen", Long.class))
                                .and(SALE.SALE_.lt(saleTable2.field("avgs", Double.class))))
                        .fetch()
        );
    }

    // EXAMPLE 9
    /*
    create view "payment_view" as 
    select 
      "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER", 
      "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
      "CLASSICMODELS"."PAYMENT"."PAYMENT_DATE", 
      "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT", 
      "CLASSICMODELS"."PAYMENT"."CACHING_DATE" 
    from 
      "CLASSICMODELS"."PAYMENT" 
    where 
      "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER" = (
        select 
          "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NUMBER" 
        from 
          "CLASSICMODELS"."CUSTOMER" 
        where 
          "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NAME" = 'Signal Gift Stores'
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
    insert into "CLASSICMODELS"."ORDER" (
      "COMMENTS", "CUSTOMER_NUMBER", "ORDER_DATE", 
      "REQUIRED_DATE", "SHIPPED_DATE", 
      "STATUS", "AMOUNT"
    ) 
    select 
      "CLASSICMODELS"."ORDER"."COMMENTS", 
      "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER", 
      "CLASSICMODELS"."ORDER"."ORDER_DATE", 
      "CLASSICMODELS"."ORDER"."REQUIRED_DATE", 
      "CLASSICMODELS"."ORDER"."SHIPPED_DATE", 
      "CLASSICMODELS"."ORDER"."STATUS", 
      "CLASSICMODELS"."ORDER"."AMOUNT" 
    from 
      "CLASSICMODELS"."ORDER" fetch next ? rows only    
     */
    @Transactional
    public void insertIntoOrder() {

        System.out.println("EXAMPLE 10 (rows affected):"
                + ctx.insertInto(ORDER, ORDER.COMMENTS, ORDER.CUSTOMER_NUMBER, ORDER.ORDER_DATE, 
                        ORDER.REQUIRED_DATE, ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.AMOUNT)
                        .select(select(ORDER.COMMENTS, ORDER.CUSTOMER_NUMBER, ORDER.ORDER_DATE, 
                                ORDER.REQUIRED_DATE, ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.AMOUNT)
                                .from(ORDER)
                                .limit(5)
                        ).execute()
        );
    }

    // EXAMPLE 11
    /*
    merge into "CLASSICMODELS"."DAILY_ACTIVITY" using (
      (
        select 
          null "DAY_ID", null "DAY_DATE", null "SALES", null "VISITORS", null "CONVERSION" 
        from 
          DUAL 
        where 
          1 = 0 
        union all 
        select 
          DAY_ID, DAY_DATE, SALES, VISITORS, CONVERSION 
        from 
          DAILY_ACTIVITY_TEMP
      )
    ) "t" on (
      (
        "CLASSICMODELS"."DAILY_ACTIVITY"."DAY_ID", 
        1
      ) = (
        ("t"."DAY_ID", 1)
      )
    ) when not matched then insert (
      "DAY_ID", "DAY_DATE", "SALES", "VISITORS", 
      "CONVERSION"
    ) 
    values 
      (
        "t"."DAY_ID", "t"."DAY_DATE", "t"."SALES", 
        "t"."VISITORS", "t"."CONVERSION"
      )
     */
    @Transactional
    public void insertAnotherTableInDailyActivity() {
              
        // create a temporary table identical to DAILY_ACTIVITY 
        ctx.createTemporaryTable("DAILY_ACTIVITY_TEMP")
                .column("DAY_ID", BIGINT.nullable(false))
                .column("DAY_DATE", DATE.nullable(false))
                .column("SALES", FLOAT.nullable(false))
                .column("VISITORS", FLOAT.nullable(false))
                .column("CONVERSION", FLOAT.nullable(false))
                .constraints(
                        primaryKey("DAY_ID")
                )
                .execute();        
        
        // insert some data into the temporary table 
        ctx.insertInto(table("DAILY_ACTIVITY_TEMP"))
                .values(11L, LocalDate.of(2004, 1, 11), 50, 3620, 0.76)                
                .execute();

        // insert into DAILY_ACTIVITY the data from the temporary table via SELECT 
        System.out.println("EXAMPLE 11 (rows affected):"
                + ctx.insertInto(DAILY_ACTIVITY)
                        .select(select(field("DAY_ID"), field("DAY_DATE"), field("SALES"), 
                                field("VISITORS"), field("CONVERSION"))
                                .from(table("DAILY_ACTIVITY_TEMP")))
                        .onDuplicateKeyIgnore()
                        .execute()
        );                                
        
        ctx.dropTemporaryTableIfExists("DAILY_ACTIVITY_TEMP")
                .execute();                
    }
    
    // EXAMPLE 12
    /*
    update 
      "CLASSICMODELS"."EMPLOYEE" 
    set 
      "CLASSICMODELS"."EMPLOYEE"."SALARY" = ("CLASSICMODELS"."EMPLOYEE"."SALARY" * ?) 
    where 
      "CLASSICMODELS"."EMPLOYEE"."JOB_TITLE" in (
        select 
          "CLASSICMODELS"."EMPLOYEE"."JOB_TITLE" 
        from 
          "CLASSICMODELS"."EMPLOYEE" 
        where 
          "CLASSICMODELS"."EMPLOYEE"."JOB_TITLE" like ?
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
      "CLASSICMODELS"."DEPARTMENT" 
    where 
      "CLASSICMODELS"."DEPARTMENT"."OFFICE_CODE" in (
        select 
          "CLASSICMODELS"."DEPARTMENT"."OFFICE_CODE" 
        from 
          "CLASSICMODELS"."DEPARTMENT" 
        where 
          "CLASSICMODELS"."DEPARTMENT"."ACCRUED_LIABILITIES" is not null
      )    
     */
    @Transactional
    public void deleteDepartmentWithAccuredLiabilitiesNotNull() {

        System.out.println("EXAMPLE 13 (rows affected):"
                + ctx.deleteFrom(DEPARTMENT)
                        .where(DEPARTMENT.OFFICE_CODE.in(select(DEPARTMENT.OFFICE_CODE).from(DEPARTMENT)
                                .where(DEPARTMENT.ACCRUED_LIABILITIES.isNotNull())))
                        .execute()
        );
    }
}