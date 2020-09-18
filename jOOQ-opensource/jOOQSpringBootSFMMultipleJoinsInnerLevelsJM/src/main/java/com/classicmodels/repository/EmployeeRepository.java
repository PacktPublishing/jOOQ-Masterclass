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
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.springframework.stereotype.Repository;
  
@Repository
public class EmployeeRepository {

    private final JdbcMapper<EmployeeDTO> employeeMapper;
    private final DSLContext create;

    public EmployeeRepository(DSLContext create) {
        this.create = create;
        this.employeeMapper = JdbcMapperFactory
                .newInstance()
                // I use @Key in EmployeeDTO, CustomerDTO and OrderDTO
                //.addKeys("employeeNumber", "customers_customerNumber", "customers_orders_orderId") 
                .newMapper(EmployeeDTO.class);
    }

    public List<EmployeeDTO> findEmployeeWithSalesAndCustomersByOfficeCode(String officeCode) {

        try ( ResultSet rs
                = create.select(EMPLOYEE.EMPLOYEE_NUMBER.as("employeeNumber"),                        
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
                        .leftOuterJoin(ORDER).using(ORDER.CUSTOMER_NUMBER)                       
                        .leftOuterJoin(ORDERDETAIL).using(ORDERDETAIL.ORDER_ID)
                        .leftOuterJoin(PRODUCT).using(PRODUCT.PRODUCT_ID)
                        .where(EMPLOYEE.OFFICE_CODE.eq(officeCode))
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
                        .fetchResultSet()) {

                    Stream<EmployeeDTO> stream = employeeMapper.stream(rs);

                    return stream.collect(toList());

                } catch (SQLException ex) {
                    // handle exception (for example, wrap it into a unchecked exception)
                }

                return Collections.emptyList();
    }
}
