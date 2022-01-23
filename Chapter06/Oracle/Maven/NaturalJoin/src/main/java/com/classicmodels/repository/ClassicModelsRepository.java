package com.classicmodels.repository;

import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Top3product.TOP3PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    public void naturalJoinEmployeeSale() {

        System.out.println("EXAMPLE 1.1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE).naturalJoin(SALE)
                        .fetch()
        );

        System.out.println("EXAMPLE 1.2\n"
                + ctx.select(field(EMPLOYEE.EMPLOYEE_NUMBER.getUnqualifiedName()).as("en"),
                        field(SALE.EMPLOYEE_NUMBER.getUnqualifiedName()).as("sn"),
                        EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE).naturalJoin(SALE)
                        .fetch()
        );

        System.out.println("EXAMPLE 1.3\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE).naturalJoin(SALE)
                        .where(field(EMPLOYEE.EMPLOYEE_NUMBER.getUnqualifiedName()).gt(1370L)) // points to EMPLOYEE.EMPLOYEE_NUMBER
                        .fetch()
        );

        System.out.println("EXAMPLE 1.4\n"
                + ctx.select()
                        .from(table("EMPLOYEE")).naturalJoin(table("SALE"))
                        .fetch()
        );

        System.out.println("EXAMPLE 1.5\n"
                + ctx.select()
                        .from(table("EMPLOYEE")).naturalJoin(table("SALE"))
                        .where(field("EMPLOYEE_NUMBER").gt(1370L)) // points to EMPLOYEE.EMPLOYEE_NUMBER
                        .fetch()
        );

        System.out.println("EXAMPLE 1.6 (uncomment to try) \n"
        // results in ORA-25155, uncomment to try
        /*
                + ctx.select()
                        .from(EMPLOYEE).naturalJoin(SALE)                        
                        .fetch()
         */
        );

        // use asterisk() to render "*"
        System.out.println("EXAMPLE 1.7\n"
                + ctx.select(asterisk())
                        .from(PRODUCT).naturalJoin(TOP3PRODUCT)
                        .fetch());
    }

    // EXAMPLE 2
    public void naturalLeftOuterJoinEmployeeSale() {

        System.out.println("EXAMPLE 2.1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE).naturalLeftOuterJoin(SALE)
                        .fetch()
        );

        System.out.println("EXAMPLE 2.2\n"
                + ctx.select(field(EMPLOYEE.EMPLOYEE_NUMBER.getUnqualifiedName()).as("en"),
                        field(SALE.EMPLOYEE_NUMBER.getUnqualifiedName()).as("sn"),
                        EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE).naturalLeftOuterJoin(SALE)
                        .fetch()
        );

        System.out.println("EXAMPLE 2.3\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE).naturalLeftOuterJoin(SALE)
                        .where(field(EMPLOYEE.EMPLOYEE_NUMBER.getUnqualifiedName()).gt(1370L)) // points to EMPLOYEE.EMPLOYEE_NUMBER
                        .fetch()
        );

        System.out.println("EXAMPLE 2.4\n"
                + ctx.select()
                        .from(table("EMPLOYEE")).naturalLeftOuterJoin(table("SALE"))
                        .fetch()
        );

        System.out.println("EXAMPLE 2.5\n"
                + ctx.select()
                        .from(table("EMPLOYEE")).naturalLeftOuterJoin(table("SALE"))
                        .where(field("EMPLOYEE_NUMBER").gt(1370L)) // points to EMPLOYEE.EMPLOYEE_NUMBER
                        .fetch()
        );

        System.out.println("EXAMPLE 2.6 (uncomment to try) \n"
        // results in ORA-25155, uncomment to try
        /*
                + ctx.select()
                        .from(EMPLOYEE).naturalLeftOuterJoin(SALE)                        
                        .fetch()
         */
        );
        
        // use asterisk() to render "*"
        System.out.println("EXAMPLE 2.7\n"
                + ctx.select(asterisk())
                        .from(PRODUCT).naturalLeftOuterJoin(TOP3PRODUCT)
                        .fetch());
    }

    // EXAMPLE 3
    public void naturalRightOuterJoinEmployeeSale() {

        System.out.println("EXAMPLE 3.1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE).naturalRightOuterJoin(SALE)
                        .fetch()
        );

        System.out.println("EXAMPLE 3.2\n"
                + ctx.select(field(EMPLOYEE.EMPLOYEE_NUMBER.getUnqualifiedName()).as("en"),
                        field(SALE.EMPLOYEE_NUMBER.getUnqualifiedName()).as("sn"),
                        EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE).naturalRightOuterJoin(SALE)
                        .fetch()
        );

        System.out.println("EXAMPLE 3.3\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE).naturalRightOuterJoin(SALE)
                        .where(field(EMPLOYEE.EMPLOYEE_NUMBER.getUnqualifiedName()).gt(1370L)) // points to SALE.EMPLOYEE_NUMBER
                        .fetch()
        );

        System.out.println("EXAMPLE 3.4\n"
                + ctx.select()
                        .from(table("EMPLOYEE")).naturalRightOuterJoin(table("SALE"))
                        .fetch()
        );

        System.out.println("EXAMPLE 3.5\n"
                + ctx.select()
                        .from(table("EMPLOYEE")).naturalRightOuterJoin(table("SALE"))
                        .where(field("EMPLOYEE_NUMBER").gt(1370L)) // points to SALE.EMPLOYEE_NUMBER
                        .fetch()
        );

        System.out.println("EXAMPLE 3.6 (uncomment to try) \n"
        // results in ORA-25155, uncomment to try
        /*
                + ctx.select()
                        .from(EMPLOYEE).naturalRightOuterJoin(SALE)                        
                        .fetch()
         */
        );
        
        // use asterisk() to render "*"
        System.out.println("EXAMPLE 3.7\n"
                + ctx.select(asterisk())
                        .from(PRODUCT).naturalRightOuterJoin(TOP3PRODUCT)
                        .fetch());
    }

    // EXAMPLE 4
    public void naturalFullOuterJoinEmployeeSale() {

        System.out.println("EXAMPLE 4.1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE).naturalFullOuterJoin(SALE)
                        .fetch()
        );

        System.out.println("EXAMPLE 4.2\n"
                + ctx.select()
                        .from(table("EMPLOYEE")).naturalFullOuterJoin(table("SALE"))
                        .fetch()
        );

        System.out.println("EXAMPLE 4.3 (uncomment to try) \n"
        // results in ORA-25155, uncomment to try
        /*
                + ctx.select()
                        .from(EMPLOYEE).naturalFullOuterJoin(SALE)                        
                        .fetch()
         */
        );
        
        // use asterisk() to render "*"
        System.out.println("EXAMPLE 4.4\n"
                + ctx.select(asterisk())
                        .from(PRODUCT).naturalFullOuterJoin(TOP3PRODUCT)
                        .fetch());
    }

    // EXAMPLE 5
    public void naturalJoinOrderCustomerPayment() {

        System.out.println("EXAMPLE 5.1\n"
                + ctx.select()
                        .from(table(name("ORDER")).naturalJoin(table("CUSTOMER"))
                                .naturalJoin(table("PAYMENT")))
                        .fetch()
        );
    }

    // EXAMPLE 6
    public void naturalJoinOfficeCustomerdetail() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select()
                        .from(table("OFFICE").naturalJoin(table("CUSTOMERDETAIL")))
                        .fetch()
        );
    }

    // EXAMPLE 7
    public void naturalJoinPaymentBankTransaction() {

        // NATURAL JOIN will not produce the expected output because
        // it will use on join the CACHING_DATE column as well since this
        // column is present in both tables
        System.out.println("EXAMPLE 7.1\n"
                + ctx.select()
                        .from(table("PAYMENT")).naturalJoin(table("BANK_TRANSACTION"))
                        .fetch()
        );

        // In such case, we have to rely on explicit ON to eliminate
        // CACHING_DATE column from JOIN
        System.out.println("EXAMPLE 7.2\n"
                + ctx.select()
                        .from(PAYMENT.innerJoin(BANK_TRANSACTION)
                                .on(PAYMENT.CUSTOMER_NUMBER.eq(BANK_TRANSACTION.CUSTOMER_NUMBER)
                                        .and(PAYMENT.CHECK_NUMBER.eq(BANK_TRANSACTION.CHECK_NUMBER))))
                        .fetch()
        );

        // Or, less verbose via jOOQ, onKey() 
        System.out.println("EXAMPLE 7.3\n"
                + ctx.select()
                        .from(PAYMENT.innerJoin(BANK_TRANSACTION)
                                .onKey())
                        .fetch()
        );

        // But, if we actually want to join only on CACHING_DATE then use ON
        System.out.println("EXAMPLE 7.4\n"
                + ctx.select()
                        .from(PAYMENT.innerJoin(BANK_TRANSACTION)
                                .on(PAYMENT.CACHING_DATE.eq(BANK_TRANSACTION.CACHING_DATE)))
                        .fetch()
        );
    }
}
