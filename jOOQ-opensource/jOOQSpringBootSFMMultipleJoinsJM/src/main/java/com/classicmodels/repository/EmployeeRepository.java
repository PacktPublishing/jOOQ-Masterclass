package com.classicmodels.repository;

import com.classicmodels.pojo.EmployeeDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

    private final DSLContext create;
    private final JdbcMapper<EmployeeDTO> jdbcMapper;

    public EmployeeRepository(DSLContext create) {
        this.create = create;

        this.jdbcMapper = JdbcMapperFactory
                .newInstance()
                // .addKeys("employeeNumber") // I use @Key in EmployeeDTO
                .newMapper(EmployeeDTO.class);
    }

    public List<EmployeeDTO> findEmployeeWithSalesAndCustomersByOfficeCode(String officeCode) {

        try ( ResultSet rs
                = create.select(EMPLOYEE.EMPLOYEE_NUMBER,
                        EMPLOYEE.FIRST_NAME,
                        EMPLOYEE.LAST_NAME,
                        CUSTOMER.CUSTOMER_NAME.as("customers_customerName"),
                        SALE.SALE_.as("sales_sale"))
                        .from(EMPLOYEE)
                        .leftJoin(CUSTOMER).on(EMPLOYEE.EMPLOYEE_NUMBER
                        .eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER))
                        .leftJoin(SALE).using(EMPLOYEE.EMPLOYEE_NUMBER)
                        .where(EMPLOYEE.OFFICE_CODE.eq(officeCode))
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
                        .fetchResultSet()) {

                    Stream<EmployeeDTO> stream = jdbcMapper.stream(rs);

                    return stream.collect(toList());

                } catch (SQLException ex) {
                    // handle exception (for example, wrap it into a unchecked exception)
                }

                return Collections.emptyList();
    }
}
