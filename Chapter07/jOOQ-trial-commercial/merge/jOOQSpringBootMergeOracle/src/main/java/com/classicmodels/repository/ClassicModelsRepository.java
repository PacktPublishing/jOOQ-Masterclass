package com.classicmodels.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jooq.generated.Keys;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.PaymentRecord;
import org.jooq.DSLContext;
import org.jooq.Table;
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
    merge into "SYSTEM"."PAYMENT" using (
      select 
        1 "one" 
      from 
        dual
    ) on (
      (
        (
          (
            (
              "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER", 
              1
            ) = (
              (?, 1)
            ) 
            or 1 = 0
          ) 
          and (
            (
              "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
              1
            ) = (
              (?, 1)
            ) 
            or 1 = 0
          )
        ) 
        or (
          "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          (?, 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "CUSTOMER_NUMBER", "CHECK_NUMBER", 
      "PAYMENT_DATE", "INVOICE_AMOUNT", 
      "CACHING_DATE"
    ) 
    values 
      (?, ?, ?, ?, ?)
     */
    public void insertPaymentOnDuplicateKeyIgnore() {

        System.out.println("EXAMPLE 1 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 2
    /*
    merge into "SYSTEM"."PAYMENT" using (
      select 
        1 "one" 
      from 
        dual
    ) on (
      (
        (
          (
            (
              "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER", 
              1
            ) = (
              (?, 1)
            ) 
            or 1 = 0
          ) 
          and (
            (
              "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
              1
            ) = (
              (?, 1)
            ) 
            or 1 = 0
          )
        ) 
        or (
          "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          (?, 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "CUSTOMER_NUMBER", "CHECK_NUMBER", 
      "PAYMENT_DATE", "INVOICE_AMOUNT", 
      "CACHING_DATE"
    ) 
    values 
      (?, ?, ?, ?, ?)
     */
    public void insertPaymentOnConflictDoNothing() {

        System.out.println("EXAMPLE 2 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onConflictDoNothing()
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    merge into "SYSTEM"."PAYMENT" using (
      select 
        1 "one" 
      from 
        dual
    ) on (
      (
        (
          "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          (?, 1)
        ) 
        or 1 = 0
      )
    ) when not matched then insert (
      "CUSTOMER_NUMBER", "CHECK_NUMBER", 
      "PAYMENT_DATE", "INVOICE_AMOUNT", 
      "CACHING_DATE"
    ) 
    values 
      (?, ?, ?, ?, ?)
     */
    public void insertPaymentOnDuplicateCheckNumberDoNothing() {

        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(100L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onConflict(PAYMENT.CHECK_NUMBER)
                        .doNothing()
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    merge into "SYSTEM"."PAYMENT" using (
    select 
      1 "one" 
    from 
      dual
    ) on (
     (
       (
         "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
         1
       ) = (
        (?, 1)
     ) 
     or 1 = 0
    )
    ) when not matched then insert (
      "CUSTOMER_NUMBER", "CHECK_NUMBER", 
      "PAYMENT_DATE", "INVOICE_AMOUNT", 
      "CACHING_DATE"
    ) 
    values 
      (?, ?, ?, ?, ?)
     */
    public void insertPaymentOnConflictOnConstraintDoNothing() {

        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onConflictOnConstraint(Keys.UNIQUE_CHECK_NUMBER)
                        .doNothing()
                        .execute()
        );
    }

    // EXAMPLE 5
    /*
    merge into "SYSTEM"."PAYMENT" using (
    select 
      1 "one" 
    from 
      dual
    ) on (
     (
       (
         (
           (
             "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER", 
             1
           ) = (
             (?, 1)
           ) 
           or 1 = 0
         ) 
        and (
          (
            "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
            1
          ) = (
            (?, 1)
          ) 
        or 1 = 0
      )
    ) 
    or (
      "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
      1
    ) = (
      (?, 1)
    ) 
    or 1 = 0
    ) 
    ) when matched then 
    update 
    set 
     "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" = ?, 
     "SYSTEM"."PAYMENT"."CACHING_DATE" = ? when not matched then insert (
     "CUSTOMER_NUMBER", "CHECK_NUMBER", 
     "PAYMENT_DATE", "INVOICE_AMOUNT", 
     "CACHING_DATE"
    ) 
    values 
     (?, ?, ?, ?, ?)
     */
    public void insertPaymentOnDuplicateKeyUpdateIt() {

        System.out.println("EXAMPLE 5.1 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
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
                        .onDuplicateKeyUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.valueOf(123.32))
                        .set(PAYMENT.CACHING_DATE, LocalDateTime.now())
                        .execute()
        );

        /*
        merge into "SYSTEM"."ORDER" using (
        select 
          1 "one" 
        from 
          dual
        ) on (
          (
            ("SYSTEM"."ORDER"."ORDER_ID", 1) = (
              (?, 1)
            ) 
          or 1 = 0
          )
        ) when matched then 
        update 
        set 
          "SYSTEM"."ORDER"."ORDER_DATE" = cast(? as date), 
          "SYSTEM"."ORDER"."REQUIRED_DATE" = cast(? as date), 
          "SYSTEM"."ORDER"."SHIPPED_DATE" = cast(? as date), 
          "SYSTEM"."ORDER"."STATUS" = ? when not matched then insert (
          "ORDER_ID", "ORDER_DATE", "REQUIRED_DATE", 
          "SHIPPED_DATE", "STATUS", "COMMENTS", 
          "CUSTOMER_NUMBER"
        ) 
        values 
         (
           ?, cast(? as date), cast(? as date), cast(? as date), ?, ?, ?
          )
         */
        System.out.println("EXAMPLE 5.3 (affected rows): "
                + ctx.insertInto(ORDER)
                        .values(10101L, LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped", "New order inserted ...", 363L)
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
    merge into "SYSTEM"."PAYMENT" using (
    select 
      1 "one" 
    from 
      dual
    ) on (
     (
       (
         "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
         1
       ) = (
        (?, 1)
       ) 
     or 1 = 0
     )
    ) when matched then 
    update 
    set 
      "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" = ?, 
      "SYSTEM"."PAYMENT"."CACHING_DATE" = ? when not matched then insert (
      "CUSTOMER_NUMBER", "CHECK_NUMBER", 
      "PAYMENT_DATE", "INVOICE_AMOUNT", 
      "CACHING_DATE"
    ) 
    values 
      (?, ?, ?, ?, ?)
     */
    public void insertPaymentOnConflictUpdateIt() {

        System.out.println("EXAMPLE 6 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onConflict(PAYMENT.CHECK_NUMBER)
                        .doUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)
                        .setNull(PAYMENT.CACHING_DATE)
                        .execute()
        );
    }

    // EXAMPLE 7
    /*
    merge into "SYSTEM"."PAYMENT" using (
    select 
        1 "one" 
      from 
        dual
    ) on (
      (
        (
          (
            (
              "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER", 
              1
            ) = (
              (?, 1)
            ) 
            or 1 = 0
          ) 
          and (
            (
              "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
              1
            ) = (
              (?, 1)
            ) 
            or 1 = 0
          )
        ) 
        or (
          "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          (?, 1)
        ) 
        or 1 = 0
      )
    ) when matched then 
    update 
    set 
      "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER" = ?, 
      "SYSTEM"."PAYMENT"."CHECK_NUMBER" = ?, 
      "SYSTEM"."PAYMENT"."PAYMENT_DATE" = ?, 
      "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" = ?, 
      "SYSTEM"."PAYMENT"."CACHING_DATE" = ? when not matched then insert (
        "CUSTOMER_NUMBER", "CHECK_NUMBER", 
        "PAYMENT_DATE", "INVOICE_AMOUNT", 
        "CACHING_DATE"
      ) 
    values 
      (?, ?, ?, ?, ?)   
     */
    public void insertPaymentRecordOnDuplicateKeyUpdateIt() {

        PaymentRecord pr = new PaymentRecord(103L, "HQ336336",
                LocalDateTime.of(2005, 11, 9, 12, 10, 11), BigDecimal.valueOf(123.32),
                LocalDateTime.of(2005, 11, 11, 14, 25, 21));

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
    merge into "SYSTEM"."PAYMENT" using (
      select 
        1 "one" 
      from 
        dual
    ) on (
      (
        (
          "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          (?, 1)
        ) 
        or 1 = 0
      )
    ) when matched then 
    update 
    set 
      "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" = ?, 
      "SYSTEM"."PAYMENT"."CACHING_DATE" = ? when not matched then insert (
        "CUSTOMER_NUMBER", "CHECK_NUMBER", 
        "PAYMENT_DATE", "INVOICE_AMOUNT", 
        "CACHING_DATE"
      ) 
    values 
      (?, ?, ?, ?, ?)
     */
    public void insertPaymentRecordOnConflictUpdateIt() {
        PaymentRecord pr = new PaymentRecord(103L, "HQ336336",
                LocalDateTime.of(2005, 11, 9, 12, 10, 11), BigDecimal.valueOf(123.32),
                LocalDateTime.of(2005, 11, 11, 14, 25, 21));

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

    /* MERGE INTO EXAMPLES */
    // EXAMPLE 9
    /*    
    merge into "SYSTEM"."PAYMENT" using (
      select 
        1 "one" 
      from 
        dual
    ) on (
      (
        (
          "SYSTEM"."PAYMENT"."CHECK_NUMBER", 
          1
        ) = (
          (?, 1)
        ) 
        or 1 = 0
      )
    ) when matched then 
    update 
    set 
      "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" = ?, 
      "SYSTEM"."PAYMENT"."CACHING_DATE" = ? when not matched then insert (
        "CUSTOMER_NUMBER", "CHECK_NUMBER", 
        "PAYMENT_DATE", "INVOICE_AMOUNT", 
        "CACHING_DATE"
      ) 
    values 
      (?, ?, ?, ?, ?)
     */
    public void updateProductNameElseInsertProduct() {

        // Since the following query leads to: "ORA-38104: Columns referenced in the ON Clause cannot be updated"
        // it was commented. The possible workarounds are very well described in this article: 
        // https://blog.jooq.org/2019/01/02/how-to-work-around-ora-38104-columns-referenced-in-the-on-clause-cannot-be-updated/
        /*
        System.out.println("EXAMPLE 9 (affected rows): "
                + ctx.mergeInto(PRODUCT)
                        .usingDual() // or, (ctx.selectOne())
                        .on(PRODUCT.PRODUCT_NAME.eq("1952 Alpine Renault 1300"))
                        .whenMatchedThenUpdate()
                        .set(PRODUCT.PRODUCT_NAME, "1952 Alpine Renault 1600")
                        .whenNotMatchedThenInsert(PRODUCT.PRODUCT_NAME)
                        .values("1952 Alpine Renault 1600")
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
                        .whenNotMatchedThenInsert(PRODUCT.PRODUCT_NAME)
                        .values("1952 Alpine Renault 1600")
                        .execute()
        );
    }

    // EXAMPLE 10
    /*
    merge into "SYSTEM"."CUSTOMER" using (
      select 
        1 "one" 
      from 
        dual
    ) on (
      (
        (
          select 
            "SYSTEM"."CUSTOMER"."CONTACT_FIRST_NAME" 
          from 
            dual
        ) = ? 
        and "SYSTEM"."CUSTOMER"."CONTACT_LAST_NAME" = ?
      )
    ) when matched then 
    update 
    set 
      "SYSTEM"."CUSTOMER"."CONTACT_FIRST_NAME" = ? when not matched then insert (
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
                        .values("Atelier One", "Camino", "Alexandra", "(91) 745 6555", 1401L, BigDecimal.ZERO)
                        .execute()
        );
    }

    // EXAMPLE 11
    /*
    merge into "SYSTEM"."DEPARTMENT" using "SYSTEM"."OFFICE" on (
      "SYSTEM"."DEPARTMENT"."OFFICE_CODE" = "SYSTEM"."OFFICE"."OFFICE_CODE"
    ) when matched then 
    update 
    set 
      "SYSTEM"."DEPARTMENT"."PHONE" = "SYSTEM"."OFFICE"."PHONE" when not matched then insert (
        "NAME", "CODE", "OFFICE_CODE", "PHONE"
      ) 
    values 
      (
        ?, ?, "SYSTEM"."OFFICE"."OFFICE_CODE", 
        "SYSTEM"."OFFICE"."PHONE"
      ) 
     */
    public void updateDepartmentPhoneElseInsertDepartment() {

        System.out.println("EXAMPLE 11 (affected rows): "
                + ctx.mergeInto(DEPARTMENT)
                        .using(OFFICE)
                        .on(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        .whenMatchedThenUpdate()
                        .set(DEPARTMENT.PHONE, OFFICE.PHONE)
                        .whenNotMatchedThenInsert(DEPARTMENT.NAME, DEPARTMENT.CODE,
                                DEPARTMENT.OFFICE_CODE, DEPARTMENT.PHONE)
                        .values(val("Management"), val(34332), OFFICE.OFFICE_CODE, OFFICE.PHONE)
                        .execute()
        );
    }

    // EXAMPLE 12
    /*
    merge into "SYSTEM"."DEPARTMENT" using "SYSTEM"."OFFICE" on (
      "SYSTEM"."DEPARTMENT"."OFFICE_CODE" = "SYSTEM"."OFFICE"."OFFICE_CODE"
    ) when matched then 
    update 
    set 
      "SYSTEM"."DEPARTMENT"."DEPARTMENT_ID" = "SYSTEM"."DEPARTMENT"."DEPARTMENT_ID" 
    where 
      (
        select 
          1 "one" 
        from 
          dual
      ) = ? delete 
    where 
      (
        select 
          1 "one" 
        from 
          dual
      ) = ? when not matched then insert (
        "NAME", "CODE", "OFFICE_CODE", "PHONE"
      ) 
    values 
      (
        ?, ?, "SYSTEM"."OFFICE"."OFFICE_CODE", 
        "SYSTEM"."OFFICE"."PHONE"
      )
     */
    public void deleteDepartmentElseInsertDepartment() {

        System.out.println("EXAMPLE 12 (affected rows): "
                + ctx.mergeInto(DEPARTMENT)
                        .using(OFFICE)
                        .on(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                        //.whenMatchedThenDelete() - not supported by Oracle
                        .whenMatchedAnd(selectOne().asField().eq(1))
                        .thenDelete()
                        .whenNotMatchedThenInsert(DEPARTMENT.NAME, DEPARTMENT.CODE,
                                DEPARTMENT.OFFICE_CODE, DEPARTMENT.PHONE)
                        .values(val("Management"), val(34332), OFFICE.OFFICE_CODE, OFFICE.PHONE)
                        .execute()
        );
    }

    // EXAMPLE 13
    /*
    merge into "SYSTEM"."DEPARTMENT" using "SYSTEM"."OFFICE" on (
      (
        "SYSTEM"."DEPARTMENT"."OFFICE_CODE" = "SYSTEM"."OFFICE"."OFFICE_CODE" 
        and "SYSTEM"."DEPARTMENT"."NAME" = ?
      )
    ) when matched then 
    update 
    set 
      "SYSTEM"."DEPARTMENT"."DEPARTMENT_ID" = "SYSTEM"."DEPARTMENT"."DEPARTMENT_ID" 
    where 
      (
        select 
          1 "one" 
        from 
          dual
      ) = ? delete 
    where 
      (
        select 
          1 "one" 
        from 
          dual
      ) = ? when not matched then insert (
        "NAME", "CODE", "OFFICE_CODE", "PHONE"
      ) 
    values 
      (
        ?, ?, "SYSTEM"."OFFICE"."OFFICE_CODE", 
        "SYSTEM"."OFFICE"."PHONE"
      )
     */
    public void deleteDepartmentAccountingElseInsertDepartment() {

        System.out.println("EXAMPLE 13 (affected rows): "
                + ctx.mergeInto(DEPARTMENT)
                        .using(OFFICE)
                        .on(DEPARTMENT.OFFICE_CODE.eq(OFFICE.OFFICE_CODE)
                                .and(DEPARTMENT.NAME.eq("Accounting")))
                        //.whenMatchedThenDelete() - not supported by Oracle
                        .whenMatchedAnd(selectOne().asField().eq(1))
                        .thenDelete()
                        .whenNotMatchedThenInsert(DEPARTMENT.NAME, DEPARTMENT.CODE,
                                DEPARTMENT.OFFICE_CODE, DEPARTMENT.PHONE)
                        .values(val("Management"), val(34332), OFFICE.OFFICE_CODE, OFFICE.PHONE)
                        .execute()
        );
    }

    // EXAMPLE 14
    /*
    merge into "SYSTEM"."DEPARTMENT" using (
      (
        select 
          "SYSTEM"."OFFICE"."OFFICE_CODE", 
          "SYSTEM"."OFFICE"."CITY", 
          "SYSTEM"."OFFICE"."PHONE", 
          "SYSTEM"."OFFICE"."ADDRESS_LINE_FIRST", 
          "SYSTEM"."OFFICE"."ADDRESS_LINE_SECOND", 
          "SYSTEM"."OFFICE"."STATE", 
          "SYSTEM"."OFFICE"."COUNTRY", 
          "SYSTEM"."OFFICE"."POSTAL_CODE", 
          "SYSTEM"."OFFICE"."TERRITORY" 
        from 
          "SYSTEM"."OFFICE" 
        where 
          "SYSTEM"."OFFICE"."CITY" = ?
      )
    ) "t" on (
      "SYSTEM"."DEPARTMENT"."OFFICE_CODE" = "t"."OFFICE_CODE"
    ) when matched then 
    update 
    set 
      "SYSTEM"."DEPARTMENT"."DEPARTMENT_ID" = "SYSTEM"."DEPARTMENT"."DEPARTMENT_ID" 
    where 
      (
        select 
          1 "one" 
        from 
          dual
      ) = ? delete 
    where 
      (
        select 
          1 "one" 
        from 
          dual
      ) = ? when not matched then insert (
        "NAME", "CODE", "OFFICE_CODE", "PHONE"
      ) 
    values 
      (
        ?, ?, "t"."OFFICE_CODE", "t"."PHONE"
      )
     */
    public void deleteParisDepartmentElseInsert() {

        Table<?> table = selectFrom(OFFICE)
                .where(OFFICE.CITY.eq("Boston")).asTable("t");

        System.out.println("EXAMPLE 14 (affected rows): "
                + ctx.mergeInto(DEPARTMENT)
                        .using(table)
                        .on(DEPARTMENT.OFFICE_CODE.eq(table.field(OFFICE.OFFICE_CODE)))
                        // .whenMatchedThenDelete() - not supported by Oracle
                        .whenMatchedAnd(selectOne().asField().eq(1))
                        .thenDelete()
                        .whenNotMatchedThenInsert(DEPARTMENT.NAME, DEPARTMENT.CODE,
                                DEPARTMENT.OFFICE_CODE, DEPARTMENT.PHONE)
                        .values(val("Management"), val(34332),
                                table.field(OFFICE.OFFICE_CODE),
                                table.field(OFFICE.PHONE))
                        .execute()
        );
    }

    // EXAMPLE 15
    /*
    merge into "SYSTEM"."SALE" using (
      select 
        1 "one" 
      from 
        dual
    ) on (
      "SYSTEM"."SALE"."FISCAL_YEAR" = ?
    ) when matched then 
    update 
    set 
      "SYSTEM"."SALE"."SALE" = case when "SYSTEM"."SALE"."SALE" > ? 
          then ("SYSTEM"."SALE"."SALE" * ?) 
            else "SYSTEM"."SALE"."SALE" end 
    where 
      "SYSTEM"."SALE"."SALE" > ? 
    delete 
    where 
      "SYSTEM"."SALE"."SALE" < ?
     */
    public void updateSaleThenDeleteViaWhenMatchedAnd() {

        System.out.println("EXAMPLE 15 (affected rows): "
                + ctx.mergeInto(SALE)
                        .usingDual()
                        .on(SALE.FISCAL_YEAR.eq(BigInteger.valueOf(2003)))
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
    merge into "SYSTEM"."SALE" using (
      select 
        1 "one" 
      from 
        dual
    ) on (
      "SYSTEM"."SALE"."FISCAL_YEAR" = ?
    ) when matched then 
    update 
    set 
      "SYSTEM"."SALE"."SALE" = case when 1 = 1 
         then ("SYSTEM"."SALE"."SALE" * ?) 
            else "SYSTEM"."SALE"."SALE" end 
    delete 
    where 
      "SYSTEM"."SALE"."SALE" < ?
     */
    public void updateSaleThenDeleteViaDeleteWhere() {

        System.out.println("EXAMPLE 16 (affected rows): "
                + ctx.mergeInto(SALE)
                        .usingDual()
                        .on(SALE.FISCAL_YEAR.eq(BigInteger.valueOf(2003)))
                        .whenMatchedThenUpdate()
                        .set(SALE.SALE_, SALE.SALE_.mul(0.50))
                        .deleteWhere(SALE.SALE_.lt(2000.0))
                        .execute()
        );
    }

    // EXAMPLE 17
    /*
    merge into "SYSTEM"."PAYMENT" using "SYSTEM"."CUSTOMER" on (
      "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER" = "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER"
    ) when matched then 
    update 
    set 
      "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" = case when 1 = 1 then (
        "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" + "SYSTEM"."CUSTOMER"."CREDIT_LIMIT"
      ) else "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" end delete 
    where 
      "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" > ?
     */
    public void updatePaymentBeforeDelete() {

        System.out.println("EXAMPLE 17 (affected rows): "
                + ctx.mergeInto(PAYMENT)
                        .using(CUSTOMER)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                        .whenMatchedThenUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, PAYMENT.INVOICE_AMOUNT.plus(CUSTOMER.CREDIT_LIMIT))
                        .deleteWhere(PAYMENT.INVOICE_AMOUNT.gt(BigDecimal.valueOf(100000)))
                        .execute()
        );
    }

    // EXAMPLE 18
    /*
    merge into "SYSTEM"."PAYMENT" using "SYSTEM"."CUSTOMER" on (
      "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER" = "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER"
    ) when matched then 
    update 
    set 
      "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" = case when 1 = 1 then (
        "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" + "SYSTEM"."CUSTOMER"."CREDIT_LIMIT"
      ) else "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" end delete 
    where 
      "SYSTEM"."CUSTOMER"."CREDIT_LIMIT" > ?
     */
    public void deletePaymentBeforeUpdate() {

        System.out.println("EXAMPLE 18 (affected rows): "
                + ctx.mergeInto(PAYMENT)
                        .using(CUSTOMER)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                        .whenMatchedThenUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, PAYMENT.INVOICE_AMOUNT.plus(CUSTOMER.CREDIT_LIMIT))
                        .deleteWhere(CUSTOMER.CREDIT_LIMIT.gt(BigDecimal.valueOf(100000)))
                        .execute()
        );

    }
}
