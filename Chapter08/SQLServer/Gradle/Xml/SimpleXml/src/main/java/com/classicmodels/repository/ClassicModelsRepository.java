package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.XML;
import org.jooq.XMLFormat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.two;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.DOUBLE;
import static org.jooq.impl.SQLDataType.INTEGER;
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
	
	public void fetchOfficesAsXMLExplicit() {

        String result1 = ctx.select().from(select(
                one().as("Tag"), inline(null, INTEGER).as("Parent"),
                EMPLOYEE.EMPLOYEE_NUMBER.as("employee!1!employee_number"),
                EMPLOYEE.FIRST_NAME.as("employee!1!first_name!ELEMENT"),
                EMPLOYEE.LAST_NAME.as("employee!1!last_name!ELEMENT"),
                EMPLOYEE.JOB_TITLE.as("employee!1!job_title!ELEMENT"),
                EMPLOYEE.SALARY.as("employee!1!salary!ELEMENT"),
                inline(null, BIGINT).as("sale!2!sale_id"),
                inline(null, INTEGER).as("sale!2!fiscal_year!ELEMENT"),
                inline(null, DOUBLE).as("sale!2!sale!ELEMENT"))
                .from(EMPLOYEE)
                .unionAll(select(two().as("Tag"), one().as("Parent"),
                        EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, 
                        EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY,
                        SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_)
                        .from(EMPLOYEE)
                        .innerJoin(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))))
                .orderBy(field(name("employee!1!salary!ELEMENT")), field(name("sale!2!fiscal_year!ELEMENT")))
                .forXML().explicit()
                .fetch()
                .formatXML(XMLFormat.DEFAULT_FOR_RECORDS);
        System.out.println("Example 1.1:\n" + result1);
    }
}
