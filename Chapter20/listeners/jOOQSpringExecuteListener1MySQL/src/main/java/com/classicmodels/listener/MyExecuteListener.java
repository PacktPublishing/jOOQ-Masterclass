package com.classicmodels.listener;

import java.util.logging.Logger;
import org.jooq.ExecuteContext;
import org.jooq.Select;
import org.jooq.impl.DefaultExecuteListener;

public class MyExecuteListener extends DefaultExecuteListener {

    private static final Logger logger = Logger.getLogger(MyExecuteListener.class.getName());

    @Override
    public void renderEnd(ExecuteContext ctx) {

        if (ctx.configuration().data().containsKey("timeout_hint_select") &&
                ctx.query() instanceof Select) {

            String sql = ctx.sql();

            if (sql != null) {
                ctx.sql(sql.replace(
                        "select",
                        "select " + ctx.configuration().data().get("timeout_hint_select")
                ));

                logger.info(() -> {
                    return "Executing modified query : " + ctx.sql();
                });
            }
        }
    }
}
