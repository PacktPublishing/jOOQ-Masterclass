package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Customer.CUSTOMER;
import jooq.generated.tables.Employee;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
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
      `classicmodels`.`employee`.`last_name`,
      `classicmodels`.`employee`.`first_name`
    from
      `classicmodels`.`employee`
    where
      `classicmodels`.`employee`.`office_code` in 
        (
          select
            `classicmodels`.`office`.`office_code`
          from
            `classicmodels`.`office`
          where
            `classicmodels`.`office`.`city` like ?
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
      `classicmodels`.`office`.`office_code`, 
      `classicmodels`.`office`.`state` 
    from 
      `classicmodels`.`employee` 
      join `classicmodels`.`office` 
        on `classicmodels`.`employee`.`office_code` = `classicmodels`.`office`.`office_code` 
    group by 
      `classicmodels`.`office`.`office_code` 
    having 
      `classicmodels`.`office`.`office_code` in (
        select 
          `classicmodels`.`office`.`office_code` 
        from 
          `classicmodels`.`office` 
        where 
          `classicmodels`.`office`.`state` <> ?
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
      `classicmodels`.`sale`.`sale_id`,
      `classicmodels`.`sale`.`sale`
    from
      `classicmodels`.`sale`,
        (
          select
            avg(`classicmodels`.`sale`.`sale`) as `avgs`,
           `classicmodels`.`sale`.`employee_number` as `sen`
          from
           `classicmodels`.`sale`
          group by
           `classicmodels`.`sale`.`employee_number`
        ) as `saleTable`
    where
      ( 
        `classicmodels`.`sale`.`employee_number` = `saleTable`.`sen`
          and `classicmodels`.`sale`.`sale` < `saleTable`.`avgs`
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
      `e1`.`first_name`,
      `e1`.`last_name`,
      `e1`.`office_code`
    from
      `classicmodels`.`employee` as `e1`,
      (
        select
          avg(`e2`.`salary`) as `avgsal`,
          `e2`.`office_code`
        from
          `classicmodels`.`employee` as `e2`
        group by
          `e2`.`office_code`
      ) as `e3`
    where
     (
       `e1`.`office_code` = `e3`.`office_code`
       and `e1`.`salary` >= `e3`.`avgsal`
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
      `classicmodels`.`employee`.`employee_number`,
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`last_name`,
      `saleTable`.`ss`
    from
     (
        select
          `classicmodels`.`sale`.`employee_number` as `sen`,
          `classicmodels`.`sale`.`sale` as `ss`
        from
          `classicmodels`.`sale`
      ) as `saleTable`,
      `classicmodels`.`employee`
    where
      `classicmodels`.`employee`.`employee_number` = `saleTable`.`sen`
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
      `saleTable`.`sen`,
      `saleTable`.`ss`
    from
      (
        select
          `classicmodels`.`sale`.`employee_number` as `sen`,
          `classicmodels`.`sale`.`sale` as `ss`
        from
          `classicmodels`.`sale`
      ) as `saleTable`
    order by
      `saleTable`.`ss`
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
      `saleTable`.`sen`,
      `saleTable`.`sales`,
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`last_name`
    from
     (
       select
         `classicmodels`.`sale`.`employee_number` as `sen`, count(*) as `sales`
       from
         `classicmodels`.`sale`
       group by
         `classicmodels`.`sale`.`employee_number`
     ) as `saleTable`
    join `classicmodels`.`employee` on `saleTable`.`sen` 
       = `classicmodels`.`employee`.`employee_number`
    order by
      `saleTable`.`sales` desc
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
      `classicmodels`.`sale`.`sale_id`,
      `classicmodels`.`sale`.`sale`
    from
      `classicmodels`.`sale`,
      (
        select
          `saleTable`.`avgs`,
          `saleTable`.`sen`
        from
          (
            select
              avg(`classicmodels`.`sale`.`sale`) as `avgs`,
              `classicmodels`.`sale`.`employee_number` as `sen`
            from
              `classicmodels`.`sale`
            group by
              `classicmodels`.`sale`.`employee_number`
          ) as `saleTable`
        where
          `saleTable`.`avgs` > (
             select
               avg(`classicmodels`.`sale`.`sale`)
             from
               `classicmodels`.`sale`
          )
       ) as `saleTable2`
    where
      (
        `classicmodels`.`sale`.`employee_number` = `saleTable2`.`sen`
           and `classicmodels`.`sale`.`sale` < `saleTable2`.`avgs`
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
    create view `payment_view` as 
    select 
      `classicmodels`.`payment`.`customer_number`, 
      `classicmodels`.`payment`.`check_number`, 
      `classicmodels`.`payment`.`payment_date`, 
      `classicmodels`.`payment`.`invoice_amount`, 
      `classicmodels`.`payment`.`caching_date` 
    from 
      `classicmodels`.`payment` 
    where 
      `classicmodels`.`payment`.`customer_number` = (
        select 
          `classicmodels`.`customer`.`customer_number` 
        from 
          `classicmodels`.`customer` 
        where 
          `classicmodels`.`customer`.`customer_name` = 'Signal Gift Stores'
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
    insert into
      `classicmodels`.`order` (
        `comments`,
        `customer_number`,
        `order_date`,
        `required_date`,
        `shipped_date`,
        `status`
      )
    select
      `classicmodels`.`order`.`comments`,
      `classicmodels`.`order`.`customer_number`,
      `classicmodels`.`order`.`order_date`,
      `classicmodels`.`order`.`required_date`,
      `classicmodels`.`order`.`shipped_date`,
      `classicmodels`.`order`.`status`
    from
      `classicmodels`.`order`
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
    insert ignore into `classicmodels`.`manager` (`manager_id`, `manager_name`)
      select * from manager_temp
     */
    @Transactional
    public void insertAnotherTableInManager() {

        // create a temporary table identical to MANAGER 
        ctx.createTemporaryTable("manager_temp")
                .column("manager_id", BIGINT.nullable(false))
                .column("manager_name", VARCHAR(50).nullable(false))
                .constraints(
                        primaryKey("manager_id")
                )
                .execute();

        // insert some data into the temporary table 
        ctx.insertInto(table("manager_temp"))
                .values(Math.random() * 1000, "John Malon")
                .execute();

        // insert into MANAGER the data from the temporary table via SELECT 
        System.out.println("EXAMPLE 11 (rows affected):"
                + ctx.insertInto(MANAGER)
                        .select(select().from(table("manager_temp")))
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 12
    /*
    update
      `classicmodels`.`employee`
    set
      `classicmodels`.`employee`.`salary` = (`classicmodels`.`employee`.`salary` * ?)
    where
      `classicmodels`.`employee`.`job_title` in (
         select
           *
         from
          (
            select
              `classicmodels`.`employee`.`job_title`
            from
              `classicmodels`.`employee`
            where
              `classicmodels`.`employee`.`job_title` like ?
          ) as `t`
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
      `classicmodels`.`payment`
    where
      `classicmodels`.`payment`.`invoice_amount` in 
        (
          select
            *
          from
           (
             select
               `classicmodels`.`payment`.`invoice_amount`
             from
               `classicmodels`.`payment`
             where
               `classicmodels`.`payment`.`caching_date` is not null
           ) as `t`
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
