package com.classicmodels.jooq.repository;

import java.math.BigDecimal;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import jooq.generated.tables.pojos.Customer;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final DSLContext ctx;

    public CustomerRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public List<Customer> findCustomerByPhone(String phone) {

        List<Customer> result = ctx.selectFrom(CUSTOMER)
                .where(CUSTOMER.PHONE.eq(phone))
                .fetchInto(Customer.class);

        return result;

    }

    @Override
    public List<Customer> findCustomersOrderedByCreditLimit() {

        List<Customer> result = ctx.selectFrom(CUSTOMER)
                .where(CUSTOMER.CREDIT_LIMIT.gt(BigDecimal.valueOf(5000)))
                .orderBy(CUSTOMER.CREDIT_LIMIT)
                .limit(5)
                .fetchInto(Customer.class);

        return result;
    }

    @Override
    public List<Customer> findLimitedTo(int value) {

        List<Customer> result = ctx.selectFrom(CUSTOMER)
                .limit(value)
                .fetchInto(Customer.class);

        return result;
    }
}
