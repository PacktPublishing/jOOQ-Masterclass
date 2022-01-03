package com.classicmodels.repository;

import com.classicmodels.pojo.SalePart;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import static jooq.generated.Routines.netpriceeach;
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
import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.default_;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.lower;
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

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1   
    public void insertOrderAutoGenKey() {

        // Consider visiting: https://github.com/jOOQ/jOOQ/issues/1818
        /*
        SET IDENTITY_INSERT [order] ON
        
        insert into [classicmodels].[dbo].[order] (
          [order_id], [order_date], [required_date], 
          [shipped_date], [status], [comments], 
          [customer_number]
        ) 
        values 
          (
            9103555, 
            cast('2003-02-12' as date), 
            cast('2003-03-01' as date), 
            cast('2003-02-27' as date), 
            'Shipped', 
            'New order inserted ...', 
            363
          )

        SET IDENTITY_INSERT [order] OFF
         */
        Query q1 = ctx.query("SET IDENTITY_INSERT [order] ON");
        Query q2 = ctx.insertInto(ORDER) // InsertSetStep<OrderRecord>
                .values(Math.random() * 10000000, // explicit random primary key
                        LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                        LocalDate.of(2003, 2, 27), "Shipped",
                        "New order inserted ...", 363L);
        Query q3 = ctx.query("SET IDENTITY_INSERT [order] OFF");

        System.out.println("EXAMPLE 1.1 (affected rows): "
                + Arrays.toString(ctx.batch(q1, q2, q3).execute())
        );

        /*
       insert into [classicmodels].[dbo].[order] (
         [comments],[order_date],[required_date],[shipped_date],[status],[customer_number])
       values
         (?, ?, ?, ?, ?, ?)
         */
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
    }

    // EXAMPLE 2
    /*
    merge into [classicmodels].[dbo].[office] using (
     select
       1 [one]
     ) t on [classicmodels].[dbo].[office].[office_code] = ?
       when not matched then
         insert
          (
            [office_code],[city],[phone],[address_line_first],
            [address_line_second],[state],[country],[postal_code],[territory]
          )
         values
            (?, ?, ?, ?, ?, ?, ?, ?, ?);
     */
    public void insertOrderManualKey() {

        System.out.println("EXAMPLE 2.1 (affected rows): "
                + ctx.insertInto(OFFICE) // InsertSetStep<OfficeRecord>
                        .values(Math.round(Math.random() * 1000), // random primary key
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", "211 333", "H", default_())
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 2.2 (affected rows): "
                + // InsertValuesStep9<OfficeRecord, String, String, String, String, String, String, String, String, String>
                ctx.insertInto(OFFICE, OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                        OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE, OFFICE.TERRITORY, OFFICE.LOCATION)
                        .values(String.valueOf(Math.round(Math.random() * 1000)), // random primary key
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", "211 333", "H", default_())
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 2.3 (affected rows): "
                + // InsertValuesStep9<OfficeRecord, String, String, String, String, String, String, String, String, String>
                ctx.insertInto(OFFICE)
                        .columns(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                                OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE, OFFICE.TERRITORY, OFFICE.LOCATION)
                        .values(String.valueOf(Math.round(Math.random() * 1000)), // random primary key
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", "211 333", "H", default_())
                        .onDuplicateKeyIgnore()
                        .execute()
        );

    }

    // EXAMPLE 3
    /*
    insert into [classicmodels].[dbo].[order] (
      [comments],[order_date],[required_date],[shipped_date],[status],[customer_number])
    values
      (?, ?, ?, ?, ?, ?), (?, ?, ?, ?, ?, ?), (?, ?, ?, ?, ?, ?)
     */
    public void insertMultipleOrderAutoGenKey() {

        System.out.println("EXAMPLE 3.1 (affected rows): "
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

        System.out.println("EXAMPLE 3.2 (affected rows): "
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
    merge into [classicmodels].[dbo].[office] using (
     (
       select
         ?,?,?,?,?,?,?,?,?
       union all
       select
         ?,?,?,?,?,?,?,?,?
       union all
       select
         ?,?,?,?,?,?,?,?,?
     )
    ) [t](
      [office_code],[city],[phone],[address_line_first],[address_line_second],
      [state],[country],[postal_code],[territory]
    ) on [classicmodels].[dbo].[office].[office_code] = [t].[office_code]
    when not matched then
    insert
      (
        [office_code],[city],[phone],[address_line_first],[address_line_second],
        [state],[country],[postal_code],[territory]
      )
    values
      (
        [t].[office_code],[t].[city],[t].[phone],[t].[address_line_first],
        [t].[address_line_second],[t].[state],[t].[country],[t].[postal_code],[t].[territory]
      );
     */
    public void insertMultipleOrderManualKey() {

        System.out.println("EXAMPLE 4.1 (affected rows): "
                + ctx.insertInto(OFFICE) // InsertSetStep<OfficeRecord>
                        .values(Math.round(Math.random() * 100),
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", "211 333", "H", null)
                        .values(Math.round(Math.random() * 1000),
                                "Campina", "+41 212 333", "addr1", "addr2", "DB", "RO", "211 311", "M", null)
                        .values(Math.round(Math.random() * 10000),
                                "Ploiesti", "+43 22222", "addr1", "addr2", "CO", "RO", "121 333", "UU", null)
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 4.2 (affected rows): "
                + // InsertValuesStep9<OfficeRecord, String, String, String, String, String, String, String, String, String>
                ctx.insertInto(OFFICE, OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                        OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE, OFFICE.TERRITORY)
                        .values(String.valueOf(Math.round(Math.random() * 100)),
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", "211 333", "H")
                        .values(String.valueOf(Math.round(Math.random() * 1000)),
                                "Campina", "+41 212 333", "addr1", "addr2", "DB", "RO", "211 311", "M")
                        .values(String.valueOf(Math.round(Math.random() * 10000)),
                                "Ploiesti", "+43 22222", "addr1", "addr2", "CO", "RO", "121 333", "UU")
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        System.out.println("EXAMPLE 4.3 (affected rows): "
                + // InsertValuesStep9<OfficeRecord, String, String, String, String, String, String, String, String, String>
                ctx.insertInto(OFFICE)
                        .columns(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                                OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE, OFFICE.TERRITORY)
                        .values(String.valueOf(Math.round(Math.random() * 100)),
                                "Banesti", "+40 323 421", "addr1", "addr2", "PH", "RO", "211 333", "H")
                        .values(String.valueOf(Math.round(Math.random() * 1000)),
                                "Campina", "+41 212 333", "addr1", "addr2", "DB", "RO", "211 311", "M")
                        .values(String.valueOf(Math.round(Math.random() * 10000)),
                                "Ploiesti", "+43 22222", "addr1", "addr2", "CO", "RO", "121 333", "UU")
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 5
    /*
    merge into [classicmodels].[dbo].[payment] using (
      select 1 [one]
    ) t on (
      [classicmodels].[dbo].[payment].[customer_number] = ?
        and [classicmodels].[dbo].[payment].[check_number] = ?
    )
    when not matched then
    insert
      (
        [customer_number],[check_number],[caching_date],[payment_date],[invoice_amount]
      )
    values
        (?, ?, ?, ?, ?);
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
    insert into
      [classicmodels].[dbo].[sale] ([employee_number], [sale], [fiscal_year])
    values
      (?, ?, ?)
     */
    public void insertOneSaleRecord() {

        /* create a SaleRecord via constructor */
        // SaleRecord sr = new SaleRecord(null, 2003, 3443.22, 1370L,
        //       false, null, null, "UP"); 
        /* or, creare a SaleRecord via constructor and setters */
        SaleRecord sr = new SaleRecord();
        sr.setFiscalYear(2003);         // or, sr.set(SALE.FISCAL_YEAR, 2003);
        sr.setSale(3443.22);            // or, sr.set(SALE.SALE_, 3443.22);        
        sr.setEmployeeNumber(1370L);    // or, sr.set(SALE.EMPLOYEE_NUMBER, 1370L);                       

        System.out.println("EXAMPLE 6.1 (affected rows): "
                + ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                        .values(sr.getFiscalYear(), sr.getSale(), sr.getEmployeeNumber())
                        .execute()
        );

        System.out.println("EXAMPLE 6.2 (affected rows): "
                + ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                        .values(sr.value2(), sr.value3(), sr.value4())
                        .execute()
        );

        System.out.println("EXAMPLE 6.3 (affected rows): "
                + ctx.insertInto(SALE, sr.field4(), sr.field3(), sr.field2())
                        .values(sr.value4(), sr.value3(), sr.value2())
                        .execute()
        );
    }

    // EXAMPLE 7
    /*
    insert into
      [classicmodels].[dbo].[sale] ([fiscal_year], [sale], [employee_number])
    values
      (?, ?, ?), (?, ?, ?)
     */
    public void insertTwoSaleRecord() {

        // Record4<Long, Integer, Double, Long>
        SaleRecord sr1 = new SaleRecord(null, 2003, 3443.22, 1370L,
                false, null, null, "UP");
        SaleRecord sr2 = new SaleRecord(null, 2005, 1221.12, 1504L,
                false, null, null, "UP");

        System.out.println("EXAMPLE 7 (affected rows): "
                + ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                        .values(sr1.getFiscalYear(), sr1.getSale(), sr1.getEmployeeNumber())
                        .values(sr2.getFiscalYear(), sr2.getSale(), sr2.getEmployeeNumber())
                        .execute()
        );
    }

    // EXAMPLE 8  
    /*
    insert into
      [classicmodels].[dbo].[sale] ([fiscal_year], [sale], [employee_number])
    values
      (?, ?, ?),(?, ?, ?),(?, ?, ?)
     */
    public void insertCollectionOfSaleRecord() {

        // consider this collection of SaleRecord
        Collection<SaleRecord> listOfRecord
                = List.of(new SaleRecord(null, 2003, 3443.22, 1370L,
                        false, null, null, "UP"),
                        new SaleRecord(null, 2005, 1221.12, 1504L,
                                true, null, null, "UP"),
                        new SaleRecord(null, 2005, 1221.12, 1504L,
                                false, null, null, "DOWN"));

        /* First Approach */
        // InsertValuesStepN<SaleRecord>
        var insert1 = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER);
        for (SaleRecord sr : listOfRecord) {
            insert1.values(sr.getFiscalYear(), sr.getSale(), sr.getEmployeeNumber());
        }
        System.out.println("EXAMPLE 8.1 (affected rows): "
                + insert1.execute()
        );

        /* Third Approach (inspired from https://github.com/jOOQ/jOOQ/issues/6604) */
        System.out.println("EXAMPLE 8.2 (affected rows): "
                + Optional.of(ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER))
                        .map(s -> {
                            listOfRecord.forEach((sr) -> {
                                s.values(sr.getFiscalYear(), sr.getSale(), sr.getEmployeeNumber());
                            });
                            return s.execute();
                        })
        );

        // jOOQ 3.15 (valuesOfRows(), valuesOfRecords()) 
        // https://github.com/jOOQ/jOOQ/issues/6604
        // This example is WIP
    }

    // EXAMPLE 9    
    public void insertNewRecord() {

        /*
        declare @result table ([sale_id] bigint);
          insert into
            [classicmodels].[dbo].[sale] ([fiscal_year], [sale], [employee_number]) 
          output [inserted].[sale_id] into @result
        values
         (?, ?, ?);
        
        select
          [r].[sale_id]
        from
          @result [r];
         */
        System.out.println("EXAMPLE 9.1 (affected rows): "
                + ctx.newRecord(SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                        .values(2004, 1233.2, 1370L)
                        .into(SALE)
                        .insert()
        );

        /*
        declare @result table ([office_code] varchar(10));
        insert into [classicmodels].[dbo].[office] (
          [office_code],[city],[phone],[address_line_first],[address_line_second],
          [state],[country],[postal_code],[territory]
        ) output [inserted].[office_code] into @result
        values
          (?, ?, ?, ?, ?, ?, ?, ?, ?);
        
        select
          [r].[office_code]
        from
          @result [r];
         */
        // This is the Office POJO generated by jOOQ
        Office office = new Office(String.valueOf(Math.round(Math.random() * 10000)),
                "Ploiesti", "+43 22222", "addr1", "addr2", "CO", "RO", "121 333", "UU", null);
        System.out.println("EXAMPLE 9.2 (affected rows): "
                + ctx.newRecord(OFFICE, office).insert()
        );

        /*
        merge into [classicmodels].[dbo].[office] using (
          select 1 [one]
        ) t on [classicmodels].[dbo].[office].[office_code] = ?
        when not matched then
          insert
           (
             [office_code],[city],[phone],[address_line_first],[address_line_second],
             [state],[country],[postal_code],[territory]
           )
          values
            (?, ?, ?, ?, ?, ?, ?, ?, ?);
         */
        OfficeRecord or = new OfficeRecord();
        or.from(office);
        or.setOfficeCode(String.valueOf(Math.round(Math.random() * 10000)));
        System.out.println("EXAMPLE 9.3 (affected rows): "
                + ctx.insertInto(OFFICE, OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                        OFFICE.ADDRESS_LINE_SECOND, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.POSTAL_CODE, OFFICE.TERRITORY, OFFICE.LOCATION)
                        .values(or.getOfficeCode(), or.getCity(), or.getPhone(), or.getAddressLineFirst(),
                                or.getAddressLineSecond(), or.getState(), or.getCountry(), or.getPostalCode(),
                                or.getTerritory(), default_())
                        .onDuplicateKeyIgnore()
                        .execute()
        );
        
        or.setOfficeCode(String.valueOf(Math.round(Math.random() * 10000)));
        or.attach(ctx.configuration()); // attach the record to the current configuration
        System.out.println("EXAMPLE 9.4 (affected rows): "
                +or.insert()
        );

        /*
        declare @result table ([sale_id] bigint);
        insert into
          [classicmodels].[dbo].[sale] ([fiscal_year], [sale], [employee_number]) 
        output [inserted].[sale_id] into @result
        values
          (?, ?, ?);

        select
          [r].[sale_id]
        from
          @result [r];
         */
        // this is user-define SalePart POJO (it contains only a part of fields)
        SalePart salePart = new SalePart(5644.32, 1370L);
        System.out.println("EXAMPLE 9.5 (affected rows): "
                + ctx.newRecord(SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                        .values(2004, salePart.getSale(), salePart.getEmployeeNumber())
                        .into(SALE)
                        .insert()
        );

        /*
        insert into
          [classicmodels].[dbo].[sale] ([fiscal_year], [sale], [employee_number])
        values
          (?, ?, ?)
         */
        SaleRecord srp = new SaleRecord();
        srp.from(salePart);      // get the available fields from SalePart
        srp.setFiscalYear(2004); // fiscal_year cannot be null and doesn't have a default value
        System.out.println("EXAMPLE 9.6 (affected rows): "
                + ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                        .values(srp.getFiscalYear(), srp.getSale(), srp.getEmployeeNumber())
                        .execute()
        );

        /*
        insert into [classicmodels].[dbo].[sale] (
          [fiscal_year], [sale], [employee_number]
        ) 
        values 
          (?, ?, ?)
         */
        System.out.println("EXAMPLE 9.7 (affected rows): "
                + ctx.executeInsert(srp)
        );
    }

    // EXAMPLE 10    
    public void insertRecordAfterResettingPK() {

        /*
        declare @result table ([sale_id] bigint);
        insert into
          [classicmodels].[dbo].[sale] ([fiscal_year], [sale], [employee_number]) 
        output [inserted].[sale_id] into @result
        values
         (?, ?, ?);
  
        select
          [r].[sale_id]
        from
          @result [r];
         */
        Sale sale = new Sale(1L, 2005, 343.22, 1504L, false, null, null, "UP");
        var record = ctx.newRecord(SALE, sale);
                
        // reset the current ID and allow DB to generate one
        record.changed(SALE.SALE_ID, false);
                
        // resets both changed flag that tracks record changes and value
        // record.reset(SALE.SALE_ID); 
        
        System.out.println("EXAMPLE 10.1 (affected rows): "
                + record.insert()
        );

        /*
        insert into
          [classicmodels].[dbo].[sale] ([fiscal_year], [sale], [employee_number])
        values
          (?, ?, ?)
         */
        SaleRecord sr = new SaleRecord();
        sr.from(sale);
        
        // resets both changed flag that tracks record changes and value
        sr.reset(SALE.SALE_ID); 
        
        System.out.println("EXAMPLE 10.2 (affected rows): "
                + ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                        .values(sr.getFiscalYear(), sr.getSale(), sr.getEmployeeNumber())
                        .execute()
        );
    }

    // EXAMPLE 11    
    public void usingFunctionsInInsert() {

        /*
        merge into [classicmodels].[dbo].[office] using (
          select 1 [one]
        ) t on [classicmodels].[dbo].[office].[office_code] = ?
        when not matched then
          insert
            (
              [office_code],[city],[phone],[address_line_first],
              [address_line_second],[state],[country],[postal_code],[territory]
            )
          values
            (?, upper(?), ?, ?, lower(?), ?, ?, ?, ?);
         */
        System.out.println("EXAMPLE 11.1 (affected rows): "
                + ctx.insertInto(OFFICE)
                        .values(String.valueOf(Math.round(Math.random() * 10000)),
                                upper("Ploiesti"), "+43 22222", "addr1", lower("ADDR2"),
                                "CO", "RO", "121 333", "UU", default_())
                        .onDuplicateKeyIgnore()
                        .execute()
        );

        /*
        merge into [classicmodels].[dbo].[orderdetail] using (
          select 1 [one]
        ) t on (
         [classicmodels].[dbo].[orderdetail].[order_id] = ?
         and [classicmodels].[dbo].[orderdetail].[product_id] = ?
        )
        when not matched then
          insert
            (
              [order_id],[product_id],[quantity_ordered],[price_each],[order_line_number]
            )
          values
           (
             ?,?,?,[classicmodels].[dbo].[netPriceEach](?, ?, ?),?
           );
         */
        System.out.println("EXAMPLE 11.2 (affected rows): "
                + ctx.insertInto(ORDERDETAIL)
                        .values(10100L, 2L, 20,
                                netpriceeach(20, BigDecimal.valueOf(4.5), BigDecimal.valueOf(0.25)), 3)
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
        department.setCode((short) 44);

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
      [status], [comments], [customer_number]
    ) 
    select 
      ?, 
      ?, 
      ?, 
      ?, 
      ?, 
      ? 
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
            [classicmodels].[dbo].[order].[customer_number] 
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
                                o.STATUS, o.COMMENTS, o.CUSTOMER_NUMBER)
                        .select(
                                select(val(LocalDate.of(2010, 10, 10)), val(LocalDate.of(2010, 11, 1)),
                                        val(LocalDate.of(2010, 11, 5)), val("Shipped"), val(""), val(100L))
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
