package com.classicmodels.repository;

import static jooq.generated.tables.Office.OFFICE;
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

    @Transactional
    public void insertNewOffice() {
        
                // The computed column OFFICE.ADDRESS_LINE_FIRST will be added by jOOQ
                ctx.insertInto(OFFICE)
                        .columns(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE,
                                OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE, 
                                OFFICE.TERRITORY, OFFICE.INTERNAL_BUDGET)
                        .values("55", "Banesti", "+021 984 333", "Prahova", "Romania",
                                "107051", "PH", 0)
                        .onDuplicateKeyIgnore()
                        .execute();
    }
}
