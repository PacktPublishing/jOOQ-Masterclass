package com.classicmodels.repository;

import com.classicmodels.pojo.OfficePart;
import java.math.BigDecimal;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.OfficeRecord;
import org.jooq.DSLContext;
import org.jooq.Row2;
import org.jooq.UpdateQuery;
import org.jooq.conf.ExecuteWithoutWhere;
import org.jooq.conf.Settings;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.field;
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
    update 
      "public"."office" 
    set 
      "city" = ?, 
      "country" = ? 
    where 
      "public"."office"."office_code" = ?    
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
    public void updateRowOffice() {

        /*
        update 
          "public"."office" 
        set 
          ("city", "country") = row (?, ?) 
        where 
          "public"."office"."office_code" = ?    
        */
        System.out.println("EXAMPLE 2.1 (affected rows): "
                + ctx.update(OFFICE)
                        .set(row(OFFICE.CITY, OFFICE.COUNTRY),
                                row("Hamburg", "Germany"))
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .execute()
        );
        
        /*
        update 
          "public"."office" 
        set 
          ("city", "country") = row (?, ?) 
        where 
          (
            "public"."office"."city", "public"."office"."country"
          ) is null        
        */
        Row2<String, String> r1 = row(OFFICE.CITY, OFFICE.COUNTRY);
        Row2<String, String> r2 = row("Hamburg", "Germany");

        System.out.println("EXAMPLE 2.2 (affected rows): "
                + ctx.update(OFFICE)
                        .set(r1, r2)
                        .where(r1.isNull())
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    update 
      "public"."customer" 
    set 
      "credit_limit" = (
        select 
          max(
            "public"."payment"."invoice_amount"
          ) 
        from 
          "public"."payment" 
        where 
          "public"."customer"."customer_number" = "public"."payment"."customer_number"
      ) 
    where 
      "public"."customer"."credit_limit" > ?    
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
      "public"."employee" 
    set 
      "salary" = (
        "public"."employee"."salary" + (
          select 
            (
              count("public"."sale"."sale") * ?
            ) 
          from 
            "public"."sale" 
          where 
            "public"."employee"."employee_number" = "public"."sale"."employee_number"
        )
      )    
     */
    public void updateEmployeeSalaryBySaleCount() {

        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(
                                field(select(count(SALE.SALE_).multiply(5.75)).from(SALE)
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)))))
                        .execute()
        );
    }

    // EXAMPLE 5    
    public void updateNewRecordOffice() {

        /* approach 1 */
        OfficeRecord or = new OfficeRecord();
        or.setCity("Constanta");
        or.setCountry("Romania");

        /*
        update 
          "public"."office" 
        set 
          "city" = ?, 
          "country" = ? 
        where 
          "public"."office"."office_code" = ?        
         */
        System.out.println("EXAMPLE 5.1 (affected rows): "
                + ctx.update(OFFICE)
                        .set(or)
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .execute()
        );

        /* approach 2 */
        System.out.println("EXAMPLE 5.2 (affected rows): "
                + ctx.executeUpdate(or, OFFICE.OFFICE_CODE.eq("1"))
        );
        
        /* approach 3 */
        /*
        update 
          "public"."office" 
        set 
          "office_code" = ?, 
          "city" = ?, 
          "country" = ? 
        where 
          "public"."office"."office_code" = ?        
         */
        System.out.println("EXAMPLE 5.3 (affected rows): "
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
          "public"."office" 
        set 
          "city" = ?, 
          "country" = ? 
        where 
          "public"."office"."office_code" = ?        
         */
        System.out.println("EXAMPLE 5.4 (affected rows): "
                + ctx.update(OFFICE)
                        .set(orFromOp)
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .execute()
        );
        
        or.setOfficeCode("2");
        or.setCity("Campina");
        or.attach(ctx.configuration()); // attach the record to the current configuration
        System.out.println("EXAMPLE 5.5 (affected rows): "
                +or.update()
        );
    }

    // EXAMPLE 6
    /*
    update
      "public"."office"
    set
     (
       "address_line_first",
       "address_line_second",
       "phone"
      ) = (
         select
           "public"."employee"."first_name",
           "public"."employee"."last_name",
           ?
         from
           "public"."employee"
         where
           "public"."employee"."job_title" = ?
      )
     */
    public void updateOfficeAddressAsPresidentName() {

        System.out.println("EXAMPLE 6 (affected rows): "
                + ctx.update(OFFICE)
                        .set(row(OFFICE.ADDRESS_LINE_FIRST, OFFICE.ADDRESS_LINE_SECOND, OFFICE.PHONE),
                                select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, val("+40 0721 456 322"))
                                        .from(EMPLOYEE)
                                        .where(EMPLOYEE.JOB_TITLE.eq("President")))
                        .execute()
        );
    }

    // EXAMPLE 7
    /*
    update
      "public"."product"
    set
      "buy_price" = "public"."orderdetail"."price_each"
    from
      "public"."orderdetail"
    where
      "public"."product"."product_id" = "public"."orderdetail"."product_id"
     */
    public void updateProductBuyPriceWithFirstPriceEach() {

        System.out.println("EXAMPLE 7 (affected rows): "
                + ctx.update(PRODUCT)
                        .set(PRODUCT.BUY_PRICE, ORDERDETAIL.PRICE_EACH)
                        .from(ORDERDETAIL)
                        .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))
                        .execute()
        );
    }

    // EXAMPLE 8
    /*
    update
      "public"."office"
    set
      "city" = ?,
      "country" = ?
    where
      "public"."office"."office_code" = ? 
    returning 
      "public"."office"."city",
      "public"."office"."country"
     */
    public void updateOfficeReturning() {

        System.out.println("EXAMPLE 8: \n"
                + ctx.update(OFFICE)
                        .set(OFFICE.CITY, "Paris")
                        .set(OFFICE.COUNTRY, "France")
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .returningResult(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetchOne()
        );
    }

    // EXAMPLE 9
    /*
    update
      "public"."employee"
    set
      "salary" = (
        "public"."employee"."salary" + (
          select
            avg("public"."sale"."sale")
          from
            "public"."sale"
          where
            "public"."sale"."employee_number" = "public"."employee"."employee_number"
        )
    )
    where
      "public"."employee"."employee_number" = ? 
    returning 
      "public"."employee"."salary"
    
    update
      "public"."customer"
    set
      "credit_limit" = ("public"."customer"."credit_limit" + ?)
    where
      "public"."customer"."sales_rep_employee_number" = ?
     */
    public void updateEmployeeSalaryAsAvgSaleAndCustomersCreditAsDoubleSalary() {

        System.out.println("EXAMPLE 9 (affected rows): "
                + ctx.update(CUSTOMER)
                        .set(CUSTOMER.CREDIT_LIMIT, CUSTOMER.CREDIT_LIMIT.plus(
                                ctx.update(EMPLOYEE)
                                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(
                                                field(select(avg(SALE.SALE_)).from(SALE)
                                                        .where(SALE.EMPLOYEE_NUMBER
                                                                .eq(EMPLOYEE.EMPLOYEE_NUMBER)))))
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1504L))
                                        .returningResult(EMPLOYEE.SALARY.coerce(BigDecimal.class))
                                        .fetchOne().value1().multiply(BigDecimal.valueOf(2))))
                        .where(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER.eq(1504L))
                        .execute()
        );
    }

    // EXAMPLE 10
    /*
    update 
      "public"."sale" 
    set 
      "hot" = (
        "public"."sale"."fiscal_year" > ?
      ) 
    where 
      "public"."sale"."employee_number" = ?    
     */
    public void updateSaleHot() {

        System.out.println("EXAMPLE 10 (affected rows): "
                + ctx.update(SALE)
                        .set(SALE.HOT.coerce(Boolean.class), field(SALE.FISCAL_YEAR.gt(2004)))
                        .where(SALE.EMPLOYEE_NUMBER.eq(1370L))
                        .execute()
        );
    }

    // EXAMPLE 11
    public void throwExceptionForUpdateWithoutWhereClause() {

        try {
            ctx.configuration().derive(new Settings()
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