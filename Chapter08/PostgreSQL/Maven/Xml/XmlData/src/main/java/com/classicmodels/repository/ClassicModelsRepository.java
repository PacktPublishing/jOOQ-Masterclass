package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.XML;
import org.jooq.XMLFormat;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlforest;
import static org.jooq.impl.DSL.xmlelement;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public String xmlCustomers() {
        
        Result<Record1<XML>> result = ctx.select(
                xmlelement("allContacts", xmlagg(xmlelement("contact",
                        xmlforest(CUSTOMER.CONTACT_FIRST_NAME.as("firstName"),
                                CUSTOMER.CONTACT_LAST_NAME.as("lastName"),
                                CUSTOMER.PHONE)))))
                .from(CUSTOMER)
                .fetch(); // feel free to practice fetchOne(), fetchAny(), fetchSingle() and so on

        System.out.println("Example 1.1:\n" + result.formatXML());
        System.out.println("Example 1.2:\n" + result.formatXML(XMLFormat.DEFAULT_FOR_RECORDS));

        // There will be a single entry in the result containing the whole XML under the "allContacts" element.        
        System.out.println("\nExample (iterate the result set) 1.3:\n");
        result.forEach(System.out::println);

        System.out.println("\nExample (extract data from result set) 1.4:\n"
                + result.get(0).component1().data()); // the whole XML data (most probably, this you'll like to return from a REST controller
        
        System.out.println("\nExample (extract data from result set) 1.5:\n"
                + result.get(0).value1().data()); // the whole XML data (most probably, this you'll like to return from a REST controller
                
        // System.out.println("\nExample (extract data from result set into org.​w3c.​dom.Document) 1.6:\n"
        //        + result.get(0).intoXML());
        
        return result.get(0).value1().data();
    }
    
    public String xmlCustomersFluentReturn() {
        
        return ctx.select(
                xmlelement("allContacts", xmlagg(xmlelement("contact",
                        xmlforest(CUSTOMER.CONTACT_FIRST_NAME.as("firstName"),
                                CUSTOMER.CONTACT_LAST_NAME.as("lastName"),
                                CUSTOMER.PHONE)))))
                .from(CUSTOMER)
                .fetchSingle()  // there is a single XML
                .value1()       // this is an org.jooq.XML
                .data();        // XML as a String
        
                // or, replace the last three lines from above with: .fetchSingleInto(String.class)
                // or, replace the last three lines with: .fetch().formatXML(XMLFormat.DEFAULT_FOR_RECORDS)
    }
}
