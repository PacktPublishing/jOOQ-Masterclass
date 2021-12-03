package com.classicmodels.test;

import static jooq.generated.tables.Product.PRODUCT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import org.jooq.DSLContext;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.RenderNameCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class ClassicmodelsIT {

    @Autowired
    private DSLContext ctx;

    @BeforeAll
    public void setup() {

        ctx.settings()
                // .withExecuteLogging(Boolean.FALSE)
                .withRenderNameCase(RenderNameCase.UPPER)
                .withRenderMapping(new RenderMapping()
                        .withSchemata(
                                new MappedSchema().withInput("classicmodels")
                                        .withOutput("PUBLIC")));
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
