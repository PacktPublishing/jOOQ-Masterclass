package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Payment.PAYMENT;
import jooq.generated.tables.Sale;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectCount;
import static org.jooq.impl.DSL.sum;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /*
    select
      `classicmodels`.`employee`.`employee_number`,
      `classicmodels`.`employee`.`last_name`,
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`extension`,
      `classicmodels`.`employee`.`email`,
      `classicmodels`.`employee`.`office_code`,
      `classicmodels`.`employee`.`salary`,
      `classicmodels`.`employee`.`reports_to`,
      `classicmodels`.`employee`.`job_title`
    from
      `classicmodels`.`employee`
    where
      `classicmodels`.`employee`.`salary` >= (
    select
      (avg(`classicmodels`.`employee`.`salary`) + ?)
    from
      `classicmodels`.`employee`
    */
    public void findSalaryGeAvgPlus25000() {

        System.out.println(
                ctx.selectFrom(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.ge(
                                select(avg(EMPLOYEE.SALARY).plus(25000)).from(EMPLOYEE).asField()))
                        .fetch()
        );
    }

    /*
    select
       min(`classicmodels`.`payment`.`invoice_amount`),
       round(
        (
          select
             min(`classicmodels`.`payment`.`invoice_amount`)
          from
             `classicmodels`.`payment`
        ), ?
      ) as `round_min`
    from
      `classicmodels`.`payment`
    */
    public void findMinAndRoundMinInvoiceAmount() {

        System.out.println(
                ctx.select(min(PAYMENT.INVOICE_AMOUNT),
                        round(select(min(PAYMENT.INVOICE_AMOUNT))
                                .from(PAYMENT).asField(), 0).as("round_min"))
                        .from(PAYMENT)
                        .fetch()
        );
    }

    /*
    select
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`last_name`,
      (
        select
           count(`classicmodels`.`sale`.`employee_number`)
        from
           `classicmodels`.`sale`
        where
           `classicmodels`.`employee`.`employee_number` = `classicmodels`.`sale`.`employee_number`
      ) as `sales`
    from
      `classicmodels`.`employee`
    */
    public void findEmployeeAndNoOfSale() {

        System.out.println(
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                        (select(count(SALE.EMPLOYEE_NUMBER)).from(SALE)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER
                                        .eq(SALE.EMPLOYEE_NUMBER))).asField("sales"))
                        .from(EMPLOYEE)
                        .fetch()
        );
    }

    /*
    select
      `classicmodels`.`office`.`city`,
      `classicmodels`.`office`.`address_line_first`,
      (
        select
          count(*)
        from
          `classicmodels`.`employee`
        where
          `classicmodels`.`employee`.`office_code` = `classicmodels`.`office`.`office_code`
      ) as `employeesNr`
    from
      `classicmodels`.`office`
    */
    public void findOfficeAndNoOfEmployee() {

        System.out.println(
                ctx.select(OFFICE.CITY, OFFICE.ADDRESS_LINE_FIRST,
                        (selectCount().from(EMPLOYEE)
                                .where(EMPLOYEE.OFFICE_CODE
                                        .eq(OFFICE.OFFICE_CODE))).asField("employeesNr"))
                        .from(OFFICE)
                        .fetch()
        );
    }

    /*
    select
      (
        `classicmodels`.`employee`.`salary` + (
          select
            avg(`classicmodels`.`employee`.`salary`)
          from
            `classicmodels`.`employee`
        )
      ) as `baseSalary`
    from
      `classicmodels`.`employee`
    */
    public void findBaseSalary() {

        System.out.println(
                ctx.select(EMPLOYEE.SALARY.plus(
                        select(avg(EMPLOYEE.SALARY)).from(EMPLOYEE).asField()).as("baseSalary"))
                        .from(EMPLOYEE)
                        .fetch()
        );
    }

    /*
    select
      `classicmodels`.`office`.`city`,
      `classicmodels`.`office`.`address_line_first`,
      (
        select
          max(`classicmodels`.`employee`.`salary`)
        from
          `classicmodels`.`employee`
        where
          `classicmodels`.`employee`.`office_code` = `classicmodels`.`office`.`office_code`
      ) as `maxSalary`,
      (
        select
          avg(`classicmodels`.`employee`.`salary`)
        from
          `classicmodels`.`employee`
      ) as `avgSalary`
    from
      `classicmodels`.`office`
    */
    public void findOfficeAndEmployeeMaxAndAvgSalary() {

        System.out.println(
                ctx.select(OFFICE.CITY, OFFICE.ADDRESS_LINE_FIRST,
                        (select(max(EMPLOYEE.SALARY)).from(EMPLOYEE)
                                .where(EMPLOYEE.OFFICE_CODE
                                        .eq(OFFICE.OFFICE_CODE))).asField("maxSalary"),
                        (select(avg(EMPLOYEE.SALARY)).from(EMPLOYEE)).asField("avgSalary"))
                        .from(OFFICE)
                        .fetch()
        );
    }   
    
    public void findEmployeeWithAvgSaleLtSumSales() {

        /*
        select
          `classicmodels`.`employee`.`first_name`,
          `classicmodels`.`employee`.`last_name`,
          `classicmodels`.`employee`.`salary`
        from
          `classicmodels`.`employee`
        where
          (
            select
              avg(`classicmodels`.`sale`.`sale`)
            from   
              `classicmodels`.`sale`
          ) < (
            select
              sum(`classicmodels`.`sale`.`sale`)
            from
              `classicmodels`.`sale`
            where
              `classicmodels`.`employee`.`employee_number` = `classicmodels`.`sale`.`employee_number`
          )
        */
        System.out.println(
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .where(field(select(avg(SALE.SALE_)).from(SALE)).lt(
                                (select(sum(SALE.SALE_)).from(SALE)
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER
                                                .eq(SALE.EMPLOYEE_NUMBER)))))
                        .fetch()
        );

        /*
        select distinct 
          `classicmodels`.`employee`.`first_name`,
          `classicmodels`.`employee`.`last_name`,
          `classicmodels`.`employee`.`salary`
        from
          `classicmodels`.`employee`
        join `classicmodels`.`office` on (
          select
            avg(`classicmodels`.`sale`.`sale`)
          from
            `classicmodels`.`sale`
        ) < (
          select
            sum(`classicmodels`.`sale`.`sale`)
          from
            `classicmodels`.`sale`
          where
            `classicmodels`.`employee`.`employee_number` = `classicmodels`.`sale`.`employee_number`
        )
        */
        System.out.println(
                ctx.selectDistinct(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .join(OFFICE)
                        .on(field(select(avg(SALE.SALE_)).from(SALE))
                                .lt(select(sum(SALE.SALE_)).from(SALE)
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER
                                                .eq(SALE.EMPLOYEE_NUMBER))))
                        .fetch()
        );
    }
     
    /*
    select
      `classicmodels`.`employee`.`employee_number`,
      `classicmodels`.`employee`.`last_name`,
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`extension`,
      `classicmodels`.`employee`.`email`,
      `classicmodels`.`employee`.`office_code`,
      `classicmodels`.`employee`.`salary`,
      `classicmodels`.`employee`.`reports_to`,
      `classicmodels`.`employee`.`job_title`
    from
      `classicmodels`.`employee`
    where
      `classicmodels`.`employee`.`salary` >= (
        select
          `classicmodels`.`employee`.`salary`
        from
          `classicmodels`.`employee`
        where
          `classicmodels`.`employee`.`employee_number` = ?
    )
    */    
    public void findEmployeeWithSalaryGt() {

        System.out.println(
                ctx.selectFrom(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.ge(
                                select(EMPLOYEE.SALARY).from(EMPLOYEE).
                                        where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1076L))))
                        .fetch()
        );
    }
      
    public void findMaxSalePerFiscalYearAndEmployee() {

        /*
        select
          `s1`.`sale`,
          `s1`.`fiscal_year`,
          `s1`.`employee_number`
        from
          `classicmodels`.`sale` as `s1`
        where
          `s1`.`sale` = (
            select
              max(`s2`.`sale`)
            from
              `classicmodels`.`sale` as `s2`
            where
              (
                `s2`.`employee_number` = `s1`.`employee_number`
                   and `s2`.`fiscal_year` = `s1`.`fiscal_year`
              )
          )
        order by
          `s1`.`fiscal_year`
        */
        Sale s1 = SALE.as("s1");
        Sale s2 = SALE.as("s2");

        System.out.println(
                ctx.select(s1.SALE_, s1.FISCAL_YEAR, s1.EMPLOYEE_NUMBER)
                        .from(s1)
                        .where(s1.SALE_.eq(select(max(s2.SALE_))
                                .from(s2)
                                .where(s2.EMPLOYEE_NUMBER.eq(s1.EMPLOYEE_NUMBER)
                                        .and(s2.FISCAL_YEAR.eq(s1.FISCAL_YEAR)))))
                        .orderBy(s1.FISCAL_YEAR)
                        .fetch()
        );

        // of course, it is simpler to rely on groupBy and not on a nested select 
        /*
        select
          `classicmodels`.`sale`.`fiscal_year`,
          `classicmodels`.`sale`.`employee_number`,
          max(`classicmodels`.`sale`.`sale`)
        from
          `classicmodels`.`sale`
        group by
          `classicmodels`.`sale`.`fiscal_year`,
          `classicmodels`.`sale`.`employee_number`
        order by
          `classicmodels`.`sale`.`fiscal_year`
        */
        System.out.println(
                ctx.select(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, max(SALE.SALE_))
                        .from(SALE)
                        .groupBy(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER)
                        .orderBy(SALE.FISCAL_YEAR)
                        .fetch()
        );
    }

    /*
    insert into
      `classicmodels`.`employee` (
        `employee_number`,`last_name`,`first_name`,`extension`,`email`,
        `office_code`,`salary`,`reports_to`,`job_title`)
    values
     (
       (
          select *
          from  
            (
              select
                max(
                      (`classicmodels`.`employee`.`employee_number` + ?)
                   )
              from
                `classicmodels`.`employee`
            ) as `t`
       ),
       ?,?,?,?,?,
       (
         select *
         from
          (
            select
              avg(`classicmodels`.`employee`.`salary`)
            from
              `classicmodels`.`employee`
          ) as `t`
       ),
       ?,?
     )
    */
    @Transactional
    public void insertEmployee() {

        System.out.println("Affected rows:"
                + ctx.insertInto(EMPLOYEE)
                        .values(select(max(EMPLOYEE.EMPLOYEE_NUMBER.plus(1))).from(EMPLOYEE),
                                "Mark", "Janel", "x4443", "markjanel@classicmodelcars.com", "1",
                                select(avg(EMPLOYEE.SALARY)).from(EMPLOYEE),
                                1002L, "VP Of Engineering")
                        .execute()
        );
    }

    /*
    delete from
      `classicmodels`.`payment`
    where
      `classicmodels`.`payment`.`customer_number` = (
         select
           `classicmodels`.`customer`.`customer_number`
         from
           `classicmodels`.`customer`
         where
           `classicmodels`.`customer`.`customer_name` = ?
    )
    */
    @Transactional
    public void deletePaymentsOfAtelierGraphique() {

        System.out.println("Affected rows:"
                + ctx.deleteFrom(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(
                                select(CUSTOMER.CUSTOMER_NUMBER).from(CUSTOMER)
                                        .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier graphique"))
                        )).execute()
        );
    }

    /*
    update
      `classicmodels`.`customer`
    set
      `classicmodels`.`customer`.`credit_limit` = (
        select
          sum(`classicmodels`.`payment`.`invoice_amount`)
        from
          `classicmodels`.`payment`
        where
          `classicmodels`.`payment`.`customer_number` = `classicmodels`.`customer`.`customer_number`
      )
    */
    @Transactional
    public void updateCustomerCreditLimit() {
        System.out.println("Affected rows:"
                + ctx.update(CUSTOMER)
                        .set(CUSTOMER.CREDIT_LIMIT,
                                select(sum(PAYMENT.INVOICE_AMOUNT)).from(PAYMENT)
                                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER)))
                        .execute()
        );

    }
}