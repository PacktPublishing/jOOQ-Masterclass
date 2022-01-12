package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.values;
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
    public void selectCommonlyUsedValues1() {

        // select count(*)
        System.out.println("EXAMPLE 1.1\n"
                + ctx.selectCount().fetch()
        );

        // select 0 as `zero`
        System.out.println("EXAMPLE 1.2\n"
                + ctx.selectZero().fetch()
        );

        // select 1 as `one`
        System.out.println("EXAMPLE 1.3\n"
                + ctx.selectOne().fetch()
        );
    }

    // EXAMPLE 2
    public void selectCommonlyUsedValues2() {

        // select count(*)
        System.out.println("EXAMPLE 2.1\n"
                + ctx.select(count()).fetch()
        );

        // select 0
        System.out.println("EXAMPLE 2.2\n"
                + ctx.select(inline(0)).fetch()
        );

        // A standard SQL way to do "DUAL" would be       
        System.out.println("EXAMPLE 2.3\n"
                + ctx.select(val(1).as("one")).fetch() // select 1 as `one`
                + ctx.fetchValue((val(1).as("one"))) // select 1 as `one`
                + ctx.select().from(values(row(1)).as("t", "one")).fetch() // select `t`.`one` from (select null as `one` where false union all select * from (values row (1)) as `t`) as `t`
        );
        
        System.out.println("EXAMPLE 2.4\n"
                // select 1 as `A`, 'John' as `B`, 4333 as `C`, false as `D`
                + ctx.select(val(1).as("A"), val("John").as("B"),
                        val(4333).as("C"), val(false).as("D")).fetch()
                // select `t`.`A`, `t`.`B`, `t`.`C`, `t`.`D` from (select null as `A`, null as `B`,
                // null as `C`, null as `D` from dual where false union all select * from 
                // (values row ('A', 'John', 4333, false)) as `t`) as `t`
                + ctx.select().from(values(row("A", "John", 4333, false))
                        .as("t", "A", "B", "C", "D")).fetch()
        );
    }

    // EXAMPLE 3
    public void selectCommonlyUsedValues3() {

        // select 1 as `one` from `classicmodels`.`manager`        
        System.out.println("EXAMPLE 3.1\n"
                + ctx.selectOne().from(MANAGER).fetch()
        );

        // select 1 as `one` from `classicmodels`.`customer`, `classicmodels`.`customerdetail`
        System.out.println("EXAMPLE 3.2\n"
                + ctx.selectOne().from(CUSTOMER, CUSTOMERDETAIL).fetch()
        );

        // select 1 as `one` from `classicmodels`.`customer` as `c`, `classicmodels`.`customerdetail` as `cd`
        System.out.println("EXAMPLE 3.3\n"
                + ctx.selectOne().from(CUSTOMER.as("c"), CUSTOMERDETAIL.as("cd")).fetch()
        );

        // select 1 as `one`
        System.out.println("EXAMPLE 3.4\n"
                + DSL.using(SQLDialect.MYSQL).selectOne().getSQL()
        );
    }

    // EXAMPLE 4
    // real usage of SELECT 1
    @Transactional
    public void deleteSales() {

        System.out.println("EXAMPLE 4\n"
                + ctx.deleteFrom(SALE)
                        .where(exists(selectOne().from(EMPLOYEE) // or, whereExists()
                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER)
                                        .and(EMPLOYEE.JOB_TITLE.ne("Sales Rep")))))
                        .execute()
        );
    }
}
