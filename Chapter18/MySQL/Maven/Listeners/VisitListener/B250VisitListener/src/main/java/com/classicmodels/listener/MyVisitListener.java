package com.classicmodels.listener;

import static org.jooq.Clause.CREATE_VIEW_AS; // this is marked as @Deprecated
import org.jooq.VisitContext;
import org.jooq.impl.DefaultVisitListener;

public class MyVisitListener extends DefaultVisitListener {
  
    @Override
    public void clauseEnd(VisitContext vcx) {

        if (vcx.clause().equals(CREATE_VIEW_AS)) {

            vcx.context()
                    .formatSeparator()
                    .sql("WITH CHECK OPTION");
        }
    }
}
