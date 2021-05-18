package com.classicmodels.repository;

import java.math.BigInteger;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.cumeDist;
import static org.jooq.impl.DSL.denseRank;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.listAgg;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.percentRank;
import static org.jooq.impl.DSL.percentileCont;
import static org.jooq.impl.DSL.percentileDisc;
import static org.jooq.impl.DSL.rank;
import static org.jooq.impl.DSL.select;
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
    }

    // RANK()
    public void rankEmployeeSalary() {

        ctx.select(count().as("salary_nr"),
                rank(val(61000)).withinGroupOrderBy(field(name("t", "salary")).desc()).as("salary_rank"))
                .from(select(EMPLOYEE.SALARY.as("salary"))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.JOB_TITLE.eq("Sales Rep"))
                        .groupBy(EMPLOYEE.SALARY)
                        .asTable("t"))
                .fetch();
    }

    // PERCENT_RANK() & CUME_DIST() 
    public void percentRankAndCumeDist() {

        ctx.select(
                percentRank(val(61000)).withinGroupOrderBy(EMPLOYEE.SALARY).as("percentile_rank"))
                .from(EMPLOYEE)
                .fetch();

        ctx.select(
                cumeDist(val(61000)).withinGroupOrderBy(PRODUCT.QUANTITY_IN_STOCK).as("cume_dist"))
                .from(PRODUCT)
                .fetch();
    }

    // PERCENTILE_DISC() & PERCENTILE_CONT()
    public void percentileDiscContEmployeePerOffice() {

        ctx.select(
                percentileDisc(0.25).withinGroupOrderBy(SALE.SALE_).as("0.25"),
                percentileDisc(0.5).withinGroupOrderBy(SALE.SALE_).as("0.50"),
                percentileDisc(0.75).withinGroupOrderBy(SALE.SALE_).as("0.75"),
                percentileDisc(1.0).withinGroupOrderBy(SALE.SALE_).as("1.0"))
                .from(SALE)
                .fetch();

        ctx.select().from(
                select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                        EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                        percentileDisc(0.5).withinGroupOrderBy(EMPLOYEE.SALARY)
                                .over().partitionBy(OFFICE.OFFICE_CODE).as("percentile_cont"))
                        .from(OFFICE)
                        .join(EMPLOYEE)
                        .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE)).asTable("t"))
                .where(field(name("t", "percentile_cont"))
                        .le(select(percentileCont(0.5).withinGroupOrderBy(EMPLOYEE.SALARY))
                                .from(EMPLOYEE)))
                .fetch();

        ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                percentileDisc(0.25).withinGroupOrderBy(SALE.SALE_)
                        .over().partitionBy(SALE.FISCAL_YEAR).as("0.25"),
                percentileDisc(0.5).withinGroupOrderBy(SALE.SALE_)
                        .over().partitionBy(SALE.FISCAL_YEAR).as("0.50"),
                percentileDisc(0.75).withinGroupOrderBy(SALE.SALE_)
                        .over().partitionBy(SALE.FISCAL_YEAR).as("0.75"),
                percentileDisc(1.0).withinGroupOrderBy(SALE.SALE_)
                        .over().partitionBy(SALE.FISCAL_YEAR).as("1.0"))
                .from(SALE)
                .fetch();

        ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                percentileCont(0.5).withinGroupOrderBy(EMPLOYEE.SALARY)
                        .over().partitionBy(OFFICE.OFFICE_CODE),
                percentileDisc(0.5).withinGroupOrderBy(EMPLOYEE.SALARY)
                        .over().partitionBy(OFFICE.OFFICE_CODE))
                .from(OFFICE)
                .join(EMPLOYEE)
                .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                .fetch();
    }

    // LIST_AGG()
    public void listAggEmployee() {

        ctx.select(
                listAgg(EMPLOYEE.FIRST_NAME).withinGroupOrderBy(EMPLOYEE.SALARY).as("list_agg"))
                .from(EMPLOYEE)
                .fetch();
        
        ctx.select(
                listAgg(EMPLOYEE.FIRST_NAME).withinGroupOrderBy(EMPLOYEE.SALARY)
                        .filterWhere(EMPLOYEE.SALARY.gt(BigInteger.valueOf(80000)))
                        .as("list_agg"))
                .from(EMPLOYEE)
                .fetch();

        ctx.select(
                listAgg(concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME), ",")
                        .withinGroupOrderBy(EMPLOYEE.SALARY.desc(), EMPLOYEE.FIRST_NAME.desc()).as("employees"))
                .from(EMPLOYEE)
                .fetch();
    } 
}
