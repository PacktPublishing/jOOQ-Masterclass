package com.classicmodels.repository;

import com.classicmodels.pojo.SalePart;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import static jooq.generated.Routines.getAvgSale;
import static jooq.generated.Sequences.ORDER_SEQ;
import jooq.generated.enums.RateType;
import jooq.generated.enums.VatType;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Manager.MANAGER;
import jooq.generated.tables.Order;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.pojos.Department;
import jooq.generated.tables.pojos.Sale;
import jooq.generated.tables.records.SaleRecord;
import static jooq.generated.udt.EvaluationCriteria.EVALUATION_CRITERIA;
import jooq.generated.udt.records.EvaluationCriteriaRecord;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Row2;
import static org.jooq.Rows.toRowArray;
import static org.jooq.Rows.toRowList;
import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.default_;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.rand;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    private Long Order_Id;
    private Long Sale_Id;
    
    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
        
        // avoid duplicate keys
        Order_Id = ctx.select(max(ORDER.ORDER_ID)).from(ORDER).fetchOneInto(Long.class) + 10000;
        Sale_Id = ctx.select(max(SALE.SALE_ID)).from(SALE).fetchOneInto(Long.class) + 10000;
    }

    // EXAMPLE 1    
    /*
    // 1.1.1
    insert into "public"."order" (
      "order_id", "order_date", "required_date", 
      "shipped_date", "status", "comments", 
      "customer_number", "amount"
    ) 
    values 
      (
        default, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, 
        ?, 
        ?, 
        ?
      ) on conflict do nothing
    
    // 1.1.2
    insert into "public"."order" (
      "order_id", "order_date", "required_date", 
      "shipped_date", "status", "comments", 
      "customer_number", "amount"
     ) 
     values 
      (
        nextval('"public"."order_seq"'), 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, 
        ?, 
        ?, 
        ?
      ) on conflict do nothing
        
    // 1.2 - 1.4
    insert into "public"."order" (
      "comments", "order_date", "required_date", 
      "shipped_date", "status", "customer_number", 
      "amount"
    ) 
    values 
      (
        ?, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, 
        ?, 
        ?
      )        
    */
    public void insertOrderAutoGenKey() {
               
        System.out.println("EXAMPLE 1.1.1 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(default_(),
                                LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped",
                                "New order inserted ...", 363L, 314.44)
                        .onDuplicateKeyIgnore()
                        .execute()
        );
        System.out.println("EXAMPLE 1.1.2 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(ORDER_SEQ.nextval(), // primary key is auto-generated
                                LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped",
                                "New order inserted ...", 363L, 314.44)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 1.2 (affected rows): "
                + // InsertValuesStep7<OrderRecord, String, LocalDate, LocalDate, LocalDate, String, Long, BigDecimal>
                ctx.insertInto(ORDER, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values("New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27),
                                "Shipped", 363L, BigDecimal.valueOf(314.44))
                        .execute()
        );

        System.out.println("EXAMPLE 1.3 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .columns(ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                                ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values("New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27),
                                "Shipped", 363L, BigDecimal.valueOf(314.44))
                        .execute()
        );

        // example 1.3 expressed via InsertQuery API
        InsertQuery iq = ctx.insertQuery(ORDER);
        iq.addValue(ORDER.COMMENTS, "New order inserted ...");
        iq.addValue(ORDER.ORDER_DATE, LocalDate.of(2003, 2, 12));
        iq.addValue(ORDER.REQUIRED_DATE, LocalDate.of(2003, 3, 1));
        iq.addValue(ORDER.SHIPPED_DATE, LocalDate.of(2003, 2, 27));
        iq.addValue(ORDER.STATUS, "Shipped");
        iq.addValue(ORDER.CUSTOMER_NUMBER, 363L);
        iq.addValue(ORDER.AMOUNT, BigDecimal.valueOf(314.44));

        System.out.println("EXAMPLE 1.4 (affected rows): "
                + iq.execute()
        );
    }

    // EXAMPLE 2
    /*
    insert into "public"."order" (
      "order_id", "order_date", "required_date", 
      "shipped_date", "status", "comments", 
      "customer_number", "amount"
    ) 
    values 
      (
        ?, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, 
        ?, 
        ?, 
        ?
      ) on conflict do nothing    
     */
    public void insertOrderManualKey() {

         System.out.println("EXAMPLE 2.1 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(++Order_Id, // computed primary key
                                LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped",
                                "New order inserted ...", 363L, 314.44)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 2.2 (affected rows): "
                + // InsertValuesStep8<OrderRecord, Long, String, LocalDate, LocalDate, LocalDate, String, Long, BigDecimal>
                ctx.insertInto(ORDER, ORDER.ORDER_ID, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values(++Order_Id, // computed primary key
                                "New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27),
                                "Shipped", 363L, BigDecimal.valueOf(314.44))
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 2.3 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .columns(ORDER.ORDER_ID, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                                ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values(++Order_Id, // computed primary key
                                "New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27),
                                "Shipped", 363L, BigDecimal.valueOf(314.44))
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    // 3.1.1
    insert into "public"."order" (
      "order_id", "order_date", "required_date", 
      "shipped_date", "status", "comments", 
      "customer_number", "amount"
    ) 
    values 
      (
        default, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, ?, ?, ?
      ), 
      (
        default, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, ?, ?, ?
      ), 
      (
        default, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, ?, ?, ?
      ) on conflict do nothing
    
    // 3.1.2
    insert into "public"."order" (
      "order_id", "order_date", "required_date", 
      "shipped_date", "status", "comments", 
      "customer_number", "amount"
    ) 
    values 
      (
        nextval('"public"."order_seq"'), 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, ?, ?, ?
      ), 
      (
        nextval('"public"."order_seq"'), 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, ?, ?, ?
      ), 
      (
        nextval('"public"."order_seq"'), 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, ?, ?, ?
      ) on conflict do nothing
    
    // 3.2, 3.3
    insert into "public"."order" (
      "comments", "order_date", "required_date", 
      "shipped_date", "status", "customer_number", 
      "amount"
    ) 
    values 
      (
        ?, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, 
        ?, 
        ?
      ), 
      (
        ?, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, 
        ?, 
        ?
      ), 
      (
        ?, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, 
        ?, 
        ?
      ) on conflict do nothing        
     */
    public void insertMultipleOrderAutoGenKey() {

        System.out.println("EXAMPLE 3.1.1 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(default_(), // primary key is auto-generated
                                LocalDate.of(2004, 10, 22), LocalDate.of(2004, 10, 23),
                                LocalDate.of(2004, 10, 23), "Shipped",
                                "New order inserted ...", 363L, 314.44)
                        .values(default_(),
                                LocalDate.of(2003, 12, 2), LocalDate.of(2003, 1, 3),
                                LocalDate.of(2003, 2, 26), "Resolved",
                                "Important order ...", 128L, 125.55)
                        .values(default_(),
                                LocalDate.of(2005, 12, 12), LocalDate.of(2005, 12, 23),
                                LocalDate.of(2005, 12, 22), "On Hold",
                                "Order of client ...", 181L, 245.53)
                        .onDuplicateKeyIgnore()
                        .execute()
        );
        
        System.out.println("EXAMPLE 3.1.2 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(ORDER_SEQ.nextval(), // primary key is auto-generated
                                LocalDate.of(2004, 10, 22), LocalDate.of(2004, 10, 23),
                                LocalDate.of(2004, 10, 23), "Shipped",
                                "New order inserted ...", 363L, 314.44)
                        .values(ORDER_SEQ.nextval(),
                                LocalDate.of(2003, 12, 2), LocalDate.of(2003, 1, 3),
                                LocalDate.of(2003, 2, 26), "Resolved",
                                "Important order ...", 128L, 125.55)
                        .values(ORDER_SEQ.nextval(),
                                LocalDate.of(2005, 12, 12), LocalDate.of(2005, 12, 23),
                                LocalDate.of(2005, 12, 22), "On Hold",
                                "Order of client ...", 181L, 245.53)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 3.2 (affected rows): "
                + // InsertValuesStep7<OrderRecord, String, LocalDate, LocalDate, LocalDate, String, Long, BigDecimal>
                ctx.insertInto(ORDER, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values("New order inserted ...", LocalDate.of(2004, 10, 22),
                                LocalDate.of(2004, 10, 23), LocalDate.of(2004, 10, 23), 
                                "Shipped", 363L, BigDecimal.valueOf(314.33))
                        .values("Important order ...", LocalDate.of(2003, 12, 2),
                                LocalDate.of(2003, 1, 3), LocalDate.of(2003, 2, 26), 
                                "Resolved", 128L, BigDecimal.valueOf(125.55))
                        .values("Order of client ...", LocalDate.of(2005, 12, 12),
                                LocalDate.of(2005, 12, 23), LocalDate.of(2005, 12, 22), 
                                "On Hold", 181L, BigDecimal.valueOf(245.53))
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 3.3 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .columns(ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                                ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values("New order inserted ...", LocalDate.of(2004, 10, 22),
                                LocalDate.of(2004, 10, 23), LocalDate.of(2004, 10, 23), 
                                "Shipped", 363L, BigDecimal.valueOf(314.44))
                        .values("Important order ...", LocalDate.of(2003, 12, 2),
                                LocalDate.of(2003, 1, 3), LocalDate.of(2003, 2, 26), 
                                "Resolved", 128L, BigDecimal.valueOf(125.55))
                        .values("Order of client ...", LocalDate.of(2005, 12, 12),
                                LocalDate.of(2005, 12, 23), LocalDate.of(2005, 12, 22), 
                                "On Hold", 181L, BigDecimal.valueOf(245.53))
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    insert into "public"."order" (
      "order_id", "order_date", "required_date", 
      "shipped_date", "status", "comments", 
      "customer_number", "amount"
    ) 
    values 
      (
        ?, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, 
        ?, 
        ?, 
        ?
      ), 
      (
        ?, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, 
        ?, 
        ?, 
        ?
      ), 
      (
        ?, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, 
        ?, 
        ?, 
        ?
      ) on conflict do nothing    
     */
    public void insertMultipleOrderManualKey() {

        System.out.println("EXAMPLE 4.1 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(++Order_Id,
                                LocalDate.of(2004, 10, 22), LocalDate.of(2004, 10, 23),
                                LocalDate.of(2004, 10, 23), "Shipped",
                                "New order inserted ...", 363L, BigDecimal.valueOf(314.44))
                        .values(++Order_Id,
                                LocalDate.of(2003, 12, 2), LocalDate.of(2003, 1, 3),
                                LocalDate.of(2003, 2, 26), "Resolved",
                                "Important order ...", 128L, BigDecimal.valueOf(125.55))
                        .values(++Order_Id,
                                LocalDate.of(2005, 12, 12), LocalDate.of(2005, 12, 23),
                                LocalDate.of(2005, 12, 22), "On Hold",
                                "Order of client ...", 181L, BigDecimal.valueOf(245.53))
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 4.2 (affected rows): "
                + // InsertValuesStep8<OrderRecord, Long, String, LocalDate, LocalDate, LocalDate, String, Long, BigDecimal>
                ctx.insertInto(ORDER, ORDER.ORDER_ID, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values(++Order_Id,
                                "New order inserted ...", LocalDate.of(2004, 10, 22),
                                LocalDate.of(2004, 10, 23), LocalDate.of(2004, 10, 23), 
                                "Shipped", 363L, BigDecimal.valueOf(314.44))
                        .values(++Order_Id,
                                "Important order ...", LocalDate.of(2003, 12, 2),
                                LocalDate.of(2003, 1, 3), LocalDate.of(2003, 2, 26), 
                                "Resolved", 128L, BigDecimal.valueOf(125.55))
                        .values(++Order_Id,
                                "Order of client ...", LocalDate.of(2005, 12, 12),
                                LocalDate.of(2005, 12, 23), LocalDate.of(2005, 12, 22), 
                                "On Hold", 181L, BigDecimal.valueOf(245.53))
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 4.3 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .columns(ORDER.ORDER_ID, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                                ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values(++Order_Id,
                                "New order inserted ...", LocalDate.of(2004, 10, 22),
                                LocalDate.of(2004, 10, 23), LocalDate.of(2004, 10, 23), 
                                "Shipped", 363L, BigDecimal.valueOf(314.44))
                        .values(++Order_Id,
                                "Important order ...", LocalDate.of(2003, 12, 2),
                                LocalDate.of(2003, 1, 3), LocalDate.of(2003, 2, 26), 
                                "Resolved", 128L, BigDecimal.valueOf(125.55))
                        .values(++Order_Id,
                                "Order of client ...", LocalDate.of(2005, 12, 12),
                                LocalDate.of(2005, 12, 23), LocalDate.of(2005, 12, 22), 
                                "On Hold", 181L, BigDecimal.valueOf(245.53))
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 5
    /*
    insert into "public"."payment" (
      "customer_number", "check_number", 
      "caching_date", "payment_date", 
      "invoice_amount"
    ) 
    values 
      (
        ?, 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        cast(
          ? as timestamp(6)
        ), 
        ?
      ) on conflict do nothing    
     */
    public void insertPaymentCompositeKey() {

        System.out.println("EXAMPLE 5 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .columns(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER, // composite primary key
                                PAYMENT.CACHING_DATE, PAYMENT.PAYMENT_DATE, PAYMENT.INVOICE_AMOUNT)
                        .values(103L, "HQ336338",
                                LocalDateTime.of(2004, 11, 17, 12, 30, 15),
                                LocalDateTime.of(2004, 10, 19, 12, 30, 15),
                                BigDecimal.valueOf(5433.22))
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 6        
    // 6.1, 6.2, 6.3, 6.7, 6.8
    /*
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "hot", "rate", 
      "vat", "fiscal_month", "revenue_growth", 
      "trend"
    ) 
    values 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      ) on conflict do nothing    
    
    // 6.4, 6.5
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "hot", "rate", 
      "vat", "fiscal_month", "revenue_growth", 
      "trend"
    ) 
    values 
      (
        ?, ?, ?, ?, default, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, default
      ) on conflict do nothing
    
    // 6.6
    insert into "public"."sale" (
      "revenue_growth", "fiscal_month", 
      "employee_number", "sale", "fiscal_year", 
      "sale_id"
    ) 
    values 
      (?, ?, ?, ?, ?, ?) on conflict do nothing    
    */
    public void insertOneSaleRecord() {
                
        System.out.println("EXAMPLE 6.1 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(
                                new SaleRecord()
                                        .value1(++Sale_Id)
                                        // .value1(ctx.select(SALE_SEQ.nextval()).fetchOneInto(Long.class))
                                        .value2(2005)
                                        .value3(3223.12)
                                        .value4(1504L)
                                        .value8(3)
                                        .value9(14.44)
                                        .value10("UP")
                                        .valuesRow().fields()
                        )
                        .onDuplicateKeyIgnore()
                        .execute()
        );
                
        System.out.println("EXAMPLE 6.2 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(new SaleRecord()
                                .values(++Sale_Id,
                                        // ctx.select(SALE_SEQ.nextval()).fetchOneInto(Long.class),
                                        2004, 143.31, 1370L, null, 
                                        RateType.SILVER, VatType.MAX, 3, 14.55, null)
                                .valuesRow().fields())
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        /* create a SaleRecord via constructor */
        // SaleRecord sr = new SaleRecord(null, 2003, 3443.22, 1370L,
        //         null, RateType.SILVER, VatType.MAX, 3, 14.55, null);
        /* or, create a SaleRecord via constructor and setters */
        SaleRecord sr = new SaleRecord();
        sr.setSaleId(++Sale_Id);                            // or, sr.set(SALE.SALE_ID, ++Sale_Id);
        sr.setFiscalYear(2003);                             // or, sr.set(SALE.FISCAL_YEAR, 2003);
        sr.setSale(3443.22);                                // or, sr.set(SALE.SALE_, 3443.22);        
        sr.setEmployeeNumber(1370L);                        // or, sr.set(SALE.EMPLOYEE_NUMBER, 1370L);                   
        sr.setFiscalMonth(3);                               // or, sr.set(SALE.FISCAL_MONTH, 3);
        sr.setRevenueGrowth(14.55);                         // or, sr.set(SALE.REVENUE_GROWTH, 14.55)

        System.out.println("EXAMPLE 6.3 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.valuesRow().fields())
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        sr.setSaleId(++Sale_Id);
        System.out.println("EXAMPLE 6.4 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.getSaleId(), sr.getFiscalYear(), sr.getSale(), sr.getEmployeeNumber(),
                                default_(), RateType.SILVER, VatType.MAX, 
                                sr.getFiscalMonth(), sr.getFiscalYear(), default_())
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        sr.setSaleId(++Sale_Id);
        System.out.println("EXAMPLE 6.5 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.value1(), sr.value2(), sr.value3(), sr.value4(),
                                default_(), RateType.SILVER, VatType.MAX, 
                                sr.value8(), sr.value9(), default_())
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        sr.setSaleId(++Sale_Id);
        System.out.println("EXAMPLE 6.6 (affected rows): "
                + ctx.insertInto(SALE, sr.field9(), sr.field8(), sr.field4(), sr.field3(), sr.field2(), sr.field1())
                        .values(sr.value9(), sr.value8(), sr.value4(), sr.value3(), sr.value2(), sr.value1())
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        sr.setSaleId(++Sale_Id);
        System.out.println("EXAMPLE 6.7 (affected rows): "
                + ctx.insertInto(SALE, sr.fields())
                        .values(sr.valuesRow().fields())
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        sr.setSaleId(++Sale_Id);
        System.out.println("EXAMPLE 6.8 (affected rows): "
                + ctx.insertInto(SALE, sr.fieldsRow().fields())
                        .values(sr.valuesRow().fields())
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 7
    /*   
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "hot", "rate", 
      "vat", "fiscal_month", "revenue_growth", 
      "trend"
    ) 
    values 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      ), 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      ) on conflict do nothing    
     */
    public void insertTwoSaleRecord() {
        
        // Record10<Long, Integer, Double, Long, Boolean, RateType, VatType, Integer, Double, String>
        SaleRecord sr1 = new SaleRecord(++Sale_Id, 2003, 3443.22, 1370L,
                null, RateType.SILVER, VatType.MAX, 3, 14.55, null);
        SaleRecord sr2 = new SaleRecord(++Sale_Id, 2005, 1221.12, 1504L,
                null, RateType.SILVER, VatType.MAX, 5, 22.11, null);

        System.out.println("EXAMPLE 7 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr1.valuesRow().fields())
                        .values(sr2.valuesRow().fields())
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 8  
    /*
    // 8.1, 8.4.3
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "hot", "rate", 
      "vat", "fiscal_month", "revenue_growth", 
      "trend"
    ) 
    values 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      ), 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      ), 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      )
    
    // 8.2
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "fiscal_month", 
      "revenue_growth"
    ) 
    values 
      (?, ?, ?, ?, ?, ?), 
      (?, ?, ?, ?, ?, ?), 
      (?, ?, ?, ?, ?, ?) on conflict do nothing
    
    // 8.3, 8.4.1
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "hot", "rate", 
      "vat", "fiscal_month", "revenue_growth", 
      "trend"
    ) 
    values 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      ), 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      ), 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      ) on conflict do nothing
    
    // 8.4.2
    insert into "public"."sale" (
      "fiscal_year", "sale", "employee_number", 
      "rate", "vat", "fiscal_month", "revenue_growth"
    ) 
    values 
      (
        ?, ?, ?, ? :: "public"."rate_type", ? :: "public"."vat_type", 
        ?, ?
      ), 
      (
        ?, ?, ?, ? :: "public"."rate_type", ? :: "public"."vat_type", 
        ?, ?
      ), 
      (
        ?, ?, ?, ? :: "public"."rate_type", ? :: "public"."vat_type", 
        ?, ?
      )    
     */
    public void insertCollectionOfSaleRecord() {

        // consider this collection of SaleRecord
        List<SaleRecord> listOfRecord
                = List.of(new SaleRecord(++Sale_Id, 
                                2003, 3443.22, 1370L, null, RateType.SILVER, VatType.MAX, 3, 14.55, null),
                        new SaleRecord(++Sale_Id, 
                                2005, 1221.12, 1504L, null, RateType.SILVER, VatType.MAX, 5, 22.11, "UP"),
                        new SaleRecord(++Sale_Id, 
                                2005, 1221.12, 1504L, null, RateType.SILVER, VatType.MAX, 7, 65.59, null));

        /* First Approach */
        // InsertValuesStepN<SaleRecord>
        var insert1 = ctx.insertInto(SALE, SALE.fields());
        for (SaleRecord sr : listOfRecord) {
            insert1.values(sr.valuesRow().fields());
        }
        System.out.println("EXAMPLE 8.1 (affected rows): "
                + insert1.execute()
        );

        /* Second Approach */
        // InsertValuesStep6<SaleRecord, Long, Integer, Double, Long, Integer, Double>
        var insert2 = ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_, 
                SALE.EMPLOYEE_NUMBER, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH);
        for (SaleRecord sr : listOfRecord) {
            insert2.values(sr.getSaleId(), sr.getFiscalYear(), 
                    sr.getSale(), sr.getEmployeeNumber(), sr.getFiscalMonth(), sr.getRevenueGrowth());
        }
        insert2.onDuplicateKeyIgnore();
        System.out.println("EXAMPLE 8.2 (affected rows): "
                + insert2.execute()
        );

        /* Third Approach (inspired from https://github.com/jOOQ/jOOQ/issues/6604) */
        System.out.println("EXAMPLE 8.3 (affected rows): "
                + Optional.of(ctx.insertInto(SALE, SALE.fields()))
                        .map(s -> {
                            listOfRecord.forEach((sr) -> {
                                s.values(sr.valuesRow().fields());
                            });
                            s.onDuplicateKeyIgnore();
                            return s.execute();
                        })
        );

        // jOOQ 3.15 (valuesOfRows(), valuesOfRecords()) 
        // https://github.com/jOOQ/jOOQ/issues/6604
        System.out.println("EXAMPLE 8.4.1 (affected rows): "
                + ctx.insertInto(SALE, SALE.fields())
                        .valuesOfRecords(listOfRecord)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        // List<Row7<Integer, Double, Long, RateType, VatType, Integer, Double>>
        var listOfRows
                = List.of(row(2003, 3443.22, 1370L,
                        RateType.SILVER, VatType.MAX, 3, 14.55),
                        row(2005, 1221.12, 1504L,
                                RateType.SILVER, VatType.MAX, 5, 22.11),
                        row(2005, 1221.12, 1504L,
                                RateType.SILVER, VatType.MAX, 7, 65.59));

        System.out.println("EXAMPLE 8.4.2 (affected rows): "
                + ctx.insertInto(SALE,
                        SALE.FISCAL_YEAR, SALE.SALE_,
                        SALE.EMPLOYEE_NUMBER, SALE.RATE, SALE.VAT,
                        SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .valuesOfRows(listOfRows)
                        .execute()
        );

        // simple example of collecting into a list of Row[N] including RowN
        List<Row2<Integer, String>> list = Stream.of(1, 2, 3).collect(
                toRowList(i -> val(i), i -> val("A")
                ));

        // simple example of collecting into an array of Row[N] including RowN
        Row2[] arr = Stream.of(1, 2, 3).collect(
                toRowArray(i -> val(i), i -> val("A")
                ));

        // collect POJO in RowN
        List<Sale> sales
                = List.of(
                        new Sale(++Sale_Id, 2003, 3443.22, 1370L,
                                null, RateType.SILVER, VatType.MAX, 3, 14.55, null),
                        new Sale(++Sale_Id, 2005, 1221.12, 1504L,
                                null, RateType.SILVER, VatType.MAX, 5, 22.11, "UP"),
                        new Sale(++Sale_Id, 2005, 1221.12, 1504L,
                                true, RateType.SILVER, VatType.MAX, 7, 65.59, null));

        //List<Row10<Long, Integer, Double, Long, Boolean, RateType, VatType, Integer, Double, String>>
        var listOfSales
                = sales.stream().collect(toRowList(
                        i -> val(i.getSaleId()), i -> val(i.getFiscalYear()), i -> val(i.getSale()),
                        i -> val(i.getEmployeeNumber()), i -> val(i.getHot()), i -> val(i.getRate()),
                        i -> val(i.getVat()), i -> val(i.getFiscalMonth()), i -> val(i.getRevenueGrowth()),
                        i -> val(i.getTrend())
                ));

        System.out.println("EXAMPLE 8.4.3 (affected rows): "
                + ctx.insertInto(SALE,
                        SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_,
                        SALE.EMPLOYEE_NUMBER, SALE.HOT, SALE.RATE, SALE.VAT,
                        SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND)
                        .valuesOfRows(listOfSales)
                        .execute()
        );
    }

    // EXAMPLE 9
    /*
    // 9.1
    insert into "public"."sale" (
      "fiscal_year", "sale", "employee_number", 
      "fiscal_month", "revenue_growth"
    ) 
    values 
      (?, ?, ?, ?, ?) returning "public"."sale"."sale_id"
    
    // 9.2, 9.4, 9.5
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "hot", "rate", 
      "vat", "fiscal_month", "revenue_growth", 
      "trend"
    ) 
    values 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      ) returning "public"."sale"."sale_id"
    
    // 9.3, 9.6
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "hot", "rate", 
      "vat", "fiscal_month", "revenue_growth", 
      "trend"
    ) 
    values 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      )
    
    // 9.7
    insert into "public"."sale" (
      "fiscal_year", "sale", "employee_number", 
      "fiscal_month", "revenue_growth"
    ) 
    values 
      (?, ?, ?, ?, ?)    
     */
    public void insertNewRecord() {

        System.out.println("EXAMPLE 9.1 (affected rows): "
                + ctx.newRecord(SALE.FISCAL_YEAR, SALE.SALE_, 
                        SALE.EMPLOYEE_NUMBER, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2004, 1233.2, 1370L, 4, 12.33)
                        .into(SALE)
                        .insert()
        );

        // This is the Sale POJO generated by jOOQ
        Sale sale = new Sale(++Sale_Id, 2005, 343.22, 1504L,
                null, RateType.SILVER, VatType.MAX, 4, 15.55, null);
        System.out.println("EXAMPLE 9.2 (affected rows): "
                + ctx.newRecord(SALE, sale).insert()
        );

        SaleRecord sr = new SaleRecord();
        sr.from(sale);
        sr.setSaleId(++Sale_Id); // set a new id
        System.out.println("EXAMPLE 9.3 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.valuesRow().fields())
                        .execute()
        );
        
        sr.setSaleId(++Sale_Id); // set a new id
        sr.attach(ctx.configuration()); // attach the record to the current configuration
        System.out.println("EXAMPLE 9.4 (affected rows): "
                +sr.insert()
        );

        // this is user-define SalePart POJO (it contains only a part of fields)
        SalePart salePart = new SalePart(5644.32, 1370L);
        System.out.println("EXAMPLE 9.5 (affected rows): "
                + ctx.newRecord(SALE)
                        .values(++Sale_Id, 2004,
                                salePart.getSale(), salePart.getEmployeeNumber(),
                                null, RateType.SILVER, VatType.MAX, 4, 12.33, null)
                        .insert()
        );

        SaleRecord srp = new SaleRecord();
        srp.from(salePart);          // get the available fields from SalePart        
        srp.setFiscalYear(2004);     // fiscal_year cannot be null and doesn't have a default value
        srp.setFiscalMonth(4);       // fiscal_month cannot be null and doesn't have a default value
        srp.setRevenueGrowth(12.22); // revenue_growth cannot be null and doesn't have a default value
        srp.setSaleId(++Sale_Id);
        System.out.println("EXAMPLE 9.6 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(srp.valuesRow().fields())
                        .execute()
        );

        srp.reset(SALE.SALE_ID);
        System.out.println("EXAMPLE 9.7 (affected rows): "
                + ctx.executeInsert(srp)
        );
    }

    // EXAMPLE 10
    /*
    // 10.1
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "hot", "rate", 
      "vat", "fiscal_month", "revenue_growth", 
      "trend"
    ) 
    values 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      ) returning "public"."sale"."sale_id"
    
    // 10.2
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "hot", "rate", 
      "vat", "fiscal_month", "revenue_growth", 
      "trend"
    ) 
    values 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      )    
     */
    public void insertRecordAfterResettingPK() {

        Sale sale = new Sale(1L, 2005, 343.22, 1504L, null, RateType.SILVER, VatType.MAX, 6, 23.99, null);
        var record = ctx.newRecord(SALE, sale);
        
        // reset the current ID and allow DB to generate one
        record.changed(SALE.SALE_ID, false);
                
        // resets both changed flag that tracks record changes and value
        // record.reset(SALE.SALE_ID); 
        
        record.setSaleId(++Sale_Id);
        // record.setSaleId(ctx.select(SALE_SEQ.nextval()).fetchOneInto(Long.class));
        System.out.println("EXAMPLE 10.1 (affected rows): "
                + record.insert()
        );

        SaleRecord sr = new SaleRecord();
        sr.from(sale);
        
        // resets both changed flag that tracks record changes and value
        sr.reset(SALE.SALE_ID); 
        
        sr.setSaleId(++Sale_Id);
        // sr.setSaleId(ctx.select(SALE_SEQ.nextval()).fetchOneInto(Long.class));
        System.out.println("EXAMPLE 10.2 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.valuesRow().fields())
                        .execute()
        );
    }

    // EXAMPLE 11    
    /*
    // 11.1
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "hot", "rate", 
      "vat", "fiscal_month", "revenue_growth", 
      "trend"
    ) 
    values 
      (
        (
          random() * ?
        ), 
        ?, 
        round(?), 
        ?, 
        default, 
        ? :: "public"."rate_type", 
        ? :: "public"."vat_type", 
        ?, 
        ?, 
        default
      ) on conflict do nothing
    
    // 11.2
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "hot", "rate", 
      "vat", "fiscal_month", "revenue_growth", 
      "trend"
    ) 
    values 
      (
        (
          random() * ?
        ), 
        ?, 
        "public"."get_avg_sale"("len_from" := ?, "len_to" := ?), 
        ?, 
        default, 
        ? :: "public"."rate_type", 
        ? :: "public"."vat_type", 
        ?, 
        ?, 
        default
      ) on conflict do nothing    
    */
    public void usingFunctionsInInsert() {
        
        System.out.println("EXAMPLE 11.1 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(rand().mul(1000), 2004, round(21112.23), 1504L,
                                default_(), RateType.SILVER, VatType.MAX,
                                3, 12.44, default_())
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    
        System.out.println("EXAMPLE 11.2 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(rand().mul(1000), 2005, getAvgSale(1000, 5000), 1370,
                                default_(), RateType.SILVER, VatType.MAX, 
                                4, 22.34, default_())
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 12
    /*
    insert into "public"."department" (
      "name", "phone", "code", "office_code"
    ) 
    values 
      (
        ?, 
        coalesce(
          case when ? is null then '+40 080 000' else ? end, 
          '+40 080 000'
        ), 
        ?, 
        ?
      )    
     */
    public void insertDepartment() {

        Department department = new Department(); // jOOQ POJO
        department.setName("IT");
        department.setOfficeCode("2");        
        department.setCode(ThreadLocalRandom.current().nextInt(10000, 20000)); // random code

        department.setPhone("+03 331 443");

        System.out.println("EXAMPLE 12 (affected rows): "
                + ctx.insertInto(DEPARTMENT, DEPARTMENT.NAME,
                        DEPARTMENT.PHONE, DEPARTMENT.CODE, DEPARTMENT.OFFICE_CODE)
                        .values(val(department.getName()),
                                coalesce(
                                        choose().when(val(department.getPhone()).isNull(), inline("+40 080 000"))
                                                .otherwise(department.getPhone()),
                                        inline("+40 080 000")),
                                val(department.getCode()), val(department.getOfficeCode())
                        )
                        .execute()
        );
    }

    // EXAMPLE 13
    /*
    insert into "public"."order" (
      "order_date", "required_date", "shipped_date", 
      "status", "comments", "customer_number", 
      "amount"
    ) 
    select 
      cast(? as date), 
      cast(? as date), 
      cast(? as date), 
      ?, 
      ?, 
      ?, 
      ? 
    where 
      not (
        exists (
          select 
            "public"."order"."order_id", 
            "public"."order"."order_date", 
            "public"."order"."required_date", 
            "public"."order"."shipped_date", 
            "public"."order"."status", 
            "public"."order"."comments", 
            "public"."order"."customer_number", 
            "public"."order"."amount" 
          from 
            "public"."order" 
          where 
            (
              (
                cast(? as date) between "public"."order"."order_date" 
                and "public"."order"."shipped_date" 
                or cast(? as date) between "public"."order"."order_date" 
                and "public"."order"."shipped_date"
              ) 
              and ? = "public"."order"."customer_number"
            )
        )
      )    
     */
    public void insertOrderBetweenDates() {

        Order o = ORDER;

        System.out.println("EXAMPLE 13 (affected rows): "
                + ctx.insertInto(o)
                        .columns(o.ORDER_DATE, o.REQUIRED_DATE, o.SHIPPED_DATE,
                                o.STATUS, o.COMMENTS, o.CUSTOMER_NUMBER, o.AMOUNT)
                        .select(
                                select(val(LocalDate.of(2010, 10, 10)), val(LocalDate.of(2010, 11, 1)),
                                        val(LocalDate.of(2010, 11, 5)), val("Shipped"), val(""), 
                                        val(103L), val(BigDecimal.valueOf(2000)))
                                        .whereNotExists(
                                                selectFrom(o)
                                                        .where(val(LocalDate.of(2010, 10, 10)).between(o.ORDER_DATE).and(o.SHIPPED_DATE)
                                                                .or(val(LocalDate.of(2010, 11, 5)).between(o.ORDER_DATE).and(o.SHIPPED_DATE)))
                                                        .and(val(103L).eq(o.CUSTOMER_NUMBER))
                                        )
                        )
                        .execute()
        );
    }

    // EXAMPLE 14
    /*
    insert into "public"."office" (
      "office_code","location","phone","address_line_first","address_line_second","postal_code","territory")
    values
      (?, row(?, ?, ?), ?, ?, ?, ?, ?) 
    on conflict do nothing
     */
    public void insertAndUDTRecord() {

        EvaluationCriteriaRecord ec = new EvaluationCriteriaRecord(55, 67, 34, 98);
        System.out.println("EXAMPLE 14 (affected rows): "
                + ctx.insertInto(MANAGER)
                        .values(default_(),
                                "Farel Ugg", default_(), ec)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        // creating a UDT record alternatives
        EvaluationCriteriaRecord r1 = EVALUATION_CRITERIA.newRecord();
        // r1.setCommunicationAbility(56); ...

        EvaluationCriteriaRecord r2 = ctx.newRecord(EVALUATION_CRITERIA);
        // r2.setCommunicationAbility(56); ...
    }
}
