package com.classicmodels.repository;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void selectProducts() {

        System.out.println("MYSQL: \n"
                + ctx.configuration().derive(SQLDialect.MYSQL).dsl().render(ctx.parser().parseQuery("""
        select product_line, regr_sxy(buy_price, msrp) as xy 
        from product 
        group by product_line
        """))
        );
        String sql = ctx.configuration().derive(SQLDialect.MYSQL).dsl().render(ctx.parser().parseQuery("""
        select product_line, regr_sxy(buy_price, msrp) as xy 
        from product 
        group by product_line
        """));
        ctx.resultQuery(sql).fetch();
        
        System.out.println("POSTGRES: \n"
                + ctx.configuration().derive(SQLDialect.POSTGRES).dsl().render(ctx.parser().parseQuery("""
        select product_line, regr_sxy(buy_price, msrp) as xy
        from product 
        group by product_line
        """))
        );
        
        System.out.println("SQLSERVER: \n"
                + ctx.configuration().derive(SQLDialect.SQLSERVER).dsl().render(ctx.parser().parseQuery("""
        select product_line, regr_sxy(buy_price, msrp) as xy 
        from product 
        group by product_line
        """))
        );
        
        System.out.println("ORACLE: \n"
                + ctx.configuration().derive(SQLDialect.ORACLE).dsl().render(ctx.parser().parseQuery("""
        select product_line, regr_sxy(buy_price, msrp) as xy 
        from product 
        group by product_line
        """))
        );
    }
}
