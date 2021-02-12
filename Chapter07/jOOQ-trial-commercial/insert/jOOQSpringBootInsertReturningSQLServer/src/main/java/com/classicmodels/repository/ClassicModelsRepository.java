package com.classicmodels.repository;

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
import static org.jooq.impl.DSL.val;
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
      [fiscal_year], [sale], [employee_number]
    ) output [inserted].[sale_id] into @result 
    values 
      (?, ?, ?);
    
    select 
      [r].[sale_id] 
    from 
      @result [r];
     */
    public void returnOneId() {

        // Record1<Long>
        var insertedId = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                .values(2004, 2311.42, 1370L)
                .returningResult(SALE.SALE_ID)
                .fetchOne();

        System.out.println("EXAMPLE 1 (inserted id): \n" + insertedId); // as Long, insertedId.value1()
    }

    // EXAMPLE 2
    /*
    declare @result table ([sale_id] bigint);
    
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number]
    ) output [inserted].[sale_id] into @result 
    values 
      (?, ?, ?), 
      (?, ?, ?), 
      (?, ?, ?);
    
    select 
      [r].[sale_id] 
    from 
      @result [r];    
     */
    public void returnMultipleIds() {

        // Result<Record1<Long>>
        var insertedIds = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                .values(2004, 2311.42, 1370L)
                .values(2003, 900.21, 1504L)
                .values(2005, 1232.2, 1166L)
                .returningResult(SALE.SALE_ID)
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

        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(CUSTOMERDETAIL)
                        .values(ctx.insertInto(CUSTOMER,
                                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE)
                                .values("Ltd. AirRoads", "Kyle", "Doyle", "+ 44 321 321")
                                .returningResult(CUSTOMER.CUSTOMER_NUMBER).fetchOne().value1(),
                                "No. 14 Avenue", default_(), "Los Angeles", default_(), default_(), "USA")
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

        var inserted = ctx.insertInto(MANAGER, MANAGER.MANAGER_NAME)
                .values(select(concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME))
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
      [created_on] date
    );
    
    merge into [classicmodels].[dbo].[productline] using (
      (
        select 
          ?, 
          ? 
        union all 
        select 
          ?, 
          ?
      )
    ) [t] (
      [product_line], [text_description]
    ) on [classicmodels].[dbo].[productline].[product_line] = [t].[product_line] when not matched then insert (
      [product_line], [text_description]
    ) 
    values 
      (
        [t].[product_line], [t].[text_description]
      );
    
    merge into @result [r] using (
      (
        select 
          * 
        from 
          [classicmodels].[dbo].[productline]
      )
    ) [s] on [r].[product_line] = [s].[product_line] when matched then 
    update 
    set 
      [r].[created_on] = [s].[created_on];
    select 
      [r].[product_line], 
      [r].[created_on] 
    from 
      @result [r];    
     */
    public void insertAndReturnMultipleColsProductline() {

        // Result<Record2<String, LocalDate>>
        var inserted = ctx.insertInto(PRODUCTLINE, PRODUCTLINE.PRODUCT_LINE, 
                PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CODE)
                .values("Electric Vans", "This new line of electric vans ...", 983423L)
                .values("Turbo N Cars", "This new line of turbo N cars ...", 193384L)
                .onDuplicateKeyIgnore()
                .returningResult(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CREATED_ON)
                .fetch();

        System.out.println("EXAMPLE 5 (inserted ids and employee numbers): \n" + inserted);
    }

    // EXAMPLE 6
    /*
    declare @result table (
      [product_line] varchar(50), 
      [text_description] varchar(4000), 
      [html_description] varchar(max), 
      [image] varbinary(max), 
      [created_on] date
    );
    
    merge into [classicmodels].[dbo].[productline] using (
      (
        select 
          ?, 
          ? 
        union all 
        select 
          ?, 
          ?
      )
    ) [t] (
      [product_line], [text_description]
    ) on [classicmodels].[dbo].[productline].[product_line] = [t].[product_line] when not matched then insert (
      [product_line], [text_description]
    ) 
    values 
      (
        [t].[product_line], [t].[text_description]
      );
    
    merge into @result [r] using (
      (
        select 
          * 
        from 
          [classicmodels].[dbo].[productline]
      )
    ) [s] on [r].[product_line] = [s].[product_line] when matched then 
    update 
    set 
      [r].[text_description] = [s].[text_description], 
      [r].[html_description] = [s].[html_description], 
      [r].[image] = [s].[image], 
      [r].[created_on] = [s].[created_on];
    select 
      [r].[product_line], 
      [r].[text_description], 
      [r].[html_description], 
      [r].[image], 
      [r].[created_on] 
    from 
      @result [r];    
     */
    public void insertAndReturnAllColsProductline() {

        // Result<ProductlineRecord>
        var inserted = ctx.insertInto(PRODUCTLINE, PRODUCTLINE.PRODUCT_LINE, 
                PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CODE)
                .values("Master Vans", "This new line of master vans ...", 983423L)
                .values("Cool Cars", "This new line of cool cars ...", 193384L)
                .onDuplicateKeyIgnore()
                .returning()
                .fetch();

        System.out.println("EXAMPLE 6 (inserted ids and employee numbers): \n" + inserted);
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
      [r].[department_id] 
    from 
      @result [r];    
     */
    public void insertReturningAndSerialInDepartment() {

        // Record1<Integer>
        var inserted = ctx.insertInto(DEPARTMENT, DEPARTMENT.NAME,
                DEPARTMENT.PHONE, DEPARTMENT.CODE, DEPARTMENT.OFFICE_CODE)
                .values("Marketing", "+2 311 312", Short.valueOf("5432"), "5")
                .returningResult(DEPARTMENT.DEPARTMENT_ID)
                .fetchOne();

        System.out.println("EXAMPLE 7 (inserted id): \n" + inserted);
    }
}