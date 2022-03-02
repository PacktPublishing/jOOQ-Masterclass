package com.classicmodels.repository;

import java.util.List;
import jooq.generated.Keys;
import static jooq.generated.tables.CustomerMaster.CUSTOMER_MASTER;
import jooq.generated.tables.records.CustomerMasterRecord;
import jooq.generated.tables.records.OfficeMasterRecord;
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
        
    public void navigatingCustomerOffice() {
        
        CustomerMasterRecord cmr = ctx.selectFrom(CUSTOMER_MASTER)
                .where(CUSTOMER_MASTER.CUSTOMER_NAME.eq("Classic Legends Inc."))
                .fetchSingle();      
                
        OfficeMasterRecord parent = cmr.fetchParent(Keys.CUSTOMER_MASTER__OFFICE_MASTER_FK);
        System.out.println("Customer:\n" + cmr + " Parent:\n" + parent);
        
        List<CustomerMasterRecord> children = parent.fetchChildren(Keys.CUSTOMER_MASTER__OFFICE_MASTER_FK);
        System.out.println("Children:\n" + children);        
    }
}
