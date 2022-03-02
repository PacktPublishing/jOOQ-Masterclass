package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Department.DEPARTMENT;
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.DepartmentRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {
    
    private final DSLContext ctx;
    
    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }
    
    @Transactional
    public void updateCustomerPhone() {
        
        CustomerRecord cr = ctx.selectFrom(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Mini Gifts Distributors Ltd."))
                .fetchSingle();
     
        cr.setPhone("4159009544");
        
        // jOOQ will use 'customer_name' as primary key instead of 'customer_number'
        // Note that update(), merge(), delete(), refresh() are also affected (they use 'customer_name'.)
        cr.store();                 
    }    
    
    @Transactional
    public void updateDepartmentTopic() {
        
        DepartmentRecord dr = ctx.selectFrom(DEPARTMENT)
                .where(DEPARTMENT.DEPARTMENT_ID.eq(1))
                .fetchSingle();
        
        dr.setTopic(new String[] {"promotion", "market", "research"});
        
        // jOOQ will use 'code' as primary key instead of 'department_id'
        // Note that update(), merge(), delete(), refresh() are also affected (they use 'code'.)
        dr.store();
    }
}
