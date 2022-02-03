package com.classicmodels.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.JSONFormat;
import org.jooq.Record1;
import org.jooq.Result;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public String jsonOfficeDepartment() {

        Result<Record1<JSON>> result = ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                select(DEPARTMENT.NAME, DEPARTMENT.PHONE)
                        .from(DEPARTMENT)
                        .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .forJSON().path().asField("departments"))
                .from(OFFICE)
                .forJSON().path().root("root")
                .fetch();  // feel free to practice fetchOne(), fetchAny(), fetchSingle() and so on

        System.out.println("\nExample (format the result set as JSON) 1.1:\n"
                + result.formatJSON());
        
        System.out.println("\nExample (format the result set as JSON) 1.2:\n"
                + result.formatJSON(JSONFormat.DEFAULT_FOR_RECORDS));

        // There will be a single entry in the result containing the whole JSON under the "root" element.        
        System.out.println("\nExample (iterate the result set) 1.3:\n");
        result.forEach(System.out::println);

        System.out.println("\nExample (extract data from result set) 1.4:\n"
                + result.get(0).component1().data()); // the whole JSON data (most probably, this you'll like to return from a REST controller

        System.out.println("\nExample (extract data from result set) 1.5:\n"
                + result.get(0).value1().data()); // the whole JSON data (most probably, this you'll like to return from a REST controller

        // create a Jackson JSON
        ObjectMapper om = new ObjectMapper();
        try {
            JsonNode jsonNode = om.readTree(result.get(0).component1().data());
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result.get(0).value1().data(); // check the following example to express this return fluently
    }

    public String jsonOfficeDepartmentFluentReturn() {

        return ctx.select(
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                select(DEPARTMENT.NAME, DEPARTMENT.PHONE)
                        .from(DEPARTMENT)
                        .where(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .forJSON().path().asField("departments"))
                .from(OFFICE)
                .forJSON().path().root("root")
                .fetchSingle() // there is a single JSON
                .value1()      // this is org.jooq.JSON
                .data();       // this is JSON data as String
        
                // or, replace the last three lines with: .fetchSingleInto(String.class);
                // or, replace the last three lines with: .fetch().formatJSON(JSONFormat.DEFAULT_FOR_RECORDS)
    }
}
