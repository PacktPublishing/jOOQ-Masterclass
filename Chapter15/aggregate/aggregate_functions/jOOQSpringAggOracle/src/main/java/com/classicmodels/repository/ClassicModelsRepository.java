package com.classicmodels.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.aggregate;
import static org.jooq.impl.DSL.boolAnd;
import static org.jooq.impl.DSL.boolOr;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.every;
import static org.jooq.impl.DSL.median;
import static org.jooq.impl.DSL.regrSXY;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sqrt;
import static org.jooq.impl.DSL.stddevPop;
import static org.jooq.impl.DSL.stddevSamp;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.val;
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

    // Covariance
    public void covarianceProductBuyPriceMSRP() {

        ctx.select(PRODUCT.PRODUCT_LINE,
                aggregate("covar_samp", Double.class, PRODUCT.BUY_PRICE, PRODUCT.MSRP).as("covar_samp"), // Covariance of the sample
                aggregate("covar_pop", Double.class, PRODUCT.BUY_PRICE, PRODUCT.MSRP).as("covar_pop")) // Covariance of the population
                .from(PRODUCT)
                .groupBy(PRODUCT.PRODUCT_LINE)
                .fetch();

        // (SUMXY-SUMX * SUMY/N)/(N-1) -> covar_samp() equivalent
        ctx.select(PRODUCT.PRODUCT_LINE,
                (sum(PRODUCT.BUY_PRICE.mul(PRODUCT.MSRP))
                        .minus(sum(PRODUCT.BUY_PRICE).mul(sum(PRODUCT.MSRP)
                                .divide(count())))).divide(count().minus(1)).as("covar_samp"))
                .from(PRODUCT)
                .groupBy(PRODUCT.PRODUCT_LINE)
                .fetch();
    }

    // Correlation(regression) functions
    public void regressionProductBuyPriceMSRP() {

        ctx.select(PRODUCT.PRODUCT_LINE,
                (regrSXY(PRODUCT.BUY_PRICE, PRODUCT.MSRP)).as("regr_sxy"))
                .from(PRODUCT)
                .groupBy(PRODUCT.PRODUCT_LINE)
                .fetch();

        // emulating regrSXY() as REGR_SXY = (SUMXY-SUMX * SUMY/N)
        ctx.select(PRODUCT.PRODUCT_LINE,
                sum((PRODUCT.BUY_PRICE).mul(PRODUCT.MSRP))
                        .minus(sum(PRODUCT.BUY_PRICE).mul(sum(PRODUCT.MSRP)
                                .divide(count()))).as("regr_sxx"))
                .from(PRODUCT)
                .groupBy(PRODUCT.PRODUCT_LINE)
                .fetch();

        // see also: regrSXX(),regrSYY(), regrAvgX​(), regrAvgXY(), regrCount(), regrIntercept(), regrR2(), regrSlope()
    }

    // Using bool_and() / bool_or()
    public void boolAndOrSample() {

        ctx.select(boolAnd(EMPLOYEE.SALARY.gt(BigInteger.valueOf(77000))),
                every(EMPLOYEE.SALARY.gt(BigInteger.valueOf(77000)))) // the standard SQL version of the BOOL_AND function
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

    // Bits operations
    public void bitsOperationsSample() {

        ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE, PRODUCT.MSRP,
                ORDERDETAIL.PRICE_EACH, ORDERDETAIL.QUANTITY_ORDERED)
                .from(PRODUCT)
                .join(ORDERDETAIL)
                .on(PRODUCT.PRODUCT_ID.bitXor(ORDERDETAIL.PRODUCT_ID).eq(0L)
                        .and(PRODUCT.MSRP.bitXor(ORDERDETAIL.PRICE_EACH).eq(BigDecimal.ZERO)))
                .fetch();

        /*
        // Replacing bits
        
                    j    i     
        q =      1001100110010
        p =         111111
        result = 1001111110010 (5106)                 
         */
        
        int i = 4;
        int j = 9;
        int q = 4914;
        int p = 63;

        Field<Integer> ones = val(0).bitNot().as("ones");
        Field<Integer> leftShiftJ = ones.shl(j + 1).as("leftShiftJ");
        Field<Integer> leftShiftI = val(1).shl(i).minus(1).as("leftShiftI");
        Field<Integer> mask = leftShiftJ.bitOr(leftShiftI).as("mask");
        Field<Integer> applyMaskToQ = val(q).bitAnd(mask).as("applyMaskToQ");
        Field<Integer> bringPInPlace = val(p).shl(i).as("bringPInPlace");
                 
        ctx.select(applyMaskToQ.bitOr(bringPInPlace)).from(
                select(applyMaskToQ, bringPInPlace).from(
                        select(mask).from(select(leftShiftJ, leftShiftI).from(
                                select(ones))))).fetch();
    }

    // Median
    public void medianSample() {
        
        ctx.select(median(PRODUCT.QUANTITY_IN_STOCK))
                .from(PRODUCT)
                .fetch();
    }        
}


