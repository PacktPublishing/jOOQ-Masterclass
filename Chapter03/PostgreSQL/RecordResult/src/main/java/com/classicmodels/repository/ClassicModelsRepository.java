package com.classicmodels.repository;

import java.math.BigDecimal;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Manager.MANAGER;
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.CustomerdetailRecord;
import jooq.generated.tables.records.ManagerRecord;
import jooq.generated.udt.records.EvaluationCriteriaRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.jooq.Record;
import org.jooq.Record15;
import org.jooq.Record2;
import org.jooq.Record22;
import org.jooq.Record3;
import org.jooq.Record5;
import org.jooq.Result;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void nonTypesafePlainSQL() {

        /* non type-safe Result<Record> */
        Result<Record> result = ctx.fetch(
                "SELECT customer_name, customer_number, credit_limit FROM customer");

        // or, like this
        /*
        Result<Record> result = ctx.resultQuery(
                "SELECT customer_name, customer_number, credit_limit FROM customer")
                .fetch();
         */
        
        /* non type-safe values */
        for (Record record : result) {
            Object r1 = record.get(0);
            Object r2 = record.get("customer_number");
            BigDecimal r3 = record.getValue("credit_limit", BigDecimal.class);

            System.out.println("customer_name:" + r1
                    + " customer_number: " + r2 + " credit_limit: " + r3);
        }
    }

    public void nonTypesafeSelect() {

        /* non type-safe Result<Record> */
        Result<Record> result = ctx.select()
                .from(CUSTOMER)
                .fetch();

        /* type-safe values */
        for (Record r : result) {
            String r1 = r.get(CUSTOMER.CUSTOMER_NAME);
            Long r2 = r.get(CUSTOMER.CUSTOMER_NUMBER);
            BigDecimal r3 = r.get(CUSTOMER.CREDIT_LIMIT);

            System.out.println("customer_name: " + r1
                    + " customer_number: " + r2 + " credit_limit: " + r3);
        }
    }

    public void typesafeSelectIntoTableRecord() {

        /* type-safe Result<Record> */
        Result<CustomerRecord> result = ctx.select()
                .from(CUSTOMER)
                .fetch().into(CUSTOMER);

        /* type-safe values */
        for (CustomerRecord r : result) {
            String r1 = r.getCustomerName();
            Long r2 = r.getCustomerNumber();
            BigDecimal r3 = r.getCreditLimit();

            System.out.println("customer_name: " + r1
                    + " customer_number: " + r2 + " credit_limit: " + r3);
        }
    }

    public void typesafeSelectIntoClass() {

        /* type-safe Result<Record> */
        List<CustomerRecord> result = ctx.select()
                .from(CUSTOMER)
                .fetch().into(CustomerRecord.class);

        /* type-safe values */
        for (CustomerRecord r : result) {
            String r1 = r.getCustomerName();
            Long r2 = r.getCustomerNumber();
            BigDecimal r3 = r.getCreditLimit();

            System.out.println("customer_name: " + r1
                    + " customer_number: " + r2 + " credit_limit: " + r3);
        }
    }

    public void nonTypesafeSelectJoin() {

        /* non type-safe Result<Record> */
        Result<Record> result = ctx.select()
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER
                        .eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetch();

        /* type-safe values */
        /*
        for (Record r : result) {
            String r1 = r.get(CUSTOMER.CUSTOMER_NAME);
            Long r2 = r.get(CUSTOMER.CUSTOMER_NUMBER);
            BigDecimal r3 = r.get(CUSTOMER.CREDIT_LIMIT);

            String r4 = r.get(CUSTOMERDETAIL.CITY);
            String r5 = r.get(CUSTOMERDETAIL.COUNTRY);

            System.out.println("customer_name: " + r1
                    + " customer_number: " + r2 + " credit_limit: " + r3
                    + " city: " + r4 + " country: " + r5);
        }
        */
        
        /* extract the two individual strongly typed 
        TableRecord types from the denormalised Record */
        Result<CustomerRecord> rcr = result.into(CUSTOMER);
        Result<CustomerdetailRecord> rcd = result.into(CUSTOMERDETAIL);

        StringBuilder sb = new StringBuilder();

        for (CustomerRecord cr : rcr) {
            String r1 = cr.getCustomerName();
            Long r2 = cr.getCustomerNumber();
            BigDecimal r3 = cr.getCreditLimit();

            sb.append(r1).append(", ").append(r2).append(", ").append(r3).append(", ");
        }

        for (CustomerdetailRecord cd : rcd) {
            String r4 = cd.getCity();
            String r5 = cd.getCountry();

            sb.append(r4).append(", ").append(r5).append("\n");
        }

        System.out.println(sb);
    }

    public void typesafeSelectJoin() {

        /* type-safe Result<Record> via select() */        
        /*
        Result<Record15<Long, String, String, String, String, Long, 
                BigDecimal, Integer, Long, String, String, String, String, String, String>> result = ...
        
        CAN BE REPLACED WITH JAVA 9
        
        var result = ...
        */
        
        Result<Record15<Long, String, String, String, String, Long, 
                BigDecimal, Integer, Long, String, String, String, String, String, String>> result
                = ctx.select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME,
                        CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME,
                        CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER, 
                        CUSTOMER.CREDIT_LIMIT, CUSTOMER.FIRST_BUY_DATE,
                        CUSTOMERDETAIL.CUSTOMER_NUMBER, CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                        CUSTOMERDETAIL.ADDRESS_LINE_SECOND, CUSTOMERDETAIL.CITY,
                        CUSTOMERDETAIL.COUNTRY, CUSTOMERDETAIL.POSTAL_CODE,
                        CUSTOMERDETAIL.STATE)
                        .from(CUSTOMER)
                        .join(CUSTOMERDETAIL)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                        .fetch();
        
        /* type-safe Result<Record> via into() */
        /*
        Result<Record15<Long, String, String, String, String, Long, 
                BigDecimal, Integer, Long, String, String, String, String, String, String>> result = ...
        
        CAN BE REPLACED WITH JAVA 9
        
        var result = ...
        */
        
        /*
        Result<Record15<Long, String, String, String, String, Long, 
                BigDecimal, Integer, Long, String, String, String, String, String, String>> result = ctx.select()
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetch()
                .into(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME,
                        CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME,
                        CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER, 
                        CUSTOMER.CREDIT_LIMIT, CUSTOMER.FIRST_BUY_DATE, 
                        CUSTOMERDETAIL.CUSTOMER_NUMBER, CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                        CUSTOMERDETAIL.ADDRESS_LINE_SECOND, CUSTOMERDETAIL.CITY,
                        CUSTOMERDETAIL.COUNTRY, CUSTOMERDETAIL.POSTAL_CODE,
                        CUSTOMERDETAIL.STATE);
        */
        
        /* type-safe values */
        /*
        for (Record14 r : result) {
            String r1 = r.get(CUSTOMER.CUSTOMER_NAME);
            Long r2 = r.get(CUSTOMER.CUSTOMER_NUMBER);
            BigDecimal r3 = r.get(CUSTOMER.CREDIT_LIMIT);

            String r4 = r.get(CUSTOMERDETAIL.CITY);
            String r5 = r.get(CUSTOMERDETAIL.COUNTRY);

            System.out.println("customer_name: " + r1
                    + " customer_number: " + r2 + " credit_limit: " + r3
                    + " city: " + r4 + " country: " + r5);
        }
        */
 
        /* extract the two individual strongly typed 
        TableRecord types from the denormalised Record */
        Result<CustomerRecord> rcr = result.into(CUSTOMER);
        Result<CustomerdetailRecord> rcd = result.into(CUSTOMERDETAIL);

        StringBuilder sb = new StringBuilder();

        for (CustomerRecord cr : rcr) {
            String r1 = cr.getCustomerName();
            Long r2 = cr.getCustomerNumber();
            BigDecimal r3 = cr.getCreditLimit();

            sb.append(r1).append(", ").append(r2).append(", ").append(r3).append(", ");
        }

        for (CustomerdetailRecord cd : rcd) {
            String r4 = cd.getCity();
            String r5 = cd.getCountry();

            sb.append(r4).append(", ").append(r5).append("\n");
        }

        System.out.println(sb);
    }

    public void typesafeSelectFrom() {

        /* type-safe Result<Record> */
        Result<CustomerRecord> result = ctx.selectFrom(CUSTOMER)
                .fetch();

        /* type-safe values */
        for (CustomerRecord r : result) {
            String r1 = r.getCustomerName();
            Long r2 = r.getCustomerNumber();
            BigDecimal r3 = r.getCreditLimit();

            System.out.println("customer_name: " + r1
                    + " customer_number: " + r2 + " credit_limit: " + r3);
        }
    }

    public void typesafeAdHocSelect() {

        /* type-safe Result<Record> */
        Result<Record3<Long, String, BigDecimal>> result = ctx.select(
                CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER)
                .fetch();

        /* type-safe values */
        for (Record3 r : result) {
            String r1 = r.get(CUSTOMER.CUSTOMER_NAME);
            Long r2 = r.get(CUSTOMER.CUSTOMER_NUMBER);
            BigDecimal r3 = r.get(CUSTOMER.CREDIT_LIMIT);

            System.out.println("customer_name: " + r1
                    + " customer_number: " + r2 + " credit_limit: " + r3);
        }
    }

    public void typesafeAdHocSelectIntoTableRecord() {

        /* type-safe Result<Record> */
        Result<CustomerRecord> result = ctx.select(
                CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER)
                .fetch()
                .into(CUSTOMER); // or, into(CustomerRecord.class)

        /* type-safe values */
        for (CustomerRecord r : result) {
            String r1 = r.getCustomerName();
            Long r2 = r.getCustomerNumber();
            BigDecimal r3 = r.getCreditLimit();
            String r4 = r.getContactFirstName(); // this is null since was not fetched in query

            System.out.println("customer_name: " + r1
                    + " customer_number: " + r2 + " credit_limit: " + r3
                    + " contact_first_name: " + r4);
        }
    }

    public void typesafeAdHocSelectJoin() {

        /* type-safe Result<Record> */
        Result<Record5<Long, BigDecimal, String, String, String>> result
                = ctx.select(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CREDIT_LIMIT,
                        CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY, CUSTOMERDETAIL.POSTAL_CODE)
                        .from(CUSTOMER)
                        .join(CUSTOMERDETAIL)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                        .fetch();

        // or, use strongly typed table records
        Result<CustomerRecord> rcr = result.into(CUSTOMER);
        Result<CustomerdetailRecord> rcd = result.into(CUSTOMERDETAIL);
        // ...
        
        /* type-safe values */
        for (Record5 r : result) {            
            Long r1 = r.get(CUSTOMER.CUSTOMER_NUMBER);
            BigDecimal r2 = r.get(CUSTOMER.CREDIT_LIMIT);

            String r3 = r.get(CUSTOMERDETAIL.CITY);
            String r4 = r.get(CUSTOMERDETAIL.COUNTRY);
            String r5 = r.get(CUSTOMERDETAIL.POSTAL_CODE);

            System.out.println("customer_name: " + r1 + " credit_limit: " + r2
                    + " city: " + r3 + " country: " + r4 + " postal_code:" + r5);
        }

    }
    
    public void typesafeUdtType() {
        
        /* type-safe Result<Record> */
        Result<Record2<String, EvaluationCriteriaRecord>> result 
                = ctx.select(MANAGER.MANAGER_NAME, MANAGER.MANAGER_EVALUATION)
                        .from(MANAGER)
                        .fetch();
        
        /* type-safe values */
        for(Record2 r : result) {
            String r1 = r.get(MANAGER.MANAGER_NAME);
            Integer r2 = r.get(MANAGER.MANAGER_EVALUATION).getCommunicationAbility();
            Integer r3 = r.get(MANAGER.MANAGER_EVALUATION).getEthics();
            Integer r4 = r.get(MANAGER.MANAGER_EVALUATION).getPerformance();
            Integer r5 = r.get(MANAGER.MANAGER_EVALUATION).getEmployeeInput();
            
            System.out.println("manager_name: " + r1
            + " communication_ability: " + r2 + " ethics: " 
                    + r3 + " performance: " +r4 + "employee_input: " + r5);
        }        
    }
    
    public void typesafeUdtTypeIntoTableRecord() {
        
        /* type-safe Result<Record> */
        Result<ManagerRecord> result 
                = ctx.select(MANAGER.MANAGER_NAME, MANAGER.MANAGER_EVALUATION)
                        .from(MANAGER)
                        .fetch()
                .into(MANAGER); // or, into(ManagerRecord.class)
        
        /* type-safe values */
        for(ManagerRecord r : result) {
            // or, r.getManagerName()
            String r1 = r.get(MANAGER.MANAGER_NAME);
            // or, r.getManagerEvaluation().getCommunicationAbility()
            Integer r2 = r.get(MANAGER.MANAGER_EVALUATION).getCommunicationAbility();
            // or, r.getManagerEvaluation().getEthics()
            Integer r3 = r.get(MANAGER.MANAGER_EVALUATION).getEthics();
            // or, r.getManagerEvaluation().getPerformance()
            Integer r4 = r.get(MANAGER.MANAGER_EVALUATION).getPerformance();
            // or, r.getManagerEvaluation().getEmployeeInput()
            Integer r5 = r.get(MANAGER.MANAGER_EVALUATION).getEmployeeInput();
            
            System.out.println("manager_name: " + r1
            + " communication_ability: " + r2 + " ethics: " 
                    + r3 + " performance: " +r4 + "employee_input: " + r5);
        }        
    }

    public void beyondDegree22() {
        
        // 22 columns 
        /* type-safe Result<Record> */
        /*
        Result<Record22<String, String, String, String, String,
                String, String, String, String, String,
                String, String, String, String, String,
                String, String, String, String, String,
                String, String>> typesafe ...
        
        CAN BE REPLACED WITH JAVA 9
        
        var typesafe = ...
        */
        
        Result<Record22<String, String, String, String, String,
                String, String, String, String, String,
                String, String, String, String, String,
                String, String, String, String, String,
                String, String>> typesafe = ctx.select(
                CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, 
                CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, 
                CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE,
                CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE,
                CUSTOMER.PHONE, CUSTOMER.PHONE)
                .from(CUSTOMER)
                .fetch();
        
        // 23 columns
        /* non-type-safe Result<Record> */
        Result<Record> nonTypesafe = ctx.select(
                CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, 
                CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, 
                CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE,
                CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE,
                CUSTOMER.PHONE, CUSTOMER.PHONE, CUSTOMER.PHONE)
                .from(CUSTOMER)
                .fetch();
    }    
    
    // fetch some records and send them to the controller
    public Result<Record> fetchCustomers() {
        
        return ctx.select()
                .from(CUSTOMER)
                .fetch();
    }
}