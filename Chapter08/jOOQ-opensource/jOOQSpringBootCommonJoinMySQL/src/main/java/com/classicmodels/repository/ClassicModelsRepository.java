package com.classicmodels.repository;

import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectCount;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.val;
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

        // 1.3 and 1.4 render the same SQL
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
    }

    // EXAMPLE 2 - typical LEFT OUTER JOIN
    public void fetchEmployeeNameSaleLeftOuterJoin() {

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
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.isNull())
                        .fetch()
        );
    }

    /* MySQL doesn't support FULL JOIN */
    // EXAMPLE 6 - emulate FULL OUTER JOIN via UNION    
    public void fetchOfficeCustomerdetailFullOuterJoinViaUnion() {

        System.out.println("EXAMPLE 6 (FULL OUTER JOIN via UNION)\n"
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

    // EXAMPLE 7 - emulate FULL OUTER JOIN (EXCLUSIVE) via UNION
    public void fetchOfficeCustomerdetailFullOuterJoinExclusiveViaUnion() {

        System.out.println("EXAMPLE 7 (FULL OUTER JOIN (EXCLUSIVE) via UNION)\n"
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

    // EXAMPLE 8
    @Transactional
    public void updateEmployeeOfficeInnerJoin() {

        System.out.println("EXAMPLE 8 (UPDATE & JOIN)\n"
                + ctx.update(EMPLOYEE
                        .innerJoin(OFFICE)
                        .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE)))
                        .set(EMPLOYEE.EXTENSION, "xParis")
                        .set(OFFICE.STATE, "N/A")
                        .where(OFFICE.CITY.eq("Paris"))
                        .execute()
        );
    }

    // EXAMPLE 9    
    public void crossJoinFirst2EmployeeFirst2Office() {

        System.out.println("EXAMPLE 9\n"
                + ctx.select()
                        .from(
                                select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME).from(EMPLOYEE)
                                        .limit(2)
                                        .asTable().crossJoin(select().from(OFFICE).limit(2))
                        )
                        .fetch()
        );
    }

    // EXAMPLE 10
    public void innerJoinFirst5EmployeeFirst5Office() {

        System.out.println("EXAMPLE 10\n"
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
                                        .on(field("a").eq(field("b")))
                        )
                        .fetch()
        );
    }

    // EXAMPLE 11
    @Transactional
    public void insertOfficesInEachCountryOfCustomer() {

        System.out.println("EXAMPLE 11\n"
                + ctx.insertInto(OFFICE)
                        .select(selectDistinct(CUSTOMERDETAIL.CUSTOMER_NUMBER.coerce(String.class),
                                CUSTOMERDETAIL.CITY, val("N/A"),
                                CUSTOMERDETAIL.ADDRESS_LINE_FIRST, CUSTOMERDETAIL.ADDRESS_LINE_SECOND,
                                CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.COUNTRY,
                                val("N/A"), val("N/A")).from(CUSTOMERDETAIL)
                                .leftOuterJoin(OFFICE)
                                .on(CUSTOMERDETAIL.COUNTRY.eq(OFFICE.COUNTRY))
                                .where(OFFICE.COUNTRY.isNull()))
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 12 (Division via JOIN - find all orders containing at least the products from the given order (e.g., 10100))
    public void fetchOrderContainingAtLeastCertainProducts() {

        System.out.println("EXAMPLE 12\n"
                + ctx.select(field("OID"))
                        .from(
                                select(PRODUCT.PRODUCT_ID.as("P1")).from(PRODUCT).asTable("T1")
                                        .innerJoin(select(ORDERDETAIL.ORDER_ID.as("OID"),
                                                ORDERDETAIL.PRODUCT_ID.as("P2")).from(ORDERDETAIL).asTable("T2"))
                                        .on(field("P1").eq(field("P2")))
                                        .innerJoin(select(ORDERDETAIL.PRODUCT_ID.as("P3"))
                                                .from(ORDERDETAIL).where(ORDERDETAIL.ORDER_ID.eq(10100L)))
                                        .on(field("P1").eq(field("P3"))))
                        .groupBy(field("OID"))
                        .having(count().eq(selectCount()
                                .from(ORDERDETAIL).where(ORDERDETAIL.ORDER_ID.eq(10100L))))
                        .fetch()
        );
    }
}
