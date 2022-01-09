package com.classicmodels.repository;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.default_;
import static org.jooq.impl.DSL.inline;
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
    declare @result table ([sale_id] bigint);
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [revenue_growth], [fiscal_month]
    ) output [inserted].[sale_id] into @result 
    values 
      (?, ?, ?, ?, ?);
    select 
      [sale_id] 
    from 
      @result [r];
     */
    public void returnOneId() {

        // Record1<Long>
        var insertedId = ctx.insertInto(SALE, 
                SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.REVENUE_GROWTH, SALE.FISCAL_MONTH)
                .values(2004, 2311.42, 1370L, 10.12, 1)
                .returningResult(SALE.SALE_ID) // or, returningResult() to return whole fields
                .fetchOne(); 

        System.out.println("EXAMPLE 1 (inserted id): \n" + insertedId); // as Long, insertedId.value1()
    }

    // EXAMPLE 2
    /*
    declare @result table ([sale_id] bigint);
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [revenue_growth], [fiscal_month]
    ) output [inserted].[sale_id] into @result 
    values 
      (?, ?, ?, ?, ?), 
      (?, ?, ?, ?, ?), 
      (?, ?, ?, ?, ?);
    select 
      [sale_id] 
    from 
      @result [r];    
     */
    public void returnMultipleIds() {

        // Result<Record1<Long>>
        var insertedIds = ctx.insertInto(SALE, 
                SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.REVENUE_GROWTH, SALE.FISCAL_MONTH)
                .values(2004, 2311.42, 1370L, 12.50, 1)
                .values(2003, 900.21, 1504L, 23.99, 2)
                .values(2005, 1232.2, 1166L, 14.65, 3)
                .returningResult(SALE.SALE_ID) // or, returningResult() to return whole fields
                .fetch();

        System.out.println("EXAMPLE 2 (inserted ids): \n" + insertedIds);
    }

    // EXAMPLE 3
    /*
    declare @result table ([customer_number] bigint);
    
    insert into [classicmodels].[dbo].[customer] (
      [customer_name], [contact_first_name], 
      [contact_last_name], [phone]
    ) output [inserted].[customer_number] into @result 
    values 
      (?, ?, ?, ?);
    
    select 
      [r].[customer_number] 
    from 
      @result [r];    
     */
    public void insertReturningOfCustomerInCustomerDetail() {
        
        // Note: passing explicit "null" instead of default_() produces implementation specific behaviour

        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(CUSTOMERDETAIL)
                        .values(ctx.insertInto(CUSTOMER,
                                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE)
                                .values(UUID.randomUUID().toString(), // random customer_name
                                        "Kyle", "Doyle", "+ 44 321 321")
                                .returningResult(CUSTOMER.CUSTOMER_NUMBER).fetchOne().value1(),
                                UUID.randomUUID().toString(), // random address_line_first
                                default_(), "Los Angeles", default_(), default_(), "USA")
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    declare @result table ([manager_id] bigint);
    
    insert into [classicmodels].[dbo].[manager] ([manager_name]) output [inserted].[manager_id] into @result 
    values 
      (
        (
          select 
            (
              [classicmodels].[dbo].[employee].[first_name] + ? + [classicmodels].[dbo].[employee].[last_name]
            ) 
          from 
            [classicmodels].[dbo].[employee] 
          where 
            [classicmodels].[dbo].[employee].[employee_number] = ?
        )
      );
    
    select 
      [r].[manager_id] 
    from 
      @result [r];    
     */
    public void insertEmployeeInManagerReturningId() {

        // Result<Record1<Long>>
        var inserted = ctx.insertInto(MANAGER, MANAGER.MANAGER_NAME)
                .values(select(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1165L)).asField())
                .returningResult(MANAGER.MANAGER_ID)
                .fetch();

        System.out.println("EXAMPLE 4 (inserted ids): \n" + inserted);
    }

    // EXAMPLE 5
    /*
    declare @result table (
      [product_line] varchar(50), 
      [created_on] date, 
      [code] bigint
    );
    insert into [classicmodels].[dbo].[productline] (
      [product_line], [text_description], 
      [code]
    ) output [inserted].[product_line], 
    [inserted].[created_on], 
    [inserted].[code] into @result 
    values 
      (?, ?, ?), 
      (?, ?, ?);
    merge into @result [r] using (
      select 
        [classicmodels].[dbo].[productline].[product_line], 
        [classicmodels].[dbo].[productline].[created_on] [alias_77609720], 
        [classicmodels].[dbo].[productline].[code] 
      from 
        [classicmodels].[dbo].[productline]
    ) [s] on (
      [r].[product_line] = [s].[product_line] 
      and [r].[code] = [s].[code]
    ) when matched then 
    update 
    set 
      [created_on] = [s].[alias_77609720];
    select 
      [product_line], 
      [created_on] 
    from 
      @result [r];    
     */
    public void insertAndReturnMultipleColsProductline() {

        // Result<Record2<String, LocalDate>>
        var inserted = ctx.insertInto(PRODUCTLINE, PRODUCTLINE.PRODUCT_LINE, 
                PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CODE)
                .values(UUID.randomUUID().toString(), // random product_line
                        "This new line of electric vans ...", 983423L)
                .values(UUID.randomUUID().toString(), // random product_line 
                        "This new line of turbo N cars ...", 193384L)     
                .returningResult(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CREATED_ON)
                .fetch();

        System.out.println("EXAMPLE 5 (inserted product lines and created on): \n" + inserted);
    }

    // EXAMPLE 6
    /*
    declare @result table (
      [product_line] varchar(50), 
      [code] bigint, 
      [text_description] varchar(4000), 
      [html_description] xml, 
      [image] varbinary(max), 
      [created_on] date
    );
    insert into [classicmodels].[dbo].[productline] (
      [product_line], [text_description], 
      [code]
    ) output [inserted].[product_line], 
    [inserted].[code], 
    [inserted].[text_description], 
    [inserted].[html_description], 
    [inserted].[image], 
    [inserted].[created_on] into @result 
    values 
      (?, ?, ?), 
      (?, ?, ?);
    merge into @result [r] using (
      select 
        [classicmodels].[dbo].[productline].[product_line], 
        [classicmodels].[dbo].[productline].[code], 
        [classicmodels].[dbo].[productline].[text_description] [alias_46240548], 
        [classicmodels].[dbo].[productline].[html_description] [alias_117132646], 
        [classicmodels].[dbo].[productline].[image] [alias_80606203], 
        [classicmodels].[dbo].[productline].[created_on] [alias_77609720] 
      from 
        [classicmodels].[dbo].[productline]
    ) [s] on (
      [r].[product_line] = [s].[product_line] 
      and [r].[code] = [s].[code]
    ) when matched then 
    update 
    set 
      [text_description] = [s].[alias_46240548], 
      [html_description] = [s].[alias_117132646], 
      [image] = [s].[alias_80606203], 
      [created_on] = [s].[alias_77609720];
    select 
      [product_line], 
      [code], 
      [text_description], 
      [html_description], 
      [image], 
      [created_on] 
    from 
      @result [r];       
     */
    public void insertAndReturnAllColsProductline() {

        // Result<ProductlineRecord>
        var inserted = ctx.insertInto(PRODUCTLINE, PRODUCTLINE.PRODUCT_LINE, 
                PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CODE)
                .values(UUID.randomUUID().toString(), // random product_line
                        "This new line of electric vans ...", 983423L)
                .values(UUID.randomUUID().toString(), // random product_line 
                        "This new line of turbo N cars ...", 193384L)                  
                .returningResult()
                .fetch();

        System.out.println("EXAMPLE 6: \n" + inserted);
    }

    // EXAMPLE 7
    /*
    declare @result table ([department_id] bigint);
   insert into [classicmodels].[dbo].[department] (
     [name], [phone], [code], [office_code]
   ) output [inserted].[department_id] into @result 
   values 
     (?, ?, ?, ?);
   select 
     [department_id] 
   from 
     @result [r];  
     */
    public void insertReturningAndSerialInDepartment() {

        // Record1<Long>
        var inserted = ctx.insertInto(DEPARTMENT, DEPARTMENT.NAME,
                DEPARTMENT.PHONE, DEPARTMENT.CODE, DEPARTMENT.OFFICE_CODE)
                .values("Marketing", "+2 311 312", 
                        ThreadLocalRandom.current().nextInt(10000, 20000), // random code
                        "5")               
                .returningResult(DEPARTMENT.DEPARTMENT_ID)
                .fetchOne();

        System.out.println("EXAMPLE 7 (inserted id): \n" + inserted);
    }
}
