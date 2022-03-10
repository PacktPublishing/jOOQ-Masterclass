package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.cumeDist;
import static org.jooq.impl.DSL.denseRank;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.listAgg;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.mode;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.percentRank;
import static org.jooq.impl.DSL.percentileCont;
import static org.jooq.impl.DSL.percentileDisc;
import static org.jooq.impl.DSL.rank;
import static org.jooq.impl.DSL.select;
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

    // DENSE_RANK()
    public void denseRankSalesLeCertainSale() {

        ctx.select(
                denseRank(val(2004), val(10000))
                        .withinGroupOrderBy(SALE.FISCAL_YEAR.desc(), SALE.SALE_).as("sales_rank"))
                .from(SALE)
                .fetch();
        
        ctx.select().from(
                select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                        denseRank().over()
                                .orderBy(SALE.FISCAL_YEAR.desc(), SALE.SALE_).as("sales_rank"))
                        .from(SALE).asTable("t"))
                .where(field(name("t", "sales_rank"))
                        .le(select(denseRank(val(2004), val(10000))
                                .withinGroupOrderBy(SALE.FISCAL_YEAR.desc(), SALE.SALE_))
                                .from(SALE)))
                .fetch();

        // same thing but using QUALIFY        
        ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_)
                .from(SALE)
                .qualify(denseRank().over().orderBy(SALE.FISCAL_YEAR.desc(), SALE.SALE_)
                        .le(select(denseRank(val(2004), val(10000))
                                .withinGroupOrderBy(SALE.FISCAL_YEAR.desc(), SALE.SALE_))
                                .from(SALE)))
                .fetch();        
    }

    // PERCENT_RANK()    
    public void percentRankEmployeeSalary() {

        // What percentage of Sales Reps salaries are higher than $61,000 
        ctx.select(count().as("nr_of_salaries"),
                percentRank(val(61000d)).withinGroupOrderBy(
                        field(name("t", "salary")).desc()).mul(100).concat("%")
                        .as("salary_percentile_rank"))
                .from(selectDistinct(EMPLOYEE.SALARY.as("salary"))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.JOB_TITLE.eq("Sales Rep"))
                        .asTable("t"))
                .fetch();

        // What percentage of Sales Reps have salaries higher than $61,000 
        ctx.select(count().as("nr_of_salaries"),
                percentRank(val(61000d)).withinGroupOrderBy(
                        EMPLOYEE.SALARY.desc()).mul(100).concat("%")
                        .as("salary_percentile_rank"))
                .from(EMPLOYEE)
                .where(EMPLOYEE.JOB_TITLE.eq("Sales Rep"))
                .fetch();
    }

    // RANK() & CUME_DIST() 
    public void rankAndCumeDist() {

        ctx.select(
                rank(val(61000d)).withinGroupOrderBy(EMPLOYEE.SALARY).as("rank"))
                .from(EMPLOYEE)
                .fetch();

        ctx.select(
                cumeDist(val(61000)).withinGroupOrderBy(PRODUCT.QUANTITY_IN_STOCK).as("cume_dist"))
                .from(PRODUCT)
                .fetch();
    }

    // PERCENTILE_DISC() & PERCENTILE_CONT()
    public void percentileDiscCont() {

        ctx.select(
                percentileDisc(0.25).withinGroupOrderBy(SALE.SALE_).as("pd - 0.25"),
                percentileCont(0.25).withinGroupOrderBy(SALE.SALE_).as("pc - 0.25"),
                percentileDisc(0.5).withinGroupOrderBy(SALE.SALE_).as("pd - 0.50"),
                percentileCont(0.5).withinGroupOrderBy(SALE.SALE_).as("pc - 0.50"),
                percentileDisc(0.75).withinGroupOrderBy(SALE.SALE_).as("pd - 0.75"),
                percentileCont(0.75).withinGroupOrderBy(SALE.SALE_).as("pc - 0.75"),
                percentileDisc(1.0).withinGroupOrderBy(SALE.SALE_).as("pd - 1.0"),
                percentileCont(1.0).withinGroupOrderBy(SALE.SALE_).as("pc - 1.0"))
                .from(SALE)
                .fetch();

        ctx.select(
                percentileDisc(0.11).withinGroupOrderBy(SALE.SALE_).as("pd - 0.11"),
                percentileCont(0.11).withinGroupOrderBy(SALE.SALE_).as("pc - 0.11"))
                .from(SALE)
                .fetch();
    }

    // LIST_AGG()
    public void listAggEmployee() {

        ctx.select(
                listAgg(EMPLOYEE.FIRST_NAME).withinGroupOrderBy(EMPLOYEE.SALARY).as("listagg"))
                .from(EMPLOYEE)
                .fetch();

        ctx.select(
                listAgg(EMPLOYEE.FIRST_NAME, ";").withinGroupOrderBy(EMPLOYEE.SALARY).as("listagg"))
                .from(EMPLOYEE)
                .fetch();

        String result = ctx.select(
                listAgg(EMPLOYEE.FIRST_NAME, ",").withinGroupOrderBy(EMPLOYEE.SALARY).as("listagg"))
                .from(EMPLOYEE)
                .fetchOneInto(String.class);
        System.out.println("Result: " + result);

        ctx.select(
                listAgg(EMPLOYEE.FIRST_NAME).withinGroupOrderBy(EMPLOYEE.SALARY)
                        .filterWhere(EMPLOYEE.SALARY.gt(80000))
                        .as("listagg"))
                .from(EMPLOYEE)
                .fetch();

        ctx.select(
                listAgg(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME), ",")
                        .withinGroupOrderBy(EMPLOYEE.SALARY.desc(), EMPLOYEE.FIRST_NAME.desc()).as("employees"))
                .from(EMPLOYEE)
                .fetch();

        ctx.select(EMPLOYEE.JOB_TITLE, listAgg(EMPLOYEE.FIRST_NAME, ",")
                .withinGroupOrderBy(EMPLOYEE.FIRST_NAME).as("employees"))
                .from(EMPLOYEE)
                .groupBy(EMPLOYEE.JOB_TITLE)
                .orderBy(EMPLOYEE.JOB_TITLE)
                .fetch();

        ctx.select(ORDERDETAIL.ORDER_ID, listAgg(PRODUCT.PRODUCT_NAME, ",")
                .withinGroupOrderBy(PRODUCT.PRODUCT_NAME).as("products"))
                .from(ORDERDETAIL)
                .join(PRODUCT)
                .on(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                .groupBy(ORDERDETAIL.ORDER_ID)
                .fetch();
    }

    // MODE()
    public void modeOrderedSetAggregateFunction() {

        ctx.select(mode().withinGroupOrderBy(SALE.FISCAL_YEAR))
                .from(SALE)
                .fetch();

        ctx.select(mode().withinGroupOrderBy(ORDERDETAIL.QUANTITY_ORDERED.desc()))
                .from(ORDERDETAIL)
                .fetch();
    }

    public void modeFunctionsVs() {

        // mode() aggregation function (no explicit ordering)
        ctx.select(mode(SALE.FISCAL_MONTH).as("fiscal_month"))
                .from(SALE)
                .fetch();

        // mode() ordered set aggregate function (explicit ordering)
        ctx.select(mode().withinGroupOrderBy(SALE.FISCAL_MONTH.desc()).as("fiscal_month"))
                .from(SALE)
                .fetch();
    }

    public void modeOrderedSetAggregateFunctionEmulation() {

        // emulation of mode that returns all results (1)
        ctx.select(SALE.FISCAL_MONTH)
                .from(SALE)
                .groupBy(SALE.FISCAL_MONTH)
                .having(count().ge(all(select(count())
                        .from(SALE).groupBy(SALE.FISCAL_MONTH))))
                .fetch();

        // emulation of mode that returns all results (2)
        ctx.select(field("fiscal_month")).from(
                select(SALE.FISCAL_MONTH, count(SALE.FISCAL_MONTH).as("cnt1"))
                        .from(SALE)
                        .groupBy(SALE.FISCAL_MONTH))
                .where(field("cnt1").eq(
                        select(max(field("cnt2")))
                                .from(select(count(SALE.FISCAL_MONTH).as("cnt2"))
                                        .from(SALE).groupBy(SALE.FISCAL_MONTH))))
                .fetch();

        // emulation of mode using a percentage of the total number of occurrences
        ctx.select(avg(ORDERDETAIL.QUANTITY_ORDERED))
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.QUANTITY_ORDERED)
                .having(count().ge(all(select(count().mul(0.75))
                        .from(ORDERDETAIL).groupBy(ORDERDETAIL.QUANTITY_ORDERED))))
                .fetch();

        ctx.select(avg(ORDERDETAIL.QUANTITY_ORDERED))
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.QUANTITY_ORDERED)
                .having(count().ge(all(select(count().mul(0.95))
                        .from(ORDERDETAIL).groupBy(ORDERDETAIL.QUANTITY_ORDERED))))
                .fetch();
    }
}
