package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.aggregate;
import static org.jooq.impl.DSL.boolAnd;
import static org.jooq.impl.DSL.boolOr;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.every;
import static org.jooq.impl.DSL.sqrt;
import static org.jooq.impl.DSL.stddevPop;
import static org.jooq.impl.DSL.stddevSamp;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.varPop;
import static org.jooq.impl.DSL.varSamp;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // Standard deviation
    public void sdSale() {

        ctx.select(stddevSamp(SALE.SALE_)) // Sample standard deviation
                .from(SALE)
                .fetch();

        ctx.select(SALE.FISCAL_YEAR, stddevSamp(SALE.SALE_)) // Sample standard deviation
                .from(SALE)
                .groupBy(SALE.FISCAL_YEAR)
                .fetch();

        ctx.select(SALE.FISCAL_YEAR,
                stddevSamp(SALE.SALE_).as("samp"), // Sample standard deviation
                stddevPop(SALE.SALE_).as("pop1"), // Population standard deviation
                sqrt(varPop(SALE.SALE_)).as("pop2")) // Population standard deviation equivalent
                .from(SALE)
                .groupBy(SALE.FISCAL_YEAR)
                .fetch();

        ctx.select(SALE.FISCAL_YEAR,
                stddevSamp(SALE.SALE_).over().partitionBy(SALE.FISCAL_YEAR).orderBy(SALE.FISCAL_YEAR).as("samp"),
                stddevPop(SALE.SALE_).over().partitionBy(SALE.FISCAL_YEAR).orderBy(SALE.FISCAL_YEAR).as("pop1"),
                sqrt(varPop(SALE.SALE_).over().partitionBy(SALE.FISCAL_YEAR).orderBy(SALE.FISCAL_YEAR)).as("pop2"))
                .from(SALE)
                .fetch();
    }

    // Variance is defined as the standard deviation squared    
    public void productBuyPriceVariance() {

        Field<BigDecimal> x = PRODUCT.BUY_PRICE;

        ctx.select((count().mul(sum(x.mul(x))).minus(sum(x).mul(sum(x)))) // Sample Variance computed via sum() and count()
                .divide(count().mul(count().minus(1))).as("VAR_SAMP"))
                .from(PRODUCT)
                .fetch();

        ctx.select(varSamp(x)) // Sample Variance
                .from(PRODUCT)
                .fetch();

        ctx.select(varPop(x)) // Population Variance
                .from(PRODUCT)
                .fetch();

        ctx.select(SALE.FISCAL_YEAR,
                varPop(SALE.SALE_).over().partitionBy(SALE.FISCAL_YEAR).orderBy(SALE.FISCAL_YEAR).as("pop"),
                varSamp(SALE.SALE_).over().partitionBy(SALE.FISCAL_YEAR).orderBy(SALE.FISCAL_YEAR).as("samp"))
                .from(SALE)
                .fetch();
    }

    public void covarianceSale() {

        ctx.select(aggregate("covar_samp", Double.class, SALE.SALE_).as("covar_samp"),
                aggregate("covar_pop", Double.class, SALE.SALE_).as("covar_pop"))
                .from(SALE)
                .fetch();
    }

    // using bool_and() / bool_or()
    public void boolAndOrSample() {

        ctx.select(boolAnd(EMPLOYEE.SALARY.gt(77000)),
                every(EMPLOYEE.SALARY.gt(77000))) // the standard SQL version of the BOOL_AND function
                .from(EMPLOYEE)
                .where(EMPLOYEE.JOB_TITLE.like("Sales Manager%"))
                .fetch();

        ctx.select(SALE.FISCAL_YEAR,
                boolAnd(SALE.SALE_.gt(5000d)).as("sale_gt_5000_and"),
                boolOr(SALE.SALE_.gt(10000d)).as("sale_gt_10000_or"))
                .from(SALE)
                .groupBy(SALE.FISCAL_YEAR)
                .fetch();
    }

    // alea cu biti
    // collect
    // median
}
