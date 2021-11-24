package com.classicmodels.listener;

import static java.lang.Boolean.TRUE;
import org.jooq.Configuration;
import org.jooq.Delete;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteType;
import org.jooq.Insert;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.LoggerListener;
import org.jooq.tools.StringUtils;

public class MyLoggerListener extends DefaultExecuteListener {

    private static final JooqLogger log = JooqLogger.getLogger(LoggerListener.class);
    private static final String PATTERN = "^(?i:(INSERT|DELETE).*)$";

    // inspired from jOOQ source code            
    @Override
    public void renderEnd(ExecuteContext ctx) {

        var query = ctx.query();

        Configuration configuration = ctx.configuration();
        String newline = TRUE.equals(configuration.settings().isRenderFormatted()) ? "\n" : "";

        if (query instanceof Insert || query instanceof Delete) {

            log.debug("Executing query", newline + ctx.sql());

            String inlined = DSL.using(configuration).renderInlined(ctx.query());
            if (!ctx.sql().equals(inlined)) {
                log.debug("-> with bind values", newline + inlined);
            }
        } else if (!StringUtils.isBlank(ctx.sql())) {

            if (ctx.sql().matches(PATTERN)) {
                if (ctx.type() == ExecuteType.BATCH) {
                    log.debug("Executing batch query", newline + ctx.sql());
                } else {
                    log.debug("Executing query", newline + ctx.sql());
                }
            }
        } else {
            String[] batchSQL = ctx.batchSQL();
            if (batchSQL[batchSQL.length - 1] != null) {
                for (String sql : batchSQL) {
                    if (sql.matches(PATTERN)) {
                        log.debug("Executing batch query", newline + sql);
                    }
                }
            }
        }
    }
}
