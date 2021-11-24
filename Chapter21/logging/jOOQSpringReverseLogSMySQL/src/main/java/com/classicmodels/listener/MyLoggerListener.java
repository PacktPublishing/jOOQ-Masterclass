package com.classicmodels.listener;

import java.util.HashMap;
import java.util.Map;
import org.jooq.ChartFormat;
import org.jooq.ExecuteContext;
import org.jooq.Field;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.LoggerListener;
import org.jooq.Result;
import org.jooq.Select;
import static org.jooq.impl.DSL.name;

public class MyLoggerListener extends DefaultExecuteListener {

    private static final JooqLogger log = JooqLogger.getLogger(LoggerListener.class);

    @Override
    public void resultEnd(ExecuteContext ecx) {

        if (ecx.query() != null && ecx.query() instanceof Select) {

            Result<?> result = ecx.result();

            if (result != null && !result.isEmpty()) {

                Map<String, Integer> positions = new HashMap<>();

                Field<?>[] fields = result.fields();
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].getQualifiedName()
                            .equalsIgnoreCase(name("product", "product_id"))) {
                        positions.put("category", i);
                    }
                    if (fields[i].getQualifiedName()
                            .equalsIgnoreCase(name("product","buy_price"))) {
                        positions.put("values", i);
                    }
                }

                if (positions.size() == 2) {

                    ChartFormat cf = new ChartFormat()
                            .category(positions.get("category"))
                            .values(positions.get("values"))
                            .shades('x');

                    String[] chart = result.formatChart(cf).split("\n");

                    log.debug("Start Chart", "");

                    for (int i = 0; i < chart.length; i++) {
                        log.debug("", chart[i]);
                    }

                    log.debug("End Chart", "");
                } else {
                    log.debug("Chart", "The chart cannot be constructed (missing data)");
                }
            }
        }
    }
}
