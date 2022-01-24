package com.classicmodels.repository;

import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
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

    // EXAMPLE 1 - typical INNER JOIN
    public void fetchEmployeeNameOfficeCityInnerJoin() {

        // 1.1 and 1.2 render the same SQL
        // force INNER to be rendered: ctx.configuration().set(new Settings().withRenderOptionalInnerKeyword(RenderOptionalKeyword.ON)).dsl() ...
        System.out.println("EXAMPLE 1.1 (INNER JOIN)\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, OFFICE.CITY)
                        .from(EMPLOYEE)
                        .innerJoin(OFFICE)
                        .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .fetch()
        );
        
        System.out.println("EXAMPLE 1.2 (INNER JOIN)\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, OFFICE.CITY)
                        .from(EMPLOYEE
                                .innerJoin(OFFICE)
                                .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE)))
                        .fetch()
        );
        
        System.out.println("EXAMPLE 1.3\n"
                + ctx.select()
                        .from(MANAGER)
                        .innerJoin(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .innerJoin(OFFICE)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .fetch()
        );
        
        System.out.println("EXAMPLE 1.4\n"
                + ctx.select()
                        .from(MANAGER
                                .innerJoin(OFFICE_HAS_MANAGER
                                        .innerJoin(OFFICE)
                                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)))
                                .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)))
                        .fetch()
        );

        // specify the order of joins via FORCE ORDER
        System.out.println("EXAMPLE 1.5\n"
                + ctx.select(PRODUCT.PRODUCT_ID, ORDER.ORDER_ID)
                        .from(PRODUCT)
                        .innerJoin(ORDERDETAIL)
                        .on(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                        .innerJoin(ORDER)
                        .on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID))
                        .option("OPTION (FORCE ORDER)")
                        .fetch()
        );
    }

    // EXAMPLE 2 - typical LEFT OUTER JOIN
    public void fetchEmployeeNameSaleLeftOuterJoin() {

        // force OUTER to be avoided: ctx.configuration().set(new Settings().withRenderOptionalOuterKeyword(RenderOptionalKeyword.OFF)).dsl() 
        System.out.println("EXAMPLE 2 (LEFT OUTER JOIN)\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE)
                        .leftOuterJoin(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .fetch()
        );
    }

    // EXAMPLE 3 - typical LEFT OUTER JOIN (EXCLUSIVE)
    public void fetchEmployeeNameSaleLeftOuterJoinExclusive() {
        
        System.out.println("EXAMPLE 3 (LEFT OUTER JOIN (EXCLUSIVE))\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE)
                        .leftOuterJoin(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .where(SALE.EMPLOYEE_NUMBER.isNull())
                        .fetch()
        );
    }

    // EXAMPLE 4 - typical RIGHT OUTER JOIN
    public void fetchEmployeeNameSaleRightOuterJoin() {
        
        System.out.println("EXAMPLE 4 (RIGHT OUTER JOIN)\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE)
                        .rightOuterJoin(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .fetch()
        );
    }

    // EXAMPLE 5 - typical RIGHT OUTER JOIN (EXCLUSIVE)
    public void fetchEmployeeNameSaleRightOuterJoinExclusive() {
        
        System.out.println("EXAMPLE 5 (RIGHT OUTER JOIN (EXCLUSIVE))\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE)
                        .rightOuterJoin(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .where(SALE.EMPLOYEE_NUMBER.isNull())
                        .fetch()
        );
    }

    // EXAMPLE 6 - FULL OUTER JOIN
    public void fetchOfficeCustomerdetailFullOuterJoin() {
        
        System.out.println("EXAMPLE 6 (FULL OUTER JOIN)\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE,
                        CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY, CUSTOMERDETAIL.CUSTOMER_NUMBER)
                        .from(OFFICE)
                        .fullOuterJoin(CUSTOMERDETAIL)
                        .on(OFFICE.CITY.eq(CUSTOMERDETAIL.CITY))
                        .fetch()
        );
    }

    // EXAMPLE 7 - FULL OUTER JOIN (EXCLUSIVE)
    public void fetchOfficeCustomerdetailFullOuterJoinExclusive() {
        
        System.out.println("EXAMPLE 7 (FULL OUTER JOIN (EXCLUSIVE))\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE,
                        CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY, CUSTOMERDETAIL.CUSTOMER_NUMBER)
                        .from(OFFICE)
                        .fullOuterJoin(CUSTOMERDETAIL)
                        .on(OFFICE.CITY.eq(CUSTOMERDETAIL.CITY))
                        .where(OFFICE.CITY.isNull().or(CUSTOMERDETAIL.CITY.isNull()))
                        .fetch()
        );
    }

    // EXAMPLE 8 - emulate FULL OUTER JOIN via UNION    
    public void fetchOfficeCustomerdetailFullOuterJoinViaUnion() {
        
        System.out.println("EXAMPLE 8 (FULL OUTER JOIN via UNION)\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE,
                        CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY, CUSTOMERDETAIL.CUSTOMER_NUMBER)
                        .from(OFFICE)
                        .leftOuterJoin(CUSTOMERDETAIL)
                        .on(OFFICE.CITY.eq(CUSTOMERDETAIL.CITY))
                        .union(select(OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE,
                                CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY, CUSTOMERDETAIL.CUSTOMER_NUMBER)
                                .from(OFFICE)
                                .rightOuterJoin(CUSTOMERDETAIL)
                                .on(OFFICE.CITY.eq(CUSTOMERDETAIL.CITY))
                                .where(OFFICE.CITY.isNull()))
                        .fetch()
        );
    }

    // EXAMPLE 9 - emulate FULL OUTER JOIN (EXCLUSIVE) via UNION
    public void fetchOfficeCustomerdetailFullOuterJoinExclusiveViaUnion() {
        
        System.out.println("EXAMPLE 9 (FULL OUTER JOIN (EXCLUSIVE) via UNION)\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE,
                        CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY, CUSTOMERDETAIL.CUSTOMER_NUMBER)
                        .from(OFFICE)
                        .leftOuterJoin(CUSTOMERDETAIL)
                        .on(OFFICE.CITY.eq(CUSTOMERDETAIL.CITY))
                        .where(CUSTOMERDETAIL.CITY.isNull())
                        .union(select(OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE,
                                CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY, CUSTOMERDETAIL.CUSTOMER_NUMBER)
                                .from(OFFICE)
                                .rightOuterJoin(CUSTOMERDETAIL)
                                .on(OFFICE.CITY.eq(CUSTOMERDETAIL.CITY))
                                .where(OFFICE.CITY.isNull()))
                        .fetch()
        );
    }

    // EXAMPLE 10
    public void crossJoinFirst2EmployeeFirst2Office() {
        
        System.out.println("EXAMPLE 10\n"
                + ctx.select()
                        .from(
                                select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME).from(EMPLOYEE)
                                        .limit(2)
                                        .asTable("t1").crossJoin(select().from(OFFICE).limit(2).asTable("t2"))
                        )
                        .fetch()
        );
    }

    // EXAMPLE 11
    public void innerJoinFirst5EmployeeFirst5Office() {
        
        System.out.println("EXAMPLE 11\n"
                + ctx.select()
                        .from(
                                select(EMPLOYEE.OFFICE_CODE.as("a"),
                                        EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME).from(EMPLOYEE)
                                        .orderBy(EMPLOYEE.SALARY)
                                        .limit(5)
                                        .asTable("at")
                                        .innerJoin(select(OFFICE.OFFICE_CODE.as("b"),
                                                OFFICE.CITY, OFFICE.COUNTRY).from(OFFICE)
                                                .orderBy(OFFICE.COUNTRY)
                                                .limit(5).asTable("bt"))
                                        .on(field(name("at", "a")).eq(field(name("bt", "b"))))
                        )
                        .fetch()
        );
    }
}
