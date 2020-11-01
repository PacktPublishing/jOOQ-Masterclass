package com.classicmodels.repository;

import java.math.BigDecimal;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import jooq.generated.tables.pojos.Customer;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class CustomerRepository extends
        jooq.generated.tables.daos.CustomerRepository {

    private final DSLContext ctx;

    public CustomerRepository(DSLContext ctx) {
        super(ctx.configuration());
        this.ctx = ctx;
    }    

    public List<Customer> findCustomersOrderedByCreditLimit() {

        List<Customer> result = ctx.selectFrom(CUSTOMER)
                .where(CUSTOMER.CREDIT_LIMIT.gt(BigDecimal.valueOf(5000)))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(5)
                .fetchInto(Customer.class);

        return result;
    }
}
