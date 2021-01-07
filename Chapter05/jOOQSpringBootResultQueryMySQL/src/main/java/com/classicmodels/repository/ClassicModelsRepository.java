package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Payment.PAYMENT;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {
    
    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;        
    }
    
    public int jooqQuery() {
        
        // Query query = ctx.query("DELETE FROM payment WHERE customer_number = 103");
        
        Query query = ctx.deleteFrom(PAYMENT)
                .where(PAYMENT.CUSTOMER_NUMBER.eq(103L));  
        
        int affectedRows = query.execute();
        
        return affectedRows;
    }
    
    public List<String> jooqResultQuery() {
        
        /*
        ResultQuery<Record> resultQuery = ctx.resultQuery(
                "SELECT job_title FROM employee WHERE office_code = '4'");        
        Result<Record> fetched = resultQuery.fetch();
        */
        
        ResultQuery<Record1<String>> resultQuery = ctx.select(EMPLOYEE.JOB_TITLE)
                .from(EMPLOYEE)
                .where(EMPLOYEE.OFFICE_CODE.eq("4"));        
        Result<Record1<String>> fetched = resultQuery.fetch();        
        
        List<String> result = fetched.into(String.class);        
        
        return result;
    }
        
}