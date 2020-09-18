package com.classicmodels.repository;

import com.classicmodels.pojo.EmployeeDTO;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.simpleflatmapper.jooq.SelectQueryMapper;
import org.simpleflatmapper.jooq.SelectQueryMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

    private final SelectQueryMapper<EmployeeDTO> employeeMapper;
    private final DSLContext create;

    public EmployeeRepository(DSLContext create) {
        this.create = create;
        this.employeeMapper = SelectQueryMapperFactory
                .newInstance()
                .newMapper(EmployeeDTO.class);
    }

    public List<EmployeeDTO> findEmployeeWithSalesAndCustomersByOfficeCode(String officeCode) {

        List<EmployeeDTO> employees = employeeMapper.asList(
                create.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                        CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME,
                        ORDER.ORDER_ID, ORDER.ORDER_DATE, ORDER.SHIPPED_DATE,
                        ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH,
                        PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                        .from(EMPLOYEE)
                        .leftOuterJoin(CUSTOMER).on(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER
                        .eq(EMPLOYEE.EMPLOYEE_NUMBER))
                        .leftOuterJoin(ORDER).using(ORDER.CUSTOMER_NUMBER)
                        .leftOuterJoin(ORDERDETAIL).using(ORDERDETAIL.ORDER_ID)
                        .leftOuterJoin(PRODUCT).using(PRODUCT.PRODUCT_ID)
                        .where(EMPLOYEE.OFFICE_CODE.eq(officeCode))
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
        );

        return employees;
    }
}
