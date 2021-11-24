package com.classicmodels.listener;

import org.jooq.ExecuteContext;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.LoggerListener;

public class MyLoggerListener extends DefaultExecuteListener {

    private static final JooqLogger log = JooqLogger.getLogger(LoggerListener.class);

    @Override
    public void renderEnd(ExecuteContext ecx) {

        if (!ecx.configuration().data().containsKey("log_batch")) {

            log.debug("jOOQ LOG START", "");
            log.debug("jOOQ LOG START", "==========================START========================");
            log.debug("jOOQ LOG START", "Dialect:" + ecx.dialect() + " | SQL Type: " + ecx.type());
            log.debug("jOOQ LOG START", "=======================================================");

            if (ecx.batchSQL().length > 1) {
                ecx.configuration().data("log_batch", Boolean.TRUE);
            }
        }
    }

    @Override
    public void end(ExecuteContext ecx) {

        log.debug("jOOQ LOG END", "===========================END=========================\n");
    }
}
