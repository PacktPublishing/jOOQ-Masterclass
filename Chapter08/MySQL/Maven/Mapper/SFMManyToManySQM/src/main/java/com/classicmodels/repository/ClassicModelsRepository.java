package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleManager;
import com.classicmodels.pojo.SimpleOffice;
import java.util.List;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.select;
import org.simpleflatmapper.jooq.SelectQueryMapper;
import org.simpleflatmapper.jooq.SelectQueryMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {
    
    private final DSLContext ctx;
    private final SelectQueryMapper<SimpleManager> sqMapper1;
    private final SelectQueryMapper<SimpleOffice> sqMapper2;
    
    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;        
        this.sqMapper1 = SelectQueryMapperFactory
                .newInstance()
                // .unorderedJoin() // use this if you don't want to order, .orderBy(MANAGER.MANAGER_ID)
                .newMapper(SimpleManager.class);
        
        this.sqMapper2 = SelectQueryMapperFactory
                .newInstance()
                // .unorderedJoin() // use this if you don't want to order, .orderBy(OFFICE.OFFICE_CODE)                
                .newMapper(SimpleOffice.class);        
    }
    
    public List<SimpleManager> findManagerAndOffice() {
        
        List<SimpleManager> result = sqMapper1.asList(
            ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME, 
                field("offices_officeCode"), field("offices_city"), field("offices_state"))
                        .from(MANAGER, lateral(select(OFFICE.OFFICE_CODE.as("offices_officeCode"),
                                OFFICE.CITY.as("offices_city"), OFFICE.STATE.as("offices_state"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))))
                .orderBy(MANAGER.MANAGER_ID)
        );

        /* or, like this
        List<SimpleManager> result = sqMapper1.asList(
                ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                        OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE)
                        .from(MANAGER, OFFICE, OFFICE_HAS_MANAGER)
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .and(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .orderBy(MANAGER.MANAGER_ID)        
        );
         */
        
        return result;
    }
    
    public List<SimpleOffice> findOfficeAndManager() {
        
        List<SimpleOffice> result = sqMapper2.asList(
                ctx.select(OFFICE.OFFICE_CODE, OFFICE.STATE, OFFICE.CITY, 
                field("managers_managerId"), field("managers_managerName"))
                        .from(OFFICE, lateral(select(MANAGER.MANAGER_ID.as("managers_managerId"),
                                MANAGER.MANAGER_NAME.as("managers_managerName"))
                        .from(MANAGER).join(OFFICE_HAS_MANAGER)
                        .on(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .where(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))))
                .orderBy(OFFICE.OFFICE_CODE)
        );

        /* or, like this
        List<SimpleOffice> result = sqMapper2.asList(
                ctx.select(OFFICE.OFFICE_CODE, OFFICE.STATE, OFFICE.CITY,
                        MANAGER.MANAGER_ID.as("managers_managerId"), 
                        MANAGER.MANAGER_NAME.as("managers_managerName"))
                        .from(OFFICE, MANAGER, OFFICE_HAS_MANAGER)
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .and(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .orderBy(OFFICE.OFFICE_CODE)  
        );
         */
        
        return result;
    }
}
