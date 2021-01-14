package com.classicmodels.repository;

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
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.OrderRecord;
import jooq.generated.tables.records.OrderdetailRecord;
import jooq.generated.tables.records.ProductRecord;
import org.jooq.DSLContext;
import org.jooq.lambda.tuple.Tuple2;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.simpleflatmapper.util.TypeReference;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final JdbcMapper<Tuple2<EmployeeRecord, List<Tuple2<CustomerRecord, 
            List<Tuple2<OrderRecord, List<Tuple2<OrderdetailRecord, ProductRecord>>>>>>>> jdbcMapper;
    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {

        this.ctx = ctx;

        this.jdbcMapper
                = JdbcMapperFactory
                        .newInstance()
                        .addKeys("employee_number", "customer_number", "order_id", "order_id", "product_id")
                        .newMapper(new TypeReference<Tuple2<EmployeeRecord, List<Tuple2<CustomerRecord, 
                                List<Tuple2<OrderRecord, List<Tuple2<OrderdetailRecord, ProductRecord>>>>>>>>() {
                        });
    }

    public List<Tuple2<EmployeeRecord, List<Tuple2<CustomerRecord, 
                    List<Tuple2<OrderRecord, List<Tuple2<OrderdetailRecord, ProductRecord>>>>>>>>
            findEmployeeWithCustomersOrdersByOfficeCode(String officeCode) {

        try ( ResultSet rs
                = ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                        CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME,
                        ORDER.ORDER_ID, ORDER.ORDER_DATE, ORDER.SHIPPED_DATE,
                        ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH,
                        PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                        .from(EMPLOYEE)
                        .join(CUSTOMER).on(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER
                        .eq(EMPLOYEE.EMPLOYEE_NUMBER))
                        .join(ORDER)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(ORDER.CUSTOMER_NUMBER))
                        .join(ORDERDETAIL)
                        .on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID))
                        .join(PRODUCT)
                        .on(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                        .where(EMPLOYEE.OFFICE_CODE.eq(officeCode))
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
                        .fetchResultSet()) {

                    Stream<Tuple2<EmployeeRecord, List<Tuple2<CustomerRecord, List<Tuple2<OrderRecord, 
                            List<Tuple2<OrderdetailRecord, ProductRecord>>>>>>>> stream = jdbcMapper.stream(rs);

                    return stream.collect(toList());

                } catch (SQLException ex) {
                    // handle exception (for example, wrap it into a unchecked exception)
                }

                return Collections.emptyList();
    }
}
