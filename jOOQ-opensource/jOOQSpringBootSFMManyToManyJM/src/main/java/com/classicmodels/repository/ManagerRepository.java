package com.classicmodels.repository;

import com.classicmodels.pojo.ManagerDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import org.jooq.DSLContext;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ManagerRepository {

    private final DSLContext create;
    private final JdbcMapper<ManagerDTO> customerMapper;

    public ManagerRepository(DSLContext create) {
        this.create = create;
        this.customerMapper = JdbcMapperFactory
                .newInstance()
                // .addKeys("managerId") // I use @Key in ManagerDTO
                .newMapper(ManagerDTO.class);
    }

    public List<ManagerDTO> findCustomerByCreditLimit(float creditLimit) {

        try ( ResultSet rs
                = create.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                        OFFICE.OFFICE_CODE.as("offices_officeCode"),
                        OFFICE.CITY.as("offices_state"), 
                        OFFICE.STATE.as("offices_city"))
                        .from(MANAGER, OFFICE, OFFICE_HAS_MANAGER)
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .and(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .orderBy(MANAGER.MANAGER_ID)
                        .fetchResultSet()) {

                    Stream<ManagerDTO> stream = customerMapper.stream(rs);

                    return stream.collect(toList());

                } catch (SQLException ex) {
                    // handle exception (for example, wrap it into a unchecked exception)
                }

                return Collections.emptyList();
    }
}
