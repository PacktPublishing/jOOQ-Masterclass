package com.classicmodels.repository;

import com.classicmodels.pojo.SalePart;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import static jooq.generated.Routines.getTotalSales;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.pojos.Sale;
import jooq.generated.tables.records.SaleRecord;
import static jooq.generated.udt.Locationtype.LOCATIONTYPE;
import jooq.generated.udt.records.LocationtypeRecord;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import static org.jooq.impl.DSL.rand;
import static org.jooq.impl.DSL.round;
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
    insert into "SYSTEM"."ORDER" (
      "ORDER_ID", "ORDER_DATE", "REQUIRED_DATE", 
      "SHIPPED_DATE", "STATUS", "COMMENTS", 
      "CUSTOMER_NUMBER"
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

        System.out.println("EXAMPLE 1.1 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(null, // primary key is auto-generated
                                LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped",
                                "New order inserted ...", 363L)
                        .execute()
        );

        System.out.println("EXAMPLE 1.2 (affected rows): "
                + // InsertValuesStep6<OrderRecord, String, LocalDate, LocalDate, LocalDate, String, Long>
                ctx.insertInto(ORDER, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER)
                        .values("New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27), "Shipped", 363L)
                        .execute()
        );

        System.out.println("EXAMPLE 1.3 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .columns(ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                                ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER)
                        .values("New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27), "Shipped", 363L)
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

        System.out.println("EXAMPLE 1.4 (affected rows): "
                + iq.execute()
        );
    }

    // EXAMPLE 2
    /*
    merge into "SYSTEM"."ORDER" using (
      select 
        1 "one" 
      from 
        dual
    ) on (
      (
        ("SYSTEM"."ORDER"."ORDER_ID", 1) = (
          (?, 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "ORDER_ID", "ORDER_DATE", "REQUIRED_DATE", 
      "SHIPPED_DATE", "STATUS", "COMMENTS", 
      "CUSTOMER_NUMBER"
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
    public void insertOrderManualKey() {

        System.out.println("EXAMPLE 2.1 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(Math.round(Math.random() * 1000), // random primary key
                                LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped",
                                "New order inserted ...", 363L)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 2.2 (affected rows): "
                + // InsertValuesStep7<OrderRecord, Long, String, LocalDate, LocalDate, LocalDate, String, Long>
                ctx.insertInto(ORDER, ORDER.ORDER_ID, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER)
                        .values(Math.round(Math.random() * 1000), // random primary key
                                "New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27), "Shipped", 363L)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 2.3 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .columns(ORDER.ORDER_ID, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                                ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER)
                        .values(Math.round(Math.random() * 1000), // random primary key
                                "New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27), "Shipped", 363L)
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    insert into "SYSTEM"."ORDER" (
      "ORDER_ID", "ORDER_DATE", "REQUIRED_DATE", 
      "SHIPPED_DATE", "STATUS", "COMMENTS", 
      "CUSTOMER_NUMBER"
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
      dual 
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
      dual 
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
      dual
     */
    public void insertMultipleOrderAutoGenKey() {

        System.out.println("EXAMPLE 3.1 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(null, // primary key is auto-generated
                                LocalDate.of(2004, 10, 22), LocalDate.of(2004, 10, 23),
                                LocalDate.of(2004, 10, 23), "Shipped",
                                "New order inserted ...", 363L)
                        .values(null,
                                LocalDate.of(2003, 12, 2), LocalDate.of(2003, 1, 3),
                                LocalDate.of(2003, 2, 26), "Resolved",
                                "Important order ...", 128L)
                        .values(null,
                                LocalDate.of(2005, 12, 12), LocalDate.of(2005, 12, 23),
                                LocalDate.of(2005, 12, 22), "On Hold",
                                "Order of client ...", 181L)
                        .execute()
        );

        System.out.println("EXAMPLE 3.2 (affected rows): "
                + // InsertValuesStep6<OrderRecord, String, LocalDate, LocalDate, LocalDate, String, Long>
                ctx.insertInto(ORDER, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER)
                        .values("New order inserted ...", LocalDate.of(2004, 10, 22),
                                LocalDate.of(2004, 10, 23), LocalDate.of(2004, 10, 23), "Shipped", 363L)
                        .values("Important order ...", LocalDate.of(2003, 12, 2),
                                LocalDate.of(2003, 1, 3), LocalDate.of(2003, 2, 26), "Resolved", 128L)
                        .values("Order of client ...", LocalDate.of(2005, 12, 12),
                                LocalDate.of(2005, 12, 23), LocalDate.of(2005, 12, 22), "On Hold", 181L)
                        .execute()
        );

        System.out.println("EXAMPLE 3.3 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .columns(ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                                ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER)
                        .values("New order inserted ...", LocalDate.of(2004, 10, 22),
                                LocalDate.of(2004, 10, 23), LocalDate.of(2004, 10, 23), "Shipped", 363L)
                        .values("Important order ...", LocalDate.of(2003, 12, 2),
                                LocalDate.of(2003, 1, 3), LocalDate.of(2003, 2, 26), "Resolved", 128L)
                        .values("Order of client ...", LocalDate.of(2005, 12, 12),
                                LocalDate.of(2005, 12, 23), LocalDate.of(2005, 12, 22), "On Hold", 181L)
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    merge into "SYSTEM"."ORDER" using (
      (
        (
          select 
            null "ORDER_ID", 
            null "ORDER_DATE", 
            null "REQUIRED_DATE", 
            null "SHIPPED_DATE", 
            null "STATUS", 
            null "COMMENTS", 
            null "CUSTOMER_NUMBER" 
          from 
            dual 
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
              ? 
            from 
              dual 
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
              dual 
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
              dual
          )
      )
    ) "t" on (
      ("SYSTEM"."ORDER"."ORDER_ID", 1) = (
        ("t"."ORDER_ID", 1)
      )
    ) when not matched then insert (
      "ORDER_ID", "ORDER_DATE", "REQUIRED_DATE", 
      "SHIPPED_DATE", "STATUS", "COMMENTS", 
      "CUSTOMER_NUMBER"
    ) 
    values 
      (
        "t"."ORDER_ID", "t"."ORDER_DATE", 
        "t"."REQUIRED_DATE", "t"."SHIPPED_DATE", 
        "t"."STATUS", "t"."COMMENTS", "t"."CUSTOMER_NUMBER"
      )
     */
    public void insertMultipleOrderManualKey() {

        System.out.println("EXAMPLE 4.1 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .values(Math.round(Math.random() * 100),
                                LocalDate.of(2004, 10, 22), LocalDate.of(2004, 10, 23),
                                LocalDate.of(2004, 10, 23), "Shipped",
                                "New order inserted ...", 363L)
                        .values(Math.round(Math.random() * 1000),
                                LocalDate.of(2003, 12, 2), LocalDate.of(2003, 1, 3),
                                LocalDate.of(2003, 2, 26), "Resolved",
                                "Important order ...", 128L)
                        .values(Math.round(Math.random() * 10000),
                                LocalDate.of(2005, 12, 12), LocalDate.of(2005, 12, 23),
                                LocalDate.of(2005, 12, 22), "On Hold",
                                "Order of client ...", 181L)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 4.2 (affected rows): "
                + // InsertValuesStep6<OrderRecord, String, LocalDate, LocalDate, LocalDate, String, Long>
                ctx.insertInto(ORDER, ORDER.ORDER_ID, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER)
                        .values(Math.round(Math.random() * 100),
                                "New order inserted ...", LocalDate.of(2004, 10, 22),
                                LocalDate.of(2004, 10, 23), LocalDate.of(2004, 10, 23), "Shipped", 363L)
                        .values(Math.round(Math.random() * 1000),
                                "Important order ...", LocalDate.of(2003, 12, 2),
                                LocalDate.of(2003, 1, 3), LocalDate.of(2003, 2, 26), "Resolved", 128L)
                        .values(Math.round(Math.random() * 10000),
                                "Order of client ...", LocalDate.of(2005, 12, 12),
                                LocalDate.of(2005, 12, 23), LocalDate.of(2005, 12, 22), "On Hold", 181L)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 4.3 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .columns(ORDER.ORDER_ID, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                                ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER)
                        .values(Math.round(Math.random() * 100),
                                "New order inserted ...", LocalDate.of(2004, 10, 22),
                                LocalDate.of(2004, 10, 23), LocalDate.of(2004, 10, 23), "Shipped", 363L)
                        .values(Math.round(Math.random() * 1000),
                                "Important order ...", LocalDate.of(2003, 12, 2),
                                LocalDate.of(2003, 1, 3), LocalDate.of(2003, 2, 26), "Resolved", 128L)
                        .values(Math.round(Math.random() * 10000),
                                "Order of client ...", LocalDate.of(2005, 12, 12),
                                LocalDate.of(2005, 12, 23), LocalDate.of(2005, 12, 22), "On Hold", 181L)
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 5
    /*
    merge into "SYSTEM"."PAYMENT" using (
      select 
        1 "one" 
      from 
        dual
    ) on (
      (
        (
          (
            "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER", 
            1
          ) = (
            (?, 1)
          ) 
          or 1 = 0
        ) 
        and (
          (
            "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
            1
          ) = (
            (?, 1)
          ) 
          or 1 = 0
        )
      )
    ) when not matched then insert (
      "CUSTOMER_NUMBER", "CHECK_NUMBER", 
      "CACHING_DATE", "PAYMENT_DATE", 
      "INVOICE_AMOUNT"
    ) 
    values 
      (?, ?, ?, ?, ?)
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
    insert into "SYSTEM"."SALE" (
      "SALE_ID", "FISCAL_YEAR", "SALE", 
      "EMPLOYEE_NUMBER"
    ) 
    values 
      (?, ?, ?, ?)
     */
    public void insertOneSaleRecord() {

        System.out.println("EXAMPLE 6.1 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(
                                new SaleRecord()
                                        .value1(null)
                                        .value2(BigInteger.valueOf(2005))
                                        .value3(3223.12)
                                        .value4(1504L)
                                        .valuesRow().fields()
                        )
                        .execute()
        );

        System.out.println("EXAMPLE 6.2 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(new SaleRecord()
                                .values(null, BigInteger.valueOf(2004), 143.31, 1370L)
                                .valuesRow().fields())
                        .execute()
        );

        /* create a SaleRecord via constructor */
        // SaleRecord sr = new SaleRecord(null, 2003, 3443.22, 1370L); // Record4<Long, Integer, Double, Long>
        /* or, creare a SaleRecord via constructor and setters */
        SaleRecord sr = new SaleRecord();
        sr.setFiscalYear(BigInteger.valueOf(2003));         // or, sr.set(SALE.FISCAL_YEAR, BigInteger.valueOf(2003));
        sr.setSale(3443.22);                                // or, sr.set(SALE.SALE_, 3443.22);        
        sr.setEmployeeNumber(1370L);                        // or, sr.set(SALE.EMPLOYEE_NUMBER, 1370L);                   

        System.out.println("EXAMPLE 6.3 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.valuesRow().fields())
                        .execute()
        );

        System.out.println("EXAMPLE 6.4 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.getSaleId(), sr.getFiscalYear(), sr.getSale(), sr.getEmployeeNumber())
                        .execute()
        );

        System.out.println("EXAMPLE 6.5 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.value1(), sr.value2(), sr.value3(), sr.value4())
                        .execute()
        );

        System.out.println("EXAMPLE 6.6 (affected rows): "
                + ctx.insertInto(SALE, sr.field4(), sr.field3(), sr.field2(), sr.field1())
                        .values(sr.value4(), sr.value3(), sr.value2(), sr.value1())
                        .execute()
        );

        System.out.println("EXAMPLE 6.7 (affected rows): "
                + ctx.insertInto(SALE, sr.fields())
                        .values(sr.valuesRow().fields())
                        .execute()
        );

        System.out.println("EXAMPLE 6.8 (affected rows): "
                + ctx.insertInto(SALE, sr.fieldsRow().fields())
                        .values(sr.valuesRow().fields())
                        .execute()
        );
    }

    // EXAMPLE 7
    /*
    insert into "SYSTEM"."SALE" (
      "SALE_ID", "FISCAL_YEAR", "SALE", 
      "EMPLOYEE_NUMBER"
    ) 
    select 
      ?, 
      ?, 
      ?, 
      ? 
    from 
      dual 
    union all 
    select 
      ?, 
      ?, 
      ?, 
      ? 
    from 
      dual
     */
    public void insertTwoSaleRecord() {

        // Record4<Long, Integer, Double, Long>
        SaleRecord sr1 = new SaleRecord(null, BigInteger.valueOf(2003), 3443.22, 1370L);
        SaleRecord sr2 = new SaleRecord(null, BigInteger.valueOf(2005), 1221.12, 1504L);

        System.out.println("EXAMPLE 7 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr1.valuesRow().fields())
                        .values(sr2.valuesRow().fields())
                        .execute()
        );
    }

    // EXAMPLE 8  
    /*
    insert into "SYSTEM"."SALE" (
      "SALE_ID", "FISCAL_YEAR", "SALE", 
      "EMPLOYEE_NUMBER"
    ) 
    select 
      ?, 
      ?, 
      ?, 
      ? 
    from 
      dual 
    union all 
    select 
      ?, 
      ?, 
      ?, 
      ? 
    from 
      dual 
    union all 
    select 
      ?, 
      ?, 
      ?, 
      ? 
    from 
      dual
     */
    public void insertCollectionOfSaleRecord() {

        // consider this collection of SaleRecord
        Collection<SaleRecord> listOfRecord
                = List.of(new SaleRecord(null, BigInteger.valueOf(2003), 3443.22, 1370L),
                        new SaleRecord(null, BigInteger.valueOf(2005), 1221.12, 1504L),
                        new SaleRecord(null, BigInteger.valueOf(2005), 1221.12, 1504L));

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
        // InsertValuesStep4<SaleRecord, Long, Integer, Double, Long>
        var insert2 = ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER);
        for (SaleRecord sr : listOfRecord) {
            insert2.values(sr.getSaleId(), sr.getFiscalYear(), sr.getSale(), sr.getEmployeeNumber());
        }
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
                            return s.execute();
                        })
        );

        // jOOQ 3.15 (valuesOfRows(), valuesOfRecords()) 
        // https://github.com/jOOQ/jOOQ/issues/6604
        // This example is WIP
    }

    // EXAMPLE 9
    /*
    insert into SYSTEM."SALE" (
      "FISCAL_YEAR", "SALE", "EMPLOYEE_NUMBER"
    ) 
    values 
      (?, ?, ?)
     */
    public void insertNewRecord() {

        System.out.println("EXAMPLE 9.1 (affected rows): "
                + ctx.newRecord(SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                        .values(BigInteger.valueOf(2004), 1233.2, 1370L)
                        .into(SALE)
                        .insert()
        );

        // This is the Sale POJO generated by jOOQ
        Sale sale = new Sale(null, BigInteger.valueOf(2005), 343.22, 1504L);
        System.out.println("EXAMPLE 9.2 (affected rows): "
                + ctx.newRecord(SALE, sale).insert()
        );

        SaleRecord sr = new SaleRecord();
        sr.from(sale);
        System.out.println("EXAMPLE 9.3 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.valuesRow().fields())
                        .execute()
        );

        // this is user-define SalePart POJO (it contains only a part of fields)
        SalePart salePart = new SalePart(5644.32, 1370L);
        System.out.println("EXAMPLE 9.4 (affected rows): "
                + ctx.newRecord(SALE, SALE.fields())
                        .values(null, BigInteger.valueOf(2004),
                                salePart.getSale(), salePart.getEmployeeNumber())
                        .insert()
        );

        SaleRecord srp = new SaleRecord();
        srp.from(salePart);                          // get the available fields from SalePart
        srp.setFiscalYear(BigInteger.valueOf(2004)); // fiscal_year cannot be null and doesn't have a default value
        System.out.println("EXAMPLE 9.5 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(srp.valuesRow().fields())
                        .execute()
        );
        
        System.out.println("EXAMPLE 9.6 (affected rows): "
                + ctx.executeInsert(srp)
        );
    }

    // EXAMPLE 10
    /*
    insert into SYSTEM."SALE" (
      "FISCAL_YEAR", "SALE", "EMPLOYEE_NUMBER"
    ) 
    values 
      (?, ?, ?)
     */
    public void insertRecordAfterResettingPK() {

        Sale sale = new Sale(BigInteger.ONE, BigInteger.valueOf(2005), 343.22, 1504L);
        var record = ctx.newRecord(SALE, sale);
        record.reset(SALE.SALE_ID); // reset the current ID and allow DB to generate one
        System.out.println("EXAMPLE 10.1 (affected rows): "
                + record.insert()
        );

        SaleRecord sr = new SaleRecord();
        sr.from(sale);
        sr.reset(SALE.SALE_ID); // reset the current ID and allow DB to generate one        
        System.out.println("EXAMPLE 10.2 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(sr.valuesRow().fields())
                        .execute()
        );
    }

    // EXAMPLE 11    
    public void usingFunctionsInInsert() {

        /*
        merge into "SYSTEM"."SALE" using (
          select 
            1 "one" 
          from 
            dual
        ) on (
          (
            ("SYSTEM"."SALE"."SALE_ID", 1) = (
              (
                ("DBMS_RANDOM"."RANDOM" * ?), 
                1
              )
            ) 
            or 1 = 0
          )
        ) when not matched then insert (
          "SALE_ID", "FISCAL_YEAR", "SALE", 
          "EMPLOYEE_NUMBER"
        ) 
        values 
          (
            ("DBMS_RANDOM"."RANDOM" * ?), 
            ?, 
            round(?), 
            ?
          )
         */
        System.out.println("EXAMPLE 11.1 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(rand().mul(1000), 2004, round(21112.23), 1504L)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        /*
        merge into "SYSTEM"."SALE" using (
          select 
            1 "one" 
          from 
            dual
        ) on (
          (
            ("SYSTEM"."SALE"."SALE_ID", 1) = (
              (
                ("DBMS_RANDOM"."RANDOM" * ?), 
                1
              )
            ) 
            or 1 = 0
          )
        ) when not matched then insert (
          "SALE_ID", "FISCAL_YEAR", "SALE", 
          "EMPLOYEE_NUMBER"
        ) 
        values 
          (
            ("DBMS_RANDOM"."RANDOM" * ?), 
            ?, 
            "SYSTEM"."GET_TOTAL_SALES"(?), 
            ?
          )
         */
        System.out.println("EXAMPLE 11.2 (affected rows): "
                + ctx.insertInto(SALE)
                        .values(rand().mul(1000), 2004, getTotalSales(2004), 1002L)
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 12
    /*
    merge into "SYSTEM"."OFFICE" using (
      select 
        1 "one" 
      from 
        dual
    ) on (
      (
        (
          "SYSTEM"."OFFICE"."OFFICE_CODE", 
          1
        ) = (
          (?, 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "OFFICE_CODE", "LOCATION", "PHONE", 
      "ADDRESS_LINE_FIRST", "ADDRESS_LINE_SECOND", 
      "POSTAL_CODE", "TERRITORY"
    ) 
    values 
      (?, ?, ?, ?, ?, ?, ?)
     */
    public void insertAndUDTRecord() {

        LocationtypeRecord locationtypeRecord = new LocationtypeRecord("Boston", "USA", "EA");
        System.out.println("EXAMPLE 12 (affected rows): "
                + ctx.insertInto(OFFICE)
                        .values(Math.round(Math.random() * 10000) + "PK",
                                locationtypeRecord, "+33 223 12", "addr1", "addr2", "659422", "MA")
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        // creating a UDT record alternatives
        LocationtypeRecord r1 = LOCATIONTYPE.newRecord();
        //r1.setCity("Boston"); ...

        LocationtypeRecord r2 = ctx.newRecord(LOCATIONTYPE);
        //r2.setCity("Boston"); ...
    }
}