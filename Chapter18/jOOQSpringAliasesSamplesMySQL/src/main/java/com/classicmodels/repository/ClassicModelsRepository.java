package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Customer.CUSTOMER;
import jooq.generated.tables.Customerdetail;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import jooq.generated.tables.Employee;
import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.Office;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Payment.PAYMENT;
import jooq.generated.tables.Product;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.Sale;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void sample1() {

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

    public void sample2() {

        // this is not type-safe and it has inconsistent identifiers
        ctx.select(field("s1.sale"), field("s1.fiscal_year"), field("s1.employee_number"))
                .from(SALE.as("s1"))
                .where(field("s1.sale").eq(select(max(field("s2.sale")))
                        .from(SALE.as("s2"))
                        .where(field("s2.employee_number").eq(field("s1.employee_number"))
                                .and(field("s2.fiscal_year").eq(field("s1.fiscal_year"))))))
                .orderBy(field("s1.fiscal_year"))
                .fetch();

        // this is not type-safe, but it has consistent identifiers
        ctx.select(field(name("s1", "sale")), field(name("s1", "fiscal_year")), field(name("s1", "employee_number")))
                .from(SALE.as("s1"))
                .where(field(name("s1", "sale")).eq(select(max(field(name("s2", "sale"))))
                        .from(SALE.as("s2"))
                        .where(field(name("s2", "employee_number")).eq(field(name("s1", "employee_number")))
                                .and(field(name("s2", "fiscal_year")).eq(field(name("s1", "fiscal_year")))))))
                .orderBy(field(name("s1", "fiscal_year")))
                .fetch();

        // extract aliases in local variables (this is type-safe)
        Sale s1 = SALE.as("s1");
        Sale s2 = SALE.as("s2");

        ctx.select(s1.SALE_, s1.FISCAL_YEAR, s1.EMPLOYEE_NUMBER)
                .from(s1)
                .where(s1.SALE_.eq(select(max(s2.SALE_))
                        .from(s2)
                        .where(s2.EMPLOYEE_NUMBER.eq(s1.EMPLOYEE_NUMBER)
                                .and(s2.FISCAL_YEAR.eq(s1.FISCAL_YEAR)))))
                .orderBy(s1.FISCAL_YEAR)
                .fetch();
    }

    public void sample3() {

        // this is not type-safe and it has inconsistent identifiers
        ctx.select(count(field("t.customer_number")).as("total_customers"), field("t.country").as("nation"))
                .from(CUSTOMERDETAIL.as("t"))
                .groupBy(field("t.country"))
                .fetch();

        // this is not type-safe, but it has consistent identifiers
        ctx.select(count(field(name("t", "customer_number"))).as("total_customers"),
                field(name("t", "country")).as("nation"))
                .from(CUSTOMERDETAIL.as("t"))
                .groupBy(field(name("t", "country")))
                .fetch();

        // extract aliases in local variables (this is type-safe)
        Customerdetail t = CUSTOMERDETAIL.as("t");
        ctx.select(count(t.CUSTOMER_NUMBER).as("total_customers"), field(t.COUNTRY).as("nation"))
                .from(t)
                .groupBy(t.COUNTRY)
                .fetch();
    }

    public void sample4() {

        // this is not type-safe and it has inconsistent identifiers
        ctx.select(field("t1.employee_number").as("identifier"),
                concat(field("t1.last_name"), val(", "), field("t1.first_name")).as("customer_name"),
                sum(field("t2.sale", BigDecimal.class)).as("total_sale"))
                .from(EMPLOYEE.as("t1"))
                .join(SALE.as("t2"))
                .on(field("t1.employee_number").eq(field("t2.employee_number")))
                .groupBy(field("t1.employee_number"),
                        concat(field("t1.last_name"), val(", "), field("t1.first_name")))
                .orderBy(field("total_sale"))
                .fetch();

        // this is not type-safe, but it has consistent identifiers
        ctx.select(field(name("t1", "employee_number")).as("identifier"),
                concat(field(name("t1", "last_name")), val(", "), field(name("t1", "first_name"))).as("customer_name"),
                sum(field(name("t2", "sale"), BigDecimal.class)).as("total_sale"))
                .from(EMPLOYEE.as("t1"))
                .join(SALE.as("t2"))
                .on(field(name("t1", "employee_number")).eq(field(name("t2", "employee_number"))))
                .groupBy(field(name("t1", "employee_number")),
                        concat(field(name("t1", "last_name")), val(", "), field(name("t1", "first_name"))))
                .orderBy(field(name("total_sale")))
                .fetch();

        // extract aliases in local variables (this is type-safe)
        Employee t1 = EMPLOYEE.as("t1");
        Sale t2 = SALE.as("t2");

        ctx.select(t1.EMPLOYEE_NUMBER.as("identifier"),
                concat(t1.LAST_NAME, val(", "), t1.FIRST_NAME).as("customer_name"),
                sum(t2.SALE_).as("total_sale"))
                .from(t1)
                .join(t2)
                .on(t1.EMPLOYEE_NUMBER.eq(t2.EMPLOYEE_NUMBER))
                .groupBy(t1.EMPLOYEE_NUMBER,
                        concat(t1.LAST_NAME, val(", "), t1.FIRST_NAME))
                .orderBy(field(name("total_sale")))
                .fetch();

        // extract more aliases
        Field<BigDecimal> s = sum(t2.SALE_).as("total_sale");

        ctx.select(t1.EMPLOYEE_NUMBER.as("identifier"),
                concat(t1.LAST_NAME, val(", "), t1.FIRST_NAME).as("customer_name"), s)
                .from(t1)
                .join(t2)
                .on(t1.EMPLOYEE_NUMBER.eq(t2.EMPLOYEE_NUMBER))
                .groupBy(t1.EMPLOYEE_NUMBER,
                        concat(t1.LAST_NAME, val(", "), t1.FIRST_NAME))
                .orderBy(s)
                .fetch();
    }

    public void sample5() {

        // Step 1
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
        // Step 2 (a)
        // use field("invoice_amount")
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME, field("invoice_amount"))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("t"))
                        .on(field("t.customer_number").eq(CUSTOMER.CUSTOMER_NUMBER)))
                .fetch();

        // Step 2 (b)
        // add an alias to field("t.invoice_amount")
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME,
                        field("t.invoice_amount").as("invoice_amount"))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("t"))
                        .on(field("t.customer_number").eq(CUSTOMER.CUSTOMER_NUMBER)))
                .fetch();

        // Step 2 (c)
        // or, even better use name() for proper quotation
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME,
                        field(name("t", "invoice_amount")))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("t"))
                        .on(field(name("t", "customer_number")).eq(CUSTOMER.CUSTOMER_NUMBER)))
                .fetch();

        // Step 3
        // add an explicit alias, so jOOQ should not generate one
        ctx.select()
                .from(select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME,
                        field(name("t", "invoice_amount")))
                        .from(CUSTOMER)
                        .join(select(PAYMENT.CUSTOMER_NUMBER, PAYMENT.INVOICE_AMOUNT)
                                .from(PAYMENT).asTable("t"))
                        .on(field(name("t", "customer_number")).eq(CUSTOMER.CUSTOMER_NUMBER))
                        .asTable("m"))
                .fetch();

        // Step 4
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

    public void sample6() {

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
    
    // WFunc si CTE  (book)
}
