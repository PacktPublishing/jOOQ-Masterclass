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
        
        // Because we use <overridePrimaryKeys>customer_name_uk</overridePrimaryKeys>,
        // jOOQ will use 'customer_name_uk' as primary key instead of 'customer_number'
        // Note that update(), merge(), delete(), refresh() are also affected (they use 'customer_name_uk'.)
        cr.store();                 
    }    
    
    @Transactional
    public void updateDepartmentTopic() {
        
        DepartmentRecord dr = ctx.selectFrom(DEPARTMENT)
                .where(DEPARTMENT.DEPARTMENT_ID.eq(1))
                .fetchSingle();
        
        dr.setTopic(new String[] {"promotion", "market", "research"});
        
        // Note that update(), merge(), delete(), refresh() are also affected (they use 'customer_name_uk'.)
        dr.store();
    }
}
