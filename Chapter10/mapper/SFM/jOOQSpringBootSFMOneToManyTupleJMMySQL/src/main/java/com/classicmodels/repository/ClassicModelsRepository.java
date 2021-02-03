package com.classicmodels.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.records.ProductRecord;
import jooq.generated.tables.records.ProductlineRecord;
import org.jooq.DSLContext;
import org.jooq.lambda.tuple.Tuple2;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.simpleflatmapper.util.TypeReference;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final JdbcMapper<Tuple2<ProductlineRecord, List<ProductRecord>>> jdbcMapper;
    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {

        this.ctx = ctx;

        this.jdbcMapper
                = JdbcMapperFactory
                        .newInstance()
                        .addKeys("product_line")
                        .newMapper(new TypeReference<Tuple2<ProductlineRecord, List<ProductRecord>>>() {});
    }

    public List<Tuple2<ProductlineRecord, List<ProductRecord>>> findProductLineWithProducts() {

        try ( ResultSet rs
                = ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION, PRODUCT.PRODUCT_NAME,
                        PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCT).using(PRODUCT.PRODUCT_LINE)
                        .orderBy(PRODUCTLINE.PRODUCT_LINE)
                        .fetchResultSet()) {

                    Stream<Tuple2<ProductlineRecord, List<ProductRecord>>> stream = jdbcMapper.stream(rs);

                    return stream.collect(toList());

                } catch (SQLException ex) {
                    // handle exception (for example, wrap it into a unchecked exception)
                }

                return Collections.emptyList();
    }
}
