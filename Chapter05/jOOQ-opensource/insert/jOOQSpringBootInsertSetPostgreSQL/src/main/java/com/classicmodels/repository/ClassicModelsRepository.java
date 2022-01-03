package com.classicmodels.repository;

import jooq.generated.enums.RateType;
import jooq.generated.enums.VatType;
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
    insert into "public"."sale" (
      "fiscal_year", "sale", "employee_number"
    ) 
    values 
      (?, ?, ?)
     */
    public void insertOneSale() {

        System.out.println("EXAMPLE 1 (affected rows): "
                + ctx.insertInto(SALE)
                        .set(SALE.FISCAL_YEAR, 2005)
                        .set(SALE.SALE_, 4523.33)
                        .set(SALE.EMPLOYEE_NUMBER, 1504L)
                        .execute()
        );
    }

    // EXAMPLE 2
    /*
    insert into "public"."sale" (
      "fiscal_year", "sale", "employee_number"
    ) 
    values 
      (?, ?, ?), 
      (?, ?, ?)
     */
    public void insertTwoSale() {

        System.out.println("EXAMPLE 2 (affected rows): "
                + ctx.insertInto(SALE)
                        .set(SALE.FISCAL_YEAR, 2005)
                        .set(SALE.SALE_, 4523.33)
                        .set(SALE.EMPLOYEE_NUMBER, 1504L)
                        .newRecord()
                        .set(SALE.FISCAL_YEAR, 2005)
                        .set(SALE.SALE_, 4523.33)
                        .set(SALE.EMPLOYEE_NUMBER, 1504L)
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    insert into "public"."sale" (
      "fiscal_year", "sale", "employee_number", 
      "hot", "rate", "vat", "trend"
    ) 
    values 
      (
        ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?
      )    
     */
    public void insertRecordSale() {

        SaleRecord sr = new SaleRecord(null, 
                2003, 123.32, 1370L, null, RateType.SILVER, VatType.MAX, null);
        
        sr.reset(SALE.SALE_ID); // reset the id

        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(SALE)
                        .set(sr)
                        .execute()
        );
    }
}