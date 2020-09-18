package com.classicmodels.repository;

import com.classicmodels.pojo.ManagerDTO;
import java.util.List;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import org.jooq.DSLContext;
import org.simpleflatmapper.jooq.SelectQueryMapper;
import org.simpleflatmapper.jooq.SelectQueryMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ManagerRepository {

    private final DSLContext create;
    private final SelectQueryMapper<ManagerDTO> customerMapper;

    public ManagerRepository(DSLContext create) {
        this.create = create;
        this.customerMapper = SelectQueryMapperFactory
                .newInstance()
                .newMapper(ManagerDTO.class);
    }

    public List<ManagerDTO> findCustomerByCreditLimit(float creditLimit) {

        List<ManagerDTO> customers = customerMapper.asList(
                create.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                        OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE)
                        .from(MANAGER, OFFICE, OFFICE_HAS_MANAGER)
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .and(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .orderBy(MANAGER.MANAGER_ID)
        );

        return customers;
    }
}
