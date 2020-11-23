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
    insert into `classicmodels`.`sale` 
      (`fiscal_year`, `sale`, `employee_number`)
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
    insert into `classicmodels`.`sale` 
      (`fiscal_year`, `sale`, `employee_number`)
    values
     (?, ?, ?),(?, ?, ?)
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
    insert into `classicmodels`.`sale` (
       `sale_id`,`fiscal_year`,`sale`,`employee_number`)
    values
       (?, ?, ?, ?)
     */
    public void insertRecordSale() {

        SaleRecord sr = new SaleRecord(null, 2003, 123.32, 1370L);

        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.insertInto(SALE)
                        .set(sr)
                        .execute()
        );
    }
}