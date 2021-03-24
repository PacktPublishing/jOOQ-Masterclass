package com.classicmodels.repository;

import java.util.List;
import jooq.generated.embeddables.pojos.ProductlinePkEmbedded;
import jooq.generated.embeddables.records.ProductlinePkEmbeddedRecord;
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

        var result1 = ctx.selectFrom(PRODUCTLINE)
                .where(PRODUCTLINE.PRODUCTLINE_PK.in(
                        new ProductlinePkEmbeddedRecord("Classic Cars", 599302L),
                        new ProductlinePkEmbeddedRecord("Vintage Cars", 223113L)))
                .fetch();

        System.out.println("EXAMPLE 1.1:\n" + result1);
        
        List<ProductlinePkEmbedded> result2 = ctx.select(PRODUCTLINE.PRODUCTLINE_PK)
                .from(PRODUCTLINE)
                .where(PRODUCTLINE.IMAGE.isNull())
                .fetchInto(ProductlinePkEmbedded.class);
        
        System.out.println("EXAMPLE 1.2:\n" + result2);
    }

    public void fetchProductlineAndDetail() {

        // Result<Record4<ProductlinePkEmbeddedRecord, String, String, Integer>>
        var result = ctx.select(PRODUCTLINE.PRODUCTLINE_PK, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCTLINEDETAIL.LINE_CAPACITY, PRODUCTLINEDETAIL.LINE_TYPE)
                .from(PRODUCTLINE)
                .join(PRODUCTLINEDETAIL)
                .on(PRODUCTLINE.PRODUCTLINE_PK.eq(PRODUCTLINEDETAIL.PRODUCTLINEDETAIL_PRODUCTLINE_FK))
                .fetch();

        System.out.println("EXAMPLE 2:\n" + result);
    }

    public void fetchProductlineAndProduct() {

        // Result<Record3<ProductlinePkEmbeddedRecord, ProductPkEmbeddedRecord, String>>
        var result = ctx.select(PRODUCTLINE.PRODUCTLINE_PK,
                PRODUCT.PRODUCT_PK, PRODUCT.PRODUCT_NAME)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCTLINE_PK.eq(PRODUCT.PRODUCT_PRODUCTLINE_FK))
                .fetch();

        System.out.println("EXAMPLE 3:\n" + result);
    }

    @Transactional
    public void insertProductline() {
        
        ProductlinePkEmbeddedRecord pk = new ProductlinePkEmbeddedRecord("Turbo Jets", 908844L);
        
        ctx.deleteFrom(PRODUCTLINE)
                .where(PRODUCTLINE.PRODUCTLINE_PK.eq(pk))
                .execute();

        ctx.insertInto(PRODUCTLINE, PRODUCTLINE.PRODUCTLINE_PK, PRODUCTLINE.TEXT_DESCRIPTION)
                .values(pk, "Some cool turbo engines")
                .execute();
    }
}