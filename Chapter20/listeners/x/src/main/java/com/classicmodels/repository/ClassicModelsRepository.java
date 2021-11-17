package com.classicmodels.repository;

import com.classicmodels.listener.MyDiagnosticsListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import org.jooq.impl.DefaultDiagnosticsListenerProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final JdbcTemplate jdbcTemplate;

    public ClassicModelsRepository(DSLContext ctx, JdbcTemplate jdbcTemplate) {
        this.ctx = ctx;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void queryOrder() {

        Connection conn = DSL.using("jdbc:mysql://localhost:3306/classicmodels", "root", "root")
                .configuration()
                .set(new DefaultDiagnosticsListenerProvider(new MyDiagnosticsListener()))
                .dsl()
                .diagnosticsConnection();

        SingleConnectionDataSource ds = new SingleConnectionDataSource(conn, true);

        jdbcTemplate.setDataSource(ds);        

        Query query = ctx.selectFrom(table(name("ORDER")))
                .where(field("ORDER_ID").between(10100, 10110));
       
        jdbcTemplate.query(query.getSQL(), (ResultSet resultSet) -> {
            while (resultSet.next()) {
                String orderId = resultSet.getString("order_id");
                System.out.println("order id: " + orderId);
            }
        }, query.getBindValues().toArray());
    }
}
