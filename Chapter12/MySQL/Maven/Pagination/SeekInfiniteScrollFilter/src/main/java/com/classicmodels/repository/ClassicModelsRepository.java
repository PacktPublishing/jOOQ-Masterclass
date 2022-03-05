package com.classicmodels.repository;

import java.math.BigDecimal;
import java.util.List;
import jooq.generated.tables.pojos.Orderdetail;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<Orderdetail> fetchOrderdetailPageAsc(long orderdetailId, int size,
            BigDecimal priceEach, Integer quantityOrdered) {

        SelectQuery sq = ctx.selectFrom(ORDERDETAIL)
                .orderBy(ORDERDETAIL.ORDERDETAIL_ID)
                .seek(orderdetailId) // or, seekAfter
                .limit(size)
                .getQuery();

        if (priceEach != null) {
            sq.addConditions(ORDERDETAIL.PRICE_EACH.between(
                    priceEach.subtract(BigDecimal.valueOf(50)), priceEach));
        }

        if (quantityOrdered != null) {
            sq.addConditions(ORDERDETAIL.QUANTITY_ORDERED.between(
                    quantityOrdered - 25, quantityOrdered));
        }

        return sq.fetchInto(Orderdetail.class);
    }
}
