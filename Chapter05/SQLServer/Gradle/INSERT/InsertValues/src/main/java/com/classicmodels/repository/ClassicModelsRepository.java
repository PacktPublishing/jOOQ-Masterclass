package com.classicmodels.repository;

import com.classicmodels.pojo.SalePart;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import static jooq.generated.Routines.salePrice;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Office.OFFICE;
import jooq.generated.tables.Order;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.pojos.Department;
import jooq.generated.tables.pojos.Office;
import jooq.generated.tables.pojos.Sale;
import jooq.generated.tables.records.OfficeRecord;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Row2;
import static org.jooq.Rows.toRowArray;
import static org.jooq.Rows.toRowList;
import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.default_;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.upper;
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
    /*       
    insert into [classicmodels].[dbo].[order] (
      [comments], [order_date], [required_date], 
      [shipped_date], [status], [customer_number], 
      [amount]
    ) 
    values 
      (?, ?, ?, ?, ?, ?, ?)           
    */    
    public void insertOrderAutoGenKey() {                
        
        System.out.println("EXAMPLE 1.1 (affected rows): "
                + // InsertValuesStep7<OrderRecord, String, LocalDate, LocalDate, LocalDate, String, Long, BigDecimal>
                ctx.insertInto(ORDER, ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                        ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values("New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27),
                                "Shipped", 363L, BigDecimal.valueOf(414.44))
                        .execute()
        );

        System.out.println("EXAMPLE 1.2 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .columns(ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
                                ORDER.SHIPPED_DATE, ORDER.STATUS, ORDER.CUSTOMER_NUMBER, ORDER.AMOUNT)
                        .values("New order inserted ...", LocalDate.of(2003, 2, 12),
                                LocalDate.of(2003, 3, 1), LocalDate.of(2003, 2, 27),
                                "Shipped", 363L, BigDecimal.valueOf(414.44))
                        .execute()
        );
    }

    // EXAMPLE 2
    /*
    // 2.1
    // Consider visiting: https://github.com/jOOQ/jOOQ/issues/1818
    
    SET IDENTITY_INSERT [order] ON

    insert into [classicmodels].[dbo].[order] (
      [order_id], [order_date], [required_date], 
      [shipped_date], [status], [comments], 
      [customer_number], [amount]
    ) 
    values 
      (
        6518554, 
        cast('2003-02-12' as date), 
        cast('2003-03-01' as date), 
        cast('2003-02-27' as date), 
       'Shipped', 
       'New order inserted ...', 
        363, 
        414.44
      )
                
    SET IDENTITY_INSERT [order] OFF
    
    // 2.2.1, 2.4.1, 2.4.2
    merge into [classicmodels].[dbo].[office] using (
      select 
        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
    ) [t] (
      [office_code], [city], [phone], [address_line_first], 
      [address_line_second], [state], 
      [country], [postal_code], [territory], 
      [location], [internal_budget]
    ) on (
      [classicmodels].[dbo].[office].[office_code] = [t].[office_code] 
      or [classicmodels].[dbo].[office].[postal_code] = [t].[postal_code]
    ) when not matched then insert (
      [office_code], [city], [phone], [address_line_first], 
      [address_line_second], [state], 
      [country], [postal_code], [territory], 
      [location], [internal_budget]
    ) 
    values 
      (
        [t].[office_code], [t].[city], [t].[phone], 
        [t].[address_line_first], [t].[address_line_second], 
        [t].[state], [t].[country], [t].[postal_code], 
        [t].[territory], [t].[location], 
        [t].[internal_budget]
      );
    
    // 2.2.2, 2.3.1, 2.3.2
    insert into [classicmodels].[dbo].[office] (
      [office_code], [city], [phone], [address_line_first], 
      [address_line_second], [state], 
      [country], [postal_code], [territory], 
      [location], [internal_budget]
    ) 
    values 
      (?, ?, ?, ?, ?, ?, ?, ?, ?, default, ?)        
     */
    public void insertOrderManualKey() {
        
        Query q1 = ctx.query("SET IDENTITY_INSERT [order] ON");
        Query q2 = ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                .values(++Order_Id, // computed primary key
                        LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                        LocalDate.of(2003, 2, 27), "Shipped",
                        "New order inserted ...", 363L, 414.44);
        Query q3 = ctx.query("SET IDENTITY_INSERT [order] OFF");

        System.out.println("EXAMPLE 2.1 (affected rows): "
                + Arrays.toString(ctx.batch(q1, q2, q3).execute())
        );

        System.out.println("EXAMPLE 2.2.1 (affected rows): "
                + ctx.insertInto(OFFICE) // InsertSetStep<OfficeRecord>
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", 
                                UUID.randomUUID().toString().substring(0,14), // random postal_code 
                                "H", null, 0)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 2.2.2 (affected rows): "
                + ctx.insertInto(OFFICE) // InsertSetStep<OfficeRecord>
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", 
                                UUID.randomUUID().toString().substring(0,14), // random postal_code 
                                "H", default_(), 0)
                        .execute()
        );

        System.out.println("EXAMPLE 2.3.1 (affected rows): "
                + // InsertValuesStep11<OfficeRecord, String, String, String, String, String, String, String, String, String, Geometry, Integer>
                ctx.insertInto(OFFICE, OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                        OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE,
                        OFFICE.TERRITORY, OFFICE.LOCATION, OFFICE.INTERNAL_BUDGET)
                        .values(val(UUID.randomUUID().toString().substring(0,10)), // random office_code 
                                val("Banesti"), val("+40 323 421"), val("addr1"), val("addr2"), val("PH"), val("RO"), 
                                val(UUID.randomUUID().toString().substring(0,14)), // random postal_code
                                val("H"), default_(OFFICE.LOCATION), val(0))
                        .execute()
        );

        System.out.println("EXAMPLE 2.3.2 (affected rows): "
                + // InsertValuesStep11<OfficeRecord, String, String, String, String, String, String, String, String, String, Geometry, Integer>
                ctx.insertInto(OFFICE)
                        .columns(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                                OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE,
                                OFFICE.TERRITORY, OFFICE.LOCATION, OFFICE.INTERNAL_BUDGET)
                        .values(val(UUID.randomUUID().toString().substring(0,10)), // random office_code 
                                val("Banesti"), val("+40 323 421"), val("addr1"), val("addr2"), val("PH"), val("RO"), 
                                val(UUID.randomUUID().toString().substring(0,14)), // random postal_code
                                val("H"), default_(OFFICE.LOCATION), val(0))
                        .execute()
        );

        System.out.println("EXAMPLE 2.4.1 (affected rows): "
                + // InsertValuesStep11<OfficeRecord, String, String, String, String, String, String, String, String, String, Geometry, Integer>
                ctx.insertInto(OFFICE, OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                        OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE,
                        OFFICE.TERRITORY, OFFICE.LOCATION, OFFICE.INTERNAL_BUDGET)
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", 
                                UUID.randomUUID().toString().substring(0,14), // random postal_code
                                "H", null, 0)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 2.4.2 (affected rows): "
                + // InsertValuesStep11<OfficeRecord, String, String, String, String, String, String, String, String, String, Geometry, Integer>
                ctx.insertInto(OFFICE)
                        .columns(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                                OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE,
                                OFFICE.TERRITORY, OFFICE.LOCATION, OFFICE.INTERNAL_BUDGET)
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", 
                                UUID.randomUUID().toString().substring(0,14), // random postal_code
                                "H", null, 0)
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    insert into [classicmodels].[dbo].[order] (
      [comments], [order_date], [required_date], 
      [shipped_date], [status], [customer_number], 
      [amount]
    ) 
    values 
      (
        ?, ?, ?, ?, ?, ?, 
        cast(
          ? as numeric(10, 2)
        )
      ), 
      (
        ?, ?, ?, ?, ?, ?, 
        cast(
          ? as numeric(10, 2)
        )
      ), 
      (
        ?, ?, ?, ?, ?, ?, 
        cast(
          ? as numeric(10, 2)
        )
      )    
     */
    public void insertMultipleOrderAutoGenKey() {

        System.out.println("EXAMPLE 3.1 (affected rows): "
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
                        .execute()
        );

        System.out.println("EXAMPLE 3.2 (affected rows): "
                + ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                        .columns(ORDER.COMMENTS, ORDER.ORDER_DATE, ORDER.REQUIRED_DATE,
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
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    // 4.1
    insert into [classicmodels].[dbo].[office] (
      [office_code], [city], [phone], [address_line_first], 
      [address_line_second], [state], 
      [country], [postal_code], [territory], 
      [location], [internal_budget]
    ) 
    values 
      (?, ?, ?, ?, ?, ?, ?, ?, ?, default, ?), 
      (?, ?, ?, ?, ?, ?, ?, ?, ?, default, ?), 
      (?, ?, ?, ?, ?, ?, ?, ?, ?, default, ?)    
    
    // 4.2, 4.3
    merge into [classicmodels].[dbo].[office] using (
      select 
        ?, ?, ?, ?, ?, ?, ?, ?, ?, ? 
      union all 
      select 
        ?, ?, ?, ?, ?, ?, ?, ?, ?, ? 
      union all 
      select 
        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
    ) [t] (
      [office_code], [city], [phone], [address_line_first], 
      [address_line_second], [state], 
      [country], [postal_code], [territory], 
      [internal_budget]
    ) on (
      [classicmodels].[dbo].[office].[office_code] = [t].[office_code] 
      or [classicmodels].[dbo].[office].[postal_code] = [t].[postal_code]
    ) when not matched then insert (
      [office_code], [city], [phone], [address_line_first], 
      [address_line_second], [state], 
      [country], [postal_code], [territory], 
      [internal_budget]
    ) 
    values 
      (
        [t].[office_code], [t].[city], [t].[phone], 
        [t].[address_line_first], [t].[address_line_second], 
        [t].[state], [t].[country], [t].[postal_code], 
        [t].[territory], [t].[internal_budget]
      );    
     */
    public void insertMultipleOrderManualKey() {

        System.out.println("EXAMPLE 4.1 (affected rows): "
                + ctx.insertInto(OFFICE) // InsertSetStep<OfficeRecord>
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", 
                                UUID.randomUUID().toString().substring(0,14), // random postal_code
                                "H", default_(), 89.87)
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Campina", "+41 212 333", "addr1", "addr2", "DB", "RO", 
                                UUID.randomUUID().toString().substring(0,14), // random postal_code
                                "M", default_(), 45.53)
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Ploiesti", "+43 22222", "addr1", "addr2", "CO", "RO",
                                UUID.randomUUID().toString().substring(0,14), // random postal_code
                                "UU", default_(), 21.23)
                        .execute()
        );

        System.out.println("EXAMPLE 4.2 (affected rows): "
                + // InsertValuesStep10<OfficeRecord, String, String, String, String, String, String, String, String, String, Integer>
                ctx.insertInto(OFFICE, OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE,
                        OFFICE.ADDRESS_LINE_FIRST, OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE,
                        OFFICE.COUNTRY, OFFICE.POSTAL_CODE, OFFICE.TERRITORY, OFFICE.INTERNAL_BUDGET)
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", 
                                UUID.randomUUID().toString().substring(0,14), // random postal_code
                                "H", 0)
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Campina", "+41 212 333", "addr1", "addr2", "DB", "RO", 
                                UUID.randomUUID().toString().substring(0,14), // random postal_code
                                "M", 0)
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Ploiesti", "+43 22222", "addr1", "addr2", "CO", "RO", 
                                UUID.randomUUID().toString().substring(0,14), // random postal_code
                                "UU", 0)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 4.3 (affected rows): "
                + // InsertValuesStep10<OfficeRecord, String, String, String, String, String, String, String, String, String, Integer>
                ctx.insertInto(OFFICE)
                        .columns(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                                OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE,
                                OFFICE.TERRITORY, OFFICE.INTERNAL_BUDGET)
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", 
                                UUID.randomUUID().toString().substring(0,14), // random postal_code
                                "H", 0)
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Campina", "+41 212 333", "addr1", "addr2", "DB", "RO", 
                                UUID.randomUUID().toString().substring(0,14), // random postal_code
                                "M", 0)
                        .values(UUID.randomUUID().toString().substring(0,10), // random office_code 
                                "Ploiesti", "+43 22222", "addr1", "addr2", "CO", "RO", 
                                UUID.randomUUID().toString().substring(0,14), // random postal_code
                                "UU", 0)
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 5
    /*
    merge into [classicmodels].[dbo].[payment] using (
      select 
        ?, ?, ?, ?, ?
    ) [t] (
      [customer_number], [check_number], 
      [caching_date], [payment_date], 
      [invoice_amount]
    ) on (
      (
        [classicmodels].[dbo].[payment].[customer_number] = [t].[customer_number] 
        and [classicmodels].[dbo].[payment].[check_number] = [t].[check_number]
      ) 
      or [classicmodels].[dbo].[payment].[check_number] = [t].[check_number]
    ) when not matched then insert (
      [customer_number], [check_number], 
      [caching_date], [payment_date], 
      [invoice_amount]
    ) 
    values 
      (
        [t].[customer_number], [t].[check_number], 
        [t].[caching_date], [t].[payment_date], 
        [t].[invoice_amount]
      );   
     */
    public void insertPaymentCompositeKey() {

        System.out.println("EXAMPLE 5 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .columns(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER, // composite primary key
                                PAYMENT.CACHING_DATE, PAYMENT.PAYMENT_DATE, PAYMENT.INVOICE_AMOUNT)
                        .values(100L, "HQ336338",
                                LocalDateTime.of(2004, 11, 17, 12, 30, 15),
                                LocalDateTime.of(2004, 10, 19, 12, 30, 15),
                                BigDecimal.valueOf(5433.22))
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 6
    /*    
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [fiscal_month], [revenue_growth]
    ) 
    values 
      (?, ?, ?, ?, ?)    
     */
    public void insertOneSaleRecord() {

        /* create a SaleRecord via constructor */
        // SaleRecord sr = new SaleRecord(null, 2003, 3443.22, 1370L,
        //       false, null, null, 3, 14.55, "UP");         
        /* or, creare a SaleRecord via constructor and setters */
        SaleRecord sr = new SaleRecord();
        sr.setFiscalYear(2003);         // or, sr.set(SALE.FISCAL_YEAR, 2003);
        sr.setSale(3443.22);            // or, sr.set(SALE.SALE_, 3443.22);        
        sr.setEmployeeNumber(1370L);    // or, sr.set(SALE.EMPLOYEE_NUMBER, 1370L);  
        sr.setFiscalMonth(3);           // or, sr.set(SALE.FISCAL_MONTH, 3);
        sr.setRevenueGrowth(14.55);     // or, sr.set(SALE.REVENUE_GROWTH, 14.55)

        System.out.println("EXAMPLE 6.1 (affected rows): "
                + ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_,
                        SALE.EMPLOYEE_NUMBER, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(sr.getFiscalYear(), sr.getSale(), sr.getEmployeeNumber(),
                                sr.getFiscalMonth(), sr.getRevenueGrowth())
                        .execute()
        );

        System.out.println("EXAMPLE 6.2 (affected rows): "
                + ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_,
                        SALE.EMPLOYEE_NUMBER, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(sr.value2(), sr.value3(), sr.value4(), sr.value8(), sr.value9())
                        .execute()
        );

        System.out.println("EXAMPLE 6.3 (affected rows): "
                + ctx.insertInto(SALE, sr.field9(), sr.field8(), sr.field4(), sr.field3(), sr.field2())
                        .values(sr.value9(), sr.value8(), sr.value4(), sr.value3(), sr.value2())
                        .execute()
        );
    }

    // EXAMPLE 7
    /*
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [fiscal_month], [revenue_growth]
    ) 
    values 
      (?, ?, ?, ?, ?), 
      (?, ?, ?, ?, ?)   
     */
    public void insertTwoSaleRecord() {

        // Record10<Long, Integer, Double, Long, Boolean, String, String, Integer, Double, String>
        SaleRecord sr1 = new SaleRecord(null, 2003, 3443.22, 1370L,
                false, null, null, 3, 14.55, "UP");
        SaleRecord sr2 = new SaleRecord(null, 2005, 1221.12, 1504L,
                false, null, null, 5, 22.11, "UP");

        System.out.println("EXAMPLE 7 (affected rows): "
                + ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_,
                        SALE.EMPLOYEE_NUMBER, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(sr1.getFiscalYear(), sr1.getSale(), sr1.getEmployeeNumber(),
                                sr1.getFiscalMonth(), sr1.getRevenueGrowth())
                        .values(sr2.getFiscalYear(), sr2.getSale(), sr2.getEmployeeNumber(),
                                sr2.getFiscalMonth(), sr2.getRevenueGrowth())
                        .execute()
        );
    }

    // EXAMPLE 8  
    /*
    // 8.1, 8.2
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [fiscal_month], [revenue_growth]
    ) 
    values 
      (?, ?, ?, ?, ?), 
      (?, ?, ?, ?, ?), 
      (?, ?, ?, ?, ?)    
    
    // 8.3.1
    SET IDENTITY_INSERT [sale] ON
    insert into [classicmodels].[dbo].[sale] (
      [sale_id], [fiscal_year], [sale], 
      [employee_number], [hot], [rate], 
      [vat], [fiscal_month], [revenue_growth], 
      [trend]
    ) 
    values 
      (
        1952, 2003, 3.44322E3, 1370, 0, null, 
        null, 3, 1.455E1, 'UP'
      ), 
      (
        1953, 2005, 1.22112E3, 1504, 1, null, 
        null, 5, 2.211E1, 'UP'
      ), 
      (
        1954, 2005, 1.22112E3, 1504, 0, null, 
        null, 7, 6.559E1, 'DOWN'
      )    
    SET IDENTITY_INSERT [sale] OFF
    
    // 8.3.2
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [rate], [vat], [fiscal_month], [revenue_growth]
    ) 
    values 
      (?, ?, ?, ?, ?, ?, ?), 
      (?, ?, ?, ?, ?, ?, ?), 
      (?, ?, ?, ?, ?, ?, ?)
    
    // 8.3.3
    SET IDENTITY_INSERT [sale] ON
    insert into [classicmodels].[dbo].[sale] (
      [sale_id], [fiscal_year], [sale], 
      [employee_number], [hot], [rate], 
      [vat], [fiscal_month], [revenue_growth], 
      [trend]
    ) 
    values 
      (
        1959, 2003, 3.44322E3, 1370, null, 'SILVER', 
        'MAX', 3, 1.455E1, null
      ), 
      (
        1964, 2005, 1.22112E3, 1504, null, 'SILVER', 
        'MAX', 5, 2.211E1, 'UP'
      ), 
      (
        1969, 2005, 1.22112E3, 1504, 1, 'SILVER', 
        'MAX', 7, 6.559E1, null
      )
      SET IDENTITY_INSERT [sale] OFF
     */
    public void insertCollectionOfSaleRecord() {

        // consider this collection of SaleRecord
        List<SaleRecord> listOfRecord
                = List.of(new SaleRecord(++Sale_Id, 2003, 3443.22, 1370L,
                        false, null, null, 3, 14.55, "UP"),
                        new SaleRecord(++Sale_Id, 2005, 1221.12, 1504L,
                                true, null, null, 5, 22.11, "UP"),
                        new SaleRecord(++Sale_Id, 2005, 1221.12, 1504L,
                                false, null, null, 7, 65.59, "DOWN"));

        /* First Approach */
        // InsertValuesStepN<SaleRecord>
        
        // add new ids
        listOfRecord.forEach(r -> r.setSaleId(++Sale_Id));
        
        var insert1 = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_,
                SALE.EMPLOYEE_NUMBER, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH);
        for (SaleRecord sr : listOfRecord) {
            insert1.values(sr.getFiscalYear(), sr.getSale(), sr.getEmployeeNumber(),
                    sr.getFiscalMonth(), sr.getRevenueGrowth());
        }
        System.out.println("EXAMPLE 8.1 (affected rows): "
                + insert1.execute()
        );

        /* Third Approach (inspired from https://github.com/jOOQ/jOOQ/issues/6604) */
        System.out.println("EXAMPLE 8.2 (affected rows): "
                + Optional.of(ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_,
                        SALE.EMPLOYEE_NUMBER, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH))
                        .map(s -> {
                            listOfRecord.forEach((sr) -> {
                                s.values(sr.getFiscalYear(), sr.getSale(), sr.getEmployeeNumber(),
                                        sr.getFiscalMonth(), sr.getRevenueGrowth());
                            });
                            return s.execute();
                        })
        );

        // jOOQ 3.15 (valuesOfRows(), valuesOfRecords()) 
        // https://github.com/jOOQ/jOOQ/issues/6604       
        Query q1 = ctx.query("SET IDENTITY_INSERT [sale] ON");
        Query q2 = ctx.insertInto(SALE, SALE.fields())
                .valuesOfRecords(listOfRecord);
        Query q3 = ctx.query("SET IDENTITY_INSERT [sale] OFF");

        System.out.println("EXAMPLE 8.3.1 (affected rows): "
                + Arrays.toString(ctx.batch(q1, q2, q3).execute()));

        // List<Row7<Integer, Double, Long, String, String, Integer, Double>>
        var listOfRows
                = List.of(row(2003, 3443.22, 1370L, "SILVER", "NONE", 3, 14.55),
                        row(2005, 1221.12, 1504L, "GOLD", "MAX", 5, 22.11),
                        row(2005, 1221.12, 1504L, "SILVER", "MIN", 7, 65.59));

        System.out.println("EXAMPLE 8.3.2 (affected rows): "
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
                        new Sale(Sale_Id+=5, 2003, 3443.22, 1370L,
                                null, "SILVER", "MAX", 3, 14.55, null),
                        new Sale(Sale_Id+=5, 2005, 1221.12, 1504L,
                                null, "SILVER", "MAX", 5, 22.11, "UP"),
                        new Sale(Sale_Id+=5, 2005, 1221.12, 1504L,
                                true, "SILVER", "MAX", 7, 65.59, null));

        //List<Row10<Long, Integer, Double, Long, Boolean, String, String, Integer, Double, String>>
        var listOfSales
                = sales.stream().collect(toRowList(
                        i -> val(i.getSaleId()), i -> val(i.getFiscalYear()), i -> val(i.getSale()),
                        i -> val(i.getEmployeeNumber()), i -> val(i.getHot()), i -> val(i.getRate()),
                        i -> val(i.getVat()), i -> val(i.getFiscalMonth()), i -> val(i.getRevenueGrowth()),
                        i -> val(i.getTrend())
                ));

        Query p1 = ctx.query("SET IDENTITY_INSERT [sale] ON");
        Query p2 = ctx.insertInto(SALE,
                SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_,
                SALE.EMPLOYEE_NUMBER, SALE.HOT, SALE.RATE, SALE.VAT,
                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND)
                .valuesOfRows(listOfSales);
        Query p3 = ctx.query("SET IDENTITY_INSERT [sale] OFF");

        System.out.println("EXAMPLE 8.3.3 (affected rows): "
                + Arrays.toString(ctx.batch(p1, p2, p3).execute()));
    }

    // EXAMPLE 9    
    // 9.1, 9.5
    /*
    declare @result table ([sale_id] bigint);
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [fiscal_month], [revenue_growth]
    ) output [inserted].[sale_id] into @result 
    values 
      (?, ?, ?, ?, ?);
    select 
      [sale_id] 
    from 
      @result [r];
    
    // 9.2, 9.4
    declare @result table (
      [office_code] varchar(10)
    );
    insert into [classicmodels].[dbo].[office] (
      [office_code], [city], [phone], [address_line_first], 
      [address_line_second], [state], 
      [country], [postal_code], [territory], 
      [location], [internal_budget]
    ) output [inserted].[office_code] into @result 
    values 
      (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
    select 
      [office_code] 
    from 
      @result [r];
    
    // 9.3
    merge into [classicmodels].[dbo].[office] using (
      select 
        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
    ) [t] (
      [office_code], [city], [phone], [address_line_first], 
      [address_line_second], [state], 
      [country], [postal_code], [territory], 
      [location], [internal_budget]
    ) on (
      [classicmodels].[dbo].[office].[office_code] = [t].[office_code] 
      or [classicmodels].[dbo].[office].[postal_code] = [t].[postal_code]
    ) when not matched then insert (
      [office_code], [city], [phone], [address_line_first], 
      [address_line_second], [state], 
      [country], [postal_code], [territory], 
      [location], [internal_budget]
    ) 
    values 
      (
        [t].[office_code], [t].[city], [t].[phone], 
        [t].[address_line_first], [t].[address_line_second], 
        [t].[state], [t].[country], [t].[postal_code], 
        [t].[territory], [t].[location], 
        [t].[internal_budget]
      );
    
    // 9.6, 9.7
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [fiscal_month], [revenue_growth]
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

        // This is the Office POJO generated by jOOQ
        Office office = new Office(
                UUID.randomUUID().toString().substring(0,10), // random office_code 
                "Ploiesti", "+43 22222", "addr1", "addr2", "CO", "RO",
                UUID.randomUUID().toString().substring(0,14), // random postal_code 
                "UU", null, 0);
        System.out.println("EXAMPLE 9.2 (affected rows): "
                + ctx.newRecord(OFFICE, office).insert()
        );

        OfficeRecord or = new OfficeRecord();
        or.from(office);
        or.setOfficeCode(UUID.randomUUID().toString().substring(0,10));
        or.setPostalCode(UUID.randomUUID().toString().substring(0,14));
        System.out.println("EXAMPLE 9.3 (affected rows): "
                + ctx.insertInto(OFFICE, OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                        OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE,
                        OFFICE.TERRITORY, OFFICE.LOCATION, OFFICE.INTERNAL_BUDGET)
                        .values(or.getOfficeCode(), or.getCity(), or.getPhone(), or.getAddressLineFirst(),
                                or.getAddressLineSecond(), or.getState(), or.getCountry(), or.getPostalCode(),
                                or.getTerritory(), or.getLocation(), or.getInternalBudget())
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        or.setOfficeCode(UUID.randomUUID().toString().substring(0,10));
        or.setPostalCode(UUID.randomUUID().toString().substring(0,14));
        or.attach(ctx.configuration()); // attach the record to the current configuration
        System.out.println("EXAMPLE 9.4 (affected rows): "
                + or.insert()
        );

        // this is user-define SalePart POJO (it contains only a part of fields)
        SalePart salePart = new SalePart(5644.32, 1370L);
        System.out.println("EXAMPLE 9.5 (affected rows): "
                + ctx.newRecord(SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                        SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2004, salePart.getSale(), salePart.getEmployeeNumber(),
                                4, 23.33)
                        .into(SALE)
                        .insert()
        );

        SaleRecord srp = new SaleRecord();
        srp.from(salePart);          // get the available fields from SalePart
        srp.setFiscalYear(2004);     // fiscal_year cannot be null and doesn't have a default value
        srp.setFiscalMonth(4);       // fiscal_month cannot be null and doesn't have a default value
        srp.setRevenueGrowth(12.22); // revenue_growth cannot be null and doesn't have a default value
        System.out.println("EXAMPLE 9.6 (affected rows): "
                + ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                        SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(srp.getFiscalYear(), srp.getSale(), srp.getEmployeeNumber(),
                                srp.getFiscalMonth(), srp.getRevenueGrowth())
                        .execute()
        );

        System.out.println("EXAMPLE 9.7 (affected rows): "
                + ctx.executeInsert(srp)
        );
    }

    // EXAMPLE 10  
    // 10.1
    /*
    declare @result table ([sale_id] bigint);
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [hot], [rate], [vat], [fiscal_month], 
      [revenue_growth], [trend]
    ) output [inserted].[sale_id] into @result 
    values 
      (?, ?, ?, ?, ?, ?, ?, ?, ?);
    select 
      [sale_id] 
    from 
      @result [r];    
    
    // 10.2
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [fiscal_month], [revenue_growth]
    ) 
    values 
      (?, ?, ?, ?, ?)    
     */
    public void insertRecordAfterResettingPK() {

        Sale sale = new Sale(1L, 2005, 343.22, 1504L, false, null, null, 3, 12.33, "UP");
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

        System.out.println("EXAMPLE 10.2 (affected rows): "
                + ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                        SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(sr.getFiscalYear(), sr.getSale(), sr.getEmployeeNumber(),
                                sr.getFiscalMonth(), sr.getRevenueGrowth())
                        .execute()
        );
    }

    // EXAMPLE 11        
    /*
    // 11.1
    insert into [classicmodels].[dbo].[office] (
      [office_code], [city], [phone], [address_line_first], 
      [address_line_second], [state], 
      [country], [postal_code], [territory], 
      [location], [internal_budget]
    ) 
    values 
      (
        ?, 
        upper(?), 
        ?, 
        ?, 
        lower(?), 
        ?, 
        ?, 
        ?, 
        ?, 
        default, 
        ?
      )
    
    // 11.2
    merge into [classicmodels].[dbo].[orderdetail] using (
      select 
        ?, 
        ?, 
        ?, 
        [dbo].[sale_price](?, ?, ?), 
        ?
    ) [t] (
      [order_id], [product_id], [quantity_ordered], 
      [price_each], [order_line_number]
    ) on (
      [classicmodels].[dbo].[orderdetail].[orderdetail_id] = ? 
      or (
        [classicmodels].[dbo].[orderdetail].[order_id] = [t].[order_id] 
        and [classicmodels].[dbo].[orderdetail].[product_id] = [t].[product_id]
      )
    ) when not matched then insert (
      [order_id], [product_id], [quantity_ordered], 
      [price_each], [order_line_number]
    ) 
    values 
      (
        [t].[order_id], [t].[product_id], 
        [t].[quantity_ordered], [t].[price_each], 
        [t].[order_line_number]
      );    
    */
    public void usingFunctionsInInsert() {
       
        System.out.println("EXAMPLE 11.1 (affected rows): "
                + ctx.insertInto(OFFICE)
                        .values(UUID.randomUUID().toString().substring(0,10),
                                upper("Ploiesti"), "+43 22222", "addr1", lower("ADDR2"), "CO", "RO", 
                                UUID.randomUUID().toString().substring(0,14), 
                                "UU", default_(), 0)                        
                        .execute()
        );

        System.out.println("EXAMPLE 11.2 (affected rows): "
                + ctx.insertInto(ORDERDETAIL, ORDERDETAIL.ORDER_ID, 
                        ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.QUANTITY_ORDERED, 
                        ORDERDETAIL.PRICE_EACH, ORDERDETAIL.ORDER_LINE_NUMBER)
                        .values(val(10100L), val(2L), val(20),
                                salePrice(20, 4.5f, 0.25f).coerce(BigDecimal.class), val(3))
                        .onDuplicateKeyIgnore()
                        .execute()
        );

    }

    // EXAMPLE 12
    /*
    insert into [classicmodels].[dbo].[department] (
      [name], [phone], [code], [office_code]
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
    insert into [classicmodels].[dbo].[order] (
      [order_date], [required_date], [shipped_date], 
      [status], [comments], [customer_number], 
      [amount]
    ) 
    select 
      ?, ?, ?, ?, ?, ?, ? 
    where 
      not (
        exists (
          select 
            [classicmodels].[dbo].[order].[order_id], 
            [classicmodels].[dbo].[order].[order_date], 
            [classicmodels].[dbo].[order].[required_date], 
            [classicmodels].[dbo].[order].[shipped_date], 
            [classicmodels].[dbo].[order].[status], 
            [classicmodels].[dbo].[order].[comments], 
            [classicmodels].[dbo].[order].[customer_number], 
            [classicmodels].[dbo].[order].[amount] 
          from 
            [classicmodels].[dbo].[order] 
          where 
            (
              (
                ? between [classicmodels].[dbo].[order].[order_date] 
                and [classicmodels].[dbo].[order].[shipped_date] 
                or ? between [classicmodels].[dbo].[order].[order_date] 
                and [classicmodels].[dbo].[order].[shipped_date]
              ) 
              and ? = [classicmodels].[dbo].[order].[customer_number]
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
                                        val(100L), val(BigDecimal.valueOf(2000)))
                                        .whereNotExists(
                                                selectFrom(o)
                                                        .where(val(LocalDate.of(2010, 10, 10)).between(o.ORDER_DATE).and(o.SHIPPED_DATE)
                                                                .or(val(LocalDate.of(2010, 11, 5)).between(o.ORDER_DATE).and(o.SHIPPED_DATE)))
                                                        .and(val(100L).eq(o.CUSTOMER_NUMBER))
                                        )
                        )
                        .execute()
        );
    }
}
