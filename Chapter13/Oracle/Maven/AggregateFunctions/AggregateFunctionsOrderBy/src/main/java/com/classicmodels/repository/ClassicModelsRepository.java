package com.classicmodels.repository;

import java.time.LocalDate;
import java.util.Arrays;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.Order;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.udt.records.SalaryArrRecord;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.collect;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.denseRank;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonEntry;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlelement;
import org.jooq.impl.SQLDataType;
import static org.jooq.util.oracle.OracleDSL.rowid;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // JSON_ARRAYAGG()
    public void jsonArrayAggSale() {

        String result = ctx.select(jsonArrayAgg(jsonObject(
                jsonEntry("id", SALE.SALE_ID),
                jsonEntry("sale", SALE.SALE_)))
                .orderBy(SALE.SALE_).as("json_result"))
                .from(SALE)
                .limit(5)
                .fetchSingleInto(String.class);

        System.out.println("Result:\n" + result);
    }

    // XMLAGG()
    public void xmlAggCustomer() {

        String result = ctx.select(xmlagg(
                xmlelement("name", CUSTOMER.CUSTOMER_NAME))
                .orderBy(CUSTOMER.CUSTOMER_NAME.desc()))
                .from(CUSTOMER)
                .fetchSingleInto(String.class);

        System.out.println("Result:\n" + result);
    }

    // COLLECT()
    public void collectSale() {

        // Result<Record1<SalaryArrRecord>>
        var result1 = ctx.select(collect(EMPLOYEE.SALARY, SalaryArrRecord.class)
                .orderBy(EMPLOYEE.SALARY.asc(), EMPLOYEE.JOB_TITLE.desc()))
                .from(EMPLOYEE)
                .fetch();

        System.out.println("Result (1):\n" + Arrays.toString(result1.get(0).value1().toArray(Integer[]::new)));
        System.out.println("Result (1) the fifth element:" + result1.get(0).value1().get(5));

        // Result<Record1<SalaryArrRecord>>
        var result2 = ctx.select(collect(EMPLOYEE.SALARY,
                SQLDataType.INTEGER.asArrayDataType(SalaryArrRecord.class))
                .orderBy(EMPLOYEE.SALARY.asc(), EMPLOYEE.JOB_TITLE.desc()))
                .from(EMPLOYEE)
                .fetch();

        System.out.println("Result (2):\n" + Arrays.toString(result2.get(0).value1().toArray(Integer[]::new)));
        System.out.println("Result (2), the fifth element:" + result2.get(0).value1().get(5));

        SalaryArrRecord result3 = ctx.select(collect(EMPLOYEE.SALARY, SalaryArrRecord.class)
                .orderBy(EMPLOYEE.SALARY.asc(), EMPLOYEE.JOB_TITLE.desc()))
                .from(EMPLOYEE)
                .fetchOneInto(SalaryArrRecord.class);

        System.out.println("Result (3):\n" + Arrays.toString(result3.toArray(Integer[]::new)));
        System.out.println("Result (3), the fifth element:fetchOneInto(SalaryArrRecord.class)" + result3.get(5));
    }

    // GROUP_CONCAT()
    public void groupConcatEmployee() {

        ctx.select(groupConcat(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME))
                .orderBy(EMPLOYEE.SALARY.desc()).separator("; ").as("names_of_employees"))
                .from(EMPLOYEE)
                .fetch();
    }

    // Oracle's keep()
    public void keepingExample() {

        ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                denseRank().over().partitionBy(SALE.FISCAL_YEAR)
                        .orderBy(SALE.SALE_.desc()).as("sales_rank"),
                min(SALE.SALE_).keepDenseRankFirstOrderBy(SALE.SALE_).over()
                        .partitionBy(SALE.FISCAL_YEAR),
                max(SALE.SALE_).keepDenseRankLastOrderBy(SALE.SALE_).over()
                        .partitionBy(SALE.FISCAL_YEAR))
                .from(SALE)
                .fetch();

        ctx.select(
                min(SALE.SALE_).keepDenseRankLastOrderBy(SALE.FISCAL_YEAR),
                max(SALE.SALE_).keepDenseRankFirstOrderBy(SALE.FISCAL_YEAR),
                sum(SALE.SALE_).keepDenseRankFirstOrderBy(SALE.FISCAL_YEAR),
                count(SALE.SALE_).keepDenseRankLastOrderBy(SALE.FISCAL_YEAR))
                .from(SALE)
                .fetch();
    }

    public void roadToKeep() {

        // select only the ORDER_DATE closest to 2004-06-06 by customer number
        ctx.select(ORDER.CUSTOMER_NUMBER, max(ORDER.ORDER_DATE))
                .from(ORDER)
                .where(ORDER.ORDER_DATE.lt(LocalDate.of(2004, 6, 6)))
                .groupBy(ORDER.CUSTOMER_NUMBER)
                .fetch();

        // select more information such as shipped date and status
        
        // using SELECT DISTINCT ON        
        ctx.selectDistinct(ORDER.CUSTOMER_NUMBER, ORDER.ORDER_DATE, 
                ORDER.SHIPPED_DATE, ORDER.STATUS).on(ORDER.CUSTOMER_NUMBER)
                .from(ORDER)
                .where(ORDER.ORDER_DATE.lt(LocalDate.of(2004, 6, 6)))
                .orderBy(ORDER.CUSTOMER_NUMBER, ORDER.ORDER_DATE.desc())
                .fetch();
        
        // using left anti join
        Order t = ORDER.as("T");
        ctx.select(ORDER.CUSTOMER_NUMBER, ORDER.ORDER_DATE, ORDER.SHIPPED_DATE, ORDER.STATUS)
                .from(ORDER)
                .where(ORDER.ORDER_DATE.lt(LocalDate.of(2004, 6, 6)).andNotExists(
                        selectOne()
                                .from(t)
                                .where(ORDER.CUSTOMER_NUMBER.eq(t.CUSTOMER_NUMBER)
                                        .and(t.ORDER_DATE.lt(LocalDate.of(2004, 6, 6))
                                                .and(t.ORDER_DATE.gt(ORDER.ORDER_DATE)))
                                ))).fetch();

        ctx.select(ORDER.CUSTOMER_NUMBER, ORDER.ORDER_DATE, ORDER.SHIPPED_DATE, ORDER.STATUS)
                .from(ORDER)
                .leftAntiJoin(t)
                .on(ORDER.CUSTOMER_NUMBER.eq(t.CUSTOMER_NUMBER)
                        .and(t.ORDER_DATE.lt(LocalDate.of(2004, 6, 6))
                                .and(t.ORDER_DATE.gt(ORDER.ORDER_DATE))))
                .where(ORDER.ORDER_DATE.lt(LocalDate.of(2004, 6, 6))).fetch();

        // using ROW_NUMBER()            
        ctx.select(field(name("R", "CUSTOMER_NUMBER")), field(name("R", "ORDER_DATE")),
                field(name("R", "SHIPPED_DATE")), field(name("R", "STATUS")))
                .from(select(ORDER.CUSTOMER_NUMBER, ORDER.ORDER_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS,
                        rowNumber().over().partitionBy(ORDER.CUSTOMER_NUMBER)
                                .orderBy(ORDER.ORDER_DATE.desc()).as("RN"))
                        .from(ORDER)
                        .where(ORDER.ORDER_DATE.lt(LocalDate.of(2004, 6, 6))).asTable("R"))
                .where(field(name("R", "RN")).eq(1))
                .fetch();

        ctx.select(ORDER.CUSTOMER_NUMBER, ORDER.ORDER_DATE, ORDER.SHIPPED_DATE, ORDER.STATUS)
                .from(ORDER)
                .where(ORDER.ORDER_DATE.lt(LocalDate.of(2004, 6, 6)))
                .qualify(rowNumber().over().partitionBy(ORDER.CUSTOMER_NUMBER)
                        .orderBy(ORDER.ORDER_DATE.desc()).eq(1))
                .fetch();        
        
        // using ORACLE's KEEP        
        ctx.select(ORDER.CUSTOMER_NUMBER,
                max(ORDER.ORDER_DATE).as("ORDER_DATE"),
                max(ORDER.SHIPPED_DATE).keepDenseRankLastOrderBy(ORDER.SHIPPED_DATE).as("SHIPPED_DATE"),
                max(ORDER.STATUS).keepDenseRankLastOrderBy(ORDER.SHIPPED_DATE).as("STATUS"))
                .from(ORDER)
                .where(ORDER.ORDER_DATE.lt(LocalDate.of(2004, 6, 6)))
                .groupBy(ORDER.CUSTOMER_NUMBER)
                .fetch();

        ctx.select(ORDER.CUSTOMER_NUMBER, ORDER.ORDER_DATE, ORDER.SHIPPED_DATE, ORDER.STATUS)
                .from(ORDER)
                .where((rowid().in(select(max((rowid())).keepDenseRankLastOrderBy(ORDER.SHIPPED_DATE))
                        .from(ORDER)
                        .where(ORDER.ORDER_DATE.lt(LocalDate.of(2004, 6, 6)))
                        .groupBy(ORDER.CUSTOMER_NUMBER))))
                .fetch();
    }
}
