package com.classicmodels.listener;

import java.time.LocalDate;
import static jooq.generated.tables.EmployeeStatus.EMPLOYEE_STATUS;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.EmployeeStatusRecord;
import org.jooq.RecordContext;
import org.jooq.impl.DefaultRecordListener;

public class MyRecordListener extends DefaultRecordListener {

    @Override
    public void insertStart(RecordContext rcx) {

        if (rcx.record() instanceof EmployeeRecord employee) {

            long secretNumber = (long) (10000 * Math.random());
           
            employee.setEmployeeNumber(secretNumber);
            employee.setExtension("x" + secretNumber);
        }       
    }

    @Override
    public void insertEnd(RecordContext rcx) {
        
        if (rcx.record() instanceof EmployeeRecord employee) {
            
            EmployeeStatusRecord status = rcx.dsl().newRecord(EMPLOYEE_STATUS);
            
            status.setEmployeeNumber(employee.getEmployeeNumber());
            status.setStatus("REGULAR");
            status.setAcquiredDate(LocalDate.now());
            
            status.insert();
        }        
    }
    
    
}
