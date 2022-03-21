package com.classicmodels.repository;

import static jooq.generated.Tables.PRODUCT_OF_PRODUCT_LINE;
import jooq.generated.tables.ProductOfProductLine;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import jooq.generated.tables.records.ProductOfProductLineRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Record;
import org.jooq.Table;
import static org.jooq.impl.DSL.name;
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

    public void selectFromTableValued() {       

        ProductOfProductLine popl = new ProductOfProductLine();
        Table<ProductOfProductLineRecord> t = popl.call("Trains");
        System.out.println("Field:\n "
                + ctx.select(t.field(name("p_name"))).from(t).fetch());

        Result<ProductOfProductLineRecord> r1 = ctx.fetch(popl.call("Trains"));
        System.out.println("Result (1):\n" + r1);

        Result<ProductOfProductLineRecord> r2 = ctx.selectFrom(popl.call("Trains")).fetch();
        System.out.println("Result: (2)\n" + r2);

        Result<Record> r3 = ctx.select().from(popl.call("Trains")).fetch();
        System.out.println("Result: (3)\n" + r3);
        
        ctx.select().from(PRODUCT_OF_PRODUCT_LINE.call("Trains")).fetch();
        ctx.selectFrom(PRODUCT_OF_PRODUCT_LINE.call("Trains")).fetch();

        ctx.select().from(PRODUCT_OF_PRODUCT_LINE(val("Trains"))).fetch();
        ctx.selectFrom(PRODUCT_OF_PRODUCT_LINE(val("Trains"))).fetch();

        ctx.select().from(popl.call("Trains"))
                .where(popl.call("Trains").P_NAME.like("1962%")).fetch();
        ctx.selectFrom(popl.call("Trains"))
                .where(popl.call("Trains").P_NAME.like("1962%")).fetch();
        ctx.select().from(PRODUCT_OF_PRODUCT_LINE.call("Trains"))
                .where(PRODUCT_OF_PRODUCT_LINE.P_NAME.like("1962%")).fetch();
        ctx.selectFrom(PRODUCT_OF_PRODUCT_LINE.call("Trains"))
                .where(PRODUCT_OF_PRODUCT_LINE.P_NAME.like("1962%")).fetch();

        ctx.select(PRODUCT_OF_PRODUCT_LINE.P_ID, PRODUCT_OF_PRODUCT_LINE.P_NAME)
                .from(PRODUCT_OF_PRODUCT_LINE.call("Classic Cars"))
                .where(PRODUCT_OF_PRODUCT_LINE.P_ID.gt(100L))
                .fetch();

        ctx.select(PRODUCT_OF_PRODUCT_LINE.P_ID, PRODUCT_OF_PRODUCT_LINE.P_NAME)
                .from(PRODUCT_OF_PRODUCT_LINE(val("Classic Cars")))
                .where(PRODUCT_OF_PRODUCT_LINE.P_ID.gt(100L))
                .fetch();
    }

    public void crossApplyFromTableValued() {

        // using call()
        ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION, PRODUCT_OF_PRODUCT_LINE.asterisk())
                .from(PRODUCTLINE).crossApply(PRODUCT_OF_PRODUCT_LINE.call("Classic Cars"))
                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT_OF_PRODUCT_LINE.P_LINE))
                .fetch();

        // using val()
        ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION, PRODUCT_OF_PRODUCT_LINE.asterisk())
                .from(PRODUCTLINE).crossApply(PRODUCT_OF_PRODUCT_LINE(val("Classic Cars")))
                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT_OF_PRODUCT_LINE.P_LINE))
                .fetch();
    }

    @Transactional
    public void outerApplyFromTableValued() {

        ctx.insertInto(PRODUCTLINE, PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE)
                .values("Helicopters", 855933L)
                .onDuplicateKeyIgnore()
                .execute();

        // since "Helicopters" has no products, CROSS APPLY will not fetch it             
        ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION, PRODUCT_OF_PRODUCT_LINE.asterisk())
                .from(PRODUCTLINE).crossApply(PRODUCT_OF_PRODUCT_LINE(PRODUCTLINE.PRODUCT_LINE))                
                .fetch();

        // OUTER APPLY returns "Helicopters" as well               
        ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION, PRODUCT_OF_PRODUCT_LINE.asterisk())
                .from(PRODUCTLINE).outerApply(PRODUCT_OF_PRODUCT_LINE(PRODUCTLINE.PRODUCT_LINE))                
                .fetch();
    }
}
