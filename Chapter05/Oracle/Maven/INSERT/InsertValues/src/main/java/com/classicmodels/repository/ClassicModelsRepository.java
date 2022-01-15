package com.classicmodels.repository;

import com.classicmodels.pojo.SalePart;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import static jooq.generated.Routines.getTotalSales;
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
    
    private long Order_Id;
    private long Sale_Id;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
        
        // avoid duplicate keys
        Order_Id = ctx.select(max(ORDER.ORDER_ID)).from(ORDER).fetchOneInto(Long.class) + 10000;
        Sale_Id = ctx.select(max(SALE.SALE_ID)).from(SALE).fetchOneInto(Long.class) + 10000;
    }

    // EXAMPLE 1
    // 1.1
    /*
    insert into "CLASSICMODELS"."ORDER" (
      "ORDER_ID", "ORDER_DATE", "REQUIRED_DATE", 
      "SHIPPED_DATE", "STATUS", "COMMENTS", 
      "CUSTOMER_NUMBER", "AMOUNT"
    ) 
    values 
      (
        default, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, ?, ?, ?
      )
    
    // 1.2 - 1.4
    insert into "CLASSICMODELS"."ORDER" (
      "COMMENTS", "ORDER_DATE", "REQUIRED_DATE", 
      "SHIPPED_DATE", "STATUS", "CUSTOMER_NUMBER", 
      "AMOUNT"
    ) 
    values 
      (
        ?, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, ?, ?
      )    
     */
    public void insertOrderAutoGenKey() {

        System.out.println("EXAMPLE 1.1 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(default_(), // primary key is auto-generated
                                LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped",
                                "New order inserted ...", 363L, 414.44)
                        .execute()
        );

        System.out.println("EXAMPLE 1.2 (affected rows): "
                + // InsertValuesStep7<OrderRecord, String, LocalDate, LocalDate, LocalDate, String, Long, BigDecimal>
                ctx.insertInto(ORDER, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values("New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27),
                                "Shipped", 363L, BigDecimal.valueOf(414.44))
                        .execute()
        );

        System.out.println("EXAMPLE 1.3 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .columns(ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                                ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values("New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27),
                                "Shipped", 363L, BigDecimal.valueOf(414.44))
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
        iq.addValue(ORDER.AMOUNT, 414.44);

        System.out.println("EXAMPLE 1.4 (affected rows): "
                + iq.execute()
        );
    }

    // EXAMPLE 2
    /*
    // 2.1.1
    insert into "CLASSICMODELS"."ORDER" (
      "ORDER_ID", "ORDER_DATE", "REQUIRED_DATE", 
      "SHIPPED_DATE", "STATUS", "COMMENTS", 
      "CUSTOMER_NUMBER", "AMOUNT"
    ) 
    values 
      (
        ?, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, ?, ?, ?
      )
    
    // 2.1.2, 2.2.2, 2.3.2
    merge into "CLASSICMODELS"."ORDER" using (
      (
        select 
          null "ORDER_ID", 
          null "ORDER_DATE", 
          null "REQUIRED_DATE", 
          null "SHIPPED_DATE", 
          null "STATUS", 
          null "COMMENTS", 
          null "CUSTOMER_NUMBER", 
          null "AMOUNT" 
        from 
          DUAL 
        where 
          1 = 0 
        union all 
        select 
          ?, 
          cast(? as date), 
          cast(? as date), 
          cast(? as date), 
          ?, 
          ?, 
          ?, 
          ? 
        from 
          DUAL
      )
    ) "t" on (
      (
        (
          "CLASSICMODELS"."ORDER"."ORDER_ID", 
          1
        ) = (
          ("t"."ORDER_ID", 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "ORDER_ID", "ORDER_DATE", "REQUIRED_DATE", 
      "SHIPPED_DATE", "STATUS", "COMMENTS", 
      "CUSTOMER_NUMBER", "AMOUNT"
    ) 
    values 
      (
        "t"."ORDER_ID", "t"."ORDER_DATE", 
        "t"."REQUIRED_DATE", "t"."SHIPPED_DATE", 
        "t"."STATUS", "t"."COMMENTS", "t"."CUSTOMER_NUMBER", 
        "t"."AMOUNT"
      )    
    
    // 2.2.1, 2.3.1
    insert into "CLASSICMODELS"."ORDER" (
      "ORDER_ID", "COMMENTS", "ORDER_DATE", 
      "REQUIRED_DATE", "SHIPPED_DATE", 
      "STATUS", "CUSTOMER_NUMBER", "AMOUNT"
    ) 
    values 
      (
        ?, 
        ?, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, 
        ?, 
        ?
      )
    
     */    
    public void insertOrderManualKey() {

        System.out.println("EXAMPLE 2.1.1 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(++Order_Id, // computed primary key
                                LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped",
                                "New order inserted ...", 363L, 414.44)
                        .execute()
        );

        System.out.println("EXAMPLE 2.1.2 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(++Order_Id, // computed primary key
                                LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped",
                                "New order inserted ...", 363L, 414.44)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 2.2.1 (affected rows): "
                + // InsertValuesStep8<OrderRecord, Long, String, LocalDate, LocalDate, LocalDate, String, Long, BigDecimal>
                ctx.insertInto(ORDER, ORDER.ORDER_ID, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values(++Order_Id, // computed primary key
                                "New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27),
                                "Shipped", 363L, BigDecimal.valueOf(314.44))
                        .execute()
        );

        System.out.println("EXAMPLE 2.2.2 (affected rows): "
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

        System.out.println("EXAMPLE 2.3.1 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .columns(ORDER.ORDER_ID, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                                ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values(++Order_Id, // computed primary key
                                "New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27),
                                "Shipped", 363L, BigDecimal.valueOf(314.44))
                        .execute()
        );

        System.out.println("EXAMPLE 2.3.2 (affected rows): "
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
    insert into "CLASSICMODELS"."ORDER" (
      "COMMENTS", "ORDER_DATE", "REQUIRED_DATE", 
      "SHIPPED_DATE", "STATUS", "CUSTOMER_NUMBER", 
      "AMOUNT"
    ) 
    select 
      ?, 
      cast(? as date), 
      cast(? as date), 
      cast(? as date), 
      ?, 
      ?, 
      ? 
    from 
      DUAL 
    union all 
    select 
      ?, 
      cast(? as date), 
      cast(? as date), 
      cast(? as date), 
      ?, 
      ?, 
      ? 
    from 
      DUAL 
    union all 
    select 
      ?, 
      cast(? as date), 
      cast(? as date), 
      cast(? as date), 
      ?, 
      ?, 
      ? 
    from 
      DUAL    
     */
    public void insertMultipleOrderAutoGenKey() {

        // Notice that we cannot use default_() because INSERT ... VALUES with multiple rows 
        // needs to be emulated as INSERT ... SELECT in Oracle, which doesn’t support DEFAULT 
        // expressions. Of course, you’ll be better off using an implicit default by listing 
        // only the other ORDER columns explicitly.                
        System.out.println("EXAMPLE 3.1 (affected rows): "
                + // InsertValuesStep7<OrderRecord, String, LocalDate, LocalDate, LocalDate, String, Long, BigDecimal>
                ctx.insertInto(ORDER, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values("New order inserted ...", LocalDate.of(2004, 10, 22),
                                LocalDate.of(2004, 10, 23), LocalDate.of(2004, 10, 23),
                                "Shipped", 363L, BigDecimal.valueOf(314.44))
                        .values("Important order ...", LocalDate.of(2003, 12, 2),
                                LocalDate.of(2003, 1, 3), LocalDate.of(2003, 2, 26),
                                "Resolved", 128L, BigDecimal.valueOf(125.55))
                        .values("Order of client ...", LocalDate.of(2005, 12, 12),
                                LocalDate.of(2005, 12, 23), LocalDate.of(2005, 12, 22),
                                "On Hold", 181L, BigDecimal.valueOf(253.43))
                        .execute()
        );

        System.out.println("EXAMPLE 3.2 (affected rows): "
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
                                "On Hold", 181L, BigDecimal.valueOf(253.43))
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    merge into "CLASSICMODELS"."ORDER" using (
      (
        (
          select 
            null "ORDER_ID", 
            null "ORDER_DATE", 
            null "REQUIRED_DATE", 
            null "SHIPPED_DATE", 
            null "STATUS", 
            null "COMMENTS", 
            null "CUSTOMER_NUMBER", 
            null "AMOUNT" 
          from 
            DUAL 
          where 
            1 = 0
        ) 
        union all 
          (
            select 
              ?, 
              cast(? as date), 
              cast(? as date), 
              cast(? as date), 
              ?, 
              ?, 
              ?, 
              ? 
            from 
              DUAL 
            union all 
            select 
              ?, 
              cast(? as date), 
              cast(? as date), 
              cast(? as date), 
              ?, 
              ?, 
              ?, 
              ? 
            from 
              DUAL 
            union all 
            select 
              ?, 
              cast(? as date), 
              cast(? as date), 
              cast(? as date), 
              ?, 
              ?, 
              ?, 
              ? 
            from 
              DUAL
          )
      )
    ) "t" on (
      (
        (
          "CLASSICMODELS"."ORDER"."ORDER_ID", 
          1
        ) = (
          ("t"."ORDER_ID", 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "ORDER_ID", "ORDER_DATE", "REQUIRED_DATE", 
      "SHIPPED_DATE", "STATUS", "COMMENTS", 
      "CUSTOMER_NUMBER", "AMOUNT"
    ) 
    values 
      (
        "t"."ORDER_ID", "t"."ORDER_DATE", 
        "t"."REQUIRED_DATE", "t"."SHIPPED_DATE", 
        "t"."STATUS", "t"."COMMENTS", "t"."CUSTOMER_NUMBER", 
        "t"."AMOUNT"
      )    
     */
    public void insertMultipleOrderManualKey() {

        System.out.println("EXAMPLE 4.1 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(++Order_Id,
                                LocalDate.of(2004, 10, 22), LocalDate.of(2004, 10, 23),
                                LocalDate.of(2004, 10, 23), "Shipped",
                                "New order inserted ...", 363L, 314.44)
                        .values(++Order_Id,
                                LocalDate.of(2003, 12, 2), LocalDate.of(2003, 1, 3),
                                LocalDate.of(2003, 2, 26), "Resolved",
                                "Important order ...", 128L, 125.55)
                        .values(++Order_Id,
                                LocalDate.of(2005, 12, 12), LocalDate.of(2005, 12, 23),
                                LocalDate.of(2005, 12, 22), "On Hold",
                                "Order of client ...", 181L, 253.44)
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
    merge into "CLASSICMODELS"."PAYMENT" using (
     (
       select 
         null "CUSTOMER_NUMBER", 
         null "CHECK_NUMBER", 
         null "CACHING_DATE", 
         null "PAYMENT_DATE", 
         null "INVOICE_AMOUNT" 
       from 
         DUAL 
       where 
         1 = 0 
       union all 
       select 
         ?, ?, ?, ?, ? 
       from 
         DUAL
     )
   ) "t" on (
     (
       (
         (
           (
             "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER", 
             1
           ) = (
             ("t"."CUSTOMER_NUMBER", 1)
           ) 
           or 1 = 0
         ) 
         and (
           (
             "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
             1
           ) = (
             ("t"."CHECK_NUMBER", 1)
           ) 
           or 1 = 0
         )
       ) 
       or (
         "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
         1
       ) = (
         ("t"."CHECK_NUMBER", 1)
       ) 
       or 1 = 0
     )
   ) when not matched then insert (
     "CUSTOMER_NUMBER", "CHECK_NUMBER", 
     "CACHING_DATE", "PAYMENT_DATE", 
     "INVOICE_AMOUNT"
   ) 
   values 
     (
       "t"."CUSTOMER_NUMBER", "t"."CHECK_NUMBER", 
       "t"."CACHING_DATE", "t"."PAYMENT_DATE", 
       "t"."INVOICE_AMOUNT"
     )
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
    /*
    // 6.1, 6.2, 6.3, 6.4, 6.5, 6.7, 6.8
    insert into "CLASSICMODELS"."SALE" (
      "SALE_ID", "FISCAL_YEAR", "SALE", 
      "EMPLOYEE_NUMBER", "HOT", "RATE", 
      "VAT", "FISCAL_MONTH", "REVENUE_GROWTH", 
      "TREND"
    ) 
    values 
      (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    
    // 6.6
    insert into "CLASSICMODELS"."SALE" (
      "REVENUE_GROWTH", "FISCAL_MONTH", 
      "EMPLOYEE_NUMBER", "SALE", "FISCAL_YEAR", 
      "SALE_ID"
    ) 
    values 
      (?, ?, ?, ?, ?, ?)    
     */
    public void insertOneSaleRecord() {

        System.out.println("EXAMPLE 6.1 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(
                                new SaleRecord()
                                        .value1(++Sale_Id)
                                        //.value1(ctx.select(SALE_SEQ.nextval()).fetchOneInto(Long.class))
                                        .value2(2005)
                                        .value3(3223.12)
                                        .value4(1504L)
                                        .value8(3)
                                        .value9(14.44)
                                        .value10("UP")
                                        .valuesRow().fields()
                        )
                        .execute()
        );

        System.out.println("EXAMPLE 6.2 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(new SaleRecord()
                                .values(++Sale_Id,
                                        // ctx.select(SALE_SEQ.nextval()).fetchOneInto(Long.class),
                                        2004, 143.31, 1370L, "1", null, null, 3, 12.22, "UP")
                                .valuesRow().fields())
                        .execute()
        );

        /* create a SaleRecord via constructor */
        // SaleRecord sr = new SaleRecord(null, 2003, 3443.22, 1370L,
        //        "0", null, null, 3, 12.22, "UP");
        /* or, creare a SaleRecord via constructor and setters */
        SaleRecord sr = new SaleRecord();
        sr.setSaleId(++Sale_Id);
        // sr.setSaleId(ctx.select(SALE_SEQ.nextval()).fetchOneInto(Long.class));
        sr.setFiscalYear(2003);                             // or, sr.set(SALE.FISCAL_YEAR, 2003);
        sr.setSale(3443.22);                                // or, sr.set(SALE.SALE_, 3443.22);        
        sr.setEmployeeNumber(1370L);                        // or, sr.set(SALE.EMPLOYEE_NUMBER, 1370L);                   
        sr.setFiscalMonth(3);                               // or, sr.set(SALE.FISCAL_MONTH, 3);
        sr.setRevenueGrowth(14.55);                         // or, sr.set(SALE.REVENUE_GROWTH, 14.55)

        System.out.println("EXAMPLE 6.3 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.valuesRow().fields())
                        .execute()
        );

        sr.setSaleId(++Sale_Id);
        System.out.println("EXAMPLE 6.4 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.getSaleId(), sr.getFiscalYear(), sr.getSale(), sr.getEmployeeNumber(),
                                "0", null, null, sr.getFiscalMonth(), sr.getRevenueGrowth(), "UP")
                        .execute()
        );

        sr.setSaleId(++Sale_Id);
        System.out.println("EXAMPLE 6.5 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.value1(), sr.value2(), sr.value3(), sr.value4(),
                                "0", null, null, sr.value8(), sr.value9(), "UP")
                        .execute()
        );

        sr.setSaleId(++Sale_Id);
        System.out.println("EXAMPLE 6.6 (affected rows): "
                + ctx.insertInto(SALE, sr.field9(), sr.field8(), sr.field4(), sr.field3(), sr.field2(), sr.field1())
                        .values(sr.value9(), sr.value8(), sr.value4(), sr.value3(), sr.value2(), sr.value1())
                        .execute()
        );

        sr.setSaleId(++Sale_Id);
        System.out.println("EXAMPLE 6.7 (affected rows): "
                + ctx.insertInto(SALE, sr.fields())
                        .values(sr.valuesRow().fields())
                        .execute()
        );

        sr.setSaleId(++Sale_Id);
        System.out.println("EXAMPLE 6.8 (affected rows): "
                + ctx.insertInto(SALE, sr.fieldsRow().fields())
                        .values(sr.valuesRow().fields())
                        .execute()
        );
    }

    // EXAMPLE 7
    /*
    insert into "CLASSICMODELS"."SALE" (
      "SALE_ID", "FISCAL_YEAR", "SALE", 
      "EMPLOYEE_NUMBER", "HOT", "RATE", 
      "VAT", "FISCAL_MONTH", "REVENUE_GROWTH", 
      "TREND"
    ) 
    select 
      ?, ?, ?, ?, ?, ?, ?, ?, ?, ? 
    from 
      DUAL 
    union all 
    select 
      ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  
    from 
      DUAL    
     */
    public void insertTwoSaleRecord() {

        // Record10<Long, Integer, Double, Long, String, String, String, Integer, Double, String>
        SaleRecord sr1 = new SaleRecord(++Sale_Id, 2003, 3443.22, 1370L,
                "0", null, null, 3, 12.12, "UP");
        SaleRecord sr2 = new SaleRecord(++Sale_Id, 2005, 1221.12, 1504L,
                "0", null, null, 4, 14.11, "DOWN");

        System.out.println("EXAMPLE 7 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr1.valuesRow().fields())
                        .values(sr2.valuesRow().fields())
                        .execute()
        );
    }

    // EXAMPLE 8  
    /*
    // 8.1, 8.4.3
    insert into "CLASSICMODELS"."SALE" (
      "SALE_ID", "FISCAL_YEAR", "SALE", 
      "EMPLOYEE_NUMBER", "HOT", "RATE", 
      "VAT", "FISCAL_MONTH", "REVENUE_GROWTH", 
      "TREND"
    ) 
    select 
      ?, ?, ?, ?, ?, ?, ?, ?, ?, ? 
    from 
      DUAL 
    union all 
    select 
      ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  
    from 
      DUAL 
    union all 
    select 
      ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  
    from 
      DUAL
    
    // 8.2
    merge into "CLASSICMODELS"."SALE" using (
      (
        (
          select 
            null "SALE_ID", null "FISCAL_YEAR", 
            null "SALE", null "EMPLOYEE_NUMBER", 
            null "FISCAL_MONTH", null "REVENUE_GROWTH" 
          from 
            DUAL 
          where 
            1 = 0
        ) 
        union all 
          (
            select 
              ?, ?, ?, ?, ?, ? 
            from 
              DUAL 
            union all 
            select 
              ?, ?, ?, ?, ?, ?  
            from 
              DUAL 
            union all 
            select 
              ?, ?, ?, ?, ?, ?  
            from 
              DUAL
          )
      )
    ) "t" on (
      (
        (
          "CLASSICMODELS"."SALE"."SALE_ID", 
          1
        ) = (
          ("t"."SALE_ID", 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "SALE_ID", "FISCAL_YEAR", "SALE", 
      "EMPLOYEE_NUMBER", "FISCAL_MONTH", 
      "REVENUE_GROWTH"
    ) 
    values 
      (
        "t"."SALE_ID", "t"."FISCAL_YEAR", 
        "t"."SALE", "t"."EMPLOYEE_NUMBER", 
        "t"."FISCAL_MONTH", "t"."REVENUE_GROWTH"
      )
    
    // 8.3, 8.4.1
    merge into "CLASSICMODELS"."SALE" using (
      (
        (
          select 
            null "SALE_ID", null "FISCAL_YEAR", 
            null "SALE", null "EMPLOYEE_NUMBER", 
            null "HOT", null "RATE", 
            null "VAT", null "FISCAL_MONTH", 
            null "REVENUE_GROWTH", null "TREND" 
          from 
            DUAL 
          where 
            1 = 0
        ) 
        union all 
          (
            select 
              ?, ?, ?, ?, ?, ?, ?, ?, ?, ? 
            from 
              DUAL 
            union all 
            select 
              ?, ?, ?, ?, ?, ?, ?, ?, ?, ? 
            from 
              DUAL 
            union all 
            select 
              ?, ?, ?, ?, ?, ?, ?, ?, ?, ? 
            from 
              DUAL
          )
      )
    ) "t" on (
      (
        (
          "CLASSICMODELS"."SALE"."SALE_ID", 
          1
        ) = (
          ("t"."SALE_ID", 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "SALE_ID", "FISCAL_YEAR", "SALE", 
      "EMPLOYEE_NUMBER", "HOT", "RATE", 
      "VAT", "FISCAL_MONTH", "REVENUE_GROWTH", 
      "TREND"
    ) 
    values 
      (
        "t"."SALE_ID", "t"."FISCAL_YEAR", 
        "t"."SALE", "t"."EMPLOYEE_NUMBER", 
        "t"."HOT", "t"."RATE", "t"."VAT", 
        "t"."FISCAL_MONTH", "t"."REVENUE_GROWTH", 
        "t"."TREND"
      )
    
    // 8.4.2
    insert into "CLASSICMODELS"."SALE" (
      "FISCAL_YEAR", "SALE", "EMPLOYEE_NUMBER", 
      "RATE", "VAT", "FISCAL_MONTH", "REVENUE_GROWTH"
    ) 
    select 
      ?, ?, ?, ?, ?, ?, ? 
    from 
      DUAL 
    union all 
    select 
      ?, ?, ?, ?, ?, ?, ?  
    from 
      DUAL 
    union all 
    select 
      ?, ?, ?, ?, ?, ?, ?  
    from 
      DUAL    
     */
    public void insertCollectionOfSaleRecord() {

        // consider this collection of SaleRecord
        List<SaleRecord> listOfRecord
                = List.of(new SaleRecord(++Sale_Id, 2003, 3443.22, 1370L,
                        "1", null, null, 7, 78.89, "UP"),
                        new SaleRecord(++Sale_Id, 2005, 1221.12, 1504L,
                                "0", null, null, 4, 12.22, "DOWN"),
                        new SaleRecord(++Sale_Id, 2005, 1221.12, 1504L,
                                "0", null, null, 1, 11.34, "UP"));

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
            insert2.values(sr.getSaleId(), sr.getFiscalYear(), sr.getSale(),
                    sr.getEmployeeNumber(), sr.getFiscalMonth(), sr.getRevenueGrowth());
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

        // List<Row7<Integer, Double, Long, String, String, Integer, Double>>
        var listOfRows
                = List.of(row(2003, 3443.22, 1370L,
                        "SILVER", "MAX", 3, 14.55),
                        row(2005, 1221.12, 1504L,
                                "SILVER", "MAX", 5, 22.11),
                        row(2005, 1221.12, 1504L,
                                "SILVER", "MAX", 7, 65.59));

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
                                null, "SILVER", "MAX", 3, 14.55, null),
                        new Sale(++Sale_Id, 2005, 1221.12, 1504L,
                                null, "SILVER", "MAX", 5, 22.11, "UP"),
                        new Sale(++Sale_Id, 2005, 1221.12, 1504L,
                                "1", "SILVER", "MAX", 7, 65.59, null));

        //List<Row10<Long, Integer, Double, Long, String, String, String, Integer, Double, String>>
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
    //9.1, 9.7
    insert into CLASSICMODELS."SALE" (
      "FISCAL_YEAR", "SALE", "EMPLOYEE_NUMBER", 
      "FISCAL_MONTH", "REVENUE_GROWTH"
    ) 
    values 
      (?, ?, ?, ?, ?)    
    
    // 9.2, 9.3, 9.4, 9.5, 9.6
    insert into "CLASSICMODELS"."SALE" (
      "SALE_ID", "FISCAL_YEAR", "SALE", 
      "EMPLOYEE_NUMBER", "HOT", "RATE", 
      "VAT", "FISCAL_MONTH", "REVENUE_GROWTH", 
      "TREND"
    ) 
    values 
      (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    
     */
    public void insertNewRecord() {

        System.out.println("EXAMPLE 9.1 (affected rows): "
                + ctx.newRecord(SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                        SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2004, 1233.2, 1370L, 4, 12.33)
                        .into(SALE)
                        .insert()
        );

        // This is the Sale POJO generated by jOOQ
        Sale sale = new Sale(++Sale_Id, 2005, 343.22, 1504L,
                "0", null, null, 3, 15.32, "UP");
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
                + sr.insert()
        );

        // this is user-define SalePart POJO (it contains only a part of fields)
        SalePart salePart = new SalePart(5644.32, 1370L);
        System.out.println("EXAMPLE 9.5 (affected rows): "
                + ctx.newRecord(SALE, SALE.fields())
                        .values(++Sale_Id, 2004,
                                salePart.getSale(), salePart.getEmployeeNumber(),
                                "0", null, null, 2, 12.21, "UP")
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
    insert into CLASSICMODELS."SALE" (
      "FISCAL_YEAR", "SALE", "EMPLOYEE_NUMBER", 
      "HOT", "RATE", "VAT", "FISCAL_MONTH", 
      "REVENUE_GROWTH", "TREND"
    ) 
    values 
      (?, ?, ?, ?, ?, ?, ?, ?, ?)   
    
    // 10.2
    insert into "CLASSICMODELS"."SALE" (
      "SALE_ID", "FISCAL_YEAR", "SALE", 
      "EMPLOYEE_NUMBER", "HOT", "RATE", 
      "VAT", "FISCAL_MONTH", "REVENUE_GROWTH", 
      "TREND"
    ) 
    values 
      (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)    
     */
    public void insertRecordAfterResettingPK() {

        Sale sale = new Sale(1L, 2005, 343.22, 1504L,
                "0", null, null, 4, 13.12, "UP");
        var record = ctx.newRecord(SALE, sale);

        // reset the current ID and allow DB to generate one
        record.changed(SALE.SALE_ID, false);

        // resets both changed flag that tracks record changes and value
        // record.reset(SALE.SALE_ID); 
        System.out.println("EXAMPLE 10.1 (affected rows): "
                + record.insert()
        );

        SaleRecord sr = new SaleRecord();
        sr.from(sale);

        // resets both changed flag that tracks record changes and value
        sr.reset(SALE.SALE_ID);
        
        sr.setSaleId(++Sale_Id);
        System.out.println("EXAMPLE 10.2 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.valuesRow().fields())
                        .execute()
        );
    }

    // EXAMPLE 11    
    /*
    // 11.1
    insert into "CLASSICMODELS"."SALE" (
      "SALE_ID", "FISCAL_YEAR", "SALE", 
      "EMPLOYEE_NUMBER", "HOT", "RATE", 
      "VAT", "FISCAL_MONTH", "REVENUE_GROWTH", 
      "TREND"
    ) 
    values 
      (
        ("DBMS_RANDOM"."RANDOM" * ?), 
        ?, 
        round(?), 
        ?, 
        ?, 
        default, 
        default, 
        ?, 
        ?, 
        ?
      )
        
    // 11.2
    insert into "CLASSICMODELS"."SALE" (
      "SALE_ID", "FISCAL_YEAR", "SALE", 
      "EMPLOYEE_NUMBER", "HOT", "RATE", 
      "VAT", "FISCAL_MONTH", "REVENUE_GROWTH", 
      "TREND"
    ) 
    values 
      (
        ("DBMS_RANDOM"."RANDOM" * ?), 
        ?, 
        "CLASSICMODELS"."GET_TOTAL_SALES"(?), 
        ?, 
        ?, 
        default, 
        default, 
        ?, 
        ?, 
        ?
      )    
    */
    public void usingFunctionsInInsert() {
       
        System.out.println("EXAMPLE 11.1 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(rand().mul(2), 2004, round(212.23), 1504L,
                                "0", default_(), default_(), 4, 12.22, "UP")                        
                        .execute()
        );

        System.out.println("EXAMPLE 11.2 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(rand().mul(2), 2004, getTotalSales(2004), 1002L,
                                "0", default_(), default_(), 4, 12.22, "UP")                        
                        .execute()
        );
    }

    // EXAMPLE 12
    /*
    insert into "CLASSICMODELS"."DEPARTMENT" (
      "NAME", "PHONE", "CODE", "OFFICE_CODE"
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
        department.setCode(44);
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
    insert into "CLASSICMODELS"."ORDER" (
      "ORDER_DATE", "REQUIRED_DATE", "SHIPPED_DATE", 
      "STATUS", "COMMENTS", "CUSTOMER_NUMBER", 
      "AMOUNT"
    ) 
    select 
      cast(? as date), 
      cast(? as date), 
      cast(? as date), 
      ?, 
      ?, 
      ?, 
      ? 
    from 
      DUAL 
    where 
      not (
        exists (
          select 
            "CLASSICMODELS"."ORDER"."ORDER_ID", 
            "CLASSICMODELS"."ORDER"."ORDER_DATE", 
            "CLASSICMODELS"."ORDER"."REQUIRED_DATE", 
            "CLASSICMODELS"."ORDER"."SHIPPED_DATE", 
            "CLASSICMODELS"."ORDER"."STATUS", 
            "CLASSICMODELS"."ORDER"."COMMENTS", 
            "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER", 
            "CLASSICMODELS"."ORDER"."AMOUNT" 
          from 
            "CLASSICMODELS"."ORDER" 
          where 
            (
              (
                cast(? as date) between "CLASSICMODELS"."ORDER"."ORDER_DATE" 
                and "CLASSICMODELS"."ORDER"."SHIPPED_DATE" 
                or cast(? as date) between "CLASSICMODELS"."ORDER"."ORDER_DATE" 
                and "CLASSICMODELS"."ORDER"."SHIPPED_DATE"
              ) 
              and ? = "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER"
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
    insert into "CLASSICMODELS"."MANAGER" (
      "MANAGER_ID", "MANAGER_NAME", "MANAGER_DETAIL", 
      "MANAGER_EVALUATION"
    ) 
    values 
      (default, ?, default, ?)    
     */
    public void insertAndUDTRecord() {

        EvaluationCriteriaRecord ec = new EvaluationCriteriaRecord(55, 67, 34, 98);
        System.out.println("EXAMPLE 14 (affected rows): "
                + ctx.insertInto(MANAGER)
                        .values(default_(),
                                "Farel Ugg", default_(), ec)
                        .execute()
        );

        // creating a UDT record alternatives
        EvaluationCriteriaRecord r1 = EVALUATION_CRITERIA.newRecord();
        // r1.setCommunicationAbility(56); ...

        EvaluationCriteriaRecord r2 = ctx.newRecord(EVALUATION_CRITERIA);
        // r2.setCommunicationAbility(56); ...
    }
}
