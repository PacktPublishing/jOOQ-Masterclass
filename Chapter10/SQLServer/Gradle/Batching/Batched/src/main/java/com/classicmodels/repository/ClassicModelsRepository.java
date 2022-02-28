package com.classicmodels.repository;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.tools.jdbc.BatchedConnection;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void batchedInsertsAndUpdates1() {

        ctx.batched(this::insertsAndUpdates);
    }

    public void batchedInsertsAndUpdates2() {

        ctx.batched((Configuration c) -> {
            inserts(c);
            updates(c);
        });
    }

    public void batchedAndReturn() {

        String result = ctx.batchedResult((Configuration c) -> {
            return insertsAndReturn(c);
        });

        System.out.println("EXAMPLE 1: " + result);
    }

    private void insertsAndUpdates(Configuration c) {

        DSLContext ctxLocal = c.dsl();

        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2005, 1370L, 1282.64, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 1370L, 3938.24, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 1370L, 4676.14, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2003, 1166L, 2223.0, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 1166L, 4531.35, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 1166L, 6751.33, 1, 0.0).execute();

        ctxLocal.update(EMPLOYEE)
                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(1_000))
                .where(EMPLOYEE.SALARY.between(100_000, 120_000)).execute();
        ctxLocal.update(EMPLOYEE)
                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(5_000))
                .where(EMPLOYEE.SALARY.between(65_000, 80_000)).execute();
        ctxLocal.update(EMPLOYEE)
                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(10_000))
                .where(EMPLOYEE.SALARY.between(55_000, 60_000)).execute();

        ctxLocal.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                .values(10L, "Toga", "Alison", "x3332", "talison@classicmodelcars.com", "1", 110000, "VP Sales")
                .onConflictDoNothing().execute();
        ctxLocal.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                .values(11L, "Marius", "Pologa", "x5332", "mpologa@classicmodelcars.com", "3", 50000, "Sales Rep")
                .onConflictDoNothing().execute();
        ctxLocal.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                .values(12L, "Ana", "Arica", "x5111", "aarica@classicmodelcars.com", "3", 50000, "Sales Rep")
                .onConflictDoNothing().execute();
    }

    private void inserts(Configuration c) {

        DSLContext ctxLocal = c.dsl();

        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2005, 1370L, 1282.64, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 1370L, 3938.24, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 1370L, 4676.14, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2003, 1166L, 2223.0, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 1166L, 4531.35, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 1166L, 6751.33, 1, 0.0).execute();

        ctxLocal.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                .values(10L, "Toga", "Alison", "x3332", "talison@classicmodelcars.com", "1", 110000, "VP Sales")
                .onConflictDoNothing().execute();
        ctxLocal.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                .values(11L, "Marius", "Pologa", "x5332", "mpologa@classicmodelcars.com", "3", 50000, "Sales Rep")
                .onConflictDoNothing().execute();
        ctxLocal.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                .values(12L, "Ana", "Arica", "x5111", "aarica@classicmodelcars.com", "3", 50000, "Sales Rep")
                .onConflictDoNothing().execute();
    }

    private String insertsAndReturn(Configuration c) {

        DSLContext ctxLocal = c.dsl();

        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2005, 1370L, 1282.64, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 1370L, 3938.24, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 1370L, 4676.14, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2003, 1166L, 2223.0, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 1166L, 4531.35, 1, 0.0).execute();
        ctxLocal.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 1166L, 6751.33, 1, 0.0).execute();

        ctxLocal.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                .values(13L, "Toga", "Alison", "x3332", "talison@classicmodelcars.com", "1", 110000, "VP Sales")
                .onConflictDoNothing().execute();
        ctxLocal.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                .values(14L, "Marius", "Pologa", "x5332", "mpologa@classicmodelcars.com", "3", 50000, "Sales Rep")
                .onConflictDoNothing().execute();
        ctxLocal.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                .values(15L, "Ana", "Arica", "x5111", "aarica@classicmodelcars.com", "3", 50000, "Sales Rep")
                .onConflictDoNothing().execute();

        return "success";
    }

    private void updates(Configuration c) {

        DSLContext ctxLocal = c.dsl();

        ctxLocal.update(EMPLOYEE)
                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(1_000))
                .where(EMPLOYEE.SALARY.between(100_000, 120_000)).execute();
        ctxLocal.update(EMPLOYEE)
                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(5_000))
                .where(EMPLOYEE.SALARY.between(65_000, 80_000)).execute();
        ctxLocal.update(EMPLOYEE)
                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(10_000))
                .where(EMPLOYEE.SALARY.between(55_000, 60_000)).execute();
    }

    public void batchedRecords() {

        // ad-hoc batching
        ctx.batched(c -> {

            Result<SaleRecord> records = c.dsl().selectFrom(SALE)
                    .limit(5)
                    .fetch();

            records.forEach(record -> {
                record.setTrend("CONSTANT");
                // ...
                record.store();
            });
        });
        
        // ad-hoc batching
        List<SaleRecord> sales = List.of(
                new SaleRecord(1L, 2005, 1223.23, 1370L, null, null, null, 1, 0.0, null),
                new SaleRecord(2L, 2004, 543.33, 1166L, null, null, null, 1, 0.0, null),
                new SaleRecord(1005L, 2005, 9022.21, 1370L, null, null, null, 1, 0.0, null),
                new SaleRecord(2005L, 2003, 4333.22, 1504L, null, null, null, 1, 0.0, null),
                new SaleRecord(3005L, 2003, 8002.22, 1504L, null, null, null, 1, 0.0, null)
        );
    }

    // use BatchedConnection    
    public void batchedConnectionUsage() {

        try ( BatchedConnection conn = new BatchedConnection(DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=classicmodels", "sa", "root"), 2)) {

            String sql1 = "insert into [sale] ([fiscal_year], [employee_number], [sale], [fiscal_month], [revenue_growth]) "
                    + "values (?, ?, ?, ?, ?)";

            // the next 2 statements will become the first batch                        
            try ( PreparedStatement stmt = conn.prepareStatement(sql1)) {
                stmt.setInt(1, 2004);
                stmt.setLong(2, 1166L);
                stmt.setDouble(3, 543.33);
                stmt.setInt(4, 1);
                stmt.setDouble(5, 0.0);
                stmt.executeUpdate();
            }

            try ( PreparedStatement stmt = conn.prepareStatement(sql1)) {
                stmt.setInt(1, 2005);
                stmt.setLong(2, 1370L);
                stmt.setDouble(3, 9022.20);
                stmt.setInt(4, 1);
                stmt.setDouble(5, 0.0);
                stmt.executeUpdate();
            }

            // reached batch limit so this is the second batch
            try ( PreparedStatement stmt = conn.prepareStatement(sql1)) {
                stmt.setInt(1, 2003);
                stmt.setLong(2, 1166L);
                stmt.setDouble(3, 3213.0);
                stmt.setInt(4, 1);
                stmt.setDouble(5, 0.0);
                stmt.executeUpdate();
            }

            // since the following SQL string is different, next statements represents the third batch
            String sql2 = "insert into [sale] ([fiscal_year], [employee_number], [sale], [fiscal_month], [revenue_growth], [trend]) "
                    + "values (?, ?, ?, ?, ?, ?)";

            try ( PreparedStatement stmt = conn.prepareStatement(sql2)) {
                stmt.setInt(1, 2004);
                stmt.setLong(2, 1166L);
                stmt.setDouble(3, 4541.35);
                stmt.setInt(4, 1);
                stmt.setDouble(5, 0.0);
                stmt.setString(6, "UP");
                stmt.executeUpdate();
            }

            try ( PreparedStatement stmt = conn.prepareStatement(sql2)) {
                stmt.setInt(1, 2005);
                stmt.setLong(2, 1370L);
                stmt.setDouble(3, 1282.64);
                stmt.setInt(4, 1);
                stmt.setDouble(5, 0.0);
                stmt.setString(6, "DOWN");
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        // same thing, less verbose
        try ( BatchedConnection conn = new BatchedConnection(DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=classicmodels", "sa", "root"), 2)) {

            try ( PreparedStatement stmt = conn.prepareStatement("insert into [sale] ([fiscal_year], [employee_number], [sale], [fiscal_month], [revenue_growth]) "
                    + "values (?, ?, ?, ?, ?);")) {

                // the next 2 statements will become the first batch                        
                stmt.setInt(1, 2004);
                stmt.setLong(2, 1166L);
                stmt.setDouble(3, 543.33);
                stmt.setInt(4, 1);
                stmt.setDouble(5, 0.0);
                stmt.executeUpdate();

                stmt.setInt(1, 2005);
                stmt.setLong(2, 1370L);
                stmt.setDouble(3, 9022.20);
                stmt.setInt(4, 1);
                stmt.setDouble(5, 0.0);
                stmt.executeUpdate();

                // reached batch limit so this is the second batch
                stmt.setInt(1, 2003);
                stmt.setLong(2, 1166L);
                stmt.setDouble(3, 3213.0);
                stmt.setInt(4, 1);
                stmt.setDouble(5, 0.0);
                stmt.executeUpdate();
            }

            // since the following SQL string is different, next statements represents the third batch
            try ( PreparedStatement stmt = conn.prepareStatement("insert into [sale] ([fiscal_year], [employee_number], [sale], [fiscal_month], [revenue_growth], [trend]) "
                    + "values (?, ?, ?, ?, ?, ?);")) {

                stmt.setInt(1, 2004);
                stmt.setLong(2, 1166L);
                stmt.setDouble(3, 4541.35);
                stmt.setInt(4, 1);
                stmt.setDouble(5, 0.0);
                stmt.setString(6, "UP");
                stmt.executeUpdate();

                stmt.setInt(1, 2005);
                stmt.setLong(2, 1370L);
                stmt.setDouble(3, 1282.64);
                stmt.setInt(4, 1);
                stmt.setDouble(5, 0.0);
                stmt.setString(6, "DOWN");
                stmt.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // batching relationships    
    public void batchingOneToMany() {

        // avoid this approach since you can optimize the number of batches by ordering inserts (executes 4 batches)
        ctx.batched((Configuration c) -> {
            c.dsl().insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                    EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                    .values(20001L, "Alison", "Joly", "x445", "ajoly@classicmodelcars.com", "3", 55000, "Sales Rep")
                    .onConflictDoNothing().execute();
            c.dsl().insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2005, 20001L, 1282.64, 1, 0.0).execute();
            c.dsl().insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2004, 20001L, 3938.24, 1, 0.0).execute();
            c.dsl().insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2004, 20001L, 4676.14, 1, 0.0).execute();

            c.dsl().insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                    EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                    .values(20002L, "Tyler", "Seven", "x101", "tseven@classicmodelcars.com", "2", 57000, "Sales Rep")
                    .onConflictDoNothing().execute();
            c.dsl().insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2003, 20002L, 2223.0, 1, 0.0).execute();
            c.dsl().insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2004, 20002L, 4531.35, 1, 0.0).execute();
            c.dsl().insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2004, 20002L, 6751.33, 1, 0.0).execute();
        });

        // prefer this approach (executes 2 batches)
        ctx.batched((Configuration c) -> {
            c.dsl().insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                    EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                    .values(20001L, "Alison", "Joly", "x445", "ajoly@classicmodelcars.com", "3", 55000, "Sales Rep")
                    .onConflictDoNothing().execute();
            c.dsl().insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                    EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                    .values(20002L, "Tyler", "Seven", "x101", "tseven@classicmodelcars.com", "2", 57000, "Sales Rep")
                    .onConflictDoNothing().execute();

            c.dsl().insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2005, 20001L, 1282.64, 1, 0.0).execute();
            c.dsl().insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2004, 20001L, 3938.24, 1, 0.0).execute();
            c.dsl().insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2004, 20001L, 4676.14, 1, 0.0).execute();
            c.dsl().insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2003, 20002L, 2223.0, 1, 0.0).execute();
            c.dsl().insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2004, 20002L, 4531.35, 1, 0.0).execute();
            c.dsl().insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2004, 20002L, 6751.33, 1, 0.0).execute();
        });
        
        ctx.batched((Configuration c) -> {
            long c1 = c.dsl().insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                    CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE)
                    .values("customer_1","","","")                    
                    .returningResult(CUSTOMER.CUSTOMER_NUMBER).fetchOneInto(Long.class);            
            long c2 = c.dsl().insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                    CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE)
                    .values("customer_2","","","")                    
                    .returningResult(CUSTOMER.CUSTOMER_NUMBER).fetchOneInto(Long.class);
            
            // only the following queries are batched
            c.dsl().insertInto(PAYMENT, PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER, PAYMENT.INVOICE_AMOUNT)
                    .values(c1, "ER998871", BigDecimal.valueOf(102.22)).execute();
            c.dsl().insertInto(PAYMENT, PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER, PAYMENT.INVOICE_AMOUNT)
                    .values(c1, "GH998872", BigDecimal.valueOf(233.42)).execute();
            c.dsl().insertInto(PAYMENT, PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER, PAYMENT.INVOICE_AMOUNT)
                    .values(c1, "YU998873", BigDecimal.valueOf(82.25)).execute();
            
            c.dsl().insertInto(PAYMENT, PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER, PAYMENT.INVOICE_AMOUNT)
                    .values(c2, "NM125563", BigDecimal.valueOf(656.23)).execute();
            c.dsl().insertInto(PAYMENT, PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER, PAYMENT.INVOICE_AMOUNT)
                    .values(c2, "IO125564", BigDecimal.valueOf(678.88)).execute();
        });
    }
}