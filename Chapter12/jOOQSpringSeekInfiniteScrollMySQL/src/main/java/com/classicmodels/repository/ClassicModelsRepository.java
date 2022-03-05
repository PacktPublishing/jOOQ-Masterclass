package com.classicmodels.repository;

import java.util.List;
import jooq.generated.tables.pojos.Orderdetail;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
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

    public List<Orderdetail> fetchOrderdetailPageAsc(long orderdetailId, int size) {

        List<Orderdetail> result = ctx.selectFrom(ORDERDETAIL)
                .orderBy(ORDERDETAIL.ORDERDETAIL_ID)
                .seek(orderdetailId) // or, seekAfter
                .limit(size)
                .fetchInto(Orderdetail.class);

        return result;
    }
}
