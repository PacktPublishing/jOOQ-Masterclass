package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleManager;
import java.util.List;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import org.simpleflatmapper.jooq.SelectQueryMapper;
import org.simpleflatmapper.jooq.SelectQueryMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final SelectQueryMapper<SimpleManager> sqMapper;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
        this.sqMapper = SelectQueryMapperFactory
                .newInstance()
                .newMapper(SimpleManager.class);
    }

    public List<SimpleManager> findManagerAndOffice() {

        List<SimpleManager> result = sqMapper.asList(
                ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                        field("officeCode"), field("city"), field("state"))
                        .from(MANAGER)
                        .join(select(OFFICE.OFFICE_CODE.as("officeCode"),
                                OFFICE.CITY.as("city"), OFFICE.STATE.as("state"),
                                OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                                .from(OFFICE).join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("t"))
                        .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
        );

        /* or, like this
        List<SimpleManager> result = sqMapper.asList(
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
}
