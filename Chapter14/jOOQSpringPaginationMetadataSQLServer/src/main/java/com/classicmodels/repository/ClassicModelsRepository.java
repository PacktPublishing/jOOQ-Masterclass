package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.JSONFormat;
import org.jooq.Select;
import org.jooq.Table;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.rowNumber;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public String fetchProductsPageAndMetadata(int limit, int offset) {
        return paginate(
                ctx, ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                        .from(PRODUCT), new Field[]{PRODUCT.PRODUCT_ID}, limit, offset
        ).fetch()
         .formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);
    }

    private Select<?> paginate(DSLContext ctx, Select<?> original, Field<?>[] sort, int limit, int offset) {

        Table<?> u = original.asTable("u");
        Field<Integer> totalRows = count().over().as("total_rows");
        Field<Integer> row = rowNumber().over().orderBy(u.fields(sort))
                .as("row");

        Table<?> t = ctx
                .select(u.asterisk())
                .select(totalRows, row)
                .from(u)
                .orderBy(u.fields(sort))
                .limit(limit)
                .offset(offset)
                .asTable("t");

        Select<?> result = ctx
                .select(t.fields(original.getSelect().toArray(Field[]::new)))
                .select(
                        count().over().as("actual_page_size"),
                        field(max(t.field(row)).over().eq(t.field(totalRows)))
                                .as("last_page"),
                        t.field(totalRows),
                        t.field(row),
                        t.field(row).minus(inline(1)).div(limit).plus(inline(1))
                                .as("current_page"))
                .from(t)
                .orderBy(t.fields(sort));

        return result;
    }
}
