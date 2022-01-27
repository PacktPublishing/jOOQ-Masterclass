package com.classicmodels.repository;

import jooq.generated.embeddables.records.PostalCodeRecord;
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

    public void fetchOffice() {

        // Record2<PostalCodeRecord, String>
        var result1 = ctx.select(OFFICE.POSTAL_CODE, OFFICE.CITY)
                .from(OFFICE)
                .fetchAny();

        System.out.println("EXAMPLE 1:\n");
        System.out.println("Result:\n" + result1);
        System.out.println("Postal code: " + result1.value1().getValue());

        var result2 = ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .where(OFFICE.POSTAL_CODE.eq(new PostalCodeRecord("AZ934VB")))
                .fetch();

        System.out.println("EXAMPLE 2:\n" + result2);
        
        var result3 = ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .where(OFFICE.POSTAL_CODE.in(
                        new PostalCodeRecord("AZ934VB"),
                        new PostalCodeRecord("DT975HH")))
                .fetch();

        System.out.println("EXAMPLE 3:\n" + result3);
    }
    
    @Transactional
    public void insertOffice() {
        
        ctx.deleteFrom(OFFICE)
                .where(OFFICE.OFFICE_CODE.eq("100"))
                .execute();
        
        ctx.insertInto(OFFICE, OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE,
                OFFICE.ADDRESS_LINE_FIRST, OFFICE.COUNTRY, OFFICE.POSTAL_CODE, OFFICE.TERRITORY, OFFICE.INTERNAL_BUDGET)
                .values("100", "Paris", "+51 12 090 4530", "143 Rue Le", 
                        "France", new PostalCodeRecord("OP909DD"), "N/A", 0)
                .execute();
    }
}
