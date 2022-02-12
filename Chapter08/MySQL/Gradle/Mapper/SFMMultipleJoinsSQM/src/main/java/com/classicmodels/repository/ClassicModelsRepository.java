package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleEmployee;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.simpleflatmapper.jooq.SelectQueryMapper;
import org.simpleflatmapper.jooq.SelectQueryMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final SelectQueryMapper<SimpleEmployee> sqMapper;
    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
        this.sqMapper = SelectQueryMapperFactory
                .newInstance()
                .newMapper(SimpleEmployee.class);
    }

    public List<SimpleEmployee> findEmployeeWithSalesAndCustomersByOfficeCode(String officeCode) {

        List<SimpleEmployee> result = sqMapper.asList(
                ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME,
                        EMPLOYEE.LAST_NAME, CUSTOMER.CUSTOMER_NAME, SALE.SALE_)
                        .from(EMPLOYEE)
                        .leftOuterJoin(CUSTOMER)
                        .on(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER
                                .eq(EMPLOYEE.EMPLOYEE_NUMBER))
                        .leftOuterJoin(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER
                                .eq(SALE.EMPLOYEE_NUMBER))
                        .where(EMPLOYEE.OFFICE_CODE.eq(officeCode))
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
        );

        return result;
    }
}
