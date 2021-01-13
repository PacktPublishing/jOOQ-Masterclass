package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleCustomer;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import org.jooq.DSLContext;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final JdbcMapper<SimpleCustomer> jdbcMapper;
    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
        this.jdbcMapper = JdbcMapperFactory
                .newInstance()
                .newMapper(SimpleCustomer.class);
    }

    public List<SimpleCustomer> findCustomerByCreditLimit(float creditLimit) {

        try ( ResultSet rs
                = ctx.select(CUSTOMER.CUSTOMER_NAME,
                        CUSTOMER.PHONE,
                        CUSTOMER.CREDIT_LIMIT,
                        CUSTOMERDETAIL.ADDRESS_LINE_FIRST.as("details_addressLineFirst"),
                        CUSTOMERDETAIL.STATE.as("details_state"),
                        CUSTOMERDETAIL.CITY.as("details_city"))
                        .from(CUSTOMER)
                        .innerJoin(CUSTOMERDETAIL)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                        .where(CUSTOMER.CREDIT_LIMIT.le(BigDecimal.valueOf(creditLimit)))
                        .orderBy(CUSTOMER.CUSTOMER_NUMBER)
                        .fetchResultSet()) {

                    Stream<SimpleCustomer> stream = jdbcMapper.stream(rs);

                    return stream.collect(toList());

                } catch (SQLException ex) {
                    // handle exception (for example, wrap it into a unchecked exception)
                }

                return Collections.emptyList();
    }
}
