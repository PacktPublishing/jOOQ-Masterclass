package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.DailyActivity.DAILY_ACTIVITY;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.abs;
import static org.jooq.impl.DSL.aggregate;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.boolAnd;
import static org.jooq.impl.DSL.boolOr;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.every;
import static org.jooq.impl.DSL.exp;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.ln;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.product;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sqrt;
import static org.jooq.impl.DSL.stddevPop;
import static org.jooq.impl.DSL.stddevSamp;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.table;
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

    // Harmonic mean
    public void saleHarmonicMean() {

        ctx.select(SALE.FISCAL_YEAR, count().divide(sum(inline(1d).divide(SALE.SALE_))).as("harmonic_mean"))
                .from(SALE)
                .groupBy(SALE.FISCAL_YEAR)
                .fetch();
    }

    // Geometric mean
    public void saleGeometricMean() {

        ctx.select(SALE.FISCAL_YEAR, exp(avg(ln(SALE.SALE_))).as("geometric_mean"))
                .from(SALE)
                .groupBy(SALE.FISCAL_YEAR)
                .fetch();
    }

    // Standard deviation
    @Transactional
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

        // Compute z-scores        
        ctx.with("sales_stats").as(
                select(avg(DAILY_ACTIVITY.SALES).as("mean"),
                        stddevSamp(DAILY_ACTIVITY.SALES).as("sd")).from(DAILY_ACTIVITY))
                .with("visitors_stats").as(
                select(avg(DAILY_ACTIVITY.VISITORS).as("mean"),
                        stddevSamp(DAILY_ACTIVITY.VISITORS).as("sd")).from(DAILY_ACTIVITY))
                .select(DAILY_ACTIVITY.DAY_DATE,
                        abs(DAILY_ACTIVITY.SALES.minus(field(name("sales_stats", "mean"))))
                                .divide(field(name("sales_stats", "sd"), Float.class)).as("z_score_sales"),
                        abs(DAILY_ACTIVITY.VISITORS.minus(field(name("visitors_stats", "mean"))))
                                .divide(field(name("visitors_stats", "sd"), Float.class)).as("z_score_visitors"))
                .from(table("sales_stats"), table("visitors_stats"), DAILY_ACTIVITY)
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

        // (SUM(x*y) - SUM(x) * SUM(y) / COUNT(*)) / (COUNT(*) - 1) -> covar_samp() equivalent
        ctx.select(PRODUCT.PRODUCT_LINE,
                (sum(PRODUCT.BUY_PRICE.mul(PRODUCT.MSRP))
                        .minus(sum(PRODUCT.BUY_PRICE).mul(sum(PRODUCT.MSRP)
                                .divide(count())))).divide(count().minus(1)).as("covar_samp"))
                .from(PRODUCT)
                .groupBy(PRODUCT.PRODUCT_LINE)
                .fetch();

        // (SUM(x*y) - SUM(x) * SUM(y) / COUNT(*)) / COUNT(*) -> covar_pop() equivalent
        ctx.select(PRODUCT.PRODUCT_LINE,
                ((sum(PRODUCT.BUY_PRICE.mul(PRODUCT.MSRP))
                        .minus(sum(PRODUCT.BUY_PRICE).mul(sum(PRODUCT.MSRP)
                                .divide(count())))).divide(count())).as("covar_pop"))
                .from(PRODUCT)
                .groupBy(PRODUCT.PRODUCT_LINE)
                .fetch();
    }

    public void mySqlConcatws() {

        ctx.select(aggregate("concat_ws", String.class, inline(" "), EMPLOYEE.FIRST_NAME,
                EMPLOYEE.LAST_NAME).as("employee"))
                .from(EMPLOYEE)
                .fetch();
    }

    // Correlation(regression) functions
    public void regressionProductBuyPriceMSRP() {

        // emulating regrSXY() as REGR_SXY = SUM(X*Y)-SUM(X) * SUM(Y)/COUNT(*)
        ctx.select(PRODUCT.PRODUCT_LINE,
                sum(PRODUCT.BUY_PRICE.mul(PRODUCT.MSRP))
                        .minus(sum(PRODUCT.BUY_PRICE).mul(sum(PRODUCT.MSRP)
                                .divide(count()))).as("regr_sxy"))
                .from(PRODUCT)
                .groupBy(PRODUCT.PRODUCT_LINE)
                .fetch();

        // see also: regrSXX(),regrSYY(), regrAvgX​(), regrAvgXY(), regrCount(), regrIntercept(), regrR2(), regrSlope()
        // emulations:
        /*
        AVG(x) AS REGR_AVGX,
        AVG(y) AS REGR_AVGY,
        SUM(1) AS REGR_COUNT,
        AVG(y)-(COVAR_POP(y, x)/VAR_POP(x))*AVG(x) AS REGR_INTERCEPT,
        REGR_R2_COEF(VAR_POP(y), VAR_POP(x), CORR(y, x)) AS REGR_R2,
        COVAR_POP(y, x)/VAR_POP(x) AS REGR_SLOPE,
        SUM(1) * VAR_POP(x) AS REGR_SXX,
        SUM(1) * COVAR_POP(y, x) AS REGR_SXY,
        SUM(1) * VAR_POP(y) AS REGR_SYY
         */
    }

    // Calculating Linear Regression Coefficients
    // y = slope * x - intercept 
    public void linearRegression() {

        var t1 = select(PRODUCT.BUY_PRICE.as("x"), avg(PRODUCT.BUY_PRICE).over().as("x_bar"),
                PRODUCT.MSRP.as("y"), avg(PRODUCT.MSRP).over().as("y_bar")).from(PRODUCT)
                .asTable("t1");

        var t2 = select(((sum(t1.field("x", Double.class).minus(t1.field("x_bar"))
                .mul(t1.field("y", Double.class).minus(t1.field("y_bar")))))
                .divide(sum((t1.field("x", Double.class).minus(t1.field("x_bar")))
                        .mul(t1.field("x", Double.class).minus(t1.field("x_bar")))))).as("slope"),
                max(t1.field("x_bar")).as("x_bar_max"),
                max(t1.field("y_bar")).as("y_bar_max"))
                .from(t1).asTable("t2");

        ctx.select(t2.field("slope"),
                t2.field("y_bar_max").minus(t2.field("x_bar_max")
                        .mul(t2.field("slope", Double.class))).as("intercept"))
                .from(t2).fetch();
    }

    // Using bool_and() / bool_or()
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

    // Bits operations
    public void bitsOperationsSample() {

        ctx.selectFrom(SALE)
                .where(SALE.FISCAL_YEAR.bitXor(2004).eq(0))
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

        Field<Integer> ones = inline(0).bitNot().as("ones");
        Field<Integer> leftShiftJ = ones.shl(j + 1).as("leftShiftJ");
        Field<Integer> leftShiftI = inline(1).shl(i).minus(1).as("leftShiftI");
        Field<Integer> mask = leftShiftJ.bitOr(leftShiftI).as("mask");
        Field<Integer> applyMaskToQ = val(q).bitAnd(mask).as("applyMaskToQ");
        Field<Integer> bringPInPlace = val(p).shl(i).as("bringPInPlace");

        ctx.select(applyMaskToQ.bitOr(bringPInPlace)).from(
                select(applyMaskToQ, bringPInPlace).from(
                        select(mask).from(select(leftShiftJ, leftShiftI).from(
                                select(ones))))).fetch();
    }

    // synthetic PRODUCT
    // the compounded month growth rate via geometric mean as
    // (PRODUCT(1+SALE.REVENUE_GROWTH)))^(1/COUNT())
    public void cmgrSale() {

        ctx.select(SALE.FISCAL_YEAR,
                round((product(one().plus(SALE.REVENUE_GROWTH.divide(100)))
                        .power(one().divide(count()))).mul(100), 2).concat("%").as("CMGR"))
                .from(SALE)
                .groupBy(SALE.FISCAL_YEAR)
                .fetch();
    }
}
