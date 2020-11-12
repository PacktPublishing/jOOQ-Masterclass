package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Payment.PAYMENT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.select;
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
      `classicmodels`.`employee`
    set
      `classicmodels`.`employee`.`salary` = (
        `classicmodels`.`employee`.`salary` + (
          select *
          from
            (
              select
                min(`classicmodels`.`employee`.`salary`)
              from
                `classicmodels`.`employee`
              where
                `classicmodels`.`employee`.`reports_to` = ?
            ) as `t`
        )
      )
    */
    @Transactional
    public void updateEmployeeSalary() {
        System.out.println("Affected rows:"
                + ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY,
                                EMPLOYEE.SALARY.plus(select(min(EMPLOYEE.SALARY)).from(EMPLOYEE)
                                        .where(EMPLOYEE.REPORTS_TO.eq(1002L)).asField()))
                        .execute()
        );

    }
}