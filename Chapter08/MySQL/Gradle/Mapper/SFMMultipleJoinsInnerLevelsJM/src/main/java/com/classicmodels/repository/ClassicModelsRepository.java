package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleEmployee;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final JdbcMapper<SimpleEmployee> jdbcMapper;
    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
        this.jdbcMapper = JdbcMapperFactory
                .newInstance()
                // I use @Key in SimpleEmployee, SimepleCustomer and SimpleOrder
                //.addKeys("employeeNumber", "customers_customerNumber", "customers_orders_orderId") 
                .newMapper(SimpleEmployee.class);
    }

    public List<SimpleEmployee> findEmployeeWithCustomersOrdersByOfficeCode(String officeCode) {

        try ( ResultSet rs
                = ctx.select(EMPLOYEE.EMPLOYEE_NUMBER.as("employeeNumber"),
                             EMPLOYEE.FIRST_NAME,
                             EMPLOYEE.LAST_NAME,
                             CUSTOMER.CUSTOMER_NUMBER.as("customers_customerNumber"),
                             CUSTOMER.CUSTOMER_NAME.as("customers_customerName"),
                             ORDER.ORDER_ID.as("customers_orders_orderId"),
                             ORDER.ORDER_DATE.as("customers_orders_orderDate"),
                             ORDER.SHIPPED_DATE.as("customers_orders_shippedDate"),
                             ORDERDETAIL.QUANTITY_ORDERED.as("customers_orders_details_quantityOrdered"),
                             ORDERDETAIL.PRICE_EACH.as("customers_orders_details_priceEach"),
                             PRODUCT.PRODUCT_ID.as("customers_orders_details_productId"),
                             PRODUCT.PRODUCT_NAME.as("customers_orders_details_product_productName"))
                        .from(EMPLOYEE)
                        .leftOuterJoin(CUSTOMER).on(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER
                        .eq(EMPLOYEE.EMPLOYEE_NUMBER))
                        .leftOuterJoin(ORDER)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(ORDER.CUSTOMER_NUMBER))
                        .leftOuterJoin(ORDERDETAIL)
                        .on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID))
                        .leftOuterJoin(PRODUCT)
                        .on(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                        .where(EMPLOYEE.OFFICE_CODE.eq(officeCode))
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
                        .fetchResultSet()) {

                    Stream<SimpleEmployee> stream = jdbcMapper.stream(rs);

                    return stream.collect(toList());

                } catch (SQLException ex) {
                    // handle exception (for example, wrap it into a unchecked exception)
                }

                return Collections.emptyList();
    }
}
