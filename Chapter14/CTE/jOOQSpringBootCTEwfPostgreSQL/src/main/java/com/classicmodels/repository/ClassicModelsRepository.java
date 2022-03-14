package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.percentRank;
import static org.jooq.impl.DSL.rank;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.year;
import static org.jooq.impl.SQLDataType.NUMERIC;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // Find gaps in ids
    public void cte1() {

        ctx.with("t", "data_val", "data_seq", "absent_data_grp")
                .as(select(EMPLOYEE.EMPLOYEE_NUMBER,
                        rowNumber().over().orderBy(EMPLOYEE.EMPLOYEE_NUMBER),
                        EMPLOYEE.EMPLOYEE_NUMBER.minus(
                                rowNumber().over().orderBy(EMPLOYEE.EMPLOYEE_NUMBER)))
                        .from(EMPLOYEE))
                .select(field(name("absent_data_grp")), count(),
                        min(field(name("data_val"))).as("start_data_val"))
                .from(name("t"))
                .groupBy(field(name("absent_data_grp")))
                .orderBy(field(name("absent_data_grp")))
                .fetch();
    }

    // Find the percentile rank of every product line by order values
    public void cte2() {

        ctx.with("t", "product_line", "sum_price_each")
                .as(select(PRODUCT.PRODUCT_LINE, sum(ORDERDETAIL.PRICE_EACH))
                        .from(PRODUCT)
                        .join(ORDERDETAIL)
                        .on(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))
                        .groupBy(PRODUCT.PRODUCT_LINE))
                .select(field(name("product_line")), field(name("sum_price_each")),
                        round(cast(percentRank().over()
                                .orderBy(field(name("sum_price_each"))).mul(100), NUMERIC), 2)
                                .concat("%").as("percentile_rank"))
                .from(name("t"))
                .fetch();
    }

    // Find the top three highest valued-orders in each year
    public void cte3() {

        ctx.with("order_values")
                .as(select(ORDER.ORDER_ID, year(ORDER.ORDER_DATE).as("year"),
                        ORDERDETAIL.QUANTITY_ORDERED.mul(ORDERDETAIL.PRICE_EACH).as("order_value"),
                        rank().over().partitionBy(year(ORDER.ORDER_DATE))
                                .orderBy(ORDERDETAIL.QUANTITY_ORDERED
                                        .mul(ORDERDETAIL.PRICE_EACH).desc()).as("order_value_rank"))
                        .from(ORDER)
                        .join(ORDERDETAIL)
                        .on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID)))
                .select()
                .from(name("order_values"))
                .where(field(name("order_value_rank")).le(3))
                .fetch();
    }
}
