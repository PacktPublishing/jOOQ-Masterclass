package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.list;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.query;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void samples() {

        ctx.fetch("""
                  SELECT first_name, last_name 
                  FROM employee WHERE salary > ? AND job_title = ?
                  """, 5000, "Sales Rep");

        ctx.resultQuery("""
                        SELECT first_name, last_name 
                        FROM employee WHERE salary > ? AND job_title = ?
                        """, 5000, "Sales Rep")
                .fetch();

        ctx.query("""
                  UPDATE product SET product.quantity_in_stock = ? 
                      WHERE product.product_id = ?
                  """, 0, 2)
                .execute();
        
        // ctx.queries(query(""), query(""), query("")).executeBatch();

        ctx.select(field("@id:=LAST_INSERT_ID()")).fetch();

        ctx.select(EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY,
                field("@num := if(@type = {0}, @num + 1, 1)", EMPLOYEE.JOB_TITLE).as("rn"),
                field("@type := {0}", EMPLOYEE.JOB_TITLE).as("dummy"))
                .from(EMPLOYEE)
                .orderBy(EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY)
                .fetch();

        var msrp = select(field("@msrp:={0}", BigDecimal.class, max(PRODUCT.MSRP))).from(PRODUCT);
        ctx.select(PRODUCT.PRODUCT_NAME)
                .from(PRODUCT)
                .where(PRODUCT.MSRP.eq(msrp))
                .fetch();

        ctx.select(
                PRODUCT.PRODUCT_NAME,
                field("CONCAT_WS({0}, {1}, {2})", String.class, val("-"), PRODUCT.BUY_PRICE, PRODUCT.MSRP))
                .from(PRODUCT)
                .fetch();

        ctx.select(
                PRODUCT.PRODUCT_NAME,
                field("CONCAT_WS({0}, {1})", String.class, val("-"), list(PRODUCT.BUY_PRICE, PRODUCT.MSRP)))
                .from(PRODUCT)
                .fetch();

        ctx.select(field("(@cnt := @cnt + 1)", SQLDataType.BIGINT).as("rowNumber"),
                OFFICE.OFFICE_CODE)
                .from(OFFICE)
                .crossJoin(table(select(field("@cnt := 0", SQLDataType.BIGINT))).as("n"))
                .orderBy(OFFICE.OFFICE_CODE)
                .limit(10)
                .fetch();

        ctx.select()
                .from(OFFICE)
                .innerJoin(CUSTOMERDETAIL)
                .on(OFFICE.POSTAL_CODE.eq(CUSTOMERDETAIL.POSTAL_CODE))
                .where("""
                       not(
                           (
                             `classicmodels`.`office`.`city`, 
                             `classicmodels`.`office`.`country`
                           ) <=> (
                             `classicmodels`.`customerdetail`.`city`, 
                             `classicmodels`.`customerdetail`.`country`
                           )
                         )
                       """)
                .fetch();

        /* The previous code is the equivalent of this one */
        ctx.select()
                .from(OFFICE)
                .innerJoin(CUSTOMERDETAIL)
                .on(OFFICE.POSTAL_CODE.eq(CUSTOMERDETAIL.POSTAL_CODE))
                .where(row(OFFICE.CITY, OFFICE.COUNTRY)
                        .isDistinctFrom(row(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)))
                .fetch();
    }
}
