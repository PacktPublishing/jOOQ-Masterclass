package com.classicmodels.test;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import static jooq.generated.tables.Product.PRODUCT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class ClassicmodelsIT {

    private static DSLContext ctx;

    @BeforeAll
    public static void setup() {

        ConnectionFactory connectionFactory = ConnectionFactories.get(
                ConnectionFactoryOptions
                        .parse("r2dbcs:mysql://root:root@localhost:3306/classicmodels?"
                                + "allowMultiQueries=true")
                        .mutate()
                        .option(ConnectionFactoryOptions.USER, "root")
                        .option(ConnectionFactoryOptions.PASSWORD, "root")
                        .build()
        );

        ctx = DSL.using(connectionFactory);
    }

    @Test
    public void givenProductsWhenFetchByQISThenResultsMoreThan50Records() {

        var result = Flux.from(
                ctx.selectFrom(PRODUCT).where(PRODUCT.QUANTITY_IN_STOCK.gt(5000)))
                .collectList()
                .block();

        assertThat(result, hasSize(greaterThan(50)));
    }
}
