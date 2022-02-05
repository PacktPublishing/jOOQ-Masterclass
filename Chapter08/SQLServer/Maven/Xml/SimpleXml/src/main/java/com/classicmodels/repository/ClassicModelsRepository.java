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

    public void fetchOfficesAsXMLAuto() {

        Result<Record1<XML>> result1 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().auto()
                .fetch();
        System.out.println("Example 1.1:\n" + result1.formatXML());

        Result<Record1<XML>> result2 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().raw()
                .fetch();
        System.out.println("Example 1.2:\n" + result2.formatXML());

        Result<Record1<XML>> result3 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().raw().elements().root("offices")
                .fetch();
        System.out.println("Example 1.3:\n" + result3.formatXML());

        Result<Record1<XML>> result4 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().auto().elements()
                .fetch();
        System.out.println("Example 1.4:\n" + result4.formatXML());

        Result<Record1<XML>> result5 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().auto().root()
                .fetch();
        System.out.println("Example 1.5:\n" + result5.formatXML());

        Result<Record1<XML>> result6 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().auto().root("offices")
                .fetch();
        System.out.println("Example 1.6:\n" + result6.formatXML());

        Result<Record1<XML>> result7 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().auto().elements().root()
                .fetch();
        System.out.println("Example 1.7:\n" + result7.formatXML());

        Result<Record1<XML>> result8 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().auto().elements().root("offices")
                .fetch();
        System.out.println("Example 1.8:\n" + result8.formatXML());

        String result9 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().auto().elements().root("offices")
                .fetch()
                .formatXML(XMLFormat.DEFAULT_FOR_RECORDS);
        System.out.println("Example 1.9:\n" + result9);
    }

    public void fetchOfficesAsXMLPath() {

        Result<Record1<XML>> result1 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().path()
                .fetch();
        System.out.println("Example 1.1:\n" + result1.formatXML());

        Result<Record1<XML>> result2 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().path().elements()
                .fetch();
        System.out.println("Example 1.2:\n" + result2.formatXML());

        Result<Record1<XML>> result3 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().path().root()
                .fetch();
        System.out.println("Example 1.3:\n" + result3.formatXML());

        Result<Record1<XML>> result4 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().path().root("offices")
                .fetch();
        System.out.println("Example 1.4:\n" + result4.formatXML());

        Result<Record1<XML>> result5 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().path().elements().root()
                .fetch();
        System.out.println("Example 1.5:\n" + result5.formatXML());

        Result<Record1<XML>> result6 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().path().elements().root("offices")
                .fetch();
        System.out.println("Example 1.6:\n" + result6.formatXML());

        Result<Record1<XML>> result71 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().path("office").elements().root("offices")
                .fetch();
        System.out.println("Example 1.7.1:\n" + result71.formatXML());

        String result72 = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .forXML().path("office").elements().root("offices")
                .fetch()
                .formatXML(XMLFormat.DEFAULT_FOR_RECORDS);
        System.out.println("Example 1.7.2:\n" + result72);
    }
}
