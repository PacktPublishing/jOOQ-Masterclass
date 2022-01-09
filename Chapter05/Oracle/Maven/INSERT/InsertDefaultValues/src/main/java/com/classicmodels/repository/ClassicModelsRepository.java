package com.classicmodels.repository;

import com.classicmodels.pojo.ProductPart;
import java.math.BigDecimal;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.records.ProductRecord;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.defaultValue;
import static org.jooq.impl.DSL.default_;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.INTEGER;
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
    insert into "CLASSICMODELS"."MANAGER" 
       values (default, default, default, default)    
     */
    public void insertAllDefaultsInManager() {

        System.out.println("EXAMPLE 1 (affected rows): "
                + ctx.insertInto(MANAGER)
                        .defaultValues()
                        .execute()
        );
    }

    // EXAMPLE 2
    /*
    insert into "CLASSICMODELS"."PRODUCT" (
      "PRODUCT_ID", "PRODUCT_NAME", "PRODUCT_LINE", 
      "CODE", "PRODUCT_SCALE", "PRODUCT_VENDOR", 
      "PRODUCT_DESCRIPTION", "QUANTITY_IN_STOCK", 
      "BUY_PRICE", "MSRP", "SPECS", "PRODUCT_UID"
    ) 
    values 
      (
        default, ?, ?, ?, default, ?, default, 
        default, ?, ?, default, default
      )    
     */
    public void insertSomeDefaultsInProduct() {

        System.out.println("EXAMPLE 2.1.1 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .values(defaultValue(), "Ultra Jet X1", "Planes", 433823L,
                                defaultValue(), "Motor City Art Classics",
                                defaultValue(), defaultValue(), 45.99, 67.99, 
                                defaultValue(), defaultValue()
                        )
                        .execute()
        );
        
        System.out.println("EXAMPLE 2.1.2 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .values(defaultValue(PRODUCT.PRODUCT_ID), "Ultra Jet X1", "Planes", 433823L,
                                defaultValue(PRODUCT.PRODUCT_SCALE),
                                "Motor City Art Classics",
                                defaultValue(PRODUCT.PRODUCT_DESCRIPTION),
                                defaultValue(PRODUCT.QUANTITY_IN_STOCK),
                                45.99, 67.99, defaultValue(PRODUCT.SPECS), 
                                defaultValue(PRODUCT.PRODUCT_UID)
                        )
                        .execute()
        );
        
        // or, use default_()
        System.out.println("EXAMPLE 2.2.1 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .values(default_(), "Ultra Jet X1", "Planes", 433823L,
                                default_(), "Motor City Art Classics",
                                default_(), default_(),
                                45.99, 67.99, default_(), default_()
                        )
                        .execute()
        );
        
        System.out.println("EXAMPLE 2.2.2 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .values(default_(PRODUCT.PRODUCT_ID), "Ultra Jet X1", "Planes", 433823L,
                                default_(PRODUCT.PRODUCT_SCALE),
                                "Motor City Art Classics",
                                default_(PRODUCT.PRODUCT_DESCRIPTION),
                                default_(PRODUCT.QUANTITY_IN_STOCK),
                                45.99, 67.99, default_(PRODUCT.SPECS), 
                                default_(PRODUCT.PRODUCT_UID)
                        )
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    insert into "CLASSICMODELS"."PRODUCT" (
      "PRODUCT_NAME", "PRODUCT_LINE", "CODE", 
      "PRODUCT_SCALE", "PRODUCT_VENDOR", 
      "PRODUCT_DESCRIPTION", "QUANTITY_IN_STOCK", 
      "BUY_PRICE", "MSRP", "SPECS", "PRODUCT_UID"
    ) 
    values 
      (
        ?, ?, ?, ?, ?, default, default, ?, ?, default, 
        default
      )    
    */
    public void insertSomeDefaultsValInProduct() {
        System.out.println("EXAMPLE 3.1 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .columns(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_LINE,
                                PRODUCT.CODE, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_VENDOR,
                                PRODUCT.PRODUCT_DESCRIPTION, PRODUCT.QUANTITY_IN_STOCK,
                                PRODUCT.BUY_PRICE, PRODUCT.MSRP, PRODUCT.SPECS, PRODUCT.PRODUCT_UID)
                        .values(val("Ultra Jet X1"), val("Planes"), val(433823L),
                                val("1:18"), val("Motor City Art Classics"),                                                               
                                defaultValue(PRODUCT.PRODUCT_DESCRIPTION),
                                defaultValue(PRODUCT.QUANTITY_IN_STOCK),
                                val(BigDecimal.valueOf(45.99)), val(BigDecimal.valueOf(67.99)),
                                defaultValue(PRODUCT.SPECS), defaultValue(PRODUCT.PRODUCT_UID))                        
                        .execute()
        );
        
        System.out.println("EXAMPLE 3.2 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .columns(PRODUCT.PRODUCT_ID, 
                                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_LINE,
                                PRODUCT.CODE, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_VENDOR,
                                PRODUCT.PRODUCT_DESCRIPTION, PRODUCT.QUANTITY_IN_STOCK,
                                PRODUCT.BUY_PRICE, PRODUCT.MSRP, PRODUCT.SPECS, PRODUCT.PRODUCT_UID)
                        .values(defaultValue(PRODUCT.PRODUCT_ID),
                                val("Ultra Jet X1"), val("Planes"), val(433823L),
                                val("1:18"), val("Motor City Art Classics"),                                                               
                                defaultValue(PRODUCT.PRODUCT_DESCRIPTION),
                                defaultValue(PRODUCT.QUANTITY_IN_STOCK),
                                val(BigDecimal.valueOf(45.99)), val(BigDecimal.valueOf(67.99)),
                                defaultValue(PRODUCT.SPECS), defaultValue(PRODUCT.PRODUCT_UID))                        
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    insert into "CLASSICMODELS"."PRODUCT" (
      "PRODUCT_NAME", "PRODUCT_LINE", "CODE", 
      "PRODUCT_SCALE", "PRODUCT_VENDOR", 
      "PRODUCT_DESCRIPTION", "QUANTITY_IN_STOCK", 
      "BUY_PRICE", "MSRP", "SPECS", "PRODUCT_UID"
    ) 
    values 
      (
        ?, ?, ?, ?, ?, default, default, ?, ?, default, 
        default
      )    
     */
    public void insertSomeDefaultsByTypeSomeExplicitInProduct() {
        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .columns(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_LINE,
                                PRODUCT.CODE, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_VENDOR,
                                PRODUCT.PRODUCT_DESCRIPTION, PRODUCT.QUANTITY_IN_STOCK,
                                PRODUCT.BUY_PRICE, PRODUCT.MSRP, PRODUCT.SPECS, PRODUCT.PRODUCT_UID)
                        .values(val("Ultra Jet X1"), val("Planes"), val(433823L),
                                val("1:18"), val("Motor City Art Classics"),
                                defaultValue(PRODUCT.PRODUCT_DESCRIPTION), // or, defaultValue(String.class)
                                defaultValue(INTEGER), // or, defaultValue(Integer.class)
                                val(BigDecimal.valueOf(45.99)), val(BigDecimal.valueOf(67.99)),
                                defaultValue(PRODUCT.SPECS), // or, defaultValue(String.class)
                                defaultValue(BIGINT)) // or, defaultValue(Long.class)                                                                                        
                        .execute()
        );
    }

    // EXAMPLE 5
    /*
    // 5.1
    insert into "CLASSICMODELS"."PRODUCT" ("CODE") values (?)
    
    // 5.2
    insert into "CLASSICMODELS"."PRODUCT" (
      "PRODUCT_NAME", "PRODUCT_LINE", "CODE", 
      "PRODUCT_SCALE", "PRODUCT_VENDOR", 
      "BUY_PRICE", "MSRP"
    ) 
    values 
      (?, ?, ?, ?, ?, ?, ?)    
     */
    public void insertSomeImplicitDefaultsInProduct() {
        
        System.out.println("EXAMPLE 5.1 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .columns(PRODUCT.CODE)
                        .values(433823L)
                        .execute()
        );
        
        System.out.println("EXAMPLE 5.2 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .columns(PRODUCT.PRODUCT_NAME,
                                PRODUCT.PRODUCT_LINE, PRODUCT.CODE, PRODUCT.PRODUCT_SCALE,
                                PRODUCT.PRODUCT_VENDOR, PRODUCT.BUY_PRICE, PRODUCT.MSRP)
                        .values("Ultra Jet X1", "Planes", 433823L, "1:18", "Motor City Art Classics",
                                BigDecimal.valueOf(45.99), BigDecimal.valueOf(67.99)
                        )
                        .execute()
        );

    }

    // EXAMPLE 6
    public void insertDefaultsViaNewRecord() {

        /* approach 1 */
        /*
        insert into CLASSICMODELS."MANAGER" 
        values 
          (
            default, default, default, default
          )        
         */
        System.out.println("EXAMPLE 6.1 (affected rows): "
                + ctx.newRecord(MANAGER).insert()
        );        

        /* approach 2 */
        /*
        insert into CLASSICMODELS."PRODUCT" (
          "PRODUCT_NAME", "PRODUCT_LINE", "CODE", 
          "PRODUCT_SCALE", "PRODUCT_VENDOR", 
          "PRODUCT_DESCRIPTION", "QUANTITY_IN_STOCK", 
          "BUY_PRICE", "MSRP", "SPECS"
        ) 
        values 
          (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)        
        */
        ProductRecord pr = new ProductRecord();
        pr.setProductName("Ultra Jet X1");
        pr.setProductLine("Planes");
        pr.setCode(433823L); // this is mandatory since there is no default value for it
        pr.setProductVendor("Motor City Art Classics");
        pr.setBuyPrice(BigDecimal.valueOf(45.99));
        pr.setMsrp(BigDecimal.valueOf(67.99));
        System.out.println("EXAMPLE 6.2 (affected rows): "
                + ctx.newRecord(PRODUCT, pr).insert()
        );

        /* approach 3 */
        /*
        insert into CLASSICMODELS."PRODUCT" (
          "PRODUCT_NAME", "PRODUCT_LINE", "CODE", 
          "PRODUCT_SCALE", "PRODUCT_VENDOR", 
          "PRODUCT_DESCRIPTION", "QUANTITY_IN_STOCK", 
          "SPECS"
        ) 
        values 
          (?, ?, ?, ?, ?, ?, ?, ?)        
        */
        ProductPart pp1 = new ProductPart(433823L);
        pr.from(pp1);
        System.out.println("EXAMPLE 6.3 (affected rows): "
                + ctx.newRecord(PRODUCT, pr).insert()
        );

        /* approach 4 */
        /*
        insert into CLASSICMODELS."PRODUCT" (
          "PRODUCT_NAME", "PRODUCT_LINE", "CODE", 
          "PRODUCT_SCALE", "PRODUCT_VENDOR", 
          "PRODUCT_DESCRIPTION", "QUANTITY_IN_STOCK", 
          "BUY_PRICE", "MSRP", "SPECS"
        ) 
        values 
          (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)           
        */
        ProductPart pp2 = new ProductPart(
                "Ultra Jet X1", "Planes", 433823L, "Motor City Art Classics",
                BigDecimal.valueOf(45.99), BigDecimal.valueOf(67.99));
        pr.from(pp2);
        System.out.println("EXAMPLE 6.4 (affected rows): "
                + ctx.newRecord(PRODUCT, pr).insert()
        );
    }
}