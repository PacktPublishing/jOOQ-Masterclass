package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.defaultValue;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    /*
    insert into `classicmodels`.`product`
    values
      (
         default,default,default,default,default,default,default,default,default
      )
     */
    public void insertAllDefaultsInProduct() {

        System.out.println("EXAMPLE 1 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .defaultValues()
                        .execute()
        );
    }

    // EXAMPLE 2
    /*
    insert into `classicmodels`.`product` (
      `product_id`,`product_name`,`product_line`,`product_scale`,`product_vendor`,
      `product_description`,`quantity_in_stock`,`buy_price`,`msrp`
    )
    values
      (?, ?, ?, default, ?, default, default, ?, ?)
     */
    public void insertSomeDefaultsInProduct() {

        System.out.println("EXAMPLE 2 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .values(null, "Ultra Jet X1", "Planes",
                                defaultValue(PRODUCT.PRODUCT_SCALE),
                                "Motor City Art Classics",
                                defaultValue(PRODUCT.PRODUCT_DESCRIPTION),
                                defaultValue(PRODUCT.QUANTITY_IN_STOCK),
                                45.99, 67.99
                        )
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    insert into `classicmodels`.`product` (
      `product_name`,`product_scale`,`product_line`,`product_vendor`,
      `product_description`,`quantity_in_stock`,`buy_price`,`msrp`
    )
    values
      (?, ?, default, ?, default, default, ?, ?)
     */
    public void insertSomeDefaultsValInProduct() {
        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .columns(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_SCALE,
                                PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_VENDOR,
                                PRODUCT.PRODUCT_DESCRIPTION, PRODUCT.QUANTITY_IN_STOCK,
                                PRODUCT.BUY_PRICE, PRODUCT.MSRP)
                        .values(val("Ultra Jet X1"),
                                val("Planes"),
                                defaultValue(PRODUCT.PRODUCT_SCALE),
                                val("Motor City Art Classics"),
                                defaultValue(PRODUCT.PRODUCT_DESCRIPTION),
                                defaultValue(PRODUCT.QUANTITY_IN_STOCK),
                                val(BigDecimal.valueOf(45.99)), val(BigDecimal.valueOf(67.99))
                        )
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    insert into `classicmodels`.`product` (
      `product_name`,`product_scale`,`product_line`,`product_vendor`,
      `product_description`,`quantity_in_stock`,`buy_price`,`msrp`
    )
    values
      (?, ?, default, ?, default, default, ?, ?)
     */
    public void insertSomeDefaultsByTypeSomeExplicitInProduct() {
        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .columns(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_SCALE,
                                PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_VENDOR,
                                PRODUCT.PRODUCT_DESCRIPTION, PRODUCT.QUANTITY_IN_STOCK,
                                PRODUCT.BUY_PRICE, PRODUCT.MSRP)
                        .values(val("Ultra Jet X1"),
                                val("Planes"),
                                defaultValue(String.class), // or, defaultValue(VARCHAR(10))
                                val("Motor City Art Classics"),
                                defaultValue(String.class), // or, defaultValue(LONGVARCHAR(1000))
                                defaultValue(Short.class), // or, defaultValue(SMALLINT)
                                val(BigDecimal.valueOf(45.99)), val(BigDecimal.valueOf(67.99))
                        )
                        .execute()
        );
    }

    // EXAMPLE 5
    /*
    insert into `classicmodels`.`product` (
      `product_name`,`product_line`,`product_vendor`,`buy_price`,`msrp`)
    values
      (?, ?, ?, ?, ?)
     */
    public void insertSomeImplicitDefaultsInProduct() {
        System.out.println("EXAMPLE 5 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .columns(PRODUCT.PRODUCT_NAME,
                                PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_VENDOR,
                                PRODUCT.BUY_PRICE, PRODUCT.MSRP)
                        .values("Ultra Jet X1",
                                "Planes",
                                "Motor City Art Classics",
                                BigDecimal.valueOf(45.99), BigDecimal.valueOf(67.99)
                        )
                        .execute()
        );
    }
}
