package com.classicmodels.listener;

import org.jooq.ExecuteContext;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.LoggerListener;
import java.util.logging.Level;
import org.jooq.Result;

public class MyLoggerListener extends DefaultExecuteListener {

    private static final JooqLogger log = JooqLogger.getLogger(LoggerListener.class);

    @Override
    public void resultEnd(ExecuteContext ecx) {

        Result<?> result = ecx.result();

        if (result != null) {

            logMultiline("Total Fetched result", result.format(), Level.FINE, result.size());
            log.debug("Total fetched row(s)", result.size());
        }
    }

    // inspired from jOOQ source code 
    private void logMultiline(String comment, String message, Level level, int size) {

        int pos = -2;
        for (String line : message.split("\n")) {
            if (level == Level.FINE) {
                if (pos > 0 && pos <= size) {
                    log.debug(comment, line + " # " + pos);
                } else {
                    log.debug(comment, line);
                }
            } else {
                if (pos > 0 && pos <= size) {
                    log.trace(comment, line + " # " + pos);
                } else {
                    log.trace(comment, line);
                }
            }

            comment = "";
            pos++;
        }
    }
}
