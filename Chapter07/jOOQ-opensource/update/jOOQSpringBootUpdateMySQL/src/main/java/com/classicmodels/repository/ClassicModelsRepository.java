package com.classicmodels.repository;

import com.classicmodels.pojo.OfficePart;
import java.math.BigDecimal;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.OfficeRecord;
import org.jooq.DSLContext;
import org.jooq.UpdateQuery;
import org.jooq.conf.ExecuteWithoutWhere;
import org.jooq.conf.Settings;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
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
    update `classicmodels`.`office`
    set
      `classicmodels`.`office`.`city` = ?,
      `classicmodels`.`office`.`country` = ?
    where
      `classicmodels`.`office`.`office_code` = ?
     */
    public void updateOffice() {

        System.out.println("EXAMPLE 1.1 (affected rows): "
                + ctx.update(OFFICE)
                        .set(OFFICE.CITY, "Banesti")
                        .set(OFFICE.COUNTRY, "Romania")
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .execute()
        );

        UpdateQuery uq = ctx.updateQuery(OFFICE);
        uq.addValue(OFFICE.CITY, "Craiova");
        uq.addValue(OFFICE.COUNTRY, "Romania");
        uq.addConditions(OFFICE.OFFICE_CODE.eq("1"));
        // uq.execute();
        System.out.println("EXAMPLE 1.2 (query): " + uq.getSQL());
    }

    // EXAMPLE 2
    /*
    update
      `classicmodels`.`office`
    set
      `classicmodels`.`office`.`city` = ?,
      `classicmodels`.`office`.`country` = ?
    where
      `classicmodels`.`office`.`office_code` = ?
     */
    public void updateRowOffice() {

        System.out.println("EXAMPLE 2 (affected rows): "
                + ctx.update(OFFICE)
                        .set(row(OFFICE.CITY, OFFICE.COUNTRY),
                                row("Hamburg", "Germany"))
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    update `classicmodels`.`customer`
    set
      `classicmodels`.`customer`.`credit_limit` = 
        (
          select
            max(`classicmodels`.`payment`.`invoice_amount`)
          from
            `classicmodels`.`payment`
          where
            `classicmodels`.`customer`.`customer_number` = `classicmodels`.`payment`.`customer_number`
        )
    where
      `classicmodels`.`customer`.`credit_limit` > ?
     */
    public void updateCustomerCreditLimitAsMaxPaymentInvoice() {

        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.update(CUSTOMER)
                        .set(CUSTOMER.CREDIT_LIMIT,
                                select(max(PAYMENT.INVOICE_AMOUNT)).from(PAYMENT)
                                        .where(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)))
                        .where(CUSTOMER.CREDIT_LIMIT.gt(BigDecimal.ZERO))
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    update
     `classicmodels`.`employee`
    set
     `classicmodels`.`employee`.`salary` = (
     `classicmodels`.`employee`.`salary` + (
       select
         (count(`classicmodels`.`sale`.`sale`) * ?)
       from
         `classicmodels`.`sale`
       where
         `classicmodels`.`employee`.`employee_number` = `classicmodels`.`sale`.`employee_number`
       )
    )
     */
    public void updateEmployeeSalaryBySaleCount() {

        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(
                                select(count(SALE.SALE_).multiply(5.75)).from(SALE)
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)).asField()))
                        .execute()
        );
    }

    // EXAMPLE 5
    /*
    update 
      `classicmodels`.`sale` 
    set 
      `classicmodels`.`sale`.`hot` = (
        `classicmodels`.`sale`.`fiscal_year` > ?
      ) 
    where 
      `classicmodels`.`sale`.`employee_number` = ?    
     */
    public void updateSaleHot() {

        System.out.println("EXAMPLE 5 (affected rows): "
                + ctx.update(SALE)
                        .set(SALE.HOT.coerce(Boolean.class), field(SALE.FISCAL_YEAR.gt(2004)))
                        .where(SALE.EMPLOYEE_NUMBER.eq(1370L))
                        .execute()
        );
    }

    // EXAMPLE 6
    public void updateNewRecordOffice() {

        /* approach 1 */
        OfficeRecord or = new OfficeRecord();
        or.setCity("Constanta");
        or.setCountry("Romania");

        /*
        update
          `classicmodels`.`office`
        set
          `classicmodels`.`office`.`city` = ?,
          `classicmodels`.`office`.`country` = ?
        where
          `classicmodels`.`office`.`office_code` = ?
         */
        System.out.println("EXAMPLE 6.1 (affected rows): "
                + ctx.update(OFFICE)
                        .set(or)
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .execute()
        );

        /* approach 2 */
        System.out.println("EXAMPLE 6.2 (affected rows): "
                + ctx.executeUpdate(or, OFFICE.OFFICE_CODE.eq("1"))
        );

        /* approach 3 */
        /*
        update
          `classicmodels`.`office`
        set
          `classicmodels`.`office`.`office_code` = ?,
          `classicmodels`.`office`.`city` = ?,
          `classicmodels`.`office`.`country` = ?
        where
          `classicmodels`.`office`.`office_code` = ?
         */
        System.out.println("EXAMPLE 6.3 (affected rows): "
                + ctx.newRecord(OFFICE)
                        .value1("1") // the ID is present in the WHERE clause
                        .value2("Parma")
                        .value7("Italy")
                        .update()
        );

        /* approach 4 */
        OfficePart op = new OfficePart("Madrid", "Spain");
        OfficeRecord orFromOp = new OfficeRecord();

        orFromOp.from(op);

        /*
        update
          `classicmodels`.`office`
        set
          `classicmodels`.`office`.`city` = ?,
          `classicmodels`.`office`.`country` = ?
        where
          `classicmodels`.`office`.`office_code` = ?
         */
        System.out.println("EXAMPLE 6.4 (affected rows): "
                + ctx.update(OFFICE)
                        .set(orFromOp)
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .execute()
        );
    }

    // EXAMPLE 7
    public void throwExceptionForUpdateWithoutWhereClause() {

        try {
            ctx.configuration().set(new Settings()
                    .withExecuteUpdateWithoutWhere(ExecuteWithoutWhere.THROW)) // check other options beside THROW
                    .dsl()
                    .update(OFFICE)
                    .set(OFFICE.CITY, "Banesti")
                    .set(OFFICE.COUNTRY, "Romania")
                    .execute();

            // in production, don't "swallow" the exception as here!
        } catch (org.jooq.exception.DataAccessException e) {
            System.out.println("Execute UPDATE without WHERE!");
        }
    }
}
