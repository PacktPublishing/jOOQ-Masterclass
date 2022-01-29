package com.classicmodels.repository;

import static com.classicmodels.converter.JsonConverter.JSON_CONVERTER;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import jooq.generated.embeddables.pojos.OfficeFullAddress;
import jooq.generated.embeddables.records.OfficeFullAddressRecord;
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
    public void insertOffice() throws JsonProcessingException {

        // using embeddable type via OfficeFullAddressRecord   
        ctx.insertInto(OFFICE, OFFICE.OFFICE_CODE, OFFICE.OFFICE_FULL_ADDRESS, OFFICE.PHONE,
                OFFICE.ADDRESS_LINE_SECOND, OFFICE.POSTAL_CODE, OFFICE.INTERNAL_BUDGET)
                .values(String.valueOf(ThreadLocalRandom.current().nextInt(10000, 20000)), // random office_code
                        new OfficeFullAddressRecord("Naples", "Giuseppe Mazzini", "Campania", "Italy", "N/A"),
                        "09822-1229-12", "N/A", "zip-2322", 0)
                .onDuplicateKeyIgnore()
                .execute();

        // using JsonNode        
        JsonNode officeFullAddress = new ObjectMapper().readTree(
                "{\"city\":\"Barcelona\",\"address\":\" Ronda de Sant Pere, 35\","
                + "\"state\":\"Catalonia\",\"country\":\"Spain\",\"territory\":\"N/A\"}");

        ctx.insertInto(OFFICE, OFFICE.OFFICE_CODE, OFFICE.OFFICE_FULL_ADDRESS, OFFICE.PHONE,
                OFFICE.ADDRESS_LINE_SECOND, OFFICE.POSTAL_CODE, OFFICE.INTERNAL_BUDGET)
                .values(String.valueOf(ThreadLocalRandom.current().nextInt(10000, 20000)), // random office_code
                        JSON_CONVERTER.to(officeFullAddress),
                        "09822-1229-12", "N/A", "zip-2322", 0)
                .onDuplicateKeyIgnore()
                .execute();
    }

    public void findOffice() {

        Result<Record1<OfficeFullAddressRecord>> result1 = ctx.select(OFFICE.OFFICE_FULL_ADDRESS)
                .from(OFFICE)
                .fetch();
        System.out.println("Result as OfficeFullAddressRecord:\n" + result1);

        List<JsonNode> result2 = ctx.select(OFFICE.OFFICE_FULL_ADDRESS)
                .from(OFFICE)
                .fetch(OFFICE.OFFICE_FULL_ADDRESS, JSON_CONVERTER);
        System.out.println("Result as JsonNode:\n" + result2);

        List<OfficeFullAddress> result3 = ctx.select(OFFICE.OFFICE_FULL_ADDRESS)
                .from(OFFICE)
                .fetchInto(OfficeFullAddress.class);

        System.out.println("Result as POJO:\n" + result3);
    }
}