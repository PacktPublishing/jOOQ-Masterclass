package com.classicmodels.listener;

import java.time.LocalDate;
import static jooq.generated.tables.EmployeeStatus.EMPLOYEE_STATUS;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.EmployeeStatusRecord;
import org.jooq.RecordContext;
import org.jooq.impl.DefaultRecordListener;

public class MyRecordListener extends DefaultRecordListener {

    @Override
    public void insertStart(RecordContext ctx) {

        if (ctx.record() instanceof EmployeeRecord employee) {

            long secretNumber = (long) (10000 * Math.random());
           
            employee.setEmployeeNumber(secretNumber);
            employee.setExtension("x" + secretNumber);
        }

        super.insertStart(ctx);
    }

    @Override
    public void insertEnd(RecordContext ctx) {
        
        if (ctx.record() instanceof EmployeeRecord employee) {
            
            EmployeeStatusRecord status = ctx.dsl().newRecord(EMPLOYEE_STATUS);
            
            status.setEmployeeNumber(employee.getEmployeeNumber());
            status.setStatus("REGULAR");
            status.setAcquiredDate(LocalDate.now());
            
            status.insert();
        }
        
        super.insertEnd(ctx); 
    }
    
    
}
