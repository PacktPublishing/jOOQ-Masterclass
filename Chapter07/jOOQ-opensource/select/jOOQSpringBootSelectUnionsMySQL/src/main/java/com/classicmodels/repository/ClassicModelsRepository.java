package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import jooq.generated.tables.Orderdetail;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    /*
    select
      `classicmodels`.`employee`.`first_name`,
      `classicmodels`.`employee`.`last_name`
    from
      `classicmodels`.`employee`
    union
    select
      `classicmodels`.`customer`.`contact_first_name`,
      `classicmodels`.`customer`.`contact_last_name`
    from
      `classicmodels`.`customer`
     */
    public void unionEmployeeAndCustomerNames() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .union(select(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME)
                                .from(CUSTOMER))
                        .fetch()
        );
    }

    // EXAMPLE 2
    /*
    select
      concat(
        `classicmodels`.`employee`.`first_name`,
        ?,
        `classicmodels`.`employee`.`last_name`
      ) as `full_name`
    from
      `classicmodels`.`employee`
    union
    select
      concat(
        `classicmodels`.`customer`.`contact_first_name`,
        ?,
        `classicmodels`.`customer`.`contact_last_name`
      )
    from
      `classicmodels`.`customer`
     */
    public void unionEmployeeAndCustomerNamesConcatColumns() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(
                        concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME).as("full_name"))
                        .from(EMPLOYEE)
                        .union(select(
                                concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME))
                                .from(CUSTOMER))
                        .fetch()
        );
    }

    // EXAMPLE 3
    /*
    select
      concat(
        `classicmodels`.`employee`.`first_name`,
        ?,
        `classicmodels`.`employee`.`last_name`
      ) as `full_name`,
      ? as `contactType`
    from
      `classicmodels`.`employee`
    union
    select
      concat(
        `classicmodels`.`customer`.`contact_first_name`,
        ?,
        `classicmodels`.`customer`.`contact_last_name`
      ),
      ? as `contactType`
    from
      `classicmodels`.`customer`
     */
    public void unionEmployeeAndCustomerNamesDifferentiate() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(
                        concat(EMPLOYEE.FIRST_NAME, val(" "),
                                EMPLOYEE.LAST_NAME).as("full_name"),
                        val("Employee").as("contactType"))
                        .from(EMPLOYEE)
                        .union(select(
                                concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "),
                                        CUSTOMER.CONTACT_LAST_NAME),
                                val("Customer").as("contactType"))
                                .from(CUSTOMER))
                        .fetch()
        );
    }

    // EXAMPLE 4
    /*
    select
      concat(
        `classicmodels`.`employee`.`first_name`,
        ?,
        `classicmodels`.`employee`.`last_name`
      ) as `full_name`
    from
      `classicmodels`.`employee`
    union
    select
      concat(
        `classicmodels`.`customer`.`contact_first_name`,
        ?,
        `classicmodels`.`customer`.`contact_last_name`
      )
    from
      `classicmodels`.`customer`
    order by
      full_name
     */
    public void unionEmployeeAndCustomerNamesOrderBy() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select(
                        concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME).as("full_name"))
                        .from(EMPLOYEE)
                        .union(select(
                                concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME))
                                .from(CUSTOMER))
                        .orderBy(field("full_name"))
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    (
       select
         `classicmodels`.`employee`.`employee_number`,
         `classicmodels`.`employee`.`last_name`,
         `classicmodels`.`employee`.`first_name`,
         `classicmodels`.`employee`.`extension`,
         `classicmodels`.`employee`.`email`,
         `classicmodels`.`employee`.`office_code`,
         `classicmodels`.`employee`.`salary`,
         `classicmodels`.`employee`.`reports_to`,
         `classicmodels`.`employee`.`job_title`
       from
         `classicmodels`.`employee`
       order by
         `classicmodels`.`employee`.`salary` asc
       limit
         ?
    )
    union
      ( 
        select
          `classicmodels`.`employee`.`employee_number`,
          `classicmodels`.`employee`.`last_name`,
          `classicmodels`.`employee`.`first_name`,
          `classicmodels`.`employee`.`extension`,
          `classicmodels`.`employee`.`email`,
          `classicmodels`.`employee`.`office_code`,
          `classicmodels`.`employee`.`salary`,
          `classicmodels`.`employee`.`reports_to`,
          `classicmodels`.`employee`.`job_title`
        from
          `classicmodels`.`employee`
        order by
          `classicmodels`.`employee`.`salary` desc
        limit
          ?
      )
    order by 1
     */
    public void unionEmployeeSmallestAndHighestSalary() {

        System.out.println("EXAMPLE 5\n"
                + ctx.selectFrom(EMPLOYEE)
                        .orderBy(EMPLOYEE.SALARY.asc()).limit(1)
                        .union(
                                selectFrom(EMPLOYEE)
                                        .orderBy(EMPLOYEE.SALARY.desc()).limit(1))
                        .orderBy(1)
                        .fetch()
        );
    }

    // EXAMPLE 6
    /*
    select
      `classicmodels`.`office`.`city`,
      `classicmodels`.`office`.`country`
    from
      `classicmodels`.`office`
    union all
    select
      `classicmodels`.`customerdetail`.`city`,
      `classicmodels`.`customerdetail`.`country`
    from
      `classicmodels`.`customerdetail`
    order by
      `city`,
      `country`
     */
    public void unionAllOfficeCustomerCityAndCountry() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .unionAll(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );
    }

    // EXAMPLE 7
    /*
    select
      `R1`.`product_id`,
      `R1`.`order_id`,
      min(`R1`.`price_each`) as `min_price`,
      count(
        case
         when not exists (
           select
             `R2`.`order_id`,
             `R2`.`product_id`,
             `R2`.`quantity_ordered`,
             `R2`.`price_each`,
             `R2`.`order_line_number`
           from
             `classicmodels`.`orderdetail` as `R2`
           where
            (
              `R2`.`product_id` = `R1`.`product_id`
               and `R2`.`price_each` < `R1`.`price_each`
            )
         ) then ?
        end
      ) as `worst_price`,
      max(`R1`.`price_each`) as `max_price`,
      count(
        case
          when not exists (
            select
             `R3`.`order_id`,
             `R3`.`product_id`,
             `R3`.`quantity_ordered`,
             `R3`.`price_each`,
             `R3`.`order_line_number`
            from
             `classicmodels`.`orderdetail` as `R3`
            where
             (
               `R3`.`product_id` = `R1`.`product_id`
               and `R3`.`price_each` > `R1`.`price_each`
             )
         ) then ?
        end
      ) as `best_price`
      from
        `classicmodels`.`orderdetail` as `R1`
      group by
        `R1`.`product_id`, `R1`.`order_id`
     */
    public void findMinMaxWorstBestPrice() {

        Orderdetail R1 = ORDERDETAIL.as("R1");
        Orderdetail R2 = ORDERDETAIL.as("R2");
        Orderdetail R3 = ORDERDETAIL.as("R3");

        System.out.println("EXAMPLE 7\n"
                + ctx.select(R1.PRODUCT_ID, R1.ORDER_ID, min(R1.PRICE_EACH).as("min_price"),
                        count(case_()
                                .when(notExists(select().from(R2)
                                        .where(R2.PRODUCT_ID.eq(R1.PRODUCT_ID)
                                                .and(R2.PRICE_EACH.lt(R1.PRICE_EACH)))), 1)
                        ).as("worst_price"), max(R1.PRICE_EACH).as("max_price"),
                        count(case_()
                                .when(notExists(select().from(R3)
                                        .where(R3.PRODUCT_ID.eq(R1.PRODUCT_ID)
                                                .and(R3.PRICE_EACH.gt(R1.PRICE_EACH)))), 1)
                        ).as("best_price"))
                        .from(R1)
                        .groupBy(R1.PRODUCT_ID, R1.ORDER_ID)
                        .fetch()
        );
    }

    // EXAMPLE 8
    /*
    select `alias_55017331`.`order_id`,
           `alias_55017331`.`price_each`,
           `alias_55017331`.`quantity_ordered`
    from (
            (select `classicmodels`.`orderdetail`.`order_id`,
                    `classicmodels`.`orderdetail`.`price_each`,
                    `classicmodels`.`orderdetail`.`quantity_ordered`
             from `classicmodels`.`orderdetail`
             where `classicmodels`.`orderdetail`.`quantity_ordered` <= ?
             order by `classicmodels`.`orderdetail`.`price_each`
             limit ?)
          union
            (select `classicmodels`.`orderdetail`.`order_id`,
                    `classicmodels`.`orderdetail`.`price_each`,
                    `classicmodels`.`orderdetail`.`quantity_ordered`
             from `classicmodels`.`orderdetail`
             where `classicmodels`.`orderdetail`.`quantity_ordered` >= ?
             order by `classicmodels`.`orderdetail`.`price_each`
             limit ?)) as `alias_55017331`    
     */
    public void findTop5OrdersHavingQuantityOrderedLe20AndGe60OrderedByPrice() {

        System.out.println("EXAMPLE 8\n"
                + ctx.select().from(
                        select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.PRICE_EACH, ORDERDETAIL.QUANTITY_ORDERED)
                                .from(ORDERDETAIL)
                                .where(ORDERDETAIL.QUANTITY_ORDERED.le(20))
                                .orderBy(ORDERDETAIL.PRICE_EACH).limit(5)
                                .union(
                                        select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.PRICE_EACH, ORDERDETAIL.QUANTITY_ORDERED)
                                                .from(ORDERDETAIL)
                                                .where(ORDERDETAIL.QUANTITY_ORDERED.ge(60))
                                                .orderBy(ORDERDETAIL.PRICE_EACH).limit(5)
                                )
                )
                        .fetch()
        );
    }

    // EXAMPLE 9
    /*
    CREATE TABLE product_stock AS
    SELECT `classicmodels`.`product`.`product_name`
    FROM `classicmodels`.`product`
    WHERE `classicmodels`.`product`.`quantity_in_stock` < ?
    UNION
    SELECT `classicmodels`.`product`.`product_name`
    FROM `classicmodels`.`product`
    WHERE `classicmodels`.`product`.`quantity_in_stock` >= ?    
     */
    public void findProductStockLt500Gt9500() {

        ctx.dropTableIfExists("product_stock").execute();

        ctx.select(PRODUCT.PRODUCT_NAME)
                .into(table("product_stock"))
                .from(PRODUCT)
                .where(PRODUCT.QUANTITY_IN_STOCK.lt(Short.valueOf("500")))
                .union(
                        select(PRODUCT.PRODUCT_NAME)
                                .from(PRODUCT)
                                .where(PRODUCT.QUANTITY_IN_STOCK.ge(Short.valueOf("9500")))
                ).fetch();

        System.out.println("EXAMPLE 9\n"
                + ctx.selectFrom(table("product_stock")).fetch()
        );
    }
}