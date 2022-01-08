package com.classicmodels.repository;

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
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [fiscal_month], [revenue_growth]
    ) 
    values 
      (?, ?, ?, ?, ?)    
     */
    public void insertOneSale() {

        System.out.println("EXAMPLE 1 (affected rows): "
                + ctx.insertInto(SALE)
                        .set(SALE.FISCAL_YEAR, 2005)
                        .set(SALE.SALE_, 4523.33)
                        .set(SALE.EMPLOYEE_NUMBER, 1504L)
                        .set(SALE.FISCAL_MONTH, 3)
                        .set(SALE.REVENUE_GROWTH, 12.22)
                        .execute()
        );
    }

    // EXAMPLE 2
    /*
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [fiscal_month], [revenue_growth]
    ) 
    values 
      (?, ?, ?, ?, ?), 
      (?, ?, ?, ?, ?)   
     */
    public void insertTwoSale() {

        System.out.println("EXAMPLE 2 (affected rows): "
                + ctx.insertInto(SALE)
                        .set(SALE.FISCAL_YEAR, 2005)
                        .set(SALE.SALE_, 4523.33)
                        .set(SALE.EMPLOYEE_NUMBER, 1504L)
                        .set(SALE.FISCAL_MONTH, 3)
                        .set(SALE.REVENUE_GROWTH, 12.22)
                        .newRecord()
                        .set(SALE.FISCAL_YEAR, 2005)
                        .set(SALE.SALE_, 4523.33)
                        .set(SALE.EMPLOYEE_NUMBER, 1504L)
                        .set(SALE.FISCAL_MONTH, 4)
                        .set(SALE.REVENUE_GROWTH, 22.12)
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    insert into [classicmodels].[dbo].[sale] (
      [fiscal_year], [sale], [employee_number], 
      [hot], [rate], [vat], [fiscal_month], 
      [revenue_growth], [trend]
    ) 
    values 
      (?, ?, ?, ?, ?, ?, ?, ?, ?)    
     */
    public void insertRecordSale() {

        SaleRecord sr = new SaleRecord(null,
                2003, 123.32, 1370L, null, "SILVER", "MAX", 3, 12.22, "UP");

        sr.reset(SALE.SALE_ID);
        
        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(SALE)
                        .set(sr)
                        .execute()
        );
    }
}
