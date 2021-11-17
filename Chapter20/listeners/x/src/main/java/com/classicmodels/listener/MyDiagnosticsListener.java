package com.classicmodels.listener;

import java.util.logging.Logger;
import org.jooq.DiagnosticsContext;
import org.jooq.impl.DefaultDiagnosticsListener;

public class MyDiagnosticsListener extends DefaultDiagnosticsListener {    

    private static final Logger logger = Logger.getLogger(MyDiagnosticsListener.class.getName());   
    
    @Override
    public void repeatedStatements(DiagnosticsContext ctx) {
        
        logger.info(() -> "This queries are prone to be a N+1 case: \n" + ctx.repeatedStatements());        
    }

    @Override
    public void duplicateStatements(DiagnosticsContext ctx) {
        System.out.println("11111111111111111111111111111111111111111111");
    }

    @Override
    public void tooManyColumnsFetched(DiagnosticsContext ctx) {
        System.out.println("22222222222222222222222222222222222222222222");
    }

    @Override
    public void tooManyRowsFetched(DiagnosticsContext ctx) {
        System.out.println("999999999999999999999999999999999999999999999");
    }
    
    
}
