package com.classicmodels.repository;

import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.Nullability.NULL;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.greatest;
import static org.jooq.impl.DSL.iif;
import static org.jooq.impl.DSL.least;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.nvl2;
import static org.jooq.impl.DSL.sign;
import static org.jooq.impl.DSL.sum;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void coalesceDecodeIif() {

        // COALESCE        
        System.out.println(
                ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE, DEPARTMENT.LOCAL_BUDGET,
                        (DEPARTMENT.LOCAL_BUDGET.mul(2)).divide(100),
                        coalesce(DEPARTMENT.LOCAL_BUDGET, 0).mul(2).divide(100).as("coalesce"))
                        .from(DEPARTMENT)
                        .fetch().format(1000));

        // DECODE
        System.out.println(
                ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE, DEPARTMENT.LOCAL_BUDGET,
                        (DEPARTMENT.LOCAL_BUDGET.mul(2)).divide(100),
                        decode(DEPARTMENT.LOCAL_BUDGET, NULL, 0, DEPARTMENT.LOCAL_BUDGET)
                                .mul(2).divide(100).as("decode"))
                        .from(DEPARTMENT)
                        .fetch().format(1000));

        // DECODE AND MULTIPLE VALUES
        System.out.println(
                ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE, DEPARTMENT.LOCAL_BUDGET,
                        decode(DEPARTMENT.NAME,
                                "Advertising", "Publicity and promotion",
                                "Accounting", "Monetary and business",
                                "Logistics", "Facilities and supplies",
                                DEPARTMENT.NAME).concat(" department").as("description"))
                        .from(DEPARTMENT)
                        .fetch().format(1000));

        // DECODE AND ORDER BY
        String c = "N"; // input parameter (it may come from the database), 
        //pay attention that ORDER BY cannot use indexes in this case      
        ctx.select(DEPARTMENT.NAME, DEPARTMENT.LOCAL_BUDGET, DEPARTMENT.OFFICE_CODE)
                .from(DEPARTMENT)
                .orderBy(
                        decode(c,
                                "N", DEPARTMENT.NAME,
                                "B", DEPARTMENT.LOCAL_BUDGET.cast(String.class),
                                "C", DEPARTMENT.CODE.cast(String.class)))
                .fetch();

        // DECODE AND GROUP BY
        System.out.println(
                ctx.select(decode(sign(PRODUCT.BUY_PRICE.minus(PRODUCT.MSRP.divide(2))),
                        1, "Buy price larger than half of MSRP",
                        0, "Buy price larger than half of MSRP",
                        -1, "Buy price smaller than half of MSRP"), count())
                        .from(PRODUCT)
                        .groupBy(decode(sign(PRODUCT.BUY_PRICE.minus(PRODUCT.MSRP.divide(2))),
                                1, "Buy price larger than half of MSRP",
                                0, "Buy price larger than half of MSRP",
                                -1, "Buy price smaller than half of MSRP"), PRODUCT.BUY_PRICE, PRODUCT.MSRP)
                        .fetch().format(10000));

        // DECODE AND SUM
        System.out.println(
                ctx.select(PRODUCT.PRODUCT_LINE,
                        sum(decode(greatest(PRODUCT.BUY_PRICE, 0), least(PRODUCT.BUY_PRICE, 35), 1, 0)).as("< 35"),
                        sum(decode(greatest(PRODUCT.BUY_PRICE, 36), least(PRODUCT.BUY_PRICE, 55), 1, 0)).as("36-55"),
                        sum(decode(greatest(PRODUCT.BUY_PRICE, 56), least(PRODUCT.BUY_PRICE, 75), 1, 0)).as("56-75"),
                        sum(decode(greatest(PRODUCT.BUY_PRICE, 76), least(PRODUCT.BUY_PRICE, 150), 1, 0)).as("76-150"))
                        .from(PRODUCT)
                        .groupBy(PRODUCT.PRODUCT_LINE)
                        .fetch().format(1000));

        // DECODE AND DECODE
        System.out.println(
                ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE,
                        DEPARTMENT.LOCAL_BUDGET, DEPARTMENT.PROFIT,
                        decode(DEPARTMENT.LOCAL_BUDGET, NULL, DEPARTMENT.PROFIT,
                                decode(sign(DEPARTMENT.PROFIT.minus(DEPARTMENT.LOCAL_BUDGET)),
                                        1, DEPARTMENT.PROFIT.minus(DEPARTMENT.LOCAL_BUDGET),
                                        0, DEPARTMENT.LOCAL_BUDGET.divide(2).mul(-1),
                                        -1, DEPARTMENT.LOCAL_BUDGET.mul(-1))).as("profit_balance"))
                        .from(DEPARTMENT)
                        .fetch().format(1000));

        // IIF
        ctx.select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.QUANTITY_ORDERED,
                iif(ORDERDETAIL.QUANTITY_ORDERED.gt(45), "MORE", "LESS").as("45"))
                .from(ORDERDETAIL)
                .fetch();

        System.out.println(
                ctx.select(
                        iif(PRODUCT.PRODUCT_SCALE.eq("1:10"), "A",
                                iif(PRODUCT.PRODUCT_SCALE.eq("1:12"), "B",
                                        iif(PRODUCT.PRODUCT_SCALE.eq("1:18"), "C",
                                                iif(PRODUCT.PRODUCT_SCALE.eq("1:24"), "D",
                                                        iif(PRODUCT.PRODUCT_SCALE.eq("1:32"), "E",
                                                                iif(PRODUCT.PRODUCT_SCALE.eq("1:50"), "F",
                                                                        iif(PRODUCT.PRODUCT_SCALE.eq("1:72"), "G",
                                                                                iif(PRODUCT.PRODUCT_SCALE.eq("1:700"), "H", "N/A")
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        ).as("class_scale"), count())
                        .from(PRODUCT)
                        .groupBy(PRODUCT.PRODUCT_SCALE)
                        .fetch().format(1000));

        // NULLIF
        ctx.selectFrom(OFFICE)
                .where(nullif(OFFICE.COUNTRY, "").isNull())
                .fetch();

        // NVL
        System.out.println(
                ctx.select(OFFICE.OFFICE_CODE, nvl(OFFICE.CITY, "N/A"), nvl(OFFICE.COUNTRY, "N/A"))
                        .from(OFFICE)
                        .fetch().format(1000));

        // NVL2        
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                iif(EMPLOYEE.COMMISSION.isNotNull(), 
                        EMPLOYEE.SALARY.plus(EMPLOYEE.COMMISSION), EMPLOYEE.SALARY).as("iif"),
                nvl2(EMPLOYEE.COMMISSION, 
                        EMPLOYEE.SALARY.plus(EMPLOYEE.COMMISSION), EMPLOYEE.SALARY).as("nvl2"))
                .from(EMPLOYEE)
                .fetch();
    }
}