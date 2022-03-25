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

        // select "public"."office"."city" from "public"."office" as "t"
        // Since we assigned an alias to "public"."office" table then 
        // 'public.office.city' column become unknown   
        /*
        ctx.select(OFFICE.CITY)
                .from(OFFICE.as("t"))
                .fetch();
         */
        // This selects all columns separated by comma, obviously not what we want
        // select t from "public"."office" as "t"    
        /*
        ctx.select(field("t", "city"))
                .from(OFFICE.as("t"))
                .fetch();
         */
        // This selects all columns, obviously not what we want
        // select "t"."office_code", "t"."city", ..., "t"."location" from "public"."office" as "t"
        /*
        ctx.select(table("t").field("city"))
                .from(OFFICE.as("t"))
                .fetch();
         */
        // The next 2 works, but are prone to ambiguities
        // select city from "public"."office" as "t"
        ctx.select(field("city"))
                .from(OFFICE.as("t"))
                .fetch();

        // select "city" from "public"."office" as "t"
        ctx.select(field(name("city")))
                .from(OFFICE.as("t"))
                .fetch();

        // This leads to an ambiguous column, city
        /*
        ctx.select(field(name("city"))) // or, field("city")
                .from(OFFICE.as("t1"), CUSTOMERDETAIL.as("t2"))
                .fetch();
         */
        // This works, but as you can see is not quite a clean result
        // select t.city from "public"."office" as "t"
        ctx.select(field("t.city"))
                .from(OFFICE.as("t"))
                .fetch();

        // No more ambiguities, but still not the best we can do
        // select t1.city, t2.city from "public"."office" as "t1", "public"."customerdetail" as "t2"
        ctx.select(field("t1.city"), field("t2.city"))
                .from(OFFICE.as("t1"), CUSTOMERDETAIL.as("t2"))
                .fetch();

        // This is better since identifiers are correctly generated
        // select "t"."city" from "public"."office" as "t"
        ctx.select(field(name("t", "city")))
                .from(OFFICE.as("t"))
                .fetch();

        // No risk for ambiguities and identifiers are correctly generated
        ctx.select(field(name("t1", "city")), field(name("t2", "city")))
                .from(OFFICE.as("t1"), CUSTOMERDETAIL.as("t2"))
                .fetch();

        ctx.select(field(name("t1", "city")).as("city_office"), field(name("t2", "city")).as("city_customer"))
                .from(OFFICE.as("t1"), CUSTOMERDETAIL.as("t2"))
                .fetch();

        // much better is to declare alias before usage
        Office t1 = OFFICE.as("t1");
        Customerdetail t2 = CUSTOMERDETAIL.as("t2");

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
        ctx.select(field("t1.city"), field("t2.name"), field("t2.profit"))
                .from(OFFICE.as("t1"))
                .join(DEPARTMENT.as("t2"))
                .on(field("t1.office_code").eq(field("t2.office_code")))
                .fetch();

        // this is not type-safe, but it has consistent identifiers
        ctx.select(field(name("t1", "city")), field(name("t2", "name")), field(name("t2", "profit")))
                .from(OFFICE.as(name("t1")))
                .join(DEPARTMENT.as(name("t2")))
                .on(field(name("t1", "office_code")).eq(field(name("t2", "office_code"))))
                .fetch();

        // extract aliases in local variables (this is type-safe)
        Office t1 = OFFICE.as("t1");
        Department t2 = DEPARTMENT.as("t2");

        ctx.select(t1.CITY, t2.NAME, t2.PROFIT)
                .from(t1)
                .join(t2)
                .on(t1.OFFICE_CODE.eq(t2.OFFICE_CODE))
                .fetch();
        
        //======or, a minimalist approach =========//        
        Department t = DEPARTMENT.as("t"); // or, Department t = DEPARTMENT;

        ctx.select(OFFICE.CITY, t.NAME, t.PROFIT)
                .from(OFFICE)
                .join(t)
                .on(OFFICE.OFFICE_CODE.eq(t.OFFICE_CODE))
                .fetch();
        //=========================================//
        
        Table<Record1<String>> t3 
                = ctx.select(t1.CITY).from(t1).asTable("t3");
        
        // refer to t3'fields type-safe
        ctx.select(t3.field(t1.CITY), CUSTOMERDETAIL.CUSTOMER_NUMBER)
                .from(t3)
                .join(CUSTOMERDETAIL)
                .on(t3.field(t1.CITY).eq(CUSTOMERDETAIL.CITY))
                .fetch();
        
        // refer to t3'fields non type-safe
        ctx.select(t3.field(name("city")), CUSTOMERDETAIL.CUSTOMER_NUMBER)
                .from(t3)
                .join(CUSTOMERDETAIL)
                .on(t3.field(name("city"), String.class).eq(CUSTOMERDETAIL.CITY))
                .fetch();
        
        // no need to extract aliases in local variables
        ctx.select(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME).as("name"),
                EMPLOYEE.EMAIL.as("contact"), EMPLOYEE.REPORTS_TO.as("boss_id"))
                .from(EMPLOYEE)
                .fetch();  
    }

    public void sample2() {

        // this is not type-safe and it has inconsistent identifiers
        ctx.selectDistinct(field("t1.city"))
                .from(OFFICE.as("t1"))
                .where(field("t1.city").in(
                        select(field("t2.city"))
                                .from(CUSTOMERDETAIL.as("t2"))
                )).fetch();

        // this is not type-safe, but it has consistent identifiers
        ctx.selectDistinct(field(name("t1", "city")))
                .from(OFFICE.as("t1"))
                .where(field(name("t1", "city")).in(
                        select(field(name("t2", "city")))
                                .from(CUSTOMERDETAIL.as("t2"))
                )).fetch();

        // extract aliases in local variables (this is type-safe)
        Office t1 = OFFICE.as("t1");
        Customerdetail t2 = CUSTOMERDETAIL.as("t2");

        ctx.selectDistinct(t1.CITY)
                .from(t1)
                .where(t1.CITY.in(select(t2.CITY)
                        .from(t2)
                )).fetch();
    }

    public void sample3() {

        // this is not type-safe and it has inconsistent identifiers
        ctx.select(count(field("t.customer_number")).as("total_customers"), field("t.country").as("nationality"))
                .from(CUSTOMERDETAIL.as("t"))
                .groupBy(field("t.country"))
                .fetch();

        ctx.select(count(field("t.customer_number")).as("total_customers"), field("t.country").as("nationality"))
                .from(CUSTOMERDETAIL.as("t"))
                .groupBy(field("country"))
                .fetch();

        ctx.select(count(field("t.customer_number")).as("total_customers"), field("t.country").as("nationality"))
                .from(CUSTOMERDETAIL.as("t"))
                .groupBy(field("nationality"))
                .fetch();

        // this is not type-safe, but it has consistent identifiers
        ctx.select(count(field(name("t", "customer_number"))).as("total_customers"),
                field(name("t", "country")).as("nationality"))
                .from(CUSTOMERDETAIL.as("t"))
                .groupBy(field(name("t", "country")))
                .fetch();

        ctx.select(count(field(name("t", "customer_number"))).as("total_customers"),
                field(name("t", "country")).as("nationality"))
                .from(CUSTOMERDETAIL.as("t"))
                .groupBy(field(name("country")))
                .fetch();

        ctx.select(count(field(name("t", "customer_number"))).as("total_customers"),
                field(name("t", "country")).as("nationality"))
                .from(CUSTOMERDETAIL.as("t"))
                .groupBy(field(name("nationality")))
                .fetch();

        // extract aliases in local variables (this is type-safe)
        Customerdetail t = CUSTOMERDETAIL.as("t");
        ctx.select(count(t.CUSTOMER_NUMBER).as("total_customers"), field(t.COUNTRY).as("nationality"))
                .from(t)
                .groupBy(t.COUNTRY)
                .fetch();
    }

    public void sample4() {

        // This is not correct! It leads to: Unknown column 'alias_foo.t.invoice_amount' in 'field list'
        /*
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME, field("t.invoice_amount"))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("t"))
                        .on(field("t.customer_number").eq(CUSTOMER.CUSTOMER_NUMBER)))
                .fetch();
        */
        
        // Solution 1: extract the aliased table    
        System.out.println("Solution 1: extract the aliased table");
        var t = select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("t");
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
                        field(name("t", "invoice_amount")))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("t"))
                        .on(field(name("t", "customer_number")).eq(CUSTOMER.CUSTOMER_NUMBER)))
                .fetch();                        
        
        // Solution 3: add an alias to field("t.invoice_amount")
        System.out.println("Solution 3: add an alias to field(\"t.invoice_amount\")");
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME,
                        field("t.invoice_amount").as("invoice_amount"))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("t"))
                        .on(field("t.customer_number").eq(CUSTOMER.CUSTOMER_NUMBER)))
                .fetch();                
        
        // Solution 4: use field("invoice_amount"), so unquoted identifier        
        System.out.println("Solution 4: use field(\"invoice_amount\"), so unquoted identifier");
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME, field("invoice_amount"))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("t"))
                        .on(field("t.customer_number").eq(CUSTOMER.CUSTOMER_NUMBER)))
                .fetch();
        
        // enrich the query with another join
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME,
                        field(name("t", "invoice_amount")))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("t"))
                        .on(field(name("t", "customer_number")).eq(CUSTOMER.CUSTOMER_NUMBER))
                        .asTable("m")
                        .join(CUSTOMERDETAIL)
                        .on(field(name("m", "customer_number")).eq(CUSTOMERDETAIL.CUSTOMER_NUMBER)))
                .fetch();
    }

    public void sample5() {

        ctx.select(field("m1.product_name").as("product_min_scale"), field("f1.product_name").as("product_max_scale"),
                coalesce(field("m1.rank"), field("f1.rank")))
                .from(select(field("p1.product_name"), count(field("p2.product_name")))
                        .from(PRODUCT.as("p1"), PRODUCT.as("p2"))
                        .where(field("p2.product_name").le(field("p1.product_name"))
                                .and(field("p1.product_scale").eq("1:24"))
                                .and(field("p2.product_scale").eq("1:24")))
                        .groupBy(field("p1.product_name")).asTable("m1", "product_name", "rank"))
                .innerJoin(select(field("p1.product_name"), count(field("p2.product_name")))
                        .from(PRODUCT.as("p1"), PRODUCT.as("p2"))
                        .where(field("p2.product_name").le(field("p1.product_name"))
                                .and(field("p1.product_scale").eq("1:32"))
                                .and(field("p2.product_scale").eq("1:32")))
                        .groupBy(field("p1.product_name")).asTable("f1", "product_name", "rank"))
                .on(field("m1.rank").eq(field("f1.rank")))
                .fetch();

        // extract aliases and add name()
        Product p1 = PRODUCT.as("p1");
        Product p2 = PRODUCT.as("p2");

        ctx.select(field(name("m1", "product_name")).as("product_min_scale"),
                field(name("f1", "product_name")).as("product_max_scale"),
                coalesce(field(name("m1", "rank")), field(name("f1", "rank"))))
                .from(select(p1.PRODUCT_NAME, count(p2.PRODUCT_NAME))
                        .from(p1, p2)
                        .where(p2.PRODUCT_NAME.le(p1.PRODUCT_NAME)
                                .and(p1.PRODUCT_SCALE.eq("1:24"))
                                .and(p2.PRODUCT_SCALE.eq("1:24")))
                        .groupBy(p1.PRODUCT_NAME).asTable("m1", "product_name", "rank"))
                .innerJoin(select(p1.PRODUCT_NAME, count(p2.PRODUCT_NAME))
                        .from(p1, p2)
                        .where(p2.PRODUCT_NAME.le(p1.PRODUCT_NAME)
                                .and(p1.PRODUCT_SCALE.eq("1:32"))
                                .and(p2.PRODUCT_SCALE.eq("1:32")))
                        .groupBy(p1.PRODUCT_NAME).asTable("f1", "product_name", "rank"))
                .on(field(name("m1", "rank")).eq(field(name("f1", "rank"))))
                .fetch();
    }

    public void sample6() {

        ctx.select(EMPLOYEE.JOB_TITLE, 
                sum(EMPLOYEE.SALARY.minus(EMPLOYEE.COMMISSION)).as("amount"))
           .from(EMPLOYEE)
           .groupBy(EMPLOYEE.JOB_TITLE)
           .orderBy(2) // or, .orderBy(field(name("amount")))
           .fetch();
         
        ctx.select(PRODUCT.PRODUCT_VENDOR.as("f"))
                .from(PRODUCT)
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

        Field<String> f1 = PRODUCT.PRODUCT_VENDOR.as("f");
        ctx.select(f1)
                .from(PRODUCT)
                .groupBy(f1)
                .fetch();

        ctx.select(f1)
                .from(PRODUCT)
                .orderBy(f1)
                .fetch();

        Field<String> f2 = PRODUCT.PRODUCT_VENDOR;
        ctx.select(f2.as("f"))
                .from(PRODUCT)
                .groupBy(field(name("f")))
                .fetch();

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

        Field<String> f6 = PRODUCT.PRODUCT_VENDOR;
        Field<String> f7 = PRODUCT.PRODUCT_VENDOR.as("f");
        ctx.select(f7)
                .from(PRODUCT)
                .groupBy(f7)
                .orderBy(f6)
                .fetch();
    }

    public void sample7() {

        // This is not correct! It leads to: Column reference "office_code" is ambiguous
        /*
        ctx.select().from(
                OFFICE.leftOuterJoin(DEPARTMENT)
                        .on(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)))
                .innerJoin(EMPLOYEE)
                .on(EMPLOYEE.OFFICE_CODE.eq(field(name("office_code"), String.class)))
                .fetch();
         */
        
        // This is not correct! It leads to: Invalid reference to FROM-clause entry for table "office"
        /*
        ctx.select().from(
                OFFICE.as("o").leftOuterJoin(DEPARTMENT.as("d"))
                        .on(field(name("o","office_code")).eq(field(name("d","office_code")))))
                .innerJoin(EMPLOYEE)
                .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                .fetch();
         */
        
        ctx.select().from(
                OFFICE.as("o").leftOuterJoin(DEPARTMENT.as("d"))
                        .on(field(name("o","office_code")).eq(field(name("d","office_code")))))
                .innerJoin(EMPLOYEE)
                .on(EMPLOYEE.OFFICE_CODE.eq(field(name("o","office_code"), String.class)))
                .fetch();
        
        Office o = OFFICE.as("o");
        Department d = DEPARTMENT.as("d");
        
        ctx.select().from(o.leftOuterJoin(d)
                        .on(o.OFFICE_CODE.eq(d.OFFICE_CODE)))
                .innerJoin(EMPLOYEE)
                .on(EMPLOYEE.OFFICE_CODE.eq(o.OFFICE_CODE))
                .fetch();
    }
    
    public void sample8() {

        // There is a mistake in this query. Can you spot it?
        ctx.select(field("s1.msrp"), field("s2.msrp"))
                .from(PRODUCT.as("s1"), PRODUCT.as("s2"))
                .where(field("s1.msrp").lt(field("s2.msrp"))
                        .and(field("s1.product_line").eq("s2.product_line")))
                .groupBy(field("s1.msrp"), field("s2.msrp"))
                .having(count().eq(selectCount().from(PRODUCT.as("s3"))
                        .where(field("s3.msrp").eq(field("s1.msrp"))))
                        .and(count().eq(selectCount().from(PRODUCT.as("s4"))
                                .where(field("s4.msrp").eq(field("s2.msrp"))))))
                .fetch();

        // If you did not spot it then you have a good reason to write it as here
        Product s1 = PRODUCT.as("s1");
        Product s2 = PRODUCT.as("s2");
        Product s3 = PRODUCT.as("s3");
        Product s4 = PRODUCT.as("s4");

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
                .from(select(EMPLOYEE.EMPLOYEE_NUMBER.as("en"), EMPLOYEE.SALARY.as("sal")).from(EMPLOYEE)
                        .where(EMPLOYEE.MONTHLY_BONUS.isNull()))
                .innerJoin(SALE)
                .on(field(name("en")).eq(SALE.EMPLOYEE_NUMBER))
                .fetch();

        ctx.select(SALE.SALE_, SALE.FISCAL_YEAR, field(name("sal")))
                .from(select(EMPLOYEE.EMPLOYEE_NUMBER.as("en"), EMPLOYEE.SALARY.as("sal")).from(EMPLOYEE)
                        .where(EMPLOYEE.MONTHLY_BONUS.isNull()))
                .innerJoin(SALE)
                .on(field(name("en")).eq(SALE.EMPLOYEE_NUMBER))
                .fetch();

        ctx.select()
                .from(select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.SALARY).from(EMPLOYEE)
                        .where(EMPLOYEE.MONTHLY_BONUS.isNull()).asTable("t"))
                .innerJoin(SALE)
                .on(field(name("t", "employee_number")).eq(SALE.EMPLOYEE_NUMBER))
                .fetch();

        ctx.select(SALE.SALE_, SALE.FISCAL_YEAR, field(name("t", "salary")))
                .from(select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.SALARY).from(EMPLOYEE)
                        .where(EMPLOYEE.MONTHLY_BONUS.isNull()).asTable("t"))
                .innerJoin(SALE)
                .on(field(name("t", "employee_number")).eq(SALE.EMPLOYEE_NUMBER))
                .fetch();

        ctx.select(SALE.SALE_, SALE.FISCAL_YEAR, field(name("t", "sal")))
                .from(select(EMPLOYEE.EMPLOYEE_NUMBER.as("en"), EMPLOYEE.SALARY.as("sal")).from(EMPLOYEE)
                        .where(EMPLOYEE.MONTHLY_BONUS.isNull()).asTable("t"))
                .innerJoin(SALE)
                .on(field(name("t", "en")).eq(SALE.EMPLOYEE_NUMBER))
                .fetch();

        ctx.select(field(name("t2", "s")).as("c1"), field(name("t2", "y")).as("c2"), field(name("t2", "i")).as("c3")).from(
                select(SALE.SALE_.as("s"), SALE.FISCAL_YEAR.as("y"), field(name("t1", "emp_sal")).as("i"))
                        .from(select(EMPLOYEE.EMPLOYEE_NUMBER.as("emp_nr"), EMPLOYEE.SALARY.as("emp_sal"))
                                .from(EMPLOYEE)
                                .where(EMPLOYEE.MONTHLY_BONUS.isNull()).asTable("t1"))
                        .innerJoin(SALE)
                        .on(field(name("t1", "emp_nr")).eq(SALE.EMPLOYEE_NUMBER)).asTable("t2"))
                .fetch();
    }
    
    public void sample10() {

        ctx.select().from(values(row("A", "John", 4333, false))
                .as("t", "A", "B", "C", "D")).fetch();

        ctx.select(min(field(name("t", "rdate"))).as("cluster_start"),
                max(field(name("t", "rdate"))).as("cluster_end"),
                min(field(name("t", "status"))).as("cluster_status"))
                .from(select(ORDER.REQUIRED_DATE, ORDER.STATUS,
                        rowNumber().over().orderBy(ORDER.REQUIRED_DATE)
                                .minus(rowNumber().over().partitionBy(ORDER.STATUS).orderBy(ORDER.REQUIRED_DATE)))
                        .from(ORDER).asTable("t", "rdate", "status", "cluster"))
                .groupBy(field(name("t", "cluster")))
                .orderBy(1)
                .fetch();
    }

    public void sample11() {
        
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.COMMISSION,
                field(EMPLOYEE.COMMISSION.isNotNull()).as("c"))
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
