package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import jooq.generated.tables.Customerdetail;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import jooq.generated.tables.Department;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.Office;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import jooq.generated.tables.Product;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Table;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectCount;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.values;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void sample0() {
        
        Field<String> pl1 = PRODUCT.PRODUCT_LINE.as("pl");                
        Field<String> pl2 = PRODUCT.PRODUCT_LINE;                
        ctx.select(pl1)
           .from(PRODUCT)
           .groupBy(pl2)
           .orderBy(pl1)
           .fetch();

        // select "CLASSICMODELS"."OFFICE"."CITY" from "CLASSICMODELS"."OFFICE" "t"
        // Since we assigned an alias to "CLASSICMODELS"."OFFICE" table then 
        // "CLASSICMODELS"."OFFICE"."CITY" column become unknown   
        /*
        ctx.select(OFFICE.CITY)
                .from(OFFICE.as("t"))
                .fetch();
         */
        
        // This leads to ORA-00904: "T": invalid identifier
        // select t from "CLASSICMODELS"."OFFICE" "t"
        /*
        ctx.select(field("t", "city"))
                .from(OFFICE.as("t"))
                .fetch();
        */
        
        // This selects all columns, obviously not what we want
        // select "t"."OFFICE_CODE", "t"."CITY", ... , "t"."LOCATION" from "CLASSICMODELS"."OFFICE" "t"
        /*
        ctx.select(table("t").field("city"))
                .from(OFFICE.as("t"))
                .fetch();
        */
        
        // The next one works, but is prone to ambiguities
        // It works because the unquoted city and CITY are the same identifiers
        // select city from "CLASSICMODELS"."OFFICE" "t"
        ctx.select(field("city"))
                .from(OFFICE.as("t"))
                .fetch();

        // This leads to ORA-00904: "city": invalid identifier
        // This doesn't work because the quoted "city" and "CITY" are not the same
        // select "city" from "CLASSICMODELS"."OFFICE" "t"
        /*
        ctx.select(field(name("city")))
                .from(OFFICE.as("t"))
                .fetch();
        */

        // This leads to ORA-00918: column ambiguously defined
        /*
        ctx.select(field("city"))
                .from(OFFICE.as("t1"), CUSTOMERDETAIL.as("t2"))
                .fetch();
        */ 
        
        // This lead to ORA-00904: "T"."CITY": invalid identifier
        // select t.city from "CLASSICMODELS"."OFFICE" "t"
        /*
        ctx.select(field("t.city"))
                .from(OFFICE.as("t"))
                .fetch();
        */

        // This leads to ORA-00904: "T2"."CITY": invalid identifier
        // select t1.city, t2.city from "CLASSICMODELS"."OFFICE" "t1", "CLASSICMODELS"."CUSTOMERDETAIL" "t2"
        /*
        ctx.select(field("t1.city"), field("t2.city"))
                .from(OFFICE.as("t1"), CUSTOMERDETAIL.as("t2"))
                .fetch();
        */

        // This leads to ORA-00904: "t"."city": invalid identifier
        // select "t"."city" from "CLASSICMODELS"."OFFICE" "t"
        /*
        ctx.select(field(name("t", "city")))
                .from(OFFICE.as("t"))
                .fetch();
        */
        
        // This finally works (but, prefer the next one)
        // select T.CITY from "CLASSICMODELS"."OFFICE" "T"
        ctx.select(field("T.CITY"))
                .from(OFFICE.as("T"))
                .fetch();
        // This DOES NOT work!
        /*
        ctx.select(field("t.CITY"))
                .from(OFFICE.as("t"))
                .fetch();
        */
        
        // select "T"."CITY" from "CLASSICMODELS"."OFFICE" "T"
        ctx.select(field(name("T", "CITY")))
                .from(OFFICE.as("T"))
                .fetch();
        // select "t"."CITY" from "CLASSICMODELS"."OFFICE" "t"
        ctx.select(field(name("t", "CITY")))
                .from(OFFICE.as("t"))
                .fetch();

        // No risk for ambiguities and identifiers are correctly generated
        ctx.select(field(name("T1", "CITY")), field(name("T2", "CITY")))
                .from(OFFICE.as("T1"), CUSTOMERDETAIL.as("T2"))
                .fetch();
        ctx.select(field(name("t1", "CITY")), field(name("t2", "CITY")))
                .from(OFFICE.as("t1"), CUSTOMERDETAIL.as("t2"))
                .fetch();
        
        ctx.select(field(name("T1", "CITY")).as("city_office"), field(name("T2", "CITY")).as("city_customer"))
                .from(OFFICE.as("T1"), CUSTOMERDETAIL.as("T2"))
                .fetch();
        ctx.select(field(name("t1", "CITY")).as("city_office"), field(name("t2", "CITY")).as("city_customer"))
                .from(OFFICE.as("t1"), CUSTOMERDETAIL.as("t2"))
                .fetch();

        // much better is to declare alias before usage
        Office t1 = OFFICE.as("t1");
        Customerdetail t2 = CUSTOMERDETAIL.as("t2"); // you can put T2 as well

        ctx.select(t1.CITY, t2.CITY)
                .from(t1, t2)
                .fetch();

        Field<String> c1 = t1.CITY.as("city_office");
        Field<String> c2 = t2.CITY.as("city_customer");
        ctx.select(c1, c2)
                .from(t1, t2)
                .fetch();
        
        // or, a minimalist approach
        Customerdetail t = CUSTOMERDETAIL;

        ctx.select(OFFICE.CITY, t.CITY)
                .from(OFFICE, t)
                .fetch();
    }
    
    public void sample1() {

        // no aliases
        ctx.select(OFFICE.CITY, DEPARTMENT.NAME, DEPARTMENT.PROFIT)
                .from(OFFICE)
                .join(DEPARTMENT)
                .on(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))
                .fetch();

        // this is not type-safe and it has inconsistent identifiers
        ctx.select(field("T1.CITY"), field("T2.NAME"), field("T2.PROFIT"))
                .from(OFFICE.as("T1"))
                .join(DEPARTMENT.as("T2"))
                .on(field("T1.OFFICE_CODE").eq(field("T2.OFFICE_CODE")))
                .fetch();

        // this is not type-safe, but it has consistent identifiers
        ctx.select(field(name("T1", "CITY")), field(name("T2", "NAME")), field(name("T2", "PROFIT")))
                .from(OFFICE.as(name("T1")))
                .join(DEPARTMENT.as(name("T2")))
                .on(field(name("T1", "OFFICE_CODE")).eq(field(name("T2", "OFFICE_CODE"))))
                .fetch();

        // extract aliases in local variables (this is type-safe)
        Office t1 = OFFICE.as("T1");
        Department t2 = DEPARTMENT.as("T2");

        ctx.select(t1.CITY, t2.NAME, t2.PROFIT)
                .from(t1)
                .join(t2)
                .on(t1.OFFICE_CODE.eq(t2.OFFICE_CODE))
                .fetch();
        
        //======or, a minimalist approach =========//        
        Department t = DEPARTMENT.as("T"); // or, Department t = DEPARTMENT;

        ctx.select(OFFICE.CITY, t.NAME, t.PROFIT)
                .from(OFFICE)
                .join(t)
                .on(OFFICE.OFFICE_CODE.eq(t.OFFICE_CODE))
                .fetch();
        //=========================================//
        
        Table<Record1<String>> t3 
                = ctx.select(t1.CITY).from(t1).asTable("T3");
        
        // refer to t3'fields type-safe
        ctx.select(t3.field(t1.CITY), CUSTOMERDETAIL.CUSTOMER_NUMBER)
                .from(t3)
                .join(CUSTOMERDETAIL)
                .on(t3.field(t1.CITY).eq(CUSTOMERDETAIL.CITY))
                .fetch();
        
        // refer to t3'fields non type-safe
        ctx.select(t3.field(name("CITY")), CUSTOMERDETAIL.CUSTOMER_NUMBER)
                .from(t3)
                .join(CUSTOMERDETAIL)
                .on(t3.field(name("CITY"), String.class).eq(CUSTOMERDETAIL.CITY))
                .fetch();
        
        // no need to extract aliases in local variables
        ctx.select(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME).as("NAME"),
                EMPLOYEE.EMAIL.as("CONTACT"), EMPLOYEE.REPORTS_TO.as("BOSS_ID"))
                .from(EMPLOYEE)
                .fetch();  
    }

    public void sample2() {

        // this is not type-safe and it has inconsistent identifiers
        ctx.selectDistinct(field("T1.CITY"))
                .from(OFFICE.as("T1"))
                .where(field("T1.CITY").in(
                        select(field("T2.CITY"))
                                .from(CUSTOMERDETAIL.as("T2"))
                )).fetch();

        // this is not type-safe, but it has consistent identifiers
        ctx.selectDistinct(field(name("T1", "CITY")))
                .from(OFFICE.as("T1"))
                .where(field(name("T1", "CITY")).in(
                        select(field(name("T2", "CITY")))
                                .from(CUSTOMERDETAIL.as("T2"))
                )).fetch();

        // extract aliases in local variables (this is type-safe)
        Office t1 = OFFICE.as("T1");
        Customerdetail t2 = CUSTOMERDETAIL.as("T2");

        ctx.selectDistinct(t1.CITY)
                .from(t1)
                .where(t1.CITY.in(select(t2.CITY)
                        .from(t2)
                )).fetch();
    }

    public void sample3() {

        // this is not type-safe and it has inconsistent identifiers
        ctx.select(count(field("T.CUSTOMER_NUMBER")).as("TOTAL_CUSTOMERS"), field("T.COUNTRY").as("NATIONALITY"))
                .from(CUSTOMERDETAIL.as("T"))
                .groupBy(field("T.COUNTRY"))
                .fetch();

        ctx.select(count(field("T.CUSTOMER_NUMBER")).as("TOTAL_CUSTOMERS"), field("T.COUNTRY").as("NATIONALITY"))
                .from(CUSTOMERDETAIL.as("T"))
                .groupBy(field("COUNTRY"))
                .fetch();

        // doesn't work in Oracle
        /*
        ctx.select(count(field("T.CUSTOMER_NUMBER")).as("TOTAL_CUSTOMERS"), field("T.COUNTRY").as("NATIONALITY"))
                .from(CUSTOMERDETAIL.as("T"))
                .groupBy(field("NATIONALITY"))
                .fetch();
        */

        // this is not type-safe, but it has consistent identifiers
        ctx.select(count(field(name("T", "CUSTOMER_NUMBER"))).as("TOTAL_CUSTOMERS"),
                field(name("T", "COUNTRY")).as("NATIONALITY"))
                .from(CUSTOMERDETAIL.as("T"))
                .groupBy(field(name("T", "COUNTRY")))
                .fetch();

        ctx.select(count(field(name("T", "CUSTOMER_NUMBER"))).as("TOTAL_CUSTOMERS"),
                field(name("T", "COUNTRY")).as("NATIONALITY"))
                .from(CUSTOMERDETAIL.as("T"))
                .groupBy(field(name("COUNTRY")))
                .fetch();

        // doesn't work in Oracle
        /*
        ctx.select(count(field(name("T", "CUSTOMER_NUMBER"))).as("TOTAL_CUSTOMERS"),
                field(name("T", "COUNTRY")).as("NATIONALITY"))
                .from(CUSTOMERDETAIL.as("T"))
                .groupBy(field(name("NATIONALITY")))
                .fetch();
        */

        // extract aliases in local variables (this is type-safe)
        Customerdetail t = CUSTOMERDETAIL.as("T");
        ctx.select(count(t.CUSTOMER_NUMBER).as("TOTAL_CUSTOMERS"), field(t.COUNTRY).as("NATIONALITY"))
                .from(t)
                .groupBy(t.COUNTRY)
                .fetch();
    }
    
    public void sample4() {
        
        // This is not correct! It leads to: ORA-00904: "alias_85650850"."T"."INVOICE_AMOUNT": invalid identifier
        /*
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME, field("T.INVOICE_AMOUNT"))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("T"))
                        .on(field("T.CUSTOMER_NUMBER").eq(CUSTOMER.CUSTOMER_NUMBER)))
                .fetch();
        */
        
        // Solution 1: extract the aliased table    
        System.out.println("Solution 1: extract the aliased table");
        var t = select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("T");
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME, 
                        t.field(PAYMENT.INVOICE_AMOUNT))
                        .from(CUSTOMER)
                        .join(t)
                        .on(t.field(CUSTOMER.CUSTOMER_NUMBER).eq(CUSTOMER.CUSTOMER_NUMBER)))
                .fetch();
        
        // Solution 2: Use name() for proper quotation
        System.out.println("Solution 2: Use name() for proper quotation");
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME,
                        field(name("T", "INVOICE_AMOUNT")))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("T"))
                        .on(field(name("T", "CUSTOMER_NUMBER")).eq(CUSTOMER.CUSTOMER_NUMBER)))
                .fetch();                        
        
        // Solution 3: add an alias to field("T.INVOICE_AMOUNT")
        System.out.println("Solution 3: add an alias to field(\"T.INVOICE_AMOUNT\")");
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME,
                        field("T.INVOICE_AMOUNT").as("INVOICE_AMOUNT"))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("T"))
                        .on(field("T.CUSTOMER_NUMBER").eq(CUSTOMER.CUSTOMER_NUMBER)))
                .fetch();                
        
        // Solution 4: use field("INVOICE_AMOUNT"), so unquoted identifier        
        System.out.println("Solution 4: use field(\"INVOICE_AMOUNT\"), so unquoted identifier ");
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME, field("INVOICE_AMOUNT"))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("T"))
                        .on(field("T.CUSTOMER_NUMBER").eq(CUSTOMER.CUSTOMER_NUMBER)))
                .fetch();
        
        // enrich the query with another join
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME,
                        field(name("T", "INVOICE_AMOUNT")))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("T"))
                        .on(field(name("T", "CUSTOMER_NUMBER")).eq(CUSTOMER.CUSTOMER_NUMBER))
                        .asTable("M")
                        .join(CUSTOMERDETAIL)
                        .on(field(name("M", "CUSTOMER_NUMBER")).eq(CUSTOMERDETAIL.CUSTOMER_NUMBER)))
                .fetch();
    }

    public void sample5() {

        ctx.select(field("M1.PRODUCT_NAME").as("PRODUCT_MIN_SCALE"), field("F1.PRODUCT_NAME").as("PRODUCT_MAX_SCALE"),
                coalesce(field("M1.RANK"), field("F1.RANK")))
                .from(select(field("P1.PRODUCT_NAME"), count(field("P2.PRODUCT_NAME")))
                        .from(PRODUCT.as("P1"), PRODUCT.as("P2"))
                        .where(field("P2.PRODUCT_NAME").le(field("P1.PRODUCT_NAME"))
                                .and(field("P1.PRODUCT_SCALE").eq("1:24"))
                                .and(field("P2.PRODUCT_SCALE").eq("1:24")))
                        .groupBy(field("P1.PRODUCT_NAME")).asTable("M1", "PRODUCT_NAME", "RANK"))
                .innerJoin(select(field("P1.PRODUCT_NAME"), count(field("P2.PRODUCT_NAME")))
                        .from(PRODUCT.as("P1"), PRODUCT.as("P2"))
                        .where(field("P2.PRODUCT_NAME").le(field("P1.PRODUCT_NAME"))
                                .and(field("P1.PRODUCT_SCALE").eq("1:32"))
                                .and(field("P2.PRODUCT_SCALE").eq("1:32")))
                        .groupBy(field("P1.PRODUCT_NAME")).asTable("F1", "PRODUCT_NAME", "RANK"))
                .on(field("M1.RANK").eq(field("F1.RANK")))
                .fetch();

        // extract aliases and add name()
        Product p1 = PRODUCT.as("P1");
        Product p2 = PRODUCT.as("P2");

        ctx.select(field(name("M1", "PRODUCT_NAME")).as("PRODUCT_MIN_SCALE"),
                field(name("F1", "PRODUCT_NAME")).as("PRODUCT_MAX_SCALE"),
                coalesce(field(name("M1", "RANK")), field(name("F1", "RANK"))))
                .from(select(p1.PRODUCT_NAME, count(p2.PRODUCT_NAME))
                        .from(p1, p2)
                        .where(p2.PRODUCT_NAME.le(p1.PRODUCT_NAME)
                                .and(p1.PRODUCT_SCALE.eq("1:24"))
                                .and(p2.PRODUCT_SCALE.eq("1:24")))
                        .groupBy(p1.PRODUCT_NAME).asTable("M1", "PRODUCT_NAME", "RANK"))
                .innerJoin(select(p1.PRODUCT_NAME, count(p2.PRODUCT_NAME))
                        .from(p1, p2)
                        .where(p2.PRODUCT_NAME.le(p1.PRODUCT_NAME)
                                .and(p1.PRODUCT_SCALE.eq("1:32"))
                                .and(p2.PRODUCT_SCALE.eq("1:32")))
                        .groupBy(p1.PRODUCT_NAME).asTable("F1", "PRODUCT_NAME", "RANK"))
                .on(field(name("M1", "RANK")).eq(field(name("F1", "RANK"))))
                .fetch();
    }

    public void sample6() {
        
         ctx.select(EMPLOYEE.JOB_TITLE, 
                sum(EMPLOYEE.SALARY.minus(EMPLOYEE.COMMISSION)).as("amount"))
           .from(EMPLOYEE)
           .groupBy(EMPLOYEE.JOB_TITLE)
           .orderBy(2) // or, .orderBy(field(name("amount")))
           .fetch();

        // Oracle: ORA-00904: "f": invalid identifier
        /*
        ctx.select(PRODUCT.PRODUCT_VENDOR.as("f"))
                .from(PRODUCT)
                .groupBy(field(name("f")))                
                .fetch();
        */
        
        // workaround
        ctx.select(field(name("f"))).from(
                select(PRODUCT.PRODUCT_VENDOR.as("f"))
                        .from(PRODUCT))
                .groupBy(field(name("f")))                
                .fetch();
        
        ctx.select(PRODUCT.PRODUCT_VENDOR.as("f"))
                .from(PRODUCT)                
                .orderBy(field(name("f")))
                .fetch();

        ctx.select(PRODUCT.PRODUCT_VENDOR.as("f"))
                .from(PRODUCT)
                .groupBy(PRODUCT.PRODUCT_VENDOR)
                .orderBy(PRODUCT.PRODUCT_VENDOR)
                .fetch();

        // Oracle: ORA-00904: "f": invalid identifier
        /*
        Field<String> f1 = PRODUCT.PRODUCT_VENDOR.as("f");
        ctx.select(f1)
                .from(PRODUCT)
                .groupBy(f1)                
                .fetch();
        */
        
        // workaround
        Field<String> f1 = PRODUCT.PRODUCT_VENDOR.as("f");
        ctx.select(field(f1.getQualifiedName())).from(
                select(f1)
                        .from(PRODUCT))
                .groupBy(f1)
                .fetch();
                
        ctx.select(f1)
                .from(PRODUCT)                
                .orderBy(f1)
                .fetch();

        // Oracle: ORA-00904: "f": invalid identifier
        /*
        Field<String> f2 = PRODUCT.PRODUCT_VENDOR;
        ctx.select(f2.as("f"))
                .from(PRODUCT)
                .groupBy(field(name("f")))
                .fetch();
        */
        
        Field<String> f2 = PRODUCT.PRODUCT_VENDOR;
        ctx.select(f2.as("f"))
                .from(PRODUCT)                
                .orderBy(field(name("f")))
                .fetch();

        Field<String> f3 = PRODUCT.PRODUCT_VENDOR;
        ctx.select(f3.as("f"))
                .from(PRODUCT)
                .groupBy(f3)                
                .fetch();
        
        ctx.select(f3.as("f"))
                .from(PRODUCT)                
                .orderBy(f3)
                .fetch();
       
        Field<String> f4 = PRODUCT.PRODUCT_VENDOR;
        Field<String> f5 = PRODUCT.PRODUCT_VENDOR.as("f");
        ctx.select(f5)
                .from(PRODUCT)
                .groupBy(f4)
                .orderBy(f5)
                .fetch();
        
        // Oracle: ORA-00904: "f": invalid identifier
        /*
        Field<String> f6 = PRODUCT.PRODUCT_VENDOR;
        Field<String> f7 = PRODUCT.PRODUCT_VENDOR.as("f");
        ctx.select(f7)
                .from(PRODUCT)
                .groupBy(f7)
                .orderBy(f6)
                .fetch();
        */
    }

    public void sample7() {

        // ORA-00918: column ambiguously defined
        /*
        ctx.select().from(
                OFFICE.leftOuterJoin(DEPARTMENT)
                        .on(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)))
                .innerJoin(EMPLOYEE)
                .on(EMPLOYEE.OFFICE_CODE.eq(field(name("OFFICE_CODE"), String.class)))
                .fetch();
        */
        
        // ORA-00904: "CLASSICMODELS"."OFFICE"."OFFICE_CODE": invalid identifier
        /*        
        ctx.select().from(
                OFFICE.as("O").leftOuterJoin(DEPARTMENT.as("D"))
                        .on(field(name("O","OFFICE_CODE")).eq(field(name("D","OFFICE_CODE")))))
                .innerJoin(EMPLOYEE)
                .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                .fetch();         
        */
        
        ctx.select().from(
                OFFICE.as("O").leftOuterJoin(DEPARTMENT.as("D"))
                        .on(field(name("O","OFFICE_CODE")).eq(field(name("D","OFFICE_CODE")))))
                .innerJoin(EMPLOYEE)
                .on(EMPLOYEE.OFFICE_CODE.eq(field(name("O","OFFICE_CODE"), String.class)))
                .fetch();
        
        Office o = OFFICE.as("O");
        Department d = DEPARTMENT.as("D");
        
        ctx.select().from(o.leftOuterJoin(d)
                        .on(o.OFFICE_CODE.eq(d.OFFICE_CODE)))
                .innerJoin(EMPLOYEE)
                .on(EMPLOYEE.OFFICE_CODE.eq(o.OFFICE_CODE))
                .fetch();
    }

    public void sample8() {

        // There is a mistake in this query. Can you spot it?
        ctx.select(field("S1.MSRP"), field("S2.MSRP"))
                .from(PRODUCT.as("S1"), PRODUCT.as("S2"))
                .where(field("S1.MSRP").lt(field("S2.MSRP"))
                        .and(field("S1.PRODUCT_LINE").eq("S2.PRODUCT_LINE")))
                .groupBy(field("S1.MSRP"), field("S2.MSRP"))                
                .having(count().eq(selectCount().from(PRODUCT.as("S3"))
                        .where(field("S3.MSRP").eq(field("S1.MSRP"))))
                        .and(count().eq(selectCount().from(PRODUCT.as("S4"))
                                .where(field("S4.MSRP").eq(field("S2.MSRP"))))))
                .fetch();

        // If you did not spot it then you have a good reason to write it as here
        Product s1 = PRODUCT.as("S1");
        Product s2 = PRODUCT.as("S2");
        Product s3 = PRODUCT.as("S3");
        Product s4 = PRODUCT.as("S4");
        
        ctx.select(s1.MSRP, s2.MSRP)
                .from(s1, s2)
                .where(s1.MSRP.lt(s2.MSRP)
                        .and(s1.PRODUCT_LINE.eq(s2.PRODUCT_LINE)))
                .groupBy(s1.MSRP, s2.MSRP)   
                .having(count().eq(selectCount().from(s3).where(s3.MSRP.eq(s1.MSRP)))
                        .and(count().eq(selectCount().from(s4).where(s4.MSRP.eq(s2.MSRP)))))
                .fetch();
    }

    public void sample9() {

        ctx.select()
                .from(select(EMPLOYEE.EMPLOYEE_NUMBER.as("EN"), EMPLOYEE.SALARY.as("SAL")).from(EMPLOYEE)
                        .where(EMPLOYEE.MONTHLY_BONUS.isNull()))
                .innerJoin(SALE)
                .on(field(name("EN")).eq(SALE.EMPLOYEE_NUMBER))
                .fetch();

        ctx.select(SALE.SALE_, SALE.FISCAL_YEAR, field(name("SAL")))
                .from(select(EMPLOYEE.EMPLOYEE_NUMBER.as("EN"), EMPLOYEE.SALARY.as("SAL")).from(EMPLOYEE)
                        .where(EMPLOYEE.MONTHLY_BONUS.isNull()))
                .innerJoin(SALE)
                .on(field(name("EN")).eq(SALE.EMPLOYEE_NUMBER))
                .fetch();

        ctx.select()
                .from(select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.SALARY).from(EMPLOYEE)
                        .where(EMPLOYEE.MONTHLY_BONUS.isNull()).asTable("T"))
                .innerJoin(SALE)
                .on(field(name("T", "EMPLOYEE_NUMBER")).eq(SALE.EMPLOYEE_NUMBER))
                .fetch();

        ctx.select(SALE.SALE_, SALE.FISCAL_YEAR, field(name("T", "SALARY")))
                .from(select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.SALARY).from(EMPLOYEE)
                        .where(EMPLOYEE.MONTHLY_BONUS.isNull()).asTable("T"))
                .innerJoin(SALE)
                .on(field(name("T", "EMPLOYEE_NUMBER")).eq(SALE.EMPLOYEE_NUMBER))
                .fetch();

        ctx.select(SALE.SALE_, SALE.FISCAL_YEAR, field(name("T", "SAL")))
                .from(select(EMPLOYEE.EMPLOYEE_NUMBER.as("EN"), EMPLOYEE.SALARY.as("SAL")).from(EMPLOYEE)
                        .where(EMPLOYEE.MONTHLY_BONUS.isNull()).asTable("T"))
                .innerJoin(SALE)
                .on(field(name("T", "EN")).eq(SALE.EMPLOYEE_NUMBER))
                .fetch();

        ctx.select(field(name("T2", "S")).as("C1"), field(name("T2", "Y")).as("C2"), field(name("T2", "I")).as("C3")).from(
                select(SALE.SALE_.as("S"), SALE.FISCAL_YEAR.as("Y"), field(name("T1", "EMP_SAL")).as("I"))
                        .from(select(EMPLOYEE.EMPLOYEE_NUMBER.as("EMP_NR"), EMPLOYEE.SALARY.as("EMP_SAL"))
                                .from(EMPLOYEE)
                                .where(EMPLOYEE.MONTHLY_BONUS.isNull()).asTable("T1"))
                        .innerJoin(SALE)
                        .on(field(name("T1", "EMP_NR")).eq(SALE.EMPLOYEE_NUMBER)).asTable("T2"))
                .fetch();
    }
    
    public void sample10() {

        ctx.select().from(values(row("A", "John", 4333, false))
                .as("T", "A", "B", "C", "D")).fetch();
               
        ctx.select(min(field(name("T", "RDATE"))).as("CLUSTER_START"),
                max(field(name("T", "RDATE"))).as("CLUSTER_END"),
                min(field(name("T", "STATUS"))).as("CLUSTER_STATUS"))
                .from(select(ORDER.REQUIRED_DATE, ORDER.STATUS,
                        rowNumber().over().orderBy(ORDER.REQUIRED_DATE)
                                .minus(rowNumber().over().partitionBy(ORDER.STATUS).orderBy(ORDER.REQUIRED_DATE)))
                        .from(ORDER).asTable("T", "RDATE", "STATUS", "CLUSTER"))
                .groupBy(field(name("T", "CLUSTER")))
                .orderBy(1)
                .fetch();
    }

    public void sample11() {
        
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.COMMISSION,
                field(EMPLOYEE.COMMISSION.isNotNull()).as("C"))
                .from(EMPLOYEE)
                .fetch();
    }
    
    public void sample12() {
        
        ctx.select(EMPLOYEE.SALARY,
                count(case_().when(EMPLOYEE.SALARY.gt(0).and(EMPLOYEE.SALARY.lt(50000)), 1)).as("< 50000"),
                count(case_().when(EMPLOYEE.SALARY.gt(50000).and(EMPLOYEE.SALARY.lt(100000)), 1)).as("50000 - 100000"),
                count(case_().when(EMPLOYEE.SALARY.gt(100000), 1)).as("> 100000"))
                .from(EMPLOYEE)
                .groupBy(EMPLOYEE.SALARY)
                .fetch();

        ctx.select(EMPLOYEE.SALARY,
                count().filterWhere(EMPLOYEE.SALARY.gt(0).and(EMPLOYEE.SALARY.lt(50000))).as("< 50000"),
                count().filterWhere(EMPLOYEE.SALARY.gt(50000).and(EMPLOYEE.SALARY.lt(100000))).as("50000 - 100000"),
                count().filterWhere(EMPLOYEE.SALARY.gt(100000)).as("> 100000"))               
                .from(EMPLOYEE)
                .groupBy(EMPLOYEE.SALARY)
                .fetch();
    }
}
