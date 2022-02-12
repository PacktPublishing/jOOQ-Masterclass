package com.classicmodels.mapper;

import com.classicmodels.pojo.SimpleCustomer;
import jooq.generated.tables.records.CustomerRecord;
import org.jooq.RecordMapper;

public class CustomerRecordMapper implements RecordMapper<CustomerRecord, SimpleCustomer> {

    @Override
    public SimpleCustomer map(CustomerRecord r) {
        
        SimpleCustomer sc = new SimpleCustomer();
        
        sc.setContactFirstName(r.getContactFirstName());
        sc.setContactLastName(r.getContactLastName());
        sc.setCreditLimit(r.getCreditLimit());
        sc.setCustomerName(r.getCustomerName());
        sc.setCustomerNumber(r.getCustomerNumber());
        sc.setFirstBuyDate(r.getFirstBuyDate());
        sc.setPhone(r.getPhone());
        sc.setSalesRepEmployeeNumber(r.getSalesRepEmployeeNumber());
        
        return sc;
    }    
}
