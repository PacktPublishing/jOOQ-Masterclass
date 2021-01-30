package com.classicmodels.repository;

import java.util.logging.Level;
import java.util.logging.Logger;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void q() {

        while(true) {
            try {
                System.out.println("inserttt...................... 2004, 1233.2 1370L");
                ctx.newRecord(SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                        .values(2004, 1233.2, 1370L)
                        .into(SALE)
                        .insert();
                Thread.sleep(2);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
