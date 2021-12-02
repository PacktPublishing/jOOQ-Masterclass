package com.classicmodels.listener;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.VisitContext;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.DefaultVisitListener;
import org.jooq.tools.JooqLogger;

public class MyLoggerListener extends DefaultExecuteListener {

    private static final JooqLogger log = JooqLogger.getLogger(MyLoggerListener.class);
    private static final String RESULT_END_EVENT = "result_end_event";
    private static final String RESULT_END_EVENT_CONSUMED = "result_end_event_consumed";

    @Override
    public void renderEnd(ExecuteContext ecx) {

        if (ecx.query() != null && !ecx.configuration().data().isEmpty()) {

            Set<Object> tables = new HashSet<>(
                    ecx.configuration().data().keySet());

            ecx.configuration().data().clear();
           
            Configuration configuration = ecx.configuration()
                    .deriveAppending(new TablesExtractor());

            String inlined = DSL.using(configuration).renderInlined(ecx.query());

            if (configuration.data().keySet().containsAll(tables)) {
                log.debug("Executing query", ecx.sql());
                if (!ecx.sql().equals(inlined)) {
                    log.debug("-> with bind values", inlined);
                }

                ecx.configuration().data().put(RESULT_END_EVENT, true);
            }
        }
    }

    @Override
    public void resultEnd(ExecuteContext ecx) {

        ecx.configuration().data().put(RESULT_END_EVENT_CONSUMED, true);
        
        Result<?> result = ecx.result();

        if (result != null && ecx.configuration().data().containsKey(RESULT_END_EVENT)) {

            logMultiline("Fetched result", result.format(5), Level.FINE);
            log.debug("Total fetched row(s)", result.size());            
        }
    }

    @Override
    public void fetchEnd(ExecuteContext ecx) {
        
        if (!ecx.configuration().data().containsKey(RESULT_END_EVENT_CONSUMED) &&
                ecx.configuration().data().containsKey(RESULT_END_EVENT)) {
            log.debug("Fetched result:", "Cannot display result set for this query");
        }
    }

    @Override
    public void executeEnd(ExecuteContext ecx) {

        if (ecx.rows() >= 0 && ecx.configuration().data().containsKey(RESULT_END_EVENT)) {
            log.debug("Affected row(s)", ecx.rows());
        }
    }

    // copied from jOOQ source code 
    private void logMultiline(String comment, String message, Level level) {
        for (String line : message.split("\n")) {
            if (level == Level.FINE) {
                log.debug(comment, line);
            } else {
                log.trace(comment, line);
            }

            comment = "";
        }
    }

    private static class TablesExtractor extends DefaultVisitListener {

        @Override
        public void visitEnd(VisitContext vcx) {

            if (vcx.renderContext() != null) {
                if (vcx.queryPart() instanceof Table) {

                    Table<?> t = (Table<?>) vcx.queryPart();

                    vcx.configuration().data()
                            .putIfAbsent(t.getQualifiedName(), "");
                }
            }
        }
    }
}
