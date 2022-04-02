package com.classicmodels.listener;

import org.jooq.ExecuteContext;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.LoggerListener;

public class MyLoggerListener extends DefaultExecuteListener {

    private static final JooqLogger log = JooqLogger.getLogger(LoggerListener.class);

    @Override
    public void renderEnd(ExecuteContext ecx) {

        var query = ecx.query();

        if (query != null) {
            var params = query.getParams();

            if (!params.isEmpty()) {
                var entries = params.entrySet();
                entries.forEach(e -> log.debug("binding parameter",
                        "[" + e.getKey() + "] as " + e.getValue().getDataType() + 
                                " - [" + e.getValue() + "]"));
            }
        }
    }
}
