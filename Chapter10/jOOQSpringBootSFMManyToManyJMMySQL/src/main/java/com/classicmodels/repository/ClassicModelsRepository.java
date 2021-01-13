package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleManager;
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
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final JdbcMapper<SimpleManager> jdbcMapper;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
        this.jdbcMapper = JdbcMapperFactory
                .newInstance()
                // .addKeys("managerId") // I use @Key in SimpleManager
                .newMapper(SimpleManager.class);
    }

    public List<SimpleManager> findManagerAndOffice() {

        try ( ResultSet rs
                = ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                        field("offices_officeCode"), field("offices_city"), field("offices_state"))
                        .from(MANAGER)
                        .join(select(OFFICE.OFFICE_CODE.as("offices_officeCode"),
                                OFFICE.CITY.as("offices_city"), OFFICE.STATE.as("offices_state"),
                                OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                                .from(OFFICE).join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("t"))
                        .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                
                /* or, like this
                = ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                        OFFICE.OFFICE_CODE.as("offices_officeCode"),
                        OFFICE.CITY.as("offices_city"),
                        OFFICE.STATE.as("offices_state"))
                        .from(MANAGER, OFFICE, OFFICE_HAS_MANAGER)
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                        .and(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .orderBy(MANAGER.MANAGER_ID)
                         */
                        .fetchResultSet()) {

                    Stream<SimpleManager> stream = jdbcMapper.stream(rs);

                    return stream.collect(toList());

                } catch (SQLException ex) {
                    // handle exception (for example, wrap it into a unchecked exception)
                }

                return Collections.emptyList();
    }
}
