package com.classicmodels.repository;

import com.classicmodels.pojo.ProductPart;
import java.math.BigDecimal;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.records.ManagerRecord;
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
    insert into [classicmodels].[dbo].[manager] default values    
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
    insert into [classicmodels].[dbo].[product] (
      [product_name], [product_line], [code], 
      [product_scale], [product_vendor], 
      [product_description], [quantity_in_stock], 
      [buy_price], [msrp], [specs], [product_uid]
    ) 
    values 
      (
        ?, ?, ?, ?, ?, default, default, ?, ?, default, 
        default
      )    
    */
    public void insertSomeDefaultsValInProduct() {
        
        System.out.println("EXAMPLE 2.1 (affected rows): "
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
        
        System.out.println("EXAMPLE 2.2 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .columns(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_LINE,
                                PRODUCT.CODE, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_VENDOR,
                                PRODUCT.PRODUCT_DESCRIPTION, PRODUCT.QUANTITY_IN_STOCK,
                                PRODUCT.BUY_PRICE, PRODUCT.MSRP, PRODUCT.SPECS, PRODUCT.PRODUCT_UID)
                        .values(val("Ultra Jet X1"), val("Planes"), val(433823L),
                                val("1:18"), val("Motor City Art Classics"),                                                               
                                default_(PRODUCT.PRODUCT_DESCRIPTION),
                                default_(PRODUCT.QUANTITY_IN_STOCK),
                                val(BigDecimal.valueOf(45.99)), val(BigDecimal.valueOf(67.99)),
                                default_(PRODUCT.SPECS), default_(PRODUCT.PRODUCT_UID))                        
                        .execute()
        );        
    }

    // EXAMPLE 3
    /*
    insert into [classicmodels].[dbo].[product] (
      [product_name], [product_line], [code], 
      [product_scale], [product_vendor], 
      [product_description], [quantity_in_stock], 
      [buy_price], [msrp], [specs], [product_uid]
    ) 
    values 
      (
        ?, ?, ?, ?, ?, default, default, ?, ?, default, default
      )    
     */
    public void insertSomeDefaultsByTypeSomeExplicitInProduct() {
        System.out.println("EXAMPLE 3 (affected rows): "
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

    // EXAMPLE 4
    /*
    // 4.1    
    insert into [classicmodels].[dbo].[product] ([code]) values (?)
    
    // 4.2
    insert into [classicmodels].[dbo].[product] (
      [product_name], [product_line], [code], 
      [product_scale], [product_vendor], 
      [buy_price], [msrp]
    ) 
    values 
      (?, ?, ?, ?, ?, ?, ?)    
     */
    public void insertSomeImplicitDefaultsInProduct() {
        
        System.out.println("EXAMPLE 4.1 (affected rows): "
                + ctx.insertInto(PRODUCT)
                        .columns(PRODUCT.CODE)
                        .values(433823L)
                        .execute()
        );
        
        System.out.println("EXAMPLE 4.2 (affected rows): "
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

    // EXAMPLE 5
    public void insertDefaultsViaNewRecord() {

        /* approach 1 */
        /*
        declare @result table ([manager_id] bigint);
        insert into [classicmodels].[dbo].[manager] output [inserted].[manager_id] into @result default 
        values 
          ;
        select 
          [manager_id] 
        from 
          @result [r];        
         */
        System.out.println("EXAMPLE 5.1 (affected rows): "
                + ctx.newRecord(MANAGER).insert()
        );

        /* approach 2 */
        /*
        declare @result table ([manager_id] bigint);
        insert into [classicmodels].[dbo].[manager] (
          [manager_detail], [manager_evaluation]
        ) output [inserted].[manager_id] into @result 
        values 
          (?, ?);
        select 
          [manager_id] 
        from 
          @result [r];        
         */
        ManagerRecord mr1 = new ManagerRecord();
        System.out.println("EXAMPLE 5.2 (affected rows): "
                + ctx.newRecord(MANAGER, mr1).insert()
        );

        /* approach 3 */
        /*
        declare @result table ([product_id] bigint);
        insert into [classicmodels].[dbo].[product] (
          [product_name], [product_line], [code], 
          [product_scale], [product_vendor], 
          [product_description], [quantity_in_stock], 
          [buy_price], [specs], [msrp]
        ) output [inserted].[product_id] into @result 
        values 
          (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        select 
          [product_id] 
        from 
          @result [r];        
        */
        ProductRecord pr = new ProductRecord();
        pr.setProductName("Ultra Jet X1");
        pr.setProductLine("Planes");
        pr.setCode(433823L); // this is mandatory since there is no default value for it
        pr.setProductVendor("Motor City Art Classics");
        pr.setBuyPrice(BigDecimal.valueOf(45.99));
        pr.setMsrp(BigDecimal.valueOf(67.99));
        System.out.println("EXAMPLE 5.3 (affected rows): "
                + ctx.newRecord(PRODUCT, pr).insert()
        );

        /* approach 4 */
        /*
        declare @result table ([product_id] bigint);
        insert into [classicmodels].[dbo].[product] (
          [product_name], [product_line], [code], 
          [product_scale], [product_vendor], 
          [product_description], [quantity_in_stock], 
          [specs]
        ) output [inserted].[product_id] into @result 
        values 
          (?, ?, ?, ?, ?, ?, ?, ?);
        select 
          [product_id] 
        from 
          @result [r];        
        */
        ProductPart pp1 = new ProductPart(433823L);
        pr.from(pp1);
        System.out.println("EXAMPLE 5.4 (affected rows): "
                + ctx.newRecord(PRODUCT, pr).insert()
        );

        /* approach 5 */
        /*
        declare @result table ([product_id] bigint);
        insert into [classicmodels].[dbo].[product] (
          [product_name], [product_line], [code], 
          [product_scale], [product_vendor], 
          [product_description], [quantity_in_stock], 
          [buy_price], [specs], [msrp]
        ) output [inserted].[product_id] into @result 
        values 
          (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        select 
          [product_id] 
        from 
          @result [r];        
        */
        ProductPart pp2 = new ProductPart(
                "Ultra Jet X1", "Planes", 433823L, "Motor City Art Classics",
                BigDecimal.valueOf(45.99), BigDecimal.valueOf(67.99));
        pr.from(pp2);
        System.out.println("EXAMPLE 5.5 (affected rows): "
                + ctx.newRecord(PRODUCT, pr).insert()
        );
    }
}