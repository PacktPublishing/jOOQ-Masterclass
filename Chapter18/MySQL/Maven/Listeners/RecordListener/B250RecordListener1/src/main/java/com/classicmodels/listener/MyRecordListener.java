package com.classicmodels.listener;

import jooq.generated.tables.records.EmployeeRecord;
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
}
