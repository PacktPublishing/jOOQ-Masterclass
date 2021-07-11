package com.classicmodels.repository;

import java.math.BigDecimal;
import jooq.generated.Routines;
import static jooq.generated.Routines.getSalaryStat;
import static jooq.generated.Routines.netPriceEach;
import static jooq.generated.Routines.updateMsrp;
import jooq.generated.routines.GetSalaryStat;
import jooq.generated.routines.NetPriceEach;
import jooq.generated.routines.Swap;
import jooq.generated.routines.UpdateMsrp;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
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

    public void executeScalarFunction() {

        // EXECUTION 1
        NetPriceEach npe1 = new NetPriceEach();
        npe1.setQuantity(25);
        npe1.setListPrice(BigDecimal.valueOf(15.5));
        npe1.setDiscount(BigDecimal.valueOf(0.75));

        npe1.execute(ctx.configuration());

        System.out.println("Execution 1: " + npe1.getReturnValue());

        // EXECUTION 2
        NetPriceEach npe2 = new NetPriceEach();
        npe2.setQuantity(field(select(PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))));
        npe2.setListPrice(field(select(PRODUCT.MSRP)
                .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))));
        npe2.setDiscount(BigDecimal.valueOf(0.75));

        System.out.println("Execution 2:\n"
                + ctx.fetchValue(npe2.asField("netPriceEach"))); // or, ctx.select(npe2.asField("netPriceEach")).fetch()

        // EXECUTION 3
        BigDecimal npe3 = Routines.netPriceEach(
                ctx.configuration(), 25, BigDecimal.valueOf(15.5), BigDecimal.valueOf(0.75));

        System.out.println("Execution 3: " + npe3);

        // EXECUTION 4
        Field<BigDecimal> npe4 = Routines.netPriceEach(
                field(select(PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                field(select(PRODUCT.MSRP)
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                val(BigDecimal.valueOf(0.75)));

        System.out.println("Execution 4:\n"
                + ctx.fetchValue(npe4)); // or, ctx.select(npe4).fetch()

        // EXECUTION 5
        ctx.select(ORDERDETAIL.ORDER_ID,
                sum(netPriceEach(ORDERDETAIL.QUANTITY_ORDERED,
                        ORDERDETAIL.PRICE_EACH, val(BigDecimal.valueOf(0.75)))).as("sum_net_price"))
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.ORDER_ID)
                .orderBy(field(name("sum_net_price")).desc())
                .fetch();
    }

    public void executeOutParamFunction() {

        // EXECUTION 1
        GetSalaryStat salStat1 = new GetSalaryStat();
        salStat1.execute(ctx.configuration());

        System.out.println("Execution 1 (min sal): " + salStat1.getMinSal());
        System.out.println("Execution 1 (max sal): " + salStat1.getMaxSal());
        System.out.println("Execution 1 (avg sal): " + salStat1.getAvgSal());

        // EXECUTION 2
        GetSalaryStat salStat2 = Routines.getSalaryStat(ctx.configuration());
        System.out.println("Execution 2 (min sal): " + salStat2.getMinSal());
        System.out.println("Execution 2 (max sal): " + salStat2.getMaxSal());
        System.out.println("Execution 2 (avg sal): " + salStat2.getAvgSal());

        // EXECUTION 3
        ctx.select(val(getSalaryStat(ctx.configuration()).getMinSal()),
                val(getSalaryStat(ctx.configuration()).getMaxSal()),
                val(getSalaryStat(ctx.configuration()).getAvgSal())).fetch();
        ctx.fetchValue(val(Routines.getSalaryStat(ctx.configuration()).getMinSal()));

        // EXECUTION 4
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.coerce(BigDecimal.class)
                        .gt(getSalaryStat(ctx.configuration()).getAvgSal()))
                .fetch();
    }

    public void executeInOutParamFunction() {
        
        // EXECUTION 1
        Swap swap = new Swap();
        swap.setX(5);
        swap.setY(10);
        
        swap.execute(ctx.configuration());               
                
        System.out.println("Execution 1 (X): " + swap.getX());
        System.out.println("Execution 1 (Y): " + swap.getY());
    }
    
    @Transactional
    public void executeUpdateFunction() {
        
        // EXECUTION 1
        UpdateMsrp um = new UpdateMsrp();
        um.setProductId(1L);
        um.setDebit(10); 
        um.execute(ctx.configuration());               
        
        int newMsrp = um.getReturnValue();
        
        System.out.println("Msrp: " + newMsrp);
        
        // EXECUTION 2
        ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.MSRP)
                .from(PRODUCT)
                .where(updateMsrp(PRODUCT.PRODUCT_ID, inline(50)).gt(50))
                .fetch();
    }
}
