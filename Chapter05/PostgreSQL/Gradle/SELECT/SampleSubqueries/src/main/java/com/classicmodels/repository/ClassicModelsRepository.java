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
      "public"."employee"."last_name", 
      "public"."employee"."first_name"
    from 
      "public"."employee" 
    where 
      "public"."employee"."office_code" in (
        select 
          "public"."office"."office_code" 
        from 
          "public"."office" 
        where 
          "public"."office"."city" like ?
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
      "public"."office"."office_code", 
      "public"."office"."state" 
    from 
      "public"."employee" 
      join "public"."office" on "public"."employee"."office_code" = "public"."office"."office_code" 
    group by 
      "public"."office"."office_code" 
    having 
      "public"."office"."office_code" in (
        select 
          "public"."office"."office_code" 
        from 
          "public"."office" 
        where 
          "public"."office"."state" <> ?
      )
     */
    public void findEmployeeInOfficeNotMA() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(count(), OFFICE.OFFICE_CODE, OFFICE.STATE)
                        .from(EMPLOYEE)
                        .join(OFFICE)
                        .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .groupBy(OFFICE.OFFICE_CODE)
                        .having(OFFICE.OFFICE_CODE.in(
                                select(OFFICE.OFFICE_CODE).from(OFFICE).where(OFFICE.STATE.ne("MA"))))
                        .fetch()
        );
    }

    // EXAMPLE 3
    /*
    select 
      "public"."sale"."sale_id", 
      "public"."sale"."sale" 
    from 
      "public"."sale", 
      (
        select 
          avg("public"."sale"."sale") as "avgs", 
          "public"."sale"."employee_number" as "sen" 
        from 
          "public"."sale" 
        group by 
          "public"."sale"."employee_number"
      ) as "saleTable" 
    where 
      (
        "public"."sale"."employee_number" = "saleTable"."sen" 
        and "public"."sale"."sale" < "saleTable"."avgs"
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

        // same query with fields extracted as local variables
        /*
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
        
        // same query with fields extracted as local variables and no derived table
        /*
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
      "e1"."first_name", 
      "e1"."last_name", 
      "e1"."office_code" 
    from 
      "public"."employee" as "e1", 
      (
        select 
          avg("e2"."salary") as "avgsal", 
          "e2"."office_code" 
        from 
          "public"."employee" as "e2" 
        group by 
          "e2"."office_code"
      ) as "e3" 
    where 
      (
        "e1"."office_code" = "e3"."office_code" 
        and "e1"."salary" >= "e3"."avgsal"
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
      "public"."employee"."employee_number", 
      "public"."employee"."first_name", 
      "public"."employee"."last_name", 
      "saleTable"."ss" 
    from 
      (
        select 
          "public"."sale"."employee_number" as "sen", 
          "public"."sale"."sale" as "ss" 
        from 
          "public"."sale"
      ) as "saleTable", 
      "public"."employee" 
    where 
      "public"."employee"."employee_number" = "saleTable"."sen"
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
                                .eq(saleTable.field("sen", Long.class)))
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
          "public"."sale"."employee_number" as "sen", 
          "public"."sale"."sale" as "ss" 
        from 
          "public"."sale"
      ) as "saleTable" 
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
      "public"."employee"."first_name", 
      "public"."employee"."last_name" 
    from 
      (
        select 
          "public"."sale"."employee_number" as "sen", 
          count(*) as "sales" 
        from 
          "public"."sale" 
        group by 
          "public"."sale"."employee_number"
      ) as "saleTable" 
      join "public"."employee" on "saleTable"."sen" = "public"."employee"."employee_number" 
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
      "public"."sale"."sale_id", 
      "public"."sale"."sale" 
    from 
      "public"."sale", 
      (
        select 
          "saleTable"."avgs", 
          "saleTable"."sen" 
        from 
          (
            select 
              avg("public"."sale"."sale") as "avgs", 
              "public"."sale"."employee_number" as "sen" 
            from 
              "public"."sale" 
            group by 
              "public"."sale"."employee_number"
          ) as "saleTable" 
        where 
          "saleTable"."avgs" > (
            select 
              avg("public"."sale"."sale") 
            from 
              "public"."sale"
          )
      ) as "saleTable2" 
    where 
      (
        "public"."sale"."employee_number" = "saleTable2"."sen" 
        and "public"."sale"."sale" < "saleTable2"."avgs"
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
      "public"."payment"."customer_number", 
      "public"."payment"."check_number", 
      "public"."payment"."payment_date", 
      "public"."payment"."invoice_amount", 
      "public"."payment"."caching_date" 
    from 
      "public"."payment" 
    where 
      "public"."payment"."customer_number" = (
        select 
          "public"."customer"."customer_number" 
        from 
          "public"."customer" 
        where 
          "public"."customer"."customer_name" = 'Signal Gift Stores'
      )
     */
    @Transactional
    public void findPaymentForCustomerSignalGiftStores() {

        ctx.createView("payment_view").as(ctx.selectFrom(PAYMENT)
                .where(PAYMENT.CUSTOMER_NUMBER.eq(
                        select(CUSTOMER.CUSTOMER_NUMBER).from(CUSTOMER)
                                .where(CUSTOMER.CUSTOMER_NAME.eq("Signal Gift Stores"))
                ))).execute();

        System.out.println("EXAMPLE 9\n"
                + ctx.selectFrom(table("payment_view"))
                        .fetch()
        );

        // clean up
        ctx.dropView("payment_view").execute();
    }

    // EXAMPLE 10
    /*
    insert into "public"."order" (
      "comments", "customer_number", "order_date", 
      "required_date", "shipped_date", 
      "status", "amount"
    ) 
    select 
      "public"."order"."comments", 
      "public"."order"."customer_number", 
      "public"."order"."order_date", 
      "public"."order"."required_date", 
      "public"."order"."shipped_date", 
      "public"."order"."status", 
      "public"."order"."amount" 
    from 
      "public"."order" fetch next ? rows only    
     */
    @Transactional
    public void insertIntoOrder() {

        System.out.println("EXAMPLE 10 (rows affected):"
                + ctx.insertInto(ORDER, ORDER.COMMENTS, ORDER.CUSTOMER_NUMBER, ORDER.ORDER_DATE, 
                        ORDER.REQUIRED_DATE, ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.AMOUNT)
                        .select(select(ORDER.COMMENTS, ORDER.CUSTOMER_NUMBER, ORDER.ORDER_DATE, 
                                ORDER.REQUIRED_DATE, ORDER.SHIPPED_DATE, ORDER.STATUS,ORDER.AMOUNT)
                                .from(ORDER)
                                .limit(5)
                        ).execute()
        );
    }

    // EXAMPLE 11
    /*
    insert into "public"."daily_activity" (
      "day_id", "day_date", "sales", "visitors", "conversion"
    ) 
    select 
      * 
    from 
      daily_activity_temp on conflict do nothing    
     */
    @Transactional
    public void insertAnotherTableInDailyActivity() {

        // create a temporary table identical to DAILY_ACTIVITY 
        ctx.createTemporaryTable("daily_activity_temp")
                .column("day_id", BIGINT.nullable(false))
                .column("day_date", DATE.nullable(false))
                .column("sales", FLOAT.nullable(false))
                .column("visitors", FLOAT.nullable(false))
                .column("conversion", FLOAT.nullable(false))
                .constraints(
                        primaryKey("day_id")
                )
                .execute();

        // insert some data into the temporary table 
        ctx.insertInto(table("daily_activity_temp"))
                .values(11L, LocalDate.of(2004, 1, 11), 50, 3620, 0.76)
                .onConflictDoNothing()  // or, .onDuplicateKeyIgnore()
                .execute(); 

        // insert into DAILY_ACTIVITY the data from the temporary table via SELECT 
        System.out.println("EXAMPLE 11 (rows affected):"
                + ctx.insertInto(DAILY_ACTIVITY)
                        .select(select().from(table("daily_activity_temp")))
                        .onConflictDoNothing() // or, .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 12
    /*
    update 
      "public"."employee" 
    set 
      "salary" = ("public"."employee"."salary" * ?) 
    where 
      "public"."employee"."job_title" in (
        select 
          "public"."employee"."job_title" 
        from 
          "public"."employee" 
        where 
          "public"."employee"."job_title" like ?
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
      "public"."department" 
    where 
      "public"."department"."office_code" in (
        select 
          "public"."department"."office_code" 
        from 
          "public"."department" 
        where 
          "public"."department"."accrued_liabilities" is not null
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