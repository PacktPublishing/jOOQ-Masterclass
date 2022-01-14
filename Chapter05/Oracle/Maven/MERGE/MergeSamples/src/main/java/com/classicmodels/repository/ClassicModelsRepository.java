package com.classicmodels.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import jooq.generated.Keys;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.PaymentRecord;
import org.jooq.DSLContext;
import org.jooq.Table;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    /*
    merge into "CLASSICMODELS"."PAYMENT" using (
      (
        select 
          null "CUSTOMER_NUMBER", 
          null "CHECK_NUMBER", 
          null "PAYMENT_DATE", 
          null "INVOICE_AMOUNT", 
          null "CACHING_DATE", 
          null "VERSION", 
          null "MODIFIED" 
        from 
          DUAL 
        where 
          1 = 0 
        union all 
        select 
          ?, ?, ?, ?, ?, ?, ? 
        from 
          DUAL
      )
    ) "t" on (
      (
        (
          (
            (
              "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER", 
              1
            ) = (
              ("t"."CUSTOMER_NUMBER", 1)
            ) 
            or 1 = 0
          ) 
          and (
            (
              "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
              1
            ) = (
              ("t"."CHECK_NUMBER", 1)
            ) 
            or 1 = 0
          )
        ) 
        or (
          "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          ("t"."CHECK_NUMBER", 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "CUSTOMER_NUMBER", "CHECK_NUMBER", 
      "PAYMENT_DATE", "INVOICE_AMOUNT", 
      "CACHING_DATE", "VERSION", "MODIFIED"
    ) 
    values 
      (
        "t"."CUSTOMER_NUMBER", "t"."CHECK_NUMBER", 
        "t"."PAYMENT_DATE", "t"."INVOICE_AMOUNT", 
        "t"."CACHING_DATE", "t"."VERSION", 
        "t"."MODIFIED"
      )    
     */
    public void insertPaymentOnDuplicateKeyIgnore() {

        System.out.println("EXAMPLE 1 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(129L, "ID449593",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21),
                                BigInteger.ZERO, LocalDateTime.now())
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 2
    /*
    merge into "CLASSICMODELS"."PAYMENT" using (
      (
        select 
          null "CUSTOMER_NUMBER", 
          null "CHECK_NUMBER", 
          null "PAYMENT_DATE", 
          null "INVOICE_AMOUNT", 
          null "CACHING_DATE", 
          null "VERSION", 
          null "MODIFIED" 
        from 
          DUAL 
        where 
          1 = 0 
        union all 
        select 
          ?, ?, ?, ?, ?, ?, ? 
        from 
          DUAL
      )
    ) "t" on (
      (
        (
          (
            (
              "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER", 
              1
            ) = (
              ("t"."CUSTOMER_NUMBER", 1)
            ) 
            or 1 = 0
          ) 
          and (
            (
              "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
              1
            ) = (
              ("t"."CHECK_NUMBER", 1)
            ) 
            or 1 = 0
          )
        ) 
        or (
          "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          ("t"."CHECK_NUMBER", 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "CUSTOMER_NUMBER", "CHECK_NUMBER", 
      "PAYMENT_DATE", "INVOICE_AMOUNT", 
      "CACHING_DATE", "VERSION", "MODIFIED"
    ) 
    values 
      (
        "t"."CUSTOMER_NUMBER", "t"."CHECK_NUMBER", 
        "t"."PAYMENT_DATE", "t"."INVOICE_AMOUNT", 
        "t"."CACHING_DATE", "t"."VERSION", 
        "t"."MODIFIED"
      )     
     */
    public void insertPaymentOnConflictDoNothing() {

        System.out.println("EXAMPLE 2 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(129L, "ID449593",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21),
                                BigInteger.ZERO, LocalDateTime.now())
                        .onConflictDoNothing()
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    merge into "CLASSICMODELS"."PAYMENT" using (
      (
        select 
          null "CUSTOMER_NUMBER", 
          null "CHECK_NUMBER", 
          null "PAYMENT_DATE", 
          null "INVOICE_AMOUNT", 
          null "CACHING_DATE", 
          null "VERSION", 
          null "MODIFIED" 
        from 
          DUAL 
        where 
          1 = 0 
        union all 
        select 
          ?, ?, ?, ?, ?, ?, ? 
        from 
          DUAL
      )
    ) "t" on (
      (
        (
          "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          ("t"."CHECK_NUMBER", 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "CUSTOMER_NUMBER", "CHECK_NUMBER", 
      "PAYMENT_DATE", "INVOICE_AMOUNT", 
      "CACHING_DATE", "VERSION", "MODIFIED"
    ) 
    values 
      (
        "t"."CUSTOMER_NUMBER", "t"."CHECK_NUMBER", 
        "t"."PAYMENT_DATE", "t"."INVOICE_AMOUNT", 
        "t"."CACHING_DATE", "t"."VERSION", 
        "t"."MODIFIED"
      )    
     */
    public void insertPaymentOnDuplicateCheckNumberDoNothing() {

        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(100L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21),
                                BigInteger.ZERO, LocalDateTime.now())
                        .onConflict(PAYMENT.CHECK_NUMBER)
                        .doNothing()
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    merge into "CLASSICMODELS"."PAYMENT" using (
      (
        select 
          null "CUSTOMER_NUMBER", 
          null "CHECK_NUMBER", 
          null "PAYMENT_DATE", 
          null "INVOICE_AMOUNT", 
          null "CACHING_DATE", 
          null "VERSION", 
          null "MODIFIED" 
        from 
          DUAL 
        where 
          1 = 0 
        union all 
        select 
          ?, ?, ?, ?, ?, ?, ? 
        from 
          DUAL
      )
    ) "t" on (
      (
        (
          "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          ("t"."CHECK_NUMBER", 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "CUSTOMER_NUMBER", "CHECK_NUMBER", 
      "PAYMENT_DATE", "INVOICE_AMOUNT", 
      "CACHING_DATE", "VERSION", "MODIFIED"
    ) 
    values 
      (
        "t"."CUSTOMER_NUMBER", "t"."CHECK_NUMBER", 
        "t"."PAYMENT_DATE", "t"."INVOICE_AMOUNT", 
        "t"."CACHING_DATE", "t"."VERSION", 
        "t"."MODIFIED"
      )     
     */
    public void insertPaymentOnConflictOnConstraintDoNothing() {

        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(129L, "ID449593",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21),
                                BigInteger.ZERO, LocalDateTime.now())
                        .onConflictOnConstraint(Keys.CHECK_NUMBER_UK)
                        // .onConflictOnConstraint(Keys.PAYMENT_PK)
                        .doNothing()
                        .execute()
        );
    }

    // EXAMPLE 5
    /*
    // 5.1, 5.2
    merge into "CLASSICMODELS"."PAYMENT" using (
      (
        select 
          null "CUSTOMER_NUMBER", 
          null "CHECK_NUMBER", 
          null "PAYMENT_DATE", 
          null "INVOICE_AMOUNT", 
          null "CACHING_DATE", 
          null "VERSION", 
          null "MODIFIED" 
        from 
          DUAL 
        where 
          1 = 0 
        union all 
        select 
          ?, ?, ?, ?, ?, ?, ? 
        from 
          DUAL
      )
    ) "t" on (
      (
        (
          (
            (
              "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER", 
              1
            ) = (
              ("t"."CUSTOMER_NUMBER", 1)
            ) 
            or 1 = 0
          ) 
          and (
            (
              "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
              1
            ) = (
              ("t"."CHECK_NUMBER", 1)
            ) 
            or 1 = 0
          )
        ) 
        or (
          "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          ("t"."CHECK_NUMBER", 1)
        ) 
        or 1 = 0
      )
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT" = ?, 
      "CLASSICMODELS"."PAYMENT"."CACHING_DATE" = ? when not matched then insert (
        "CUSTOMER_NUMBER", "CHECK_NUMBER", 
        "PAYMENT_DATE", "INVOICE_AMOUNT", 
        "CACHING_DATE", "VERSION", "MODIFIED"
      ) 
    values 
      (
        "t"."CUSTOMER_NUMBER", "t"."CHECK_NUMBER", 
        "t"."PAYMENT_DATE", "t"."INVOICE_AMOUNT", 
        "t"."CACHING_DATE", "t"."VERSION", 
        "t"."MODIFIED"
      )
    
    // 5.3
    merge into "CLASSICMODELS"."ORDER" using (
      (
        select 
          null "ORDER_ID", 
          null "ORDER_DATE", 
          null "REQUIRED_DATE", 
          null "SHIPPED_DATE", 
          null "STATUS", 
          null "COMMENTS", 
          null "CUSTOMER_NUMBER", 
          null "AMOUNT" 
        from 
          DUAL 
        where 
          1 = 0 
        union all 
        select 
          ?, 
          cast(? as date), 
          cast(? as date), 
          cast(? as date), 
          ?, ?, ?, ? 
        from 
          DUAL
      )
    ) "t" on (
      (
        (
          "CLASSICMODELS"."ORDER"."ORDER_ID", 
          1
        ) = (
          ("t"."ORDER_ID", 1)
        ) 
        or 1 = 0
      )
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."ORDER"."ORDER_DATE" = cast(? as date), 
      "CLASSICMODELS"."ORDER"."REQUIRED_DATE" = cast(? as date), 
      "CLASSICMODELS"."ORDER"."SHIPPED_DATE" = cast(? as date), 
      "CLASSICMODELS"."ORDER"."STATUS" = ? when not matched then insert (
        "ORDER_ID", "ORDER_DATE", "REQUIRED_DATE", 
        "SHIPPED_DATE", "STATUS", "COMMENTS", 
        "CUSTOMER_NUMBER", "AMOUNT"
      ) 
    values 
      (
        "t"."ORDER_ID", "t"."ORDER_DATE", 
        "t"."REQUIRED_DATE", "t"."SHIPPED_DATE", 
        "t"."STATUS", "t"."COMMENTS", "t"."CUSTOMER_NUMBER", 
        "t"."AMOUNT"
      )    
     */
    public void insertPaymentOnDuplicateKeyUpdateIt() {

        System.out.println("EXAMPLE 5.1 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(129L, "ID449593",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21),
                                BigInteger.ZERO, LocalDateTime.now())
                        .onDuplicateKeyUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.valueOf(123.32))
                        .set(PAYMENT.CACHING_DATE, LocalDateTime.now())
                        .execute()
        );

        System.out.println("EXAMPLE 5.2 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .set(PAYMENT.CUSTOMER_NUMBER, 103L)
                        .set(PAYMENT.CHECK_NUMBER, "HQ336336")
                        .set(PAYMENT.PAYMENT_DATE, LocalDateTime.of(2005, 11, 9, 12, 10, 11))
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.valueOf(123.32))
                        .set(PAYMENT.CACHING_DATE, LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .set(PAYMENT.VERSION, BigInteger.ZERO)
                        .set(PAYMENT.MODIFIED, LocalDateTime.now())
                        .onDuplicateKeyUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.valueOf(123.32))
                        .set(PAYMENT.CACHING_DATE, LocalDateTime.now())
                        .execute()
        );

        System.out.println("EXAMPLE 5.3 (affected rows): "
                + ctx.insertInto(ORDER)
                        .values(10101L, LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped", "New order inserted ...", 363L, 414.44)
                        .onDuplicateKeyUpdate()
                        .set(ORDER.ORDER_DATE, LocalDate.of(2003, 2, 12))
                        .set(ORDER.REQUIRED_DATE, LocalDate.of(2003, 3, 1))
                        .set(ORDER.SHIPPED_DATE, LocalDate.of(2003, 2, 27))
                        .set(ORDER.STATUS, "Shipped")
                        .execute()
        );
    }

    // EXAMPLE 6
    /*
    merge into "CLASSICMODELS"."PAYMENT" using (
    (
      select 
        null "CUSTOMER_NUMBER", 
        null "CHECK_NUMBER", 
        null "PAYMENT_DATE", 
        null "INVOICE_AMOUNT", 
        null "CACHING_DATE", 
        null "VERSION", 
        null "MODIFIED" 
      from 
        DUAL 
      where 
        1 = 0 
      union all 
      select 
        ?, ?, ?, ?, ?, ?, ? 
      from 
        DUAL
    )
  ) "t" on (
    (
      (
        "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
        1
      ) = (
        ("t"."CHECK_NUMBER", 1)
      ) 
      or 1 = 0
    )
  ) when matched then 
  update 
  set 
    "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT" = ?, 
    "CLASSICMODELS"."PAYMENT"."CACHING_DATE" = ? when not matched then insert (
      "CUSTOMER_NUMBER", "CHECK_NUMBER", 
      "PAYMENT_DATE", "INVOICE_AMOUNT", 
      "CACHING_DATE", "VERSION", "MODIFIED"
    ) 
  values 
    (
      "t"."CUSTOMER_NUMBER", "t"."CHECK_NUMBER", 
      "t"."PAYMENT_DATE", "t"."INVOICE_AMOUNT", 
      "t"."CACHING_DATE", "t"."VERSION", 
      "t"."MODIFIED"
    )  
     */
    public void insertPaymentOnConflictUpdateIt() {

        System.out.println("EXAMPLE 6 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(129L, "ID449593",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21),
                                BigInteger.ZERO, LocalDateTime.now())
                        .onConflict(PAYMENT.CHECK_NUMBER)
                        .doUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)
                        .setNull(PAYMENT.CACHING_DATE)
                        .execute()
        );
    }

    // EXAMPLE 7
    /*
    merge into "CLASSICMODELS"."PAYMENT" using (
      (
        select 
          null "CUSTOMER_NUMBER", 
          null "CHECK_NUMBER", 
          null "PAYMENT_DATE", 
          null "INVOICE_AMOUNT", 
          null "CACHING_DATE", 
          null "VERSION", 
          null "MODIFIED" 
        from 
          DUAL 
        where 
          1 = 0 
        union all 
        select 
          ?, ?, ?, ?, ?, ?, ? 
        from 
          DUAL
      )
    ) "t" on (
      (
        (
          (
            (
              "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER", 
              1
            ) = (
              ("t"."CUSTOMER_NUMBER", 1)
            ) 
            or 1 = 0
          ) 
          and (
            (
              "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
              1
            ) = (
              ("t"."CHECK_NUMBER", 1)
            ) 
            or 1 = 0
          )
        ) 
        or (
          "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          ("t"."CHECK_NUMBER", 1)
        ) 
        or 1 = 0
      )
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER" = ?, 
      "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER" = ?, 
      "CLASSICMODELS"."PAYMENT"."PAYMENT_DATE" = ?, 
      "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT" = ?, 
      "CLASSICMODELS"."PAYMENT"."CACHING_DATE" = ?, 
      "CLASSICMODELS"."PAYMENT"."VERSION" = ?, 
      "CLASSICMODELS"."PAYMENT"."MODIFIED" = ? when not matched then insert (
        "CUSTOMER_NUMBER", "CHECK_NUMBER", 
        "PAYMENT_DATE", "INVOICE_AMOUNT", 
        "CACHING_DATE", "VERSION", "MODIFIED"
      ) 
    values 
      (
        "t"."CUSTOMER_NUMBER", "t"."CHECK_NUMBER", 
        "t"."PAYMENT_DATE", "t"."INVOICE_AMOUNT", 
        "t"."CACHING_DATE", "t"."VERSION", 
        "t"."MODIFIED"
      )    
     */
    public void insertPaymentRecordOnDuplicateKeyUpdateIt() {

        PaymentRecord pr = new PaymentRecord(129L, "ID449593",
                LocalDateTime.of(2005, 11, 9, 12, 10, 11), BigDecimal.valueOf(123.32),
                LocalDateTime.of(2005, 11, 11, 14, 25, 21), BigInteger.ZERO, LocalDateTime.now());

        System.out.println("EXAMPLE 7 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .set(pr) // or, values(pr)                
                        .onDuplicateKeyUpdate()
                        .set(pr)
                        .execute()
        );
    }

    // EXAMPLE 8
    /*
    merge into "CLASSICMODELS"."PAYMENT" using (
      (
        select 
          null "CUSTOMER_NUMBER", 
          null "CHECK_NUMBER", 
          null "PAYMENT_DATE", 
          null "INVOICE_AMOUNT", 
          null "CACHING_DATE", 
          null "VERSION", 
          null "MODIFIED" 
        from 
          DUAL 
        where 
          1 = 0 
        union all 
        select 
          ?, ?, ?, ?, ?, ?, ? 
        from 
          DUAL
      )
    ) "t" on (
      (
        (
          "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          ("t"."CHECK_NUMBER", 1)
        ) 
        or 1 = 0
      )
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT" = ?, 
      "CLASSICMODELS"."PAYMENT"."CACHING_DATE" = ? when not matched then insert (
        "CUSTOMER_NUMBER", "CHECK_NUMBER", 
        "PAYMENT_DATE", "INVOICE_AMOUNT", 
        "CACHING_DATE", "VERSION", "MODIFIED"
      ) 
    values 
      (
        "t"."CUSTOMER_NUMBER", "t"."CHECK_NUMBER", 
        "t"."PAYMENT_DATE", "t"."INVOICE_AMOUNT", 
        "t"."CACHING_DATE", "t"."VERSION", 
        "t"."MODIFIED"
      )    
     */
    public void insertPaymentRecordOnConflictUpdateIt() {
        PaymentRecord pr = new PaymentRecord(129L, "ID449593",
                LocalDateTime.of(2005, 11, 9, 12, 10, 11), BigDecimal.valueOf(123.32),
                LocalDateTime.of(2005, 11, 11, 14, 25, 21), BigInteger.ZERO, LocalDateTime.now());

        System.out.println("EXAMPLE 8 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .set(pr) // or, values(pr)                
                        .onConflict(PAYMENT.CHECK_NUMBER)
                        .doUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)
                        .setNull(PAYMENT.CACHING_DATE)
                        .execute()
        );
    }

    /***********************/
    /* MERGE INTO EXAMPLES */
    /***********************/
        
    // EXAMPLE 9
    /*    
    merge into "CLASSICMODELS"."PRODUCT" using (
      select 
        1 "one" 
      from 
        DUAL
    ) on (
      (
        select 
          "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME" 
        from 
          DUAL
      ) = ?
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME" = ? when not matched then insert ("PRODUCT_NAME", "CODE") 
    values 
      (?, ?)  
     */
    public void updateProductNameElseInsertProduct() {

        // Since the following query leads to: "ORA-38104: Columns referenced in the ON Clause 
        // cannot be updated" it was commented. The possible workarounds are very well described in this article: 
        // https://blog.jooq.org/2019/01/02/how-to-work-around-ora-38104-columns-referenced-in-the-on-clause-cannot-be-updated/
        /*
        System.out.println("EXAMPLE 9 (affected rows): "
                + ctx.mergeInto(PRODUCT)
                        .usingDual() // or, (ctx.selectOne())
                        .on(PRODUCT.PRODUCT_NAME.eq("1952 Alpine Renault 1300"))
                        .whenMatchedThenUpdate()
                        .set(PRODUCT.PRODUCT_NAME, "1952 Alpine Renault 1600")
                        .whenNotMatchedThenInsert(PRODUCT.PRODUCT_NAME, PRODUCT.CODE)
                        .values("1952 Alpine Renault 1600", 599302L)
                        .execute()
        );        
         */
        // Using a correlated subquery (if indexes are not used then try another approach from the above article)
        System.out.println("EXAMPLE 9 (affected rows): "
                + ctx.mergeInto(PRODUCT)
                        .usingDual() // or, (ctx.selectOne())
                        .on(select(PRODUCT.PRODUCT_NAME).asField()
                                .eq("1952 Alpine Renault 1300"))
                        .whenMatchedThenUpdate()
                        .set(PRODUCT.PRODUCT_NAME, "1952 Alpine Renault 1600")
                        .whenNotMatchedThenInsert(PRODUCT.PRODUCT_NAME, PRODUCT.CODE)
                        .values("1952 Alpine Renault 1600", 599302L)
                        .execute()
        );
    }

    // EXAMPLE 10
    /*
    merge into "CLASSICMODELS"."CUSTOMER" using (
      select 
        1 "one" 
      from 
        DUAL
    ) on (
      (
        (
          select 
            "CLASSICMODELS"."CUSTOMER"."CONTACT_FIRST_NAME" 
          from 
            DUAL
        ) = ? 
        and "CLASSICMODELS"."CUSTOMER"."CONTACT_LAST_NAME" = ?
      )
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."CUSTOMER"."CONTACT_FIRST_NAME" = ? when not matched then insert (
        "CUSTOMER_NAME", "CONTACT_FIRST_NAME", 
        "CONTACT_LAST_NAME", "PHONE", "SALES_REP_EMPLOYEE_NUMBER", 
        "CREDIT_LIMIT"
      ) 
    values 
      (?, ?, ?, ?, ?, ?)    
     */
    public void updateCustomerFirstNameElseInsertCustomer() {

        System.out.println("EXAMPLE 10 (affected rows): "
                + ctx.mergeInto(CUSTOMER)
                        .usingDual() //or, using(ctx.selectOne())
                        .on(select(CUSTOMER.CONTACT_FIRST_NAME).asField().eq("Alejandra") // same issue as in Example 9
                                .and(CUSTOMER.CONTACT_LAST_NAME.eq("Camino")))
                        .whenMatchedThenUpdate()
                        .set(CUSTOMER.CONTACT_FIRST_NAME, "Alexandra")
                        .whenNotMatchedThenInsert(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER,
                                CUSTOMER.CREDIT_LIMIT)
                        .values(UUID.randomUUID().toString(), // random customer name
                                "Camino", "Alexandra", "(91) 745 6555", 1401L, BigDecimal.ZERO)
                        .execute()
        );
    }

    // EXAMPLE 11
    /*
    merge into "CLASSICMODELS"."SALE" using "CLASSICMODELS"."EMPLOYEE" on (
      "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."SALE"."SALE" = coalesce(
        (
          "CLASSICMODELS"."SALE"."SALE" - "CLASSICMODELS"."EMPLOYEE"."COMMISSION"
        ), 
        "CLASSICMODELS"."SALE"."SALE"
      ) when not matched then insert (
        "EMPLOYEE_NUMBER", "FISCAL_YEAR", 
        "SALE", "FISCAL_MONTH", "REVENUE_GROWTH"
      ) 
    values 
      (
        "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER", 
        ?, 
        coalesce(
          (
            ? * "CLASSICMODELS"."EMPLOYEE"."COMMISSION"
          ), 
          ?
        ), 
        ?, 
        ?
      )    
     */
    public void updateSaleElseInsertSale() {

        System.out.println("EXAMPLE 11 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(EMPLOYEE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .whenMatchedThenUpdate()
                        .set(SALE.SALE_, coalesce(SALE.SALE_.minus(EMPLOYEE.COMMISSION), SALE.SALE_))
                        .whenNotMatchedThenInsert(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(EMPLOYEE.EMPLOYEE_NUMBER, val(2015),
                                coalesce(val(-1.0).mul(EMPLOYEE.COMMISSION), val(0.0)), val(1), val(0.0))
                        .execute()
        );
    }

    // EXAMPLE 12
    /*
    merge into "CLASSICMODELS"."SALE" using "CLASSICMODELS"."EMPLOYEE" on (
      "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."SALE"."SALE_ID" = "CLASSICMODELS"."SALE"."SALE_ID" 
    where 
      (
        select 
          1 "one" 
        from 
          DUAL
      ) = ? delete 
    where 
      (
        select 
          1 "one" 
        from 
          DUAL
      ) = ? when not matched then insert (
        "EMPLOYEE_NUMBER", "FISCAL_YEAR", 
        "SALE", "FISCAL_MONTH", "REVENUE_GROWTH"
      ) 
    values 
      (
        "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER", 
        ?, 
        coalesce(
          (
            ? * "CLASSICMODELS"."EMPLOYEE"."COMMISSION"
          ), 
          ?
        ), 
        ?, 
        ?
      )    
     */
    public void deleteSaleElseInsertSale() {
        
        System.out.println("EXAMPLE 12 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(EMPLOYEE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        // .whenMatchedThenDelete() - not supported by Oracle
                        .whenMatchedAnd(selectOne().asField().eq(1))
                        .thenDelete()
                        .whenNotMatchedThenInsert(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(EMPLOYEE.EMPLOYEE_NUMBER, val(2015),
                                coalesce(val(-1.0).mul(EMPLOYEE.COMMISSION), val(0.0)), val(1), val(0.0))
                        .execute()
        );
    }

    // EXAMPLE 13
    /*
    merge into "CLASSICMODELS"."SALE" using "CLASSICMODELS"."EMPLOYEE" on (
      (
        "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" 
        and "CLASSICMODELS"."SALE"."SALE" < ?
      )
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."SALE"."SALE_ID" = "CLASSICMODELS"."SALE"."SALE_ID" 
    where 
      (
        select 
          1 "one" 
        from 
          DUAL
      ) = ? delete 
    where 
      (
        select 
          1 "one" 
        from 
          DUAL
      ) = ? when not matched then insert (
        "EMPLOYEE_NUMBER", "FISCAL_YEAR", 
        "SALE", "FISCAL_MONTH", "REVENUE_GROWTH"
      ) 
    values 
      (
        "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER", 
        ?, 
        coalesce(
          (
            ? * "CLASSICMODELS"."EMPLOYEE"."COMMISSION"
          ), 
          ?
        ), 
        ?, 
        ?
      )    
     */
    public void deleteNegativeSaleElseInsertSaleAsDoubleCommission() {

        System.out.println("EXAMPLE 13 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(EMPLOYEE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)
                                .and(SALE.SALE_.lt(0.0)))
                        //.whenMatchedThenDelete() - not supported by Oracle
                        .whenMatchedAnd(selectOne().asField().eq(1))
                        .thenDelete()
                        .whenNotMatchedThenInsert(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(EMPLOYEE.EMPLOYEE_NUMBER, val(2015),
                                coalesce(val(2.0).mul(EMPLOYEE.COMMISSION), val(0.0)), val(1), val(0.0))
                        .execute()
        );        
    }

    // EXAMPLE 14
    /*
    merge into "CLASSICMODELS"."SALE" using (
      select 
        "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER", 
        "CLASSICMODELS"."EMPLOYEE"."LAST_NAME", 
        "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
        "CLASSICMODELS"."EMPLOYEE"."EXTENSION", 
        "CLASSICMODELS"."EMPLOYEE"."EMAIL", 
        "CLASSICMODELS"."EMPLOYEE"."OFFICE_CODE", 
        "CLASSICMODELS"."EMPLOYEE"."SALARY", 
        "CLASSICMODELS"."EMPLOYEE"."COMMISSION", 
        "CLASSICMODELS"."EMPLOYEE"."REPORTS_TO", 
        "CLASSICMODELS"."EMPLOYEE"."JOB_TITLE", 
        "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_OF_YEAR", 
        "CLASSICMODELS"."EMPLOYEE"."MONTHLY_BONUS" 
      from 
        "CLASSICMODELS"."EMPLOYEE" 
      where 
        "CLASSICMODELS"."EMPLOYEE"."SALARY" < ?
    ) "t" on (
      "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" = "t"."EMPLOYEE_NUMBER"
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."SALE"."SALE_ID" = "CLASSICMODELS"."SALE"."SALE_ID" 
    where 
      (
        select 
          1 "one" 
        from 
          DUAL
      ) = ? delete 
    where 
      (
        select 
          1 "one" 
        from 
          DUAL
      ) = ? when not matched then insert (
        "EMPLOYEE_NUMBER", "FISCAL_YEAR", 
        "SALE", "FISCAL_MONTH", "REVENUE_GROWTH"
      ) 
    values 
      (
        "t"."EMPLOYEE_NUMBER", 
        ?, 
        coalesce("t"."COMMISSION", ?), 
        ?, 
        ?
      )    
     */
    public void deleteSalesOfEmpoyeeSalaryLt65000ElseInsert() {

        Table<?> table = selectFrom(EMPLOYEE)
                .where(EMPLOYEE.SALARY.lt(65000)).asTable("t");

        System.out.println("EXAMPLE 14 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(table)
                        .on(SALE.EMPLOYEE_NUMBER.eq(table.field(EMPLOYEE.EMPLOYEE_NUMBER)))
                        // .whenMatchedThenDelete() - not supported by Oracle
                        .whenMatchedAnd(selectOne().asField().eq(1))
                        .thenDelete()
                        .whenNotMatchedThenInsert(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(table.field(EMPLOYEE.EMPLOYEE_NUMBER), val(2015),
                                coalesce(table.field(EMPLOYEE.COMMISSION).coerce(Double.class), val(0.0)), 
                                val(1), val(0.0))
                        .execute()
        );        
    }

    // EXAMPLE 15
    /*
    merge into "CLASSICMODELS"."SALE" using (
      select 
        1 "one" 
      from 
        DUAL
    ) on (
      "CLASSICMODELS"."SALE"."FISCAL_YEAR" = ?
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."SALE"."SALE" = case when "CLASSICMODELS"."SALE"."SALE" > ? then (
        "CLASSICMODELS"."SALE"."SALE" * ?
      ) else "CLASSICMODELS"."SALE"."SALE" end 
    where 
      "CLASSICMODELS"."SALE"."SALE" > ? delete 
    where 
      "CLASSICMODELS"."SALE"."SALE" < ?    
     */
    public void updateSaleThenDeleteViaWhenMatchedAnd() {

        System.out.println("EXAMPLE 15 (affected rows): "
                + ctx.mergeInto(SALE)
                        .usingDual()
                        .on(SALE.FISCAL_YEAR.eq(2003))
                        .whenMatchedAnd(SALE.SALE_.gt(2000.0))
                        .thenUpdate()
                        .set(SALE.SALE_, SALE.SALE_.mul(0.50))
                        .whenMatchedAnd(SALE.SALE_.lt(2000.0))
                        .thenDelete()
                        .execute()
        );
    }

    // EXAMPLE 16
    /*
    merge into "CLASSICMODELS"."SALE" using (
      select 
        1 "one" 
      from 
        DUAL
    ) on (
      "CLASSICMODELS"."SALE"."FISCAL_YEAR" = ?
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."SALE"."SALE" = case when 1 = 1 then (
        "CLASSICMODELS"."SALE"."SALE" * ?
      ) else "CLASSICMODELS"."SALE"."SALE" end delete 
    where 
      "CLASSICMODELS"."SALE"."SALE" < ?    
     */
    public void updateSaleThenDeleteViaDeleteWhere() {

        System.out.println("EXAMPLE 16 (affected rows): "
                + ctx.mergeInto(SALE)
                        .usingDual()
                        .on(SALE.FISCAL_YEAR.eq(2003))
                        .whenMatchedThenUpdate()
                        .set(SALE.SALE_, SALE.SALE_.mul(0.50))
                        .deleteWhere(SALE.SALE_.lt(2000.0))
                        .execute()
        );
    }

    // EXAMPLE 17
    /*
    merge into "CLASSICMODELS"."SALE" using "CLASSICMODELS"."EMPLOYEE" on (
      "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER"
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."SALE"."SALE" = case when 1 = 1 then coalesce(
        (
          "CLASSICMODELS"."SALE"."SALE" - "CLASSICMODELS"."EMPLOYEE"."COMMISSION"
        ), 
        "CLASSICMODELS"."SALE"."SALE"
      ) else "CLASSICMODELS"."SALE"."SALE" end delete 
    where 
      "CLASSICMODELS"."SALE"."SALE" < ?    
     */
    public void updateSaleBeforeDelete() {

        System.out.println("EXAMPLE 17 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(EMPLOYEE)
                        .on(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))
                        .whenMatchedThenUpdate()
                        .set(SALE.SALE_, coalesce(SALE.SALE_.minus(EMPLOYEE.COMMISSION), SALE.SALE_))
                        .deleteWhere(SALE.SALE_.lt(1000.0))
                        .execute()
        );
    }

    // EXAMPLE 18
    /*
    merge into "CLASSICMODELS"."SALE" using "CLASSICMODELS"."EMPLOYEE" on (
      "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER"
    ) when matched then 
    update 
    set 
      "CLASSICMODELS"."SALE"."SALE" = case when 1 = 1 then coalesce(
        (
          "CLASSICMODELS"."SALE"."SALE" - "CLASSICMODELS"."EMPLOYEE"."COMMISSION"
        ), 
        "CLASSICMODELS"."SALE"."SALE"
      ) else "CLASSICMODELS"."SALE"."SALE" end delete 
    where 
      "CLASSICMODELS"."EMPLOYEE"."COMMISSION" < ?    
     */
    public void deleteSaleBeforeUpdate() {

        System.out.println("EXAMPLE 18 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(EMPLOYEE)
                        .on(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))
                        .whenMatchedThenUpdate()
                        .set(SALE.SALE_, coalesce(SALE.SALE_.minus(EMPLOYEE.COMMISSION), SALE.SALE_))
                        .deleteWhere(EMPLOYEE.COMMISSION.lt(1000))
                        .execute()
        );
    }
}