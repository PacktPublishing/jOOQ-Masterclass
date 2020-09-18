package com.classicmodels.repository;

import com.classicmodels.model.ProductLine;
import java.util.List;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext create;

    public ClassicModelsRepository(DSLContext create) {
        this.create = create;
    }

    public List<ProductLine> fetchOnlyProductLine() {
        return create.selectFrom(PRODUCTLINE)
                .fetchInto(ProductLine.class);
    }        
}
