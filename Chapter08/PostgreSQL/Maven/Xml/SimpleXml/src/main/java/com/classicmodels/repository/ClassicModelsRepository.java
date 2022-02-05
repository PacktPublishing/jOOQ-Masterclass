package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.XML;
import org.jooq.XMLFormat;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlattributes;
import static org.jooq.impl.DSL.xmlcomment;
import static org.jooq.impl.DSL.xmlconcat;
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.DSL.xmlexists;
import static org.jooq.impl.DSL.xmlforest;
import static org.jooq.impl.DSL.xmlparseContent;
import static org.jooq.impl.DSL.xmlparseDocument;
import static org.jooq.impl.DSL.xmlpi;
import static org.jooq.impl.DSL.xmlquery;
import static org.jooq.impl.DSL.xmltable;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.SQLDataType.XML;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void fetchSimpleXml() {

        // simple example of using xmlelement()
        Result<Record1<XML>> result1 = ctx.select(
                xmlelement("name", CUSTOMER.CUSTOMER_NAME))
                .from(CUSTOMER)
                .fetch();
        System.out.println("Example 1.1.1:\n" + result1);
        System.out.println("Example 1.1.2:\n" + result1.get(0).value1().data());
        System.out.println("Example 1.1.3:\n" + result1.formatXML()); // or, .formatXML(XMLFormat.DEFAULT_FOR_RESULTS)
        System.out.println("Example 1.1.4:\n" + result1.formatXML(XMLFormat.DEFAULT_FOR_RECORDS));

        Result<Record1<XML>> result2 = ctx.select(
                xmlelement("name", CUSTOMER.CUSTOMER_NAME))
                .from(CUSTOMER)
                .orderBy(CUSTOMER.CUSTOMER_NAME).limit(3)
                .fetch();
        System.out.println("Example 1.2:\n" + result2);

        List<String> result3 = ctx.select(
                xmlelement("name", CUSTOMER.CUSTOMER_NAME))
                .from(CUSTOMER)
                .fetchInto(String.class);
        System.out.println("Example 1.3:\n" + result3);

        // simple example of using xmlattributes()
        Result<Record1<XML>> result4 = ctx.select(xmlelement("contact",
                xmlattributes(CUSTOMER.CONTACT_FIRST_NAME,
                        CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE)))
                .from(CUSTOMER)
                .fetch();
        System.out.println("Example 1.4:\n" + result4.formatXML());

        // simple example of using xmlattributes()
        Result<Record1<XML>> result5 = ctx.select(xmlelement("contact",
                xmlattributes(CUSTOMER.CONTACT_FIRST_NAME.as("firstName"),
                        CUSTOMER.CONTACT_LAST_NAME.as("lastName"),
                        CUSTOMER.PHONE)))
                .from(CUSTOMER)
                .fetch();
        System.out.println("Example 1.5:\n" + result5.formatXML());

        // simple example of using xmlagg()        
        Result<Record1<XML>> result61 = ctx.select(xmlagg(
                xmlelement("name", CUSTOMER.CUSTOMER_NAME)))
                .from(CUSTOMER)
                .fetch();
        System.out.println("Example 1.6.1:\n" + result61);

        String result62 = ctx.select(xmlagg(
                xmlelement("name", CUSTOMER.CUSTOMER_NAME)))
                .from(CUSTOMER)
                .fetchSingleInto(String.class);
        System.out.println("Example 1.6.2:\n" + result62);

        String result63 = ctx.select(xmlelement("names", xmlagg(
                xmlelement("name", CUSTOMER.CUSTOMER_NAME))))
                .from(CUSTOMER)
                .fetchSingleInto(String.class);
        System.out.println("Example 1.6.3:\n" + result63);

        Result<Record1<XML>> result7 = ctx.select(
                xmlelement("allContacts", xmlagg(xmlelement("contact",
                        xmlattributes(CUSTOMER.CONTACT_FIRST_NAME.as("firstName"),
                                CUSTOMER.CONTACT_LAST_NAME.as("lastName"),
                                CUSTOMER.PHONE)))))
                .from(CUSTOMER)
                .fetch();
        System.out.println("Example 1.7:\n" + result7.formatXML());

        // simple example of using xmlforest()
        Result<Record1<XML>> result81 = ctx.select(
                xmlelement("allContacts", xmlagg(xmlelement("contact",
                        xmlforest(CUSTOMER.CONTACT_FIRST_NAME.as("firstName"),
                                CUSTOMER.CONTACT_LAST_NAME.as("lastName"),
                                CUSTOMER.PHONE)))))
                .from(CUSTOMER)
                .fetch();
        System.out.println("Example 1.8.1:\n" + result81.formatXML());

        // ordering and limiting   
        String result82 = ctx.select(
                xmlelement("allContacts", xmlagg(xmlelement("contact",
                        xmlforest(field("contact_first_name").as("firstName"),
                                field("contact_last_name").as("lastName"), field("phone"))))
                        .orderBy(field("contact_first_name"))))
                .from(select(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE)
                        .from(CUSTOMER).orderBy(CUSTOMER.CONTACT_LAST_NAME).limit(3))
                .fetchSingleInto(String.class);
        System.out.println("Example 1.8.2:\n" + result82);

        // simple example of using xmlcomment()
        Result<Record1<XML>> result9 = ctx.select(
                xmlelement("name", xmlcomment("Customer names"), CUSTOMER.CUSTOMER_NAME))
                .from(CUSTOMER)
                .fetch();
        System.out.println("Example 1.9:\n" + result9.formatXML());

        // simple example of using xmlcomment()
        Result<Record1<XML>> result10 = ctx.select(
                xmlelement("name", xmlcomment(
                        concat(CUSTOMER.CONTACT_FIRST_NAME,
                                val(" "), CUSTOMER.CONTACT_LAST_NAME)),
                        CUSTOMER.CUSTOMER_NAME))
                .from(CUSTOMER)
                .fetch();
        System.out.println("Example 1.10:\n" + result10.formatXML());

        // simple example of using xmlcomment()
        Result<Record1<XML>> result11 = ctx.select(
                xmlelement("allContacts",
                        xmlcomment("This is a list of customer contacts"),
                        xmlagg(xmlelement("contact",
                                xmlattributes(CUSTOMER.CONTACT_FIRST_NAME.as("firstName"),
                                        CUSTOMER.CONTACT_LAST_NAME.as("lastName"),
                                        CUSTOMER.PHONE)))))
                .from(CUSTOMER)
                .fetch();
        System.out.println("Example 1.11:\n" + result11.formatXML());

        // simple example of using xmlparseContent()
        Result<Record1<XML>> result12 = ctx.select(xmlparseContent(
                DEPARTMENT.TOPIC.coerce(String.class)))
                .from(DEPARTMENT)
                .fetch();

        System.out.println("Example 1.12:\n" + result12.formatXML());

        // simple example of using xmlparseDocument()
        Result<Record1<XML>> result13 = ctx.select(xmlparseDocument(
                PRODUCTLINE.HTML_DESCRIPTION.coerce(String.class)))
                .from(PRODUCTLINE)
                .fetch();

        System.out.println("Example 1.13:\n" + result13.formatXML());

        // simple example of using xmlconcat()
        Result<Record1<XML>> result14 = ctx.select(
                xmlelement("fullName", xmlconcat(
                        xmlelement("firstName", CUSTOMER.CONTACT_FIRST_NAME),
                        xmlelement("lastName", CUSTOMER.CONTACT_LAST_NAME))))
                .from(CUSTOMER)
                .fetch();
        System.out.println("Example 1.14:\n" + result14.formatXML());
    }

    public void fetchXmlValue() {

        // simple example of using xmlquery()
        Result<Record1<XML>> result21 = ctx.select(
                xmlquery("productline/capacity/c[position()=last()]")
                        .passing(PRODUCTLINE.HTML_DESCRIPTION))
                .from(PRODUCTLINE)
                .fetch();

        System.out.println("Example 2.1.1:\n" + result21.formatXML());

        List<String> result22 = ctx.select(
                xmlquery("productline/capacity/c[position()=last()]")
                        .passing(PRODUCTLINE.HTML_DESCRIPTION))
                .from(PRODUCTLINE)
                .fetchInto(String.class);

        System.out.println("Example 2.1.2:\n" + result22);

        String result23 = ctx.select(xmlagg(
                xmlquery("productline/capacity/c[position()=last()]")
                        .passing(PRODUCTLINE.HTML_DESCRIPTION)))
                .from(PRODUCTLINE)
                .fetchSingleInto(String.class);

        System.out.println("Example 2.1.3:\n" + result23);

        // simple example of using xmlquery()        
        Result<Record1<XML>> result2 = ctx.select(xmlquery("//contact/phone").passing(
                xmlelement("allContacts", xmlagg(xmlelement("contact",
                        xmlforest(CUSTOMER.CONTACT_FIRST_NAME.as("firstName"),
                                CUSTOMER.CONTACT_LAST_NAME.as("lastName"),
                                CUSTOMER.PHONE))))))
                .from(CUSTOMER)
                .fetch();
        System.out.println("Example 2.2:\n" + result2.formatXML());

        // simple example of using xmlexists()        
        Result<Record1<XML>> result3 = ctx.select(PRODUCTLINE.HTML_DESCRIPTION)
                .from(PRODUCTLINE)
                .where(xmlexists("//b1").passing(PRODUCTLINE.HTML_DESCRIPTION))
                .fetch();

        System.out.println("Example 2.3:\n" + result3.formatXML());

        // simple example of using xmlexists()        
        Result<Record1<String>> result4 = ctx.select(PRODUCTLINE.PRODUCT_LINE)
                .from(PRODUCTLINE)
                .where(xmlexists("/productline").passing(PRODUCTLINE.HTML_DESCRIPTION))
                .fetch();

        System.out.println("Example 2.4:\n" + result4);

        // simple example of using xmlpi()        
        Result<Record1<XML>> result5 = ctx.select(xmlpi("php"))
                .fetch();

        System.out.println("Example 2.5:\n" + result5);
    }

    public void xmlTableExample() {

        Result<Record> result1 = ctx.select(table("t").asterisk())
                .from(PRODUCTLINE, xmltable("//productline").passing(PRODUCTLINE.HTML_DESCRIPTION)
                        .column("id").forOrdinality()
                        .column("name", VARCHAR)
                        .column("code", VARCHAR)
                        .column("capacity", XML)
                        .column("power", VARCHAR).path("details/power")
                        .column("command", VARCHAR).path("details/type/@command")
                        .as("t"))
                .fetch();

        System.out.println("Example 3.1:\n" + result1);

        Result<Record> result2 = ctx.select(table("t").asterisk())
                .from(PRODUCTLINE, xmltable("//productline/details").passing(PRODUCTLINE.HTML_DESCRIPTION)
                        .column("id").forOrdinality()
                        .column("power", VARCHAR)
                        .column("type", VARCHAR)
                        .column("nr_of_lines", INTEGER).path("type/@nr_of_lines")
                        .column("command", VARCHAR).path("type/@command")
                        .as("t"))
                .fetch();

        System.out.println("Example 3.2:\n" + result2);

        // filter result
        Result<Record> result3 = ctx.select(table("t").asterisk())
                .from(PRODUCTLINE, xmltable("//productline/details").passing(PRODUCTLINE.HTML_DESCRIPTION)
                        .column("id").forOrdinality()
                        .column("power", VARCHAR)
                        .column("type", VARCHAR)
                        .column("nr_of_lines", INTEGER).path("type/@nr_of_lines")
                        .column("command", VARCHAR).path("type/@command")
                        .as("t"))
                .where(field("command").eq("ERP"))
                .fetch();

        System.out.println("Example 3.3:\n" + result3);

        // back to XML        
        Result<Record1<XML>> result4 = ctx.select(xmlelement("details",
                xmlelement("power", (field("power"))),
                xmlelement("type", xmlattributes(field("nr_of_lines"), field("command")), field("type"))))
                .from(PRODUCTLINE, xmltable("//productline/details").passing(PRODUCTLINE.HTML_DESCRIPTION)
                        .column("id").forOrdinality()
                        .column("power", VARCHAR)
                        .column("type", VARCHAR)
                        .column("nr_of_lines", INTEGER).path("type/@nr_of_lines")
                        .column("command", VARCHAR).path("type/@command")
                        .as("t"))
                .fetch();
        System.out.println("Example 3.4:\n" + result4.formatXML());

        // aggregate
        Result<Record2<String, Integer>> result5 = ctx.select(field("command", String.class), count(field("command")))
                .from(PRODUCTLINE, xmltable("//productline/details").passing(PRODUCTLINE.HTML_DESCRIPTION)
                        .column("command", VARCHAR).path("type/@command")
                        .as("t"))
                .groupBy(field("command"))
                .fetch();

        System.out.println("Example 3.5:\n" + result5);

        // order and limit result
        Result<Record> result6 = ctx.select(table("t").asterisk())
                .from(PRODUCTLINE, xmltable("//productline").passing(PRODUCTLINE.HTML_DESCRIPTION)
                        .column("id").forOrdinality()
                        .column("name", VARCHAR)
                        .column("code", VARCHAR)
                        .column("capacity", XML)
                        .column("power", VARCHAR).path("details/power")
                        .column("command", VARCHAR).path("details/type/@command")
                        .as("t"))
                .orderBy(field("name"))
                .limit((2))
                .fetch();

        System.out.println("Example 3.6:\n" + result6);
    }
}
