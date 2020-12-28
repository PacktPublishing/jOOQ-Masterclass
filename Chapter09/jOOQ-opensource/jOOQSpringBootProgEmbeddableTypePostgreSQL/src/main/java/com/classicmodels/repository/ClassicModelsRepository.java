package com.classicmodels.repository;

import java.util.List;
import jooq.generated.embeddables.pojos.Location;
import jooq.generated.embeddables.records.LocationRecord;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
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
    public void insertOffice() {

        // using embeddable type via LocationRecord   
        ctx.insertInto(OFFICE, OFFICE.OFFICE_CODE, OFFICE.LOCATION, OFFICE.PHONE,
                OFFICE.ADDRESS_LINE_SECOND, OFFICE.POSTAL_CODE, OFFICE.TERRITORY)
                .values(String.valueOf((int) (Math.random() * 1000)),
                        new LocationRecord("Naples", "Giuseppe Mazzini", "Campania", "Italy"),
                        "09822-1229-12", "N/A", "zip-2322", "N/A")
                .execute();               
    }

    public void findOffice() {

        Result<Record1<LocationRecord>> resultRecord = ctx.select(OFFICE.LOCATION)
                .from(OFFICE)
                .fetch();
        System.out.println("Result record:" + resultRecord);
        
        List<Location> result = ctx.select(OFFICE.LOCATION)
                .from(OFFICE)
                .fetchInto(Location.class);  
        
        System.out.println("Result:" + result);
    }
}
