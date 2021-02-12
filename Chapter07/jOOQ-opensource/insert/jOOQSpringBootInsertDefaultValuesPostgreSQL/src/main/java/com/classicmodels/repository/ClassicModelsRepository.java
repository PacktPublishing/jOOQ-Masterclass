package com.classicmodels.repository;

import com.classicmodels.pojo.ProductPart;
import java.math.BigDecimal;
import static jooq.generated.Sequences.PRODUCT_SEQ;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.records.ProductRecord;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.defaultValue;
import static org.jooq.impl.DSL.default_;
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
    insert into "public"."product" default 
    values
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
    insert into "public"."product" (
      "product_id", "product_name", "product_line", 
      "product_scale", "product_vendor", 
      "product_description", "quantity_in_stock", 
      "buy_price", "msrp"
    ) 
    values 
      (
        nextval('"public"."product_seq"'), 
        ?, 
        ?, 
        default, 
        ?, 
        default, 
        default, 
        ?, 
        ?,
        default
      )    
     */
    public void insertSomeDefaultsInProduct() {

        System.out.println("EXAMPLE 2.1 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .values(PRODUCT_SEQ.nextval(), "Ultra Jet X1", "Planes",
                                defaultValue(PRODUCT.PRODUCT_SCALE),
                                "Motor City Art Classics",
                                defaultValue(PRODUCT.PRODUCT_DESCRIPTION),
                                defaultValue(PRODUCT.QUANTITY_IN_STOCK),
                                45.99, 67.99, defaultValue()
                        )
                        .execute()
        );
        
        // or, using defaultValues()
        System.out.println("EXAMPLE 2.2 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .values(defaultValue(), "Ultra Jet X1", "Planes",
                                defaultValue(PRODUCT.PRODUCT_SCALE),
                                "Motor City Art Classics",
                                defaultValue(PRODUCT.PRODUCT_DESCRIPTION),
                                defaultValue(PRODUCT.QUANTITY_IN_STOCK),
                                45.99, 67.99, defaultValue()
                        )
                        .execute()
        );
        
        // or, using default_()
        System.out.println("EXAMPLE 2.3 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .values(default_(), "Ultra Jet X1", "Planes",
                                default_(PRODUCT.PRODUCT_SCALE),
                                "Motor City Art Classics",
                                default_(PRODUCT.PRODUCT_DESCRIPTION),
                                default_(PRODUCT.QUANTITY_IN_STOCK),
                                45.99, 67.99, default_()
                        )
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    insert into "public"."product" (
      "product_name", "product_scale", 
      "product_line", "product_vendor", 
      "product_description", "quantity_in_stock", 
      "buy_price", "msrp"
    ) 
    values 
      (
        ?, ?, default, ?, default, default, ?, 
        ?
      )
     */
    public void insertSomeDefaultsValInProduct() {
        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .columns(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_SCALE,
                                PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_VENDOR,
                                PRODUCT.PRODUCT_DESCRIPTION, PRODUCT.QUANTITY_IN_STOCK,
                                PRODUCT.BUY_PRICE, PRODUCT.MSRP)
                        .values(val("Ultra Jet X1"),
                                val("1:18"),
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
    insert into "public"."product" (
      "product_name", "product_scale", 
      "product_line", "product_vendor", 
      "product_description", "quantity_in_stock", 
      "buy_price", "msrp"
    ) 
    values 
      (
        ?, ?, default, ?, default, default, ?, 
        ?
      )
     */
    public void insertSomeDefaultsByTypeSomeExplicitInProduct() {
        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .columns(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_SCALE,
                                PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_VENDOR,
                                PRODUCT.PRODUCT_DESCRIPTION, PRODUCT.QUANTITY_IN_STOCK,
                                PRODUCT.BUY_PRICE, PRODUCT.MSRP)
                        .values(val("Ultra Jet X1"),
                                val("1:18"),
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
    insert into "public"."product" (
      "product_name", "product_line", "product_vendor", 
      "buy_price", "msrp"
    ) 
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
    
    // EXAMPLE 6
    public void insertDefaultsViaNewRecordInProduct() {

        /* approach 1 */
        /*
        insert into "public"."product" default 
        values 
        returning "public"."product"."product_id"
        */
        System.out.println("EXAMPLE 6.1 (affected rows): "
                + ctx.newRecord(PRODUCT).insert()
        );

        /* approach 2 */
        /*
        insert into "public"."product" (
          "product_name", "product_line", "product_scale", 
          "product_vendor", "product_description", 
          "quantity_in_stock", "buy_price", 
          "msrp"
        ) 
        values 
          (?, ?, ?, ?, ?, ?, ?, ?) 
        returning "public"."product"."product_id"
        */
        ProductRecord pr1 = new ProductRecord();
        System.out.println("EXAMPLE 6.2 (affected rows): "
                + ctx.newRecord(PRODUCT, pr1).insert()
        );

        /* approach 3 */
        ProductRecord pr2 = new ProductRecord();
        pr2.setProductName("Ultra Jet X1");
        pr2.setProductLine("Planes");
        pr2.setProductVendor("Motor City Art Classics");
        pr2.setBuyPrice(BigDecimal.valueOf(45.99));
        pr2.setMsrp(BigDecimal.valueOf(67.99));
        System.out.println("EXAMPLE 6.3 (affected rows): "
                + ctx.newRecord(PRODUCT, pr2).insert()
        );

        /* approach 4 */
        ProductPart pp1 = new ProductPart();
        pr1.from(pp1);
        System.out.println("EXAMPLE 6.4 (affected rows): "
                + ctx.newRecord(PRODUCT, pr1).insert()
        );

        /* approach 5 */
        ProductPart pp2 = new ProductPart(
                "Ultra Jet X1", "Planes", "Motor City Art Classics",
                BigDecimal.valueOf(45.99), BigDecimal.valueOf(67.99));
        pr2.from(pp2);
        System.out.println("EXAMPLE 6.5 (affected rows): "
                + ctx.newRecord(PRODUCT, pr2).insert()
        );
    }
}
