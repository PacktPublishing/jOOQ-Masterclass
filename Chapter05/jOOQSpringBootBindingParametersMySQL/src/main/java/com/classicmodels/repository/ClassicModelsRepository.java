package com.classicmodels.repository;

import java.time.LocalDate;
import javax.sql.DataSource;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Payment.PAYMENT;
import org.jooq.DSLContext;
import org.jooq.Param;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.param;
import static org.jooq.impl.DSL.val;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final DataSource ds;

    public ClassicModelsRepository(DSLContext ctx, DataSource ds) {
        this.ctx = ctx;
        this.ds = ds;
    }

    ////////////////////////
    /* Indexed parameters */
    ////////////////////////
    public void hardCodedValuesAsIndexedParams() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(5000)
                        .and(EMPLOYEE.JOB_TITLE.eq("Sales Rep")))
                .fetch();

        // the previous query is similar to the following one, 
        // but there is no need to explicitly use val() in this case
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(val(5000))
                        .and(EMPLOYEE.JOB_TITLE.eq(val("Sales Rep"))))
                .fetch();
    }

    public void userInputValuesAsIndexedParams(int salary, String job) {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(salary)
                        .and(EMPLOYEE.JOB_TITLE.eq(job)))
                .fetch();

        // the previous query is similar to the following one, 
        // but there is no need to explicitly use val() in this case
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(val(salary))
                        .and(EMPLOYEE.JOB_TITLE.eq(val(job))))
                .fetch();
    }

    // must use val() to wrap the value in a Param explicitly
    public void usingValExplicitly1() {

        ctx.select(PAYMENT.INVOICE_AMOUNT)
                .from(PAYMENT)
                .where(val(LocalDate.now()).between(PAYMENT.PAYMENT_DATE)
                        .and(PAYMENT.CACHING_DATE))
                .fetch();
    }

    public void usingValExplicitly2(LocalDate date) {

        ctx.select(PAYMENT.INVOICE_AMOUNT)
                .from(PAYMENT)
                .where(val(date).between(PAYMENT.PAYMENT_DATE)
                        .and(PAYMENT.CACHING_DATE))
                .fetch();
    }

    public void usingValExplicitly3() {

        ctx.select(CUSTOMER.CUSTOMER_NUMBER,
                concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME))
                .from(CUSTOMER)
                .fetch();
    }

    public void usingValExplicitly4(float vat) {

        ctx.select(EMPLOYEE.SALARY, EMPLOYEE.SALARY.mul(vat).as("vat_salary"), val(vat).as("vat"))
                .from(EMPLOYEE)
                .fetch();
    }

    public void usingValExplicitly5() {

        ctx.select(val(10).sub(2).mul(val(7).div(3)).div(2).mod(10)).fetch();
    }

    public void usingValExplicitly6(int salary) {

        Param<Integer> salaryParam = val(salary);

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, salaryParam.as("base_salary"))
                .from(EMPLOYEE)
                .where(salaryParam.eq(EMPLOYEE.SALARY))
                .and(salaryParam.mul(0.15).gt(10000))
                .fetch();
    }

    // bind values from string query
    public void plainSQLHardCodedValues() {

        ctx.fetch("""
                  SELECT first_name, last_name 
                  FROM employee WHERE salary > ? AND job_title = ?
                  """, 5000, "Sales Rep");

        ctx.resultQuery("""
                        SELECT first_name, last_name 
                        FROM employee WHERE salary > ? AND job_title = ?
                        """, 5000, "Sales Rep")
                .fetch();
    }

    public void plainSQLUserInputValues(int salary, String job) {

        ctx.fetch("""
                  SELECT first_name, last_name 
                  FROM employee WHERE salary > ? AND job_title = ?
                  """, salary, job);

        ctx.resultQuery("""
                        SELECT first_name, last_name 
                        FROM employee WHERE salary > ? AND job_title = ?
                        """, salary, job)
                .fetch();
    }

    // extract bind values
    public void extractBindValuesIndexedParams() {

        ResultQuery query
                = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.gt(5000))
                        .and(EMPLOYEE.JOB_TITLE.eq("Sales Rep"));

        System.out.println("Bind values: " + query.getBindValues());
    }

    // extract a single bind value via Param
    public void extractBindValueIndexedParams() {

        ResultQuery query
                = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.gt(5000))
                        .and(EMPLOYEE.JOB_TITLE.eq("Sales Rep"));

        Param<?> p1 = query.getParam("1"); // wrap the value, 5000
        Param<?> p2 = query.getParam("2"); // wrap the value, "Sales Rep"

        System.out.println("First bind value: " + p1);
        System.out.println("Second bind value: " + p2);
    }

    // set a new bind value
    public void modifyingTheBindValueIndexedParam1() {
        try ( ResultQuery query = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(5000))
                .and(EMPLOYEE.JOB_TITLE.eq("Sales Rep")).keepStatement(true)) {

            // lazily create a new PreparedStatement
            Result result1 = query.fetch();
            System.out.println("Result 1: " + result1);

            // set new bind values
            Param<?> p1 = query.getParam("1");
            Param<?> p2 = query.getParam("2");
            p1.setConverted(75000);
            p2.setConverted("VP Marketing");

            // re-use the previous PreparedStatement
            Result result2 = query.fetch();
            System.out.println("Result 2: " + result2);
        }
    }

    // set a new bind value
    public void modifyingTheBindValueIndexedParam2() {
        try ( ResultQuery query = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(5000))
                .and(EMPLOYEE.JOB_TITLE.eq("Sales Rep")).keepStatement(true)) {

            // lazily create a new PreparedStatement
            Result result1 = query.fetch();
            System.out.println("Result 1: " + result1);

            // set new bind values
            query.bind(1, 75000);
            query.bind(2, "VP Marketing");

            // re-use the previous PreparedStatement
            Result result2 = query.fetch();
            System.out.println("Result 2: " + result2);
        }
    }

    //////////////////////
    /* Named parameters */
    //////////////////////
    public void hardCodedValuesAsNamedParams() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(param("employeeSalary", 5000))
                        .and(EMPLOYEE.JOB_TITLE.eq(param("employeeJobTitle", "Sales Rep"))))
                .fetch();

        // actually render named parameter names in generated SQL
        String sql = ctx.renderNamedParams(ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(param("employeeSalary", 5000))
                        .and(EMPLOYEE.JOB_TITLE.eq(param("employeeJobTitle", "Sales Rep"))))
        );

        System.out.println("SQL (renderNamedParams): " + sql);
    }

    public void userInputValuesAsNamedParams(int salary, String job) {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(param("employeeSalary", salary))
                        .and(EMPLOYEE.JOB_TITLE.eq(param("employeeJobTitle", job))))
                .fetch();

        // actually render named parameter names in generated SQL
        String sql = ctx.renderNamedParams(ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(param("employeeSalary", salary))
                        .and(EMPLOYEE.JOB_TITLE.eq(param("employeeJobTitle", job))))
        );

        System.out.println("SQL (renderNamedParams): " + sql);
    }

    // named parameter with a generic type and no initial value 
    public void namedParameterNoInitialValueOrType() {

        Param<Object> phoneParam = DSL.param("phone");

        phoneParam.setValue("(26) 642-7555"); // In jOOQ 4.0, Param will be made immutable!                        
        ctx.selectFrom(CUSTOMER)
                .where(phoneParam.eq(CUSTOMER.PHONE))
                .fetch();
    }

    // named parameter with a defined class-type and no initial value 
    public void namedParameterWithClassTypeNoInitialValue() {

        Param<String> phoneParam = DSL.param("phone", String.class);

        phoneParam.setValue("(26) 642-7555"); // In jOOQ 4.0, Param will be made immutable!                        
        ctx.selectFrom(CUSTOMER)
                .where(phoneParam.eq(CUSTOMER.PHONE))
                .fetch();
    }

    // named parameter with a defined data-type and no initial value 
    public void namedParameterWithDataTypeNoInitialValue() {

        Param<String> phoneParam = DSL.param("phone", SQLDataType.VARCHAR);

        phoneParam.setValue("(26) 642-7555"); // In jOOQ 4.0, Param will be made immutable!                        
        ctx.selectFrom(CUSTOMER)
                .where(phoneParam.eq(CUSTOMER.PHONE))
                .fetch();
    }

    // named parameter with a defined type of another field and no initial value 
    public void namedParameterWithFieldTypeNoInitialValue() {

        Param<String> phoneParam = DSL.param("phone", CUSTOMER.PHONE);

        phoneParam.setValue("(26) 642-7555"); // In jOOQ 4.0, Param will be made immutable!                        
        ctx.selectFrom(CUSTOMER)
                .where(phoneParam.eq(CUSTOMER.PHONE))
                .fetch();
    }

    // unnamed parameter with a defined class-type and no initial value 
    public void unnamedParameterWithClassTypeNoInitialValue() {

        Param<String> phoneParam = DSL.param(String.class);

        phoneParam.setValue("(26) 642-7555"); // In jOOQ 4.0, Param will be made immutable!                        
        ctx.selectFrom(CUSTOMER)
                .where(phoneParam.eq(CUSTOMER.PHONE))
                .fetch();
    }

    // unnamed parameter with a defined data-type and no initial value 
    public void unnamedParameterWithDataTypeNoInitialValue() {

        Param<String> phoneParam = DSL.param(SQLDataType.VARCHAR);

        phoneParam.setValue("(26) 642-7555"); // In jOOQ 4.0, Param will be made immutable!                        
        ctx.selectFrom(CUSTOMER)
                .where(phoneParam.eq(CUSTOMER.PHONE))
                .fetch();
    }

    // unnamed parameter with a defined type of another field and no initial value 
    public void unnamedParameterWithFieldTypeNoInitialValue() {

        Param<String> phoneParam = DSL.param(CUSTOMER.PHONE);

        phoneParam.setValue("(26) 642-7555"); // In jOOQ 4.0, Param will be made immutable!                        
        ctx.selectFrom(CUSTOMER)
                .where(phoneParam.eq(CUSTOMER.PHONE))
                .fetch();
    }

    // keep a reference to a named parameter having an inital value
    public void namedParameterWithTypeAndInitialValue() {

        Param<String> phoneParam = DSL.param("phone", "(26) 642-7555");

        phoneParam.setValue("another_value"); // In jOOQ 4.0, Param will be made immutable!                        
        ctx.selectFrom(CUSTOMER)
                .where(phoneParam.eq(CUSTOMER.PHONE))
                .fetch();
    }

    // extract bind values
    public void extractBindValuesNamedParams() {

        ResultQuery query
                = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.gt(param("employeeSalary", 5000))
                                .and(EMPLOYEE.JOB_TITLE.eq(param("employeeJobTitle", "Sales Rep"))));

        System.out.println("Bind values: " + query.getBindValues());
    }

    // extract a single bind value via Param
    public void extractBindValueNamedParams() {

        ResultQuery query
                = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.gt(param("employeeSalary", 5000)))
                        .and(EMPLOYEE.JOB_TITLE.eq(param("employeeJobTitle", "Sales Rep")));

        Param<?> p1 = query.getParam("employeeSalary"); // wrap the value, 5000
        Param<?> p2 = query.getParam("employeeJobTitle"); // wrap the value, "Sales Rep"

        System.out.println("First bind value: " + p1);
        System.out.println("Second bind value: " + p2);
    }

    // set a new bind value
    public void modifyingTheBindValueNamedParam1() {
        try ( ResultQuery query = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(param("employeeSalary", 5000)))
                .and(EMPLOYEE.JOB_TITLE.eq(param("employeeJobTitle", "Sales Rep"))).keepStatement(true)) {

            // lazily create a new PreparedStatement
            Result result1 = query.fetch();
            System.out.println("Result 1: " + result1);

            // set new bind values
            Param<?> p1 = query.getParam("employeeSalary");
            Param<?> p2 = query.getParam("employeeJobTitle");
            p1.setConverted(75000);
            p2.setConverted("VP Marketing");

            // re-use the previous PreparedStatement
            Result result2 = query.fetch();
            System.out.println("Result 2: " + result2);
        }
    }

    // modify the current bind value
    public void modifyingTheBindValueNamedParam2() {
        try ( ResultQuery query = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(param("employeeSalary", 5000)))
                .and(EMPLOYEE.JOB_TITLE.eq(param("employeeJobTitle", "Sales Rep"))).keepStatement(true)) {

            // lazily create a new PreparedStatement
            Result result1 = query.fetch();
            System.out.println("Result 1: " + result1);

            // set new bind values
            query.bind("employeeSalary", 75000);
            query.bind("employeeJobTitle", "VP Marketing");

            // re-use the previous PreparedStatement
            Result result2 = query.fetch();
            System.out.println("Result 2: " + result2);
        }
    }

    ///////////////////////
    /* Inline parameters */
    ///////////////////////
    public void hardCodedValuesAsInlineParams() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(inline(5000))
                        .and(EMPLOYEE.JOB_TITLE.eq(inline("Sales Rep"))))
                .fetch();
    }

    public void userInputValuesAsInlineParams(int salary, String job) {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(inline(salary))
                        .and(EMPLOYEE.JOB_TITLE.eq(inline(job))))
                .fetch();
    }

    public void inlineParamsViaSettings() {

        DSL.using(ds, SQLDialect.MYSQL,
                new Settings().withStatementType(StatementType.STATIC_STATEMENT))
                .select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(5000)
                        .and(EMPLOYEE.JOB_TITLE.eq("Sales Rep")))
                .fetch();
    }

    // extract a single bind value via Param
    public void extractBindValueInlineParams() {

        ResultQuery query
                = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.gt(inline(5000))
                                .and(EMPLOYEE.JOB_TITLE.eq(inline("Sales Rep"))));

        Param<?> p1 = query.getParam("1"); // wrap the value, 5000
        Param<?> p2 = query.getParam("2"); // wrap the value, "Sales Rep"

        System.out.println("First bind value: " + p1);
        System.out.println("Second bind value: " + p2);
    }
    
    // set a new bind value
    public void modifyingTheBindValueInlineParam1() {
    ResultQuery query = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(inline(5000)))
                .and(EMPLOYEE.JOB_TITLE.eq(inline("Sales Rep")));

            // lazily create a new PreparedStatement
            Result result1 = query.fetch();
            System.out.println("Result 1: " + result1);

            // set new bind values
            Param<?> p1 = query.getParam("1");
            Param<?> p2 = query.getParam("2");
            p1.setConverted(75000);
            p2.setConverted("VP Marketing");

            // re-use the previous PreparedStatement
            Result result2 = query.fetch();
            System.out.println("Result 2: " + result2);        
    }

    // set a new bind value
    public void modifyingTheBindValueInlineParam2() {
        ResultQuery query = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(inline(5000)))
                .and(EMPLOYEE.JOB_TITLE.eq(inline("Sales Rep")));

            // lazily create a new PreparedStatement
            Result result1 = query.fetch();
            System.out.println("Result 1: " + result1);

            // set new bind values
            query.bind("1", 75000);
            query.bind("2", "VP Marketing");                        

            // re-use the previous PreparedStatement
            Result result2 = query.fetch();
            System.out.println("Result 2: " + result2);        
    }

    // Render query with different types of parameter placeholders
    public void viaResultQueryGetSQL() {

        // initially, indexed parameters
        ResultQuery query1 = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(5000)
                        .and(EMPLOYEE.JOB_TITLE.eq("Sales Rep")));

        // initially, named parameters
        ResultQuery query2 = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(param("employeeSalary", 5000))
                        .and(EMPLOYEE.JOB_TITLE.eq(param("employeeJobTitle", "Sales Rep"))));

        // initially, iniline parameters
        ResultQuery query3 = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .where(EMPLOYEE.SALARY.gt(inline(5000)))
                .and(EMPLOYEE.JOB_TITLE.eq(inline("Sales Rep")));

        System.out.println("QUERY 1 ...");
        System.out.println("SQL (indexed): " + query1.getSQL(ParamType.INDEXED));
        System.out.println("SQL (named): " + query1.getSQL(ParamType.NAMED));
        System.out.println("SQL (inlined): " + query1.getSQL(ParamType.INLINED));
        System.out.println("SQL (named or inlined): " + query1.getSQL(ParamType.NAMED_OR_INLINED));

        System.out.println("QUERY 2 ...");
        System.out.println("SQL (indexed): " + query2.getSQL(ParamType.INDEXED));
        System.out.println("SQL (named): " + query2.getSQL(ParamType.NAMED));
        System.out.println("SQL (inlined): " + query2.getSQL(ParamType.INLINED));
        System.out.println("SQL (named or inlined): " + query2.getSQL(ParamType.NAMED_OR_INLINED));

        System.out.println("QUERY 3 ...");
        System.out.println("SQL (indexed): " + query3.getSQL(ParamType.INDEXED));
        System.out.println("SQL (named): " + query3.getSQL(ParamType.NAMED));
        System.out.println("SQL (inlined): " + query3.getSQL(ParamType.INLINED));
        System.out.println("SQL (named or inlined): " + query3.getSQL(ParamType.NAMED_OR_INLINED));
    }

}