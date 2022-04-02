package com.classicmodels.test.mock.provider;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;

public class ClassicmodelsMockProvider implements MockDataProvider {

    private static final String ACCEPTED_SQL = "(SELECT|UPDATE|INSERT|DELETE).*";

    private static final String SELECT_ONE_RESULT_ONE_RECORD
            = "select `classicmodels`.`product`.`product_id`, `classicmodels`.`product`.`product_name` from `classicmodels`.`product` where `classicmodels`.`product`.`product_id` = ?";
    private static final String SELECT_ONE_RESULT_THREE_RECORDS
            = "select `classicmodels`.`product`.`product_id`, `classicmodels`.`product`.`product_name`, `classicmodels`.`product`.`quantity_in_stock` from `classicmodels`.`product` where `classicmodels`.`product`.`quantity_in_stock` > ? limit ?";
    private static final String UPDATE_SELECT_THREE_RESULTS
            = "update employee set employee.job_title='Sales Manager (NA)' where employee.employee_number=?;select employee.job_title from employee where employee.employee_number=?;select office.city from office where office.office_code=?";
    private static final String SELECT_RESULT_BIND_VALUES
            = "select `classicmodels`.`order`.`customer_number`, `classicmodels`.`order`.`status` from `classicmodels`.`order` where (`classicmodels`.`order`.`required_date` between ? and ? and `classicmodels`.`order`.`order_id` = ?)";
    private static final String UPDATE_RESULT_BIND_VALUES
            = "update `classicmodels`.`sale` set `classicmodels`.`sale`.`fiscal_year` = ? where `classicmodels`.`sale`.`fiscal_year` > ?";

    private static final String BATCH_INSERT_PART
            = "insert into `classicmodels`.`sale` (`sale_id`, `fiscal_year`, `employee_number`, `sale`, `fiscal_month`, `revenue_growth`) values";
    private static final String BATCH_DELETE_PART
            = "delete from `classicmodels`.`sale` where `classicmodels`.`sale`.`sale`";

    @Override
    public MockResult[] execute(MockExecuteContext mex) throws SQLException {

        // The DSLContext can be used to create org.jooq.Result and org.jooq.Record objects
        DSLContext ctx = DSL.using(SQLDialect.MYSQL);
        MockResult[] mock = new MockResult[3];

        // The execute context contains SQL string(s), bind values, and other meta-data
        String sql = mex.sql();

        // Exceptions are propagated through the JDBC and jOOQ APIs
        if (!sql.toUpperCase().matches(ACCEPTED_SQL)) {
            throw new SQLException("Statement not supported: " + sql);
            
        } // From this point forward, you decide, whether any given statement returns results, and how many
        else if (sql.equals(SELECT_ONE_RESULT_ONE_RECORD)) {

            Result<Record2<Long, String>> result
                    = ctx.newResult(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME);
            result.add(ctx
                    .newRecord(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                    .values(1L, "2002 Suzuki XREO"));

            mock[0] = new MockResult(-1, result);

        } else if (sql.equals(SELECT_ONE_RESULT_THREE_RECORDS)) {

            Result<Record3<Long, String, Integer>> result
                    = ctx.newResult(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK);
            result.add(ctx
                    .newRecord(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK)
                    .values(10L, "2002 Suzuki XREO", 9997));
            result.add(ctx
                    .newRecord(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK)
                    .values(11L, "1969 Corvair Monza", 6906));
            result.add(ctx
                    .newRecord(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK)
                    .values(12L, "1968 Dodge Charger", 9123));

            mock[0] = new MockResult(-1, result);

        } else if (sql.equals(UPDATE_SELECT_THREE_RESULTS)) {

            mock[0] = new MockResult(1); // 1 row is affected by update, no results

            Result<Record1<String>> result2
                    = ctx.newResult(EMPLOYEE.JOB_TITLE);
            result2.add(ctx
                    .newRecord(EMPLOYEE.JOB_TITLE)
                    .values("Sales Manager (NA)"));
            mock[1] = new MockResult(-1, result2);

            Result<Record1<String>> result3
                    = ctx.newResult(OFFICE.CITY);
            result3.add(ctx
                    .newRecord(OFFICE.CITY)
                    .values("San Francisco"));
            mock[2] = new MockResult(-1, result3);

        } else if (sql.equals(SELECT_RESULT_BIND_VALUES)) {

            Object[] bindings = mex.bindings();

            if (bindings != null && bindings.length == 3) {

                LocalDate startDate = ((Date) bindings[0]).toLocalDate();
                LocalDate endDate = ((Date) bindings[1]).toLocalDate();

                Result<Record2<Long, String>> result
                        = ctx.newResult(ORDER.CUSTOMER_NUMBER, ORDER.STATUS);

                if (startDate.isBefore(LocalDate.now()) && endDate.isBefore(LocalDate.now())) {

                    result.add(ctx
                            .newRecord(ORDER.CUSTOMER_NUMBER, ORDER.STATUS)
                            .values(145L, "Shipped"));
                } else if (startDate.isBefore(LocalDate.now()) && endDate.isAfter(LocalDate.now())) {

                    result.add(ctx
                            .newRecord(ORDER.CUSTOMER_NUMBER, ORDER.STATUS)
                            .values(145L, "In Process"));
                } else {
                    throw new SQLException("Statement has improper bind values: " + sql);
                }

                mock[0] = new MockResult(-1, result);
            }
        } else if (sql.equals(UPDATE_RESULT_BIND_VALUES)) {

            Object[] bindings = mex.bindings();

            if (bindings != null && bindings.length == 2) {

                int year = (int) bindings[1]; // for,...where(SALE.FISCAL_YEAR.gt(2003), year=2003

                if (year < 2000) {
                    mock[0] = new MockResult(100); // 100 rows should be affected if year < 2000
                } else {
                    mock[0] = new MockResult(250); // 250 rows should be affected if year >= 2000
                }
            } else {
                mock[0] = new MockResult(0);
            }

        } else if (mex.batch()) {

            String[] sqls = mex.batchSQL();
            MockResult[] mockbatch = new MockResult[sqls.length];

            for (int i = 0; i < sqls.length; i++) {
                if (sqls[i].startsWith(BATCH_INSERT_PART)) {
                    mockbatch[i] = new MockResult(1);
                } else if (sqls[i].startsWith(BATCH_DELETE_PART)) {
                    mockbatch[i] = new MockResult(10);
                }
            }

            return mockbatch;
        }

        return mock;
    }
}
