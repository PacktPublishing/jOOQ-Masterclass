package com.classicmodels.repository;

import static jooq.generated.System.SYSTEM;
import jooq.generated.tables.Customerdetail;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.Office;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.records.ProductRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void simpleTableAlias() {

        // using schema name
        Table<ProductRecord> p0 = SYSTEM.PRODUCT;

        Table<ProductRecord> p1 = PRODUCT;
        Table<ProductRecord> p2 = PRODUCT.as("p");
        Table<ProductRecord> p3 = PRODUCT.as(name("p"));

        System.out.println("Table name (0): " + p0.getName());
        System.out.println("Table name (1): " + p1.getName());
        System.out.println("Table name (2): " + p2.getName());
        System.out.println("Table name (3): " + p3.getName());

        System.out.println("Table unqualified name (0): " + p0.getUnqualifiedName());
        System.out.println("Table unqualified name (1): " + p1.getUnqualifiedName());
        System.out.println("Table unqualified name (2): " + p2.getUnqualifiedName());
        System.out.println("Table unqualified name (3): " + p3.getUnqualifiedName());

        System.out.println("Table qualified name (0): " + p0.getQualifiedName());
        System.out.println("Table qualified name (1): " + p1.getQualifiedName());
        System.out.println("Table qualified name (2): " + p2.getQualifiedName());
        System.out.println("Table qualified name (3): " + p3.getQualifiedName());
    }

    public void tableEquality() {

        Table<ProductRecord> p1 = PRODUCT.as("p1");
        Table<ProductRecord> p2 = PRODUCT.as("p2");
        Table<ProductRecord> p3 = PRODUCT;
        Table<ProductRecord> p4 = PRODUCT;

        System.out.println("p1 = p2 ? " + (p1 == p2));
        System.out.println("p3 = p4 ? " + (p3 == p4));
        System.out.println("p1 = p3 ? " + (p1 == p3));
        System.out.println("p2 = p4 ? " + (p2 == p4));
    }

    public void simpleColumnAlias() {

        // using schema name
        Field<String> f0 = SYSTEM.PRODUCT.PRODUCT_NAME;

        Field<String> f1 = PRODUCT.PRODUCT_NAME;
        Field<String> f2 = PRODUCT.PRODUCT_NAME.as("f");
        Field<String> f3 = PRODUCT.PRODUCT_NAME.as(name("f"));

        System.out.println("Field name (0): " + f0.getName());
        System.out.println("Field name (1): " + f1.getName());
        System.out.println("Field name (2): " + f2.getName());
        System.out.println("Field name (3): " + f3.getName());

        System.out.println("Field unqualified name (0): " + f0.getUnqualifiedName());
        System.out.println("Field unqualified name (1): " + f1.getUnqualifiedName());
        System.out.println("Field unqualified name (2): " + f2.getUnqualifiedName());
        System.out.println("Field unqualified name (3): " + f3.getUnqualifiedName());

        System.out.println("Field qualified name (0): " + f0.getQualifiedName());
        System.out.println("Field qualified name (1): " + f1.getQualifiedName());
        System.out.println("Field qualified name (2): " + f2.getQualifiedName());
        System.out.println("Field qualified name (3): " + f3.getQualifiedName());
    }

    public void fieldEquality() {

        Field<String> f1 = PRODUCT.PRODUCT_NAME.as("f");
        Field<String> f2 = PRODUCT.PRODUCT_NAME.as("f");
        Field<String> f3 = PRODUCT.PRODUCT_NAME;
        Field<String> f4 = PRODUCT.PRODUCT_NAME;

        System.out.println("f1 = f2 ? " + (f1 == f2));
        System.out.println("f3 = f4 ? " + (f3 == f4));
        System.out.println("f1 = f3 ? " + (f1 == f3));
        System.out.println("f2 = f4 ? " + (f2 == f4));
    }

    public void simpleSelectAndAs() {

        // column aliases in select
        ctx.select(EMPLOYEE.FIRST_NAME.as("fn"), EMPLOYEE.LAST_NAME.as("ln"))
                .from(EMPLOYEE)
                .fetch();

        // select "SYSTEM"."OFFICE"."CITY" from "SYSTEM"."OFFICE" "t"
        // Since we assigned an alias to "SYSTEM"."OFFICE" table then 
        // "SYSTEM"."OFFICE"."CITY" column become unknown   
        /*
        ctx.select(OFFICE.CITY)
                .from(OFFICE.as("t"))
                .fetch();
         */
        
        // This leads to ORA-00904: "T": invalid identifier
        // select t from "SYSTEM"."OFFICE" "t"
        /*
        ctx.select(field("t", "city"))
                .from(OFFICE.as("t"))
                .fetch();
        */
        
        // This selects all columns, obviously not what we want
        // select "t"."OFFICE_CODE", "t"."CITY", ... , "t"."LOCATION" from "SYSTEM"."OFFICE" "t"
        /*
        ctx.select(table("t").field("city"))
                .from(OFFICE.as("t"))
                .fetch();
        */
        
        // The next one works, but is prone to ambiguities
        // It works because the unquoted city and CITY are the same identifiers
        // select city from "SYSTEM"."OFFICE" "t"
        ctx.select(field("city"))
                .from(OFFICE.as("t"))
                .fetch();

        // This leads to ORA-00904: "city": invalid identifier
        // This doesn't work because the quoted "city" and "CITY" are not the same
        // select "city" from "SYSTEM"."OFFICE" "t"
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
        // select t.city from "SYSTEM"."OFFICE" "t"
        /*
        ctx.select(field("t.city"))
                .from(OFFICE.as("t"))
                .fetch();
        */

        // This leads to ORA-00904: "T2"."CITY": invalid identifier
        // select t1.city, t2.city from "SYSTEM"."OFFICE" "t1", "SYSTEM"."CUSTOMERDETAIL" "t2"
        /*
        ctx.select(field("t1.city"), field("t2.city"))
                .from(OFFICE.as("t1"), CUSTOMERDETAIL.as("t2"))
                .fetch();
        */

        // This leads to ORA-00904: "t"."city": invalid identifier
        // select "t"."city" from "SYSTEM"."OFFICE" "t"
        /*
        ctx.select(field(name("t", "city")))
                .from(OFFICE.as("t"))
                .fetch();
        */
        
        // This finally works (but, prefer the next one)
        // select T.CITY from "SYSTEM"."OFFICE" "T"
        ctx.select(field("T.CITY"))
                .from(OFFICE.as("T"))
                .fetch();
        // This DOES NOT work!
        /*
        ctx.select(field("t.CITY"))
                .from(OFFICE.as("t"))
                .fetch();
        */
        
        // select "T"."CITY" from "SYSTEM"."OFFICE" "T"
        ctx.select(field(name("T", "CITY")))
                .from(OFFICE.as("T"))
                .fetch();
        // select "t"."CITY" from "SYSTEM"."OFFICE" "t"
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
    }
}
