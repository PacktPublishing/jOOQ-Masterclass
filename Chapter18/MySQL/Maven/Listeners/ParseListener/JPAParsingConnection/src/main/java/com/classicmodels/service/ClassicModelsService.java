package com.classicmodels.service;

import com.classicmodels.dao.Dao;
import com.classicmodels.utils.DaoUtils;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class ClassicModelsService {

    private final Dao dao;
    private final EntityManager em;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String pass;

    public ClassicModelsService(Dao dao, EntityManager em) {

        this.dao = dao;
        this.em = em;
    }

    public void fetchCustomers() {

        Query query = dao.fetchCustomers();
        String sql = DaoUtils.sql(query);

        try ( Connection c = DSL.using(url, user, pass)
                .configuration()
                .set(new Settings().withParseDialect(SQLDialect.MYSQL))               
                .dsl()                
                .parsingConnection();  
                
                PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, "%Home%");

            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) {
                        System.out.print(",  ");
                    }
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnLabel(i));
                }
                System.out.println();
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClassicModelsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fetchCustomerdetails() {

        Query query = dao.fetchCustomerdetails();
        String sql = DaoUtils.sql(query);
        
        System.out.println("\nThe SQL that will pass through jOOQ parser is:\n" + sql);

        try ( Connection c = DSL.using(url, user, pass)
                .configuration()
                .set(new Settings().withParseDialect(SQLDialect.MYSQL))               
                .dsl()
                .parsingConnection();  
                
                PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setLong(1, 99L);

            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) {
                        System.out.print(",  ");
                    }
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnLabel(i));
                }
                System.out.println();
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClassicModelsService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
