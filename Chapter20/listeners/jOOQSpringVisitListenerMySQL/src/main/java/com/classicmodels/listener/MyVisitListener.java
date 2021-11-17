package com.classicmodels.listener;

import static org.jooq.Clause.CREATE_VIEW_AS; // this is marked as @Deprecated
import org.jooq.VisitContext;
import org.jooq.impl.DefaultVisitListener;

public class MyVisitListener extends DefaultVisitListener {
  
    @Override
    public void clauseEnd(VisitContext context) {

        if (context.clause().equals(CREATE_VIEW_AS)) {

            context.context()
                    .formatSeparator()
                    .sql("WITH CHECK OPTION");
        }
    }
}
