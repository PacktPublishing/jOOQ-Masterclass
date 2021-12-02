package com.classicmodels.listener;

import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.ChartFormat;
import org.jooq.ExecuteContext;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.LoggerListener;
import org.jooq.Result;
import org.jooq.Select;

public class MyLoggerListener extends DefaultExecuteListener {

    private static final JooqLogger log = JooqLogger.getLogger(LoggerListener.class);

    @Override
    public void resultEnd(ExecuteContext ecx) {

        if (ecx.query() != null && ecx.query() instanceof Select) {

            Result<?> result = ecx.result();

            if (result != null && !result.isEmpty()) {

                if (result.field(PRODUCT.PRODUCT_ID) != null
                        && result.field(PRODUCT.BUY_PRICE) != null) {

                    ChartFormat cf = new ChartFormat()
                            .category(result.indexOf(PRODUCT.PRODUCT_ID))
                            .values(result.indexOf(PRODUCT.BUY_PRICE))
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
