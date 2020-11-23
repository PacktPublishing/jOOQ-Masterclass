package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.PaymentRecord;
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
    insert ignore into `classicmodels`.`payment` (
      `customer_number`, `check_number`, `payment_date`, `invoice_amount`, `caching_date`
    )
    values
      (?, ?, ?, ?, ?)
     */
    public void insertPaymentOnDuplicateKeyIgnore() {

        System.out.println("EXAMPLE 1 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 2
    /*    
    insert ignore into `classicmodels`.`payment` (
      `customer_number`, `check_number`, `payment_date`, `invoice_amount`, `caching_date`
    )
    values
      (?, ?, ?, ?, ?)
     */
    public void insertPaymentOnConflictDoNothing() {

        System.out.println("EXAMPLE 2 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onConflictDoNothing()
                        .execute()
        );
    }

    // EXAMPLE 3
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
    public void insertOrderOtherwiseUpdateIt() {

        System.out.println("EXAMPLE 3.1 (affected rows): "
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

        System.out.println("EXAMPLE 3.2 (affected rows): "
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

    // EXAMPLE 4
    /*
    insert into `classicmodels`.`payment` (
      `customer_number`, `check_number`, `payment_date`, `invoice_amount`, `caching_date`
    ) 
    values
     (?, ?, ?, ?, ?) 
    on duplicate key
    update
      `classicmodels`.`payment`.`invoice_amount` = ?,
      `classicmodels`.`payment`.`caching_date` = ?
    */
    public void insertPaymentOnConflictUpdateIt() {

        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onConflict()
                        .doUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)
                        .setNull(PAYMENT.CACHING_DATE)
                        .execute()
        );
    }

    // EXAMPLE 5
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
    public void insertSaleRecordOnDuplicateKeyUpdateIt() {

        SaleRecord sr = new SaleRecord(1L, 2003, 123.32, 1370L);

        System.out.println("EXAMPLE 5 (affected rows): "
                + ctx.insertInto(SALE)
                        .set(sr) // or, values(sr)                
                        .onDuplicateKeyUpdate()
                        .set(sr)
                        .execute()
        );
    }

    // EXAMPLE 6
    /*
    insert into `classicmodels`.`payment` (
      `customer_number`, `check_number`, `payment_date`, `invoice_amount`, `caching_date`
    )
    values
      (?, ?, ?, ?, ?) 
    on duplicate key
    update
      `classicmodels`.`payment`.`invoice_amount` = ?,
      `classicmodels`.`payment`.`caching_date` = ?
    */
    public void insertPaymentRecordOnConflictUpdateIt() {
        PaymentRecord pr = new PaymentRecord(103L, "HQ336336",
                LocalDateTime.of(2005, 11, 9, 12, 10, 11), BigDecimal.valueOf(123.32),
                LocalDateTime.of(2005, 11, 11, 14, 25, 21));

        System.out.println("EXAMPLE 6 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .set(pr) // or, values(pr)                
                        .onConflict(PAYMENT.CHECK_NUMBER)
                        .doUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)
                        .setNull(PAYMENT.CACHING_DATE)
                        .execute()
        );
    }
}