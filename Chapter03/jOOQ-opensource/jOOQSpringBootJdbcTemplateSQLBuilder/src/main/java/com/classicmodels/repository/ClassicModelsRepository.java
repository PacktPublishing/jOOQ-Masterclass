package com.classicmodels.repository;

import com.classicmodels.pojo.Manager;
import org.jooq.DSLContext;
import org.jooq.Query;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final JdbcTemplate jdbcTemplate;

    public ClassicModelsRepository(DSLContext ctx, JdbcTemplate jdbcTemplate) {
        this.ctx = ctx;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Manager findManager(Long managerId) {

        /* Using only JdbcTemplate */
        /*
        String sql = """
                     SELECT * FROM MANAGER WHERE MANAGER_ID=?
                     """;        
        
        Manager result = (Manager) jdbcTemplate.queryForObject(sql, new Object[]{managerId},
                new BeanPropertyRowMapper(Manager.class));                 
         */
        
        /* Using jOOQ to build the SQL and JdbcTemplate to execute it */
        Query query = ctx.selectFrom(table("MANAGER")) // or, ctx.select().from(table("MANAGER"))
                .where(field("MANAGER_ID").eq(managerId));

        Manager result = (Manager) jdbcTemplate.queryForObject(query.getSQL(),
                query.getBindValues().toArray(), new BeanPropertyRowMapper(Manager.class));

        return result;
    }
}