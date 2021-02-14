package com.classicmodels.repository;

import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.XML;
import org.jooq.XMLFormat;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public String xmlOffices() {
        
        Result<Record1<XML>> result = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().path("office").elements().root("offices")
                .fetch();

        System.out.println("Example 1.1:\n" + result.formatXML());
        System.out.println("Example 1.2:\n" + result.formatXML(XMLFormat.DEFAULT_FOR_RECORDS));

        // There will be a single entry in the result containing the whole XML under the "allContacts" element.        
        System.out.println("\nExample (iterate the result set) 1.3:\n");
        result.forEach(System.out::println);

        System.out.println("\nExample (extract data from result set) 1.4:\n"
                + result.get(0).component1().data()); // the whole XML data (most probably, this you'll like to return from a REST controller
        
        System.out.println("\nExample (extract data from result set) 1.5:\n"
                + result.get(0).value1().data()); // the whole XML data (most probably, this you'll like to return from a REST controller
                
        // System.out.println("\nExample (extract data from result set into org.​w3c.​dom.Document) 1.5:\n"
        //        + result.get(0).intoXML());
        
        return result.get(0).value1().data();
    }
    
    public String xmlOfficesFluentReturn() {
        
        return ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().path("office").elements().root("offices")                              
                .fetchSingle()  // there is a single XML
                .value1()       // this is an org.jooq.XML
                .data();        // XML as a String
        
                // or, replace the last three lines from above with: .fetchSingleInto(String.class)
                // or, replace the last three lines with: .fetch().formatXML(XMLFormat.DEFAULT_FOR_RECORDS)
    }
}