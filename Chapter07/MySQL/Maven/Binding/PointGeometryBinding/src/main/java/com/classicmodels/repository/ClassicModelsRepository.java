package com.classicmodels.repository;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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
    public void insertOfficeLocation() {

        // non type-safe
        ctx.insertInto(OFFICE)
                .values(ThreadLocalRandom.current().nextInt(10000, 20000), // random PK
                        "Napoli", "09822-1229-12", "N/A", "N/A", "N/A",
                        "Italy", "zip-2322", "N/A", new Point2D.Double(40.839981, 14.252540), 0)
                .onDuplicateKeyIgnore()
                .execute();

        // type-safe
        ctx.insertInto(OFFICE, OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE, OFFICE.TERRITORY,
                OFFICE.LOCATION, OFFICE.INTERNAL_BUDGET)
                .values(String.valueOf(ThreadLocalRandom.current().nextInt(10000, 20000)), // random pk
                        "Banesti", "0893-23-334", "N/A", "N/A", "N/A",
                        "Romania", "zip-107050", "N/A", new Point2D.Double(45.100842, 25.760010), 0)
                .onDuplicateKeyIgnore()
                .execute();
    }

    public void fetchOfficeLocation() {

        List<Point2D> locations = ctx.select(OFFICE.LOCATION)
                .from(OFFICE)
                .where(OFFICE.LOCATION.isNotNull())
                .fetch(OFFICE.LOCATION);

        System.out.println("Locations: " + locations);
    }
}
