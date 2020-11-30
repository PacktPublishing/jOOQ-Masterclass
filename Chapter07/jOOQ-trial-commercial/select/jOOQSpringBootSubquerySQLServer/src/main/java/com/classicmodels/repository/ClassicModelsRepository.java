package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Customer.CUSTOMER;
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
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
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
      [classicmodels].[dbo].[employee].[employee_number], 
      [classicmodels].[dbo].[employee].[last_name], 
      [classicmodels].[dbo].[employee].[first_name], 
      [classicmodels].[dbo].[employee].[extension], 
      [classicmodels].[dbo].[employee].[email], 
      [classicmodels].[dbo].[employee].[office_code], 
      [classicmodels].[dbo].[employee].[salary], 
      [classicmodels].[dbo].[employee].[reports_to], 
      [classicmodels].[dbo].[employee].[job_title] 
    from 
      [classicmodels].[dbo].[employee] 
    where 
      [classicmodels].[dbo].[employee].[office_code] in (
        select 
          [classicmodels].[dbo].[office].[office_code] 
        from 
          [classicmodels].[dbo].[office] 
        where 
          [classicmodels].[dbo].[office].[city] like ?
      )    
     */
    public void findlEmployeeInOfficeStartingS() {

        System.out.println("EXAMPLE 1.1\n"
                + ctx.selectFrom(EMPLOYEE)
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
      [classicmodels].[dbo].[office].[office_code] 
    from 
      [classicmodels].[dbo].[employee] 
      join [classicmodels].[dbo].[office] on [classicmodels].[dbo].[employee].[office_code] 
        = [classicmodels].[dbo].[office].[office_code] 
    group by 
      [classicmodels].[dbo].[office].[office_code] 
    having 
      [classicmodels].[dbo].[office].[office_code] in (
        select 
          [classicmodels].[dbo].[office].[office_code] 
        from 
          [classicmodels].[dbo].[office] 
        where 
          [classicmodels].[dbo].[office].[state] <> ?
      )    
     */
    public void findEmployeeInOfficeNotMA() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(count(), OFFICE.OFFICE_CODE)
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
      [classicmodels].[dbo].[sale].[sale_id], 
      [classicmodels].[dbo].[sale].[sale] 
    from 
      [classicmodels].[dbo].[sale], 
      (
        select 
          avg(
            [classicmodels].[dbo].[sale].[sale]
          ) [avgs], 
          [classicmodels].[dbo].[sale].[employee_number] [sen] 
        from 
          [classicmodels].[dbo].[sale] 
        group by 
          [classicmodels].[dbo].[sale].[employee_number]
      ) [saleTable] 
    where 
      (
        [classicmodels].[dbo].[sale].[employee_number] = [saleTable].[sen] 
        and [classicmodels].[dbo].[sale].[sale] < [saleTable].[avgs]
      )   
     */
    public void findSaleLtAvg() {

        // Table<Record2<BigDecimal, Long>>
        var saleTable = select(avg(SALE.SALE_).as("avgs"), SALE.EMPLOYEE_NUMBER.as("sen"))
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER)
                .asTable("saleTable"); // derived table

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
      [e1].[first_name], 
      [e1].[last_name], 
      [e1].[office_code] 
    from 
      [classicmodels].[dbo].[employee] [e1], 
      (
        select 
          avg([e2].[salary]) [avgsal], 
          [e2].[office_code] 
        from 
          [classicmodels].[dbo].[employee] [e2] 
        group by 
          [e2].[office_code]
      ) [e3] 
    where 
      (
        [e1].[office_code] = [e3].[office_code] 
        and [e1].[salary] >= [e3].[avgsal]
      )   
     */
    public void findEmployeesWithSalaryGeAvgPerOffice() {

        Employee e1 = EMPLOYEE.as("e1");
        Employee e2 = EMPLOYEE.as("e2");

        // Table<Record2<BigDecimal, String>>
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
      [classicmodels].[dbo].[employee].[employee_number], 
      [classicmodels].[dbo].[employee].[first_name], 
      [classicmodels].[dbo].[employee].[last_name], 
      [saleTable].[ss] 
    from 
      (
        select 
          [classicmodels].[dbo].[sale].[employee_number] [sen], 
          [classicmodels].[dbo].[sale].[sale] [ss] 
        from 
          [classicmodels].[dbo].[sale]
      ) [saleTable], 
      [classicmodels].[dbo].[employee] 
    where 
      [classicmodels].[dbo].[employee].[employee_number] = [saleTable].[sen]    
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
      [saleTable].[sen], 
      [saleTable].[ss] 
    from 
      (
        select 
          [classicmodels].[dbo].[sale].[employee_number] [sen], 
          [classicmodels].[dbo].[sale].[sale] [ss] 
        from 
          [classicmodels].[dbo].[sale]
      ) [saleTable] 
    order by 
      [saleTable].[ss]    
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
      [saleTable].[sen], 
      [saleTable].[sales], 
      [classicmodels].[dbo].[employee].[first_name], 
      [classicmodels].[dbo].[employee].[last_name] 
    from 
      (
        select 
          [classicmodels].[dbo].[sale].[employee_number] [sen], 
          count(*) [sales] 
        from 
          [classicmodels].[dbo].[sale] 
        group by 
          [classicmodels].[dbo].[sale].[employee_number]
      ) [saleTable] 
      join [classicmodels].[dbo].[employee] on [saleTable].[sen] 
         = [classicmodels].[dbo].[employee].[employee_number] 
    order by 
      [saleTable].[sales] desc    
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
      [classicmodels].[dbo].[sale].[sale_id], 
      [classicmodels].[dbo].[sale].[sale] 
    from 
      [classicmodels].[dbo].[sale], 
      (
        select 
          [saleTable].[avgs], 
          [saleTable].[sen] 
        from 
          (
            select 
              avg(
                [classicmodels].[dbo].[sale].[sale]
              ) [avgs], 
              [classicmodels].[dbo].[sale].[employee_number] [sen] 
            from 
              [classicmodels].[dbo].[sale] 
            group by 
              [classicmodels].[dbo].[sale].[employee_number]
          ) [saleTable] 
        where 
          [saleTable].[avgs] > (
            select 
              avg(
                [classicmodels].[dbo].[sale].[sale]
              ) 
            from 
              [classicmodels].[dbo].[sale]
          )
      ) [saleTable2] 
    where 
      (
        [classicmodels].[dbo].[sale].[employee_number] = [saleTable2].[sen] 
        and [classicmodels].[dbo].[sale].[sale] < [saleTable2].[avgs]
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
    create view [payment_view] as 
    select 
      [classicmodels].[dbo].[payment].[customer_number], 
      [classicmodels].[dbo].[payment].[check_number], 
      [classicmodels].[dbo].[payment].[payment_date], 
      [classicmodels].[dbo].[payment].[invoice_amount], 
      [classicmodels].[dbo].[payment].[caching_date] 
    from 
      [classicmodels].[dbo].[payment] 
    where 
      [classicmodels].[dbo].[payment].[customer_number] = (
        select 
          [classicmodels].[dbo].[customer].[customer_number] 
        from 
          [classicmodels].[dbo].[customer] 
        where 
          [classicmodels].[dbo].[customer].[customer_name] = 'Signal Gift Stores'
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
    insert into [classicmodels].[dbo].[order] (
      [comments], [customer_number], [order_date], 
      [required_date], [shipped_date], 
      [status]
    ) 
    select 
      top 5 [classicmodels].[dbo].[order].[comments], 
      [classicmodels].[dbo].[order].[customer_number], 
      [classicmodels].[dbo].[order].[order_date], 
      [classicmodels].[dbo].[order].[required_date], 
      [classicmodels].[dbo].[order].[shipped_date], 
      [classicmodels].[dbo].[order].[status] 
    from 
      [classicmodels].[dbo].[order] 
    order by 
      (
        select 
          0
      )
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
    update 
      [classicmodels].[dbo].[employee] 
    set 
      [classicmodels].[dbo].[employee].[salary] = (
        [classicmodels].[dbo].[employee].[salary] * ?
      ) 
    where 
      [classicmodels].[dbo].[employee].[job_title] in (
        select 
          [classicmodels].[dbo].[employee].[job_title] 
        from 
          [classicmodels].[dbo].[employee] 
        where 
          [classicmodels].[dbo].[employee].[job_title] like ?
      )    
     */
    @Transactional
    public void updateEmployeeSalaryByJobTitle() {

        System.out.println("EXAMPLE 11 (rows affected):"
                + ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.mul(0.25))
                        .where(EMPLOYEE.JOB_TITLE.in(select(EMPLOYEE.JOB_TITLE).from(EMPLOYEE)
                                .where(EMPLOYEE.JOB_TITLE.like("Sale%"))))
                        .execute()
        );
    }

    // EXAMPLE 12
    /*
    delete from 
      [classicmodels].[dbo].[payment] 
    where 
      [classicmodels].[dbo].[payment].[invoice_amount] in (
        select 
          [classicmodels].[dbo].[payment].[invoice_amount] 
        from 
          [classicmodels].[dbo].[payment] 
        where 
          [classicmodels].[dbo].[payment].[caching_date] is not null
      )    
     */
    @Transactional
    public void deletePaymentWithCachingDateNotNull() {

        System.out.println("EXAMPLE 12 (rows affected):"
                + ctx.deleteFrom(PAYMENT)
                        .where(PAYMENT.INVOICE_AMOUNT.in(select(PAYMENT.INVOICE_AMOUNT).from(PAYMENT)
                                .where(PAYMENT.CACHING_DATE.isNotNull())))
                        .execute()
        );
    }
}
