package com.classicmodels.listener;

import java.util.logging.Logger;
import org.jooq.ExecuteContext;
import org.jooq.Select;
import org.jooq.impl.DefaultExecuteListener;

public class MyExecuteListener extends DefaultExecuteListener {

    private static final Logger logger = Logger.getLogger(MyExecuteListener.class.getName());

    @Override
    public void renderEnd(ExecuteContext ecx) {

        if (ecx.configuration().data().containsKey("timeout_hint_select") &&
                ecx.query() instanceof Select) {

            String sql = ecx.sql();

            if (sql != null) {
                ecx.sql(sql.replace(
                        "select",
                        "select " + ecx.configuration().data().get("timeout_hint_select")
                ));

                logger.info(() -> {
                    return "Executing modified query : " + ecx.sql();
                });
            }
        }
    }
}
