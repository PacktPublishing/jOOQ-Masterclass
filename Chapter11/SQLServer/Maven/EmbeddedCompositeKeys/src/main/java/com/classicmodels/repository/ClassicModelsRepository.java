package com.classicmodels.repository;

import java.util.List;
import jooq.generated.embeddables.pojos.EmbeddedProductlinePk;
import jooq.generated.embeddables.records.EmbeddedProductlinePkRecord;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Productlinedetail.PRODUCTLINEDETAIL;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void fetchProductline() {

        // Result<Record2<EmbeddedProductlinePkRecord, LocalDate>>
        var result1 = ctx.select(PRODUCTLINE.PRODUCTLINE_PK, PRODUCTLINE.CREATED_ON)
                .from(PRODUCTLINE)
                .where(PRODUCTLINE.IMAGE.isNull())
                .fetch();

        System.out.println("EXAMPLE 1.1:\n" + result1.get(0).value1().getProductLine()
                + ", " + result1.get(0).value1().getCode());

        List<EmbeddedProductlinePk> result2 = ctx.select(PRODUCTLINE.PRODUCTLINE_PK)
                .from(PRODUCTLINE)
                .where(PRODUCTLINE.IMAGE.isNull())
                .fetchInto(EmbeddedProductlinePk.class);

        System.out.println("EXAMPLE 1.2:\n" + result2);

        var result3 = ctx.selectFrom(PRODUCTLINE)
                .where(PRODUCTLINE.PRODUCTLINE_PK.in(
                        new EmbeddedProductlinePkRecord("Classic Cars", 599302L),
                        new EmbeddedProductlinePkRecord("Vintage Cars", 223113L)))
                .fetch();

        System.out.println("EXAMPLE 1.3:\n" + result3);
    }
    
    public void fetchProductlineAndProduct() {

        // Result<Record3<EmbeddedProductlinePkRecord, Long, String>>
        var result = ctx.select(PRODUCTLINE.PRODUCTLINE_PK,
                PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCTLINE_PK.eq(PRODUCT.PRODUCT_PRODUCTLINE_FK))
                .fetch();

        System.out.println("EXAMPLE 2:\n" + result);
    }
    
    public void fetchProductlineAndDetail() {

        // Result<Record4<EmbeddedProductlinePkRecord, String, String, Integer>>
        var result = ctx.select(PRODUCTLINE.PRODUCTLINE_PK, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCTLINEDETAIL.LINE_CAPACITY, PRODUCTLINEDETAIL.LINE_TYPE)
                .from(PRODUCTLINE)
                .join(PRODUCTLINEDETAIL)
                .on(PRODUCTLINE.PRODUCTLINE_PK.eq(PRODUCTLINEDETAIL.PRODUCTLINEDETAIL_PRODUCTLINE_FK))
                .fetch();

        System.out.println("EXAMPLE 3:\n" + result);
    }

    @Transactional
    public void insertProductline() {

        EmbeddedProductlinePkRecord pk = new EmbeddedProductlinePkRecord("Turbo Jets", 908844L);

        ctx.update(PRODUCTLINE)
                .set(PRODUCTLINE.TEXT_DESCRIPTION, "Not available")
                .where(PRODUCTLINE.PRODUCTLINE_PK.eq(pk))
                .execute();
        
        ctx.deleteFrom(PRODUCTLINE)
                .where(PRODUCTLINE.PRODUCTLINE_PK.eq(pk))
                .execute();

        ctx.insertInto(PRODUCTLINE, PRODUCTLINE.PRODUCTLINE_PK, PRODUCTLINE.TEXT_DESCRIPTION)
                .values(pk, "Some cool turbo engines")
                .execute();
    }
}
