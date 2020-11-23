package com.classicmodels.repository;

import java.time.LocalDate;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
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
    insert into `classicmodels`.`order` (
      `order_id`,`order_date`,`required_date`,`shipped_date`,`status`,`comments`,`customer_number`)
    values
      (?, ?, ?, ?, ?, ?, ?) on duplicate key
    update
      `classicmodels`.`order`.`order_date` = ?,
      `classicmodels`.`order`.`required_date` = ?,
      `classicmodels`.`order`.`shipped_date` = ?,
      `classicmodels`.`order`.`status` = ?
     */
    public void insertOtherwiseUpdate() {        

        System.out.println("EXAMPLE 1.1 (affected rows): "
                + ctx.insertInto(ORDER)
                        .values(10101L, LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped", "New order inserted ...", 363L)
                        .onDuplicateKeyUpdate()
                        .set(ORDER.ORDER_DATE, LocalDate.of(2003, 2, 12))
                        .set(ORDER.REQUIRED_DATE, LocalDate.of(2003, 3, 1))
                        .set(ORDER.SHIPPED_DATE, LocalDate.of(2003, 2, 27))
                        .set(ORDER.STATUS, "Shipped")
                        .execute()
        );
        
        System.out.println("EXAMPLE 1.2 (affected rows): "
                + ctx.insertInto(ORDER)
                        .set(ORDER.ORDER_ID, 10101L)
                        .set(ORDER.ORDER_DATE, LocalDate.of(2003, 2, 12))
                        .set(ORDER.REQUIRED_DATE, LocalDate.of(2003, 3, 1))
                        .set(ORDER.SHIPPED_DATE, LocalDate.of(2003, 2, 27))
                        .set(ORDER.STATUS, "Shipped")
                        .set(ORDER.COMMENTS, "New order inserted ...")
                        .set(ORDER.CUSTOMER_NUMBER, 128L)
                        .onDuplicateKeyUpdate()
                        .set(ORDER.ORDER_DATE, LocalDate.of(2003, 2, 12))
                        .set(ORDER.REQUIRED_DATE, LocalDate.of(2003, 3, 1))
                        .set(ORDER.SHIPPED_DATE, LocalDate.of(2003, 2, 27))
                        .set(ORDER.STATUS, "Shipped")
                        .execute()
        );
    }
    
    // EXAMPLE 2
    /*
    insert into `classicmodels`.`sale` (
      `sale_id`,`fiscal_year`,`sale`,`employee_number`)
    values
      (?, ?, ?, ?) on duplicate key
    update
      `classicmodels`.`sale`.`sale_id` = ?,
      `classicmodels`.`sale`.`fiscal_year` = ?,
      `classicmodels`.`sale`.`sale` = ?,
      `classicmodels`.`sale`.`employee_number` = ?
    */
    public void insertOtherwiseUpdateRecordSale() {

        SaleRecord sr = new SaleRecord(1L, 2003, 123.32, 1370L);

        System.out.println("EXAMPLE 2 (affected rows): "
                + ctx.insertInto(SALE)
                        .set(sr) // or, values(sr)                
                        .onDuplicateKeyUpdate()
                        .set(sr)
                        .execute()
        );
    }
}