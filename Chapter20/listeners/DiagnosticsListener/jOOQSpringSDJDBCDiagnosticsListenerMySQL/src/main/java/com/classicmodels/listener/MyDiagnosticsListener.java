package com.classicmodels.listener;

import java.util.logging.Logger;
import org.jooq.DiagnosticsContext;
import org.jooq.impl.DefaultDiagnosticsListener;

public class MyDiagnosticsListener extends DefaultDiagnosticsListener {

    private static final Logger logger = Logger.getLogger(MyDiagnosticsListener.class.getName());

    @Override
    public void repeatedStatements(DiagnosticsContext dcx) {

        logger.info(() -> "This queries are prone to be a N+1 case: \n" + dcx.repeatedStatements());
    }
}
