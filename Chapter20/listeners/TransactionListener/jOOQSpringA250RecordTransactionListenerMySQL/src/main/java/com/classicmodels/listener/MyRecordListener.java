package com.classicmodels.listener;

import jooq.generated.tables.records.EmployeeRecord;
import org.jooq.RecordContext;
import org.jooq.impl.DefaultRecordListener;

public class MyRecordListener extends DefaultRecordListener {

    @Override
    public void insertStart(RecordContext ctx) {

        if (ctx.record() instanceof EmployeeRecord) {

            long secretNumber = (long) (10000 * Math.random());

            EmployeeRecord employee = (EmployeeRecord) ctx.record();
            employee.setEmployeeNumber(secretNumber);
            employee.setExtension("x" + secretNumber);
        }      
    }

    @Override
    public void updateStart(RecordContext ctx) {

        if (ctx.record() instanceof EmployeeRecord) {

            EmployeeRecord employee = (EmployeeRecord) ctx.record().original();

            ctx.configuration().data("employee", employee);
        }        
    }
}
