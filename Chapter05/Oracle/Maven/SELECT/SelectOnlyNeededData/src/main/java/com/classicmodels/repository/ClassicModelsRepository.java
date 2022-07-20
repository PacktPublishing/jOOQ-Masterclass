package com.classicmodels.repository;

import java.util.Map;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.Comparator;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
 
@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;
 
    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    // if all you need is a sub-set of columns then 
    // avoid (or, at least pay attention to) these approaches 
    // since they (may) fetches too much data (all columns)
    public void findOrderAllFields() {

        /*
        select 
          "CLASSICMODELS"."ORDER"."ORDER_ID", 
          "CLASSICMODELS"."ORDER"."ORDER_DATE", 
          "CLASSICMODELS"."ORDER"."REQUIRED_DATE", 
          "CLASSICMODELS"."ORDER"."SHIPPED_DATE", 
          "CLASSICMODELS"."ORDER"."STATUS", 
          "CLASSICMODELS"."ORDER"."COMMENTS", 
          "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER" 
        from 
          "CLASSICMODELS"."ORDER" 
        where 
          "CLASSICMODELS"."ORDER"."ORDER_ID" = ?
        */
        System.out.println("EXAMPLE 1.1\n" +
                ctx.select()
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
        
        System.out.println("EXAMPLE 1.2\n" +
                ctx.selectFrom(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );        

        System.out.println("EXAMPLE 1.3\n" +
                ctx.select(ORDER.fields())
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
        
        /*
        select 
          * 
        from 
          "CLASSICMODELS"."ORDER" 
        where 
          "CLASSICMODELS"."ORDER"."ORDER_ID" = ?        
        */
        System.out.println("EXAMPLE 1.4\n" +
                ctx.select(asterisk())
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
        
        /*
        select 
          "CLASSICMODELS"."ORDER"."ORDER_ID", 
          "CLASSICMODELS"."ORDER"."ORDER_DATE", 
          "CLASSICMODELS"."ORDER"."REQUIRED_DATE", 
          "CLASSICMODELS"."ORDER"."SHIPPED_DATE", 
          "CLASSICMODELS"."ORDER"."STATUS", 
          "CLASSICMODELS"."ORDER"."COMMENTS", 
          "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER", 
          "CLASSICMODELS"."ORDERDETAIL"."QUANTITY_ORDERED" 
        from 
          "CLASSICMODELS"."ORDER" 
          join "CLASSICMODELS"."ORDERDETAIL" on "CLASSICMODELS"."ORDER"."ORDER_ID" = "CLASSICMODELS"."ORDERDETAIL"."ORDER_ID" 
        where 
          "CLASSICMODELS"."ORDER"."ORDER_ID" = ?        
        */
        System.out.println("EXAMPLE 1.5\n"
                + ctx.select(ORDER.fields())
                        .select(ORDERDETAIL.QUANTITY_ORDERED)
                        .from(ORDER)
                        .innerJoin(ORDERDETAIL)
                        .on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID))
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
        
        /*
        select 
          "alias_3062530".* 
        from 
          (
            "CLASSICMODELS"."ORDER" 
            join "CLASSICMODELS"."CUSTOMER" "alias_3062530" 
             on "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER" = "alias_3062530"."CUSTOMER_NUMBER"
          ) 
        where 
          "CLASSICMODELS"."ORDER"."ORDER_ID" = ?        
        */
        System.out.println("EXAMPLE 1.6\n"
                + ctx.select(ORDER.customer().asterisk())                        
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
    }

    // EXAMPLE 2    
    // list only the columns (fields) that are needed
    /*
    select 
      "CLASSICMODELS"."ORDER"."ORDER_ID", 
      "CLASSICMODELS"."ORDER"."ORDER_DATE", 
      "CLASSICMODELS"."ORDER"."REQUIRED_DATE", 
      "CLASSICMODELS"."ORDER"."SHIPPED_DATE", 
      "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER" 
    from 
      "CLASSICMODELS"."ORDER" 
    where 
      "CLASSICMODELS"."ORDER"."ORDER_ID" = ?    
    */
    public void findOrderExplicitFields() {

        System.out.println("EXAMPLE 2\n" +
                ctx.select(ORDER.ORDER_ID, ORDER.ORDER_DATE,
                        ORDER.REQUIRED_DATE, ORDER.SHIPPED_DATE, ORDER.CUSTOMER_NUMBER)
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
    }

    // EXAMPLE 3
    // list the columns (fields) that should be skipped
    /*
    select 
      "CLASSICMODELS"."ORDER"."ORDER_ID", 
      "CLASSICMODELS"."ORDER"."ORDER_DATE", 
      "CLASSICMODELS"."ORDER"."REQUIRED_DATE", 
      "CLASSICMODELS"."ORDER"."SHIPPED_DATE", 
      "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER" 
    from 
      "CLASSICMODELS"."ORDER" 
    where 
      "CLASSICMODELS"."ORDER"."ORDER_ID" = ?    
    */
    public void findOrderAsteriskExcept() {

        System.out.println("EXAMPLE 3\n" +
                ctx.select(asterisk().except(ORDER.COMMENTS, ORDER.STATUS))
                        .from(ORDER)
                        .where(ORDER.ORDER_ID.eq(10101L))
                        .fetch()
        );
    }

    // EXAMPLE 4
    /*
    select 
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
      "CLASSICMODELS"."SALE".* 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
      join "CLASSICMODELS"."SALE" on "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"    
    */
    public void findOrderAndSale() {

        System.out.println("EXAMPLE 4\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.asterisk())
                        .from(EMPLOYEE)
                        .join(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    select 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."EXTENSION", 
      "CLASSICMODELS"."EMPLOYEE"."EMAIL", 
      "CLASSICMODELS"."EMPLOYEE"."SALARY", 
      "CLASSICMODELS"."EMPLOYEE"."REPORTS_TO", 
      "CLASSICMODELS"."EMPLOYEE"."JOB_TITLE", 
      "CLASSICMODELS"."SALE".* 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
      join "CLASSICMODELS"."SALE" on "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"   
    */
    public void findOrderExceptAndSale() {

        System.out.println("EXAMPLE 5\n" +
                ctx.select(EMPLOYEE.asterisk().except(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.OFFICE_CODE),
                        SALE.asterisk())
                        .from(EMPLOYEE)
                        .join(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .fetch()
        );
    }                          

    // EXAMPLE 6
    /*
    select 
      "CLASSICMODELS"."OFFICE"."CITY" "location", 
      "CLASSICMODELS"."OFFICE"."OFFICE_CODE", 
      "CLASSICMODELS"."OFFICE"."PHONE", 
      "CLASSICMODELS"."OFFICE"."ADDRESS_LINE_FIRST", 
      "CLASSICMODELS"."OFFICE"."ADDRESS_LINE_SECOND", 
      "CLASSICMODELS"."OFFICE"."STATE", 
      "CLASSICMODELS"."OFFICE"."COUNTRY", 
      "CLASSICMODELS"."OFFICE"."POSTAL_CODE", 
      "CLASSICMODELS"."OFFICE"."TERRITORY" 
    from 
      "CLASSICMODELS"."OFFICE"    
    */
    public void findOfficeUseAliasForCity() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select(OFFICE.CITY.as("location"),
                        OFFICE.asterisk().except(OFFICE.CITY))
                        .from(OFFICE)
                        .fetch()
        );
    }
    
    // EXAMPLE 7
    /*
    select 
      nvl("CLASSICMODELS"."OFFICE"."CITY", ?), 
      "CLASSICMODELS"."OFFICE"."OFFICE_CODE", 
      "CLASSICMODELS"."OFFICE"."PHONE", 
      "CLASSICMODELS"."OFFICE"."ADDRESS_LINE_FIRST", 
      "CLASSICMODELS"."OFFICE"."ADDRESS_LINE_SECOND", 
      "CLASSICMODELS"."OFFICE"."STATE", 
      "CLASSICMODELS"."OFFICE"."COUNTRY", 
      "CLASSICMODELS"."OFFICE"."POSTAL_CODE", 
      "CLASSICMODELS"."OFFICE"."TERRITORY" 
    from 
      "CLASSICMODELS"."OFFICE"        
    */
    public void findOfficeUseNvlForCity() {

        System.out.println("EXAMPLE 7\n"
                + ctx.select(nvl(OFFICE.CITY, "N/A"),
                        OFFICE.asterisk().except(OFFICE.CITY))
                        .from(OFFICE)
                        .fetch()
        );
    } 
    
    // EXAMPLE 8
    /*
    select 
      case when "CLASSICMODELS"."SALE"."SALE" > ? then 1 when not ("CLASSICMODELS"."SALE"."SALE" > ?) 
         then 0 end "saleGt5000", 
      "CLASSICMODELS"."SALE"."SALE_ID", 
      "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
      "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" 
    from 
      "CLASSICMODELS"."SALE"    
    */
    public void findSaleGt5000() {
        
        System.out.println("EXAMPLE 8\n"
                + ctx.select(field(SALE.SALE_.gt(5000.0)).as("saleGt5000"),                        
                        SALE.asterisk().except(SALE.SALE_))
                        .from(SALE)
                        .fetch()
        );
    }
    
    // EXAMPLE 9
    /*
    select 
      ("CLASSICMODELS"."SALE"."SALE" * ?) "saleMul025", 
      "CLASSICMODELS"."SALE"."SALE_ID", 
      "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
      "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" 
    from 
      "CLASSICMODELS"."SALE"    
    */
    public void findSaleMul025() {
        
        System.out.println("EXAMPLE 9\n"
                + ctx.select(field(SALE.SALE_.mul(0.25)).as("saleMul025"),                        
                        SALE.asterisk().except(SALE.SALE_))
                        .from(SALE)
                        .fetch()
        );
    }
    
    // EXAMPLE 10
    /*
    select 
      "CLASSICMODELS"."OFFICE"."CITY", 
      "CLASSICMODELS"."OFFICE"."COUNTRY", 
      "CLASSICMODELS"."OFFICE"."OFFICE_CODE" 
    from 
      "CLASSICMODELS"."OFFICE" 
    where 
      "CLASSICMODELS"."OFFICE"."CITY" in (?, ?, ?) 
    order by 
      case "CLASSICMODELS"."OFFICE"."CITY" when ? then 0 when ? then 1 when ? then 2 end asc    
    */
    // Consider reading: https://blog.jooq.org/2014/05/07/how-to-implement-sort-indirection-in-sql/
    public void findOfficeInCityByCertainSort() {

        String[] citiesArr = {"Paris", "Tokyo", "Boston"};
        System.out.println("EXAMPLE 10.1\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE)
                        .from(OFFICE)
                        .where(OFFICE.CITY.in(citiesArr))
                        .orderBy(OFFICE.CITY.sortAsc(citiesArr)) 
                        .fetch()
        );
        
        Map<String, Integer> citiesMap = Map.of("Paris", 1, "Tokyo", 3, "Boston", 2);
        System.out.println("EXAMPLE 10.2\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE)
                        .from(OFFICE)
                        .where(OFFICE.CITY.in(citiesMap.keySet()))
                        .orderBy(OFFICE.CITY.sort(citiesMap)) 
                        .fetch()
        );      
    }
    
    // EXAMPLE 11
    /*
    select 
      "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER", 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."EXTENSION", 
      "CLASSICMODELS"."EMPLOYEE"."EMAIL", 
      "CLASSICMODELS"."EMPLOYEE"."OFFICE_CODE", 
      "CLASSICMODELS"."EMPLOYEE"."SALARY", 
      "CLASSICMODELS"."EMPLOYEE"."REPORTS_TO", 
      "CLASSICMODELS"."EMPLOYEE"."JOB_TITLE" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    where 
      "CLASSICMODELS"."EMPLOYEE"."SALARY" in (
        select 
          "CLASSICMODELS"."EMPLOYEE"."SALARY" 
        from 
          "CLASSICMODELS"."EMPLOYEE" 
        where 
          "CLASSICMODELS"."EMPLOYEE"."SALARY" < ?
      ) 
    order by 
      "CLASSICMODELS"."EMPLOYEE"."SALARY"    
     */
    public void findEmployeeSalary60000(boolean isSaleRep) {

        System.out.println("EXAMPLE 11\n"
                + ctx.select()
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.compare(isSaleRep ? Comparator.IN : Comparator.NOT_IN,
                                select(EMPLOYEE.SALARY).from(EMPLOYEE).where(EMPLOYEE.SALARY.lt(60000))))
                        .orderBy(EMPLOYEE.SALARY)
                        .fetch()
        );
    }
    
    // EXAMPLE 12
    /*
    select 
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."SALARY" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    order by "CLASSICMODELS"."EMPLOYEE"."SALARY"
    fetch next ? rows only    
    */
    public void findEmployeeLimit() {

        System.out.println("EXAMPLE 12\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .orderBy(EMPLOYEE.SALARY)
                        .limit(10)
                        .fetch()
        );
    }
    
    // EXAMPLE 13    
    public void findEmployeeLimitOffset() {

        /*
        select 
          "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
          "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
          "CLASSICMODELS"."EMPLOYEE"."SALARY" 
        from 
          "CLASSICMODELS"."EMPLOYEE" 
        order by "CLASSICMODELS"."EMPLOYEE"."SALARY"
        offset ? rows fetch next ? rows only    
        */
        System.out.println("EXAMPLE 13.1\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .orderBy(EMPLOYEE.SALARY)
                        .limit(10)
                        .offset(5)
                        .fetch()
        );
        
        /*
        
        */
        System.out.println("EXAMPLE 13.2\n"
                + ctx.select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.QUANTITY_ORDERED)
                        .from(ORDERDETAIL)
                        .orderBy(ORDERDETAIL.QUANTITY_ORDERED)                        
                        .limit(field(select(min(ORDERDETAIL.QUANTITY_ORDERED)).from(ORDERDETAIL)))                        
                        .fetch()
        );        
    }
    
    // EXAMPLE 14
    /*
    select 
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."SALARY" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    order by "CLASSICMODELS"."EMPLOYEE"."SALARY"
    offset ? rows fetch next ? rows only    
    */
    public void findEmployeeLimitAndOffset() {

        System.out.println("EXAMPLE 14\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .orderBy(EMPLOYEE.SALARY)
                        .limit(5, 10)                        
                        .fetch()
        );
    }
    
    public void limit1InJoinedTable() {

        /*
        select 
          "CLASSICMODELS"."PRODUCTLINE"."PRODUCT_LINE", 
          "CLASSICMODELS"."PRODUCTLINE"."CODE", 
          "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME", 
          "CLASSICMODELS"."PRODUCT"."QUANTITY_IN_STOCK", 
          "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" 
        from 
          "CLASSICMODELS"."PRODUCTLINE" 
          join "CLASSICMODELS"."PRODUCT" on "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" = (
            select 
              "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" 
            from 
              "CLASSICMODELS"."PRODUCT" 
            where 
              "CLASSICMODELS"."PRODUCTLINE"."PRODUCT_LINE" = "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE" 
            order by 
              "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" fetch next ? rows only
          )
         */
        System.out.println("EXAMPLE 15\n"
                + ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE,
                        PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK, PRODUCT.PRODUCT_ID)
                        .from(PRODUCTLINE)
                        .join(PRODUCT)
                        .on(PRODUCT.PRODUCT_ID.eq(select(PRODUCT.PRODUCT_ID).from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                                .orderBy(PRODUCT.PRODUCT_ID).limit(1)))
                        .fetch()
        );
    }

    // EXAMPLE 16
    /*
    select 
      "v0" "CITY", 
      "v1" "COUNTRY", 
      "v2" "JOB_TITLE", 
      "v3" "CUSTOMER_NUMBER", 
      "v4" "CUSTOMER_NAME", 
      "v5" "PHONE", 
      "v6" "SALES_REP_EMPLOYEE_NUMBER", 
      "v7" "CREDIT_LIMIT", 
      "v8" "CUSTOMER_NUMBER", 
      "v9" "CHECK_NUMBER", 
      "v10" "PAYMENT_DATE", 
      "v11" "INVOICE_AMOUNT", 
      "v12" "CACHING_DATE" 
    from 
      (
        select 
          "x"."v0", 
          "x"."v1", 
          "x"."v2", 
          "x"."v3", 
          "x"."v4", 
          "x"."v5", 
          "x"."v6", 
          "x"."v7", 
          "x"."v8", 
          "x"."v9", 
          "x"."v10", 
          "x"."v11", 
          "x"."v12", 
          rownum "rn" 
        from 
          (
            select 
              "CLASSICMODELS"."OFFICE"."CITY" "v0", 
              "CLASSICMODELS"."OFFICE"."COUNTRY" "v1", 
              "CLASSICMODELS"."EMPLOYEE"."JOB_TITLE" "v2", 
              "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NUMBER" "v3", 
              "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NAME" "v4", 
              "CLASSICMODELS"."CUSTOMER"."PHONE" "v5", 
              "CLASSICMODELS"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER" "v6", 
              "CLASSICMODELS"."CUSTOMER"."CREDIT_LIMIT" "v7", 
              "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER" "v8", 
              "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER" "v9", 
              "CLASSICMODELS"."PAYMENT"."PAYMENT_DATE" "v10", 
              "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT" "v11", 
              "CLASSICMODELS"."PAYMENT"."CACHING_DATE" "v12" 
            from 
              "CLASSICMODELS"."OFFICE", 
              "CLASSICMODELS"."EMPLOYEE", 
              "CLASSICMODELS"."CUSTOMER", 
              "CLASSICMODELS"."PAYMENT"
          ) "x" 
        where 
          rownum <= (0 + ?)
      ) 
    where 
      "rn" > 0    
    */
    public void decomposeSelect() {
        
        SelectQuery select = ctx.select()
                .from(OFFICE, EMPLOYEE, CUSTOMER, PAYMENT).limit(100).getQuery();
                
        select.addSelect(OFFICE.CITY, OFFICE.COUNTRY);
        select.addSelect(EMPLOYEE.JOB_TITLE);
        select.addSelect(CUSTOMER.asterisk().except(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME));
        select.addSelect(PAYMENT.fields());
        
        System.out.println("EXAMPLE 16\n" +
                select.fetch()
        );
    }
        
    // EXAMPLE 17
    /*    
    select 
      "v0" "CITY", 
      "v1" "COUNTRY", 
      "v2" "JOB_TITLE", 
      "v3" "CUSTOMER_NUMBER", 
      "v4" "CUSTOMER_NAME", 
      "v5" "PHONE", 
      "v6" "SALES_REP_EMPLOYEE_NUMBER", 
      "v7" "CREDIT_LIMIT", 
      "v8" "CUSTOMER_NUMBER", 
      "v9" "CHECK_NUMBER", 
      "v10" "PAYMENT_DATE", 
      "v11" "INVOICE_AMOUNT", 
      "v12" "CACHING_DATE" 
    from 
      (
        select 
          "x"."v0", 
          "x"."v1", 
          "x"."v2", 
          "x"."v3", 
          "x"."v4", 
          "x"."v5", 
          "x"."v6", 
          "x"."v7", 
          "x"."v8", 
          "x"."v9", 
          "x"."v10", 
          "x"."v11", 
          "x"."v12", 
          rownum "rn" 
        from 
          (
            select 
              "CLASSICMODELS"."OFFICE"."CITY" "v0", 
              "CLASSICMODELS"."OFFICE"."COUNTRY" "v1", 
              "CLASSICMODELS"."EMPLOYEE"."JOB_TITLE" "v2", 
              "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NUMBER" "v3", 
              "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NAME" "v4", 
              "CLASSICMODELS"."CUSTOMER"."PHONE" "v5", 
              "CLASSICMODELS"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER" "v6", 
              "CLASSICMODELS"."CUSTOMER"."CREDIT_LIMIT" "v7", 
              "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER" "v8", 
              "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER" "v9", 
              "CLASSICMODELS"."PAYMENT"."PAYMENT_DATE" "v10", 
              "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT" "v11", 
              "CLASSICMODELS"."PAYMENT"."CACHING_DATE" "v12" 
            from 
              "CLASSICMODELS"."OFFICE", 
              "CLASSICMODELS"."EMPLOYEE", 
              "CLASSICMODELS"."CUSTOMER", 
              "CLASSICMODELS"."PAYMENT"
          ) "x" 
        where 
          rownum <= (0 + ?)
      ) 
    where 
      "rn" > 0    
    */    
    public void decomposeSelectAndFrom() {
        
        SelectQuery select = ctx.select().limit(100).getQuery();
                        
        select.addFrom(OFFICE);        
        select.addSelect(OFFICE.CITY, OFFICE.COUNTRY);
        
        select.addFrom(EMPLOYEE);
        select.addSelect(EMPLOYEE.JOB_TITLE);
        
        select.addFrom(CUSTOMER);
        select.addSelect(CUSTOMER.asterisk().except(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME));
        
        select.addFrom(PAYMENT);
        select.addSelect(PAYMENT.fields());
        
        System.out.println("EXAMPLE 17\n" +
                select.fetch()
        );
    }    
}