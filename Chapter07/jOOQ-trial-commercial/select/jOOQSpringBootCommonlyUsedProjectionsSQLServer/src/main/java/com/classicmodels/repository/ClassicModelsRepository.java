package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Manager.MANAGER;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.inline;
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

    // EXAMPLE 1
    public void selectCommonlyUsedValues1() {

        // select count(*)
        System.out.println("EXAMPLE 1.1\n"
                + ctx.selectCount().fetch()
        );

        // select 0 [zero]
        System.out.println("EXAMPLE 1.2\n"
                + ctx.selectZero().fetch()
        );

        // select 1 [one]
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

        // select 1
        System.out.println("EXAMPLE 2.3\n"
                + ctx.select(val(1)).fetch()
        );
        
        // select 1 [A], 'John' [B], 4333 [C], 0 [D]
        System.out.println("EXAMPLE 2.4\n"
                + ctx.select(val(1).as("A"), val("John").as("B"), 
                        val(4333).as("C"), val(false).as("D")).fetch()
        );
    }

    // EXAMPLE 3
    public void selectCommonlyUsedValues3() {

        // select 1 [one] from [classicmodels].[dbo].[manager]
        System.out.println("EXAMPLE 3.1\n"
                + ctx.selectOne().from(MANAGER).fetch()
        );

        // select 1 [one] from [classicmodels].[dbo].[customer], [classicmodels].[dbo].[customerdetail]
        System.out.println("EXAMPLE 3.2\n"
                + ctx.selectOne().from(CUSTOMER, CUSTOMERDETAIL).fetch()
        );

        // select 1 [one] from [classicmodels].[dbo].[customer] [c], [classicmodels].[dbo].[customerdetail] [cd]
        System.out.println("EXAMPLE 3.3\n"
                + ctx.selectOne().from(CUSTOMER.as("c"), CUSTOMERDETAIL.as("cd")).fetch()
        );

        // select 1 [one]
        System.out.println("EXAMPLE 3.4\n"
                + DSL.using(SQLDialect.SQLSERVER).selectOne().getSQL()
        );
    }
}