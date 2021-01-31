package com.classicmodels.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import static jooq.generated.tables.Customer.CUSTOMER;
import org.jooq.DSLContext;
import org.jooq.ResultQuery;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final DataSource ds;

    public ClassicModelsRepository(DSLContext ctx, DataSource ds) {
        this.ctx = ctx;
        this.ds = ds;
    }

    // execute a ResultQuery with jOOQ, but return a JDBC ResultSet, not a jOOQ object
    public void executeQueryViajOOQReturnJDBCResultSet() {
        
        ResultQuery<Record> resultQuery = ctx.resultQuery(
                "SELECT customer_name, credit_limit FROM customer");

        // ResultSet rs = ctx.select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT)
        //       .from(CUSTOMER).fetchResultSet();        
        
        try (ResultSet rs = resultQuery.fetchResultSet()) {

            System.out.println("Example 1\n");

            while (rs.next()) {
                String customerName = rs.getString("customer_name");
                BigDecimal creditLimit = rs.getBigDecimal("credit_limit");
                System.out.println("Customer name: " + customerName
                        + " Credit limit: " + creditLimit);
            }
        } catch (SQLException ex) {
            // handle exception
        }
    }

    // transform jOOQ's Result into a JDBC ResultSet
    public void transformjOOQResultToJDBCResultSet() {               
                
        var result = ctx.select( // Result<Record2<String, BigDecimal>>
                CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT).from(CUSTOMER).fetch();               
        
        // at this point, the connection is closed (is back into the connection pool)
        ResultSet rsInMem = result.intoResultSet(); // in-memory ResultSet               

        System.out.println("Example 2\n");
        try {
            while (rsInMem.next()) {
                String customerName = rsInMem.getString("customer_name");
                BigDecimal creditLimit = rsInMem.getBigDecimal("credit_limit");
                System.out.println("Customer name: " + customerName
                        + " Credit limit: " + creditLimit);
            }
        } catch (SQLException ex) {
            // handle exception
        }
    }
    
    // fetch data from a legacy ResultSet using jOOQ
    public void legacyDmResultSetIntojOOQRecord() {
                
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/classicmodels", "root", "root")) {

            ResultSet rsJdbc = conn.createStatement().executeQuery(
                    "SELECT customer_name, credit_limit FROM customer");

            System.out.println("Example 3\n"
                 // + ctx.fetch(rsJdbc) // Result<Record>
                    + ctx.fetch(rsJdbc, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT)   // Result<Record>
                 // + ctx.fetch(rsJdbc, String.class, BigDecimal.class)                  // Result<Record>
                 // + ctx.fetch(rsJdbc, VARCHAR, DECIMAL)                                // Result<Record>
            );

        } catch (SQLException ex) {
            // handle exception
        }
    }
    
    // fetch data from a legacy ResultSet using jOOQ
    public void legacyDsResultSetIntojOOQRecord() {
        
        // fetch data from a legacy ResultSet using jOOQ
        try (Connection conn = ds.getConnection()) {

            ResultSet rsJdbc = conn.createStatement().executeQuery(
                    "SELECT customer_name, credit_limit FROM customer");

            System.out.println("Example 4\n"
                 // + ctx.fetch(rsJdbc) // Result<Record>
                    + ctx.fetch(rsJdbc, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT)   // Result<Record>
                 // + ctx.fetch(rsJdbc, String.class, BigDecimal.class)                  // Result<Record>
                 // + ctx.fetch(rsJdbc, VARCHAR, DECIMAL)                                // Result<Record>
            );

        } catch (SQLException ex) {
            // handle exception
        }
    }
}