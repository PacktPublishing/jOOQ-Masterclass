package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.Sequences.EMPLOYEE_SEQ;
import static jooq.generated.Sequences.SALE_SEQ;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.daos.SaleRepository;
import jooq.generated.tables.pojos.Sale;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import static org.jooq.Records.intoList;
import org.jooq.conf.Settings;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final SaleRepository saleRepository;

    public ClassicModelsRepository(DSLContext ctx, SaleRepository saleRepository) {
        this.ctx = ctx;
        this.saleRepository = saleRepository;
    }

    /* Insert and return primary key */
    @Transactional
    public void insertIntoAndReturnPrimaryKey() {
        
        Record1<Long> insertedId1 = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 2311.42, 1370L, 1, 0.0)
                .returningResult(SALE.getIdentity().getField())
                // or, .returningResult(SALE.SALE_ID)
                .fetchOne();
                
        long insertedId2 = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 2311.42, 1370L, 1, 0.0)
                .returningResult(SALE.getIdentity().getField())
                // or, .returningResult(SALE.SALE_ID)
                .fetchOneInto(long.class); 
                // or, .fetchOne().value1();

        System.out.println("Inserted ID:\n" + insertedId1);
        System.out.println("Inserted ID:\n" + insertedId2);

        // Result<Record1<Long>>
        var insertedIds1 = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 2311.42, 1370L, 1, 0.0)
                .values(2003, 900.21, 1504L, 1, 0.0)
                .values(2005, 1232.2, 1166L, 1, 0.0)
                .returningResult(SALE.getIdentity().getField())
                // or, .returningResult(SALE.SALE_ID)
                .fetch();
        
        List<Long> insertedIds2 = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2004, 2311.42, 1370L, 1, 0.0)
                .values(2003, 900.21, 1504L, 1, 0.0)
                .values(2005, 1232.2, 1166L, 1, 0.0)
                .returningResult(SALE.getIdentity().getField())
                // or, .returningResult(SALE.SALE_ID)
                .collect(intoList());
                // or, .fetchInto(Long.class);

        System.out.println("Inserted IDs:\n" + insertedIds1);
        System.out.println("Inserted IDs:\n" + insertedIds2);

        // use lastID()
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2002, 9876.96, 1504L, 1, 0.0)
                .execute();

        // PAY ATTENTION TO THE FACT THAT, MEANWHILE, A CONCURRENT TRANSACTION CAN MODIFY THE CURRENT VALUE
        // SO, THERE IS NO GUARANTEE THAT THE BELOW FETCHED *lastId* IS THE PRIMARY KEY OF THE PREVIOUS INSERT
        // if you cannot provide an identity
        var lastId = ctx.lastID();

        System.out.println("Last ID: " + lastId);
    }

    /* Primary keys and updatable records */
    @Transactional
    public void insertAndReturnPrimaryKey() {

        // insert record
        SaleRecord sr = ctx.newRecord(SALE);

        sr.setFiscalYear(2021);
        sr.setSale(4500.25);
        sr.setEmployeeNumber(1504L);
        sr.setFiscalMonth(1);
        sr.setRevenueGrowth(0.0);

        sr.insert();

        System.out.println("The inserted record ID: " + sr.getSaleId());

        // insert POJO
        Sale s = new Sale();
        s.setFiscalYear(2020);
        s.setSale(643.23);
        s.setEmployeeNumber(1370L);
        s.setFiscalMonth(1);
        s.setRevenueGrowth(0.0);

        saleRepository.insert(s);

        System.out.println("The inserted POJO ID: " + s.getSaleId());
    }

    @Transactional
    public void suppressPrimaryKeyReturnOnUpdatableRecord() {

        // Insert without returning the generated primary key
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withReturnIdentityOnUpdatableRecord(false)).dsl();

        SaleRecord srNoReturnId = derivedCtx.newRecord(SALE);

        srNoReturnId.setFiscalYear(2021);
        srNoReturnId.setSale(4500.25);
        srNoReturnId.setEmployeeNumber(1504L);
        srNoReturnId.setFiscalMonth(1);
        srNoReturnId.setRevenueGrowth(0.0);

        srNoReturnId.insert();

        System.out.println("The inserted record ID (should be null): " + srNoReturnId.getSaleId());
    }

    @Transactional
    public void updatePrimaryKeyOnUpdatableRecord() {

        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withUpdatablePrimaryKeys(true)).dsl();

        SaleRecord sr = derivedCtx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(2L))
                .fetchSingle();

        // Forcing an UPDATE can be done via Settings.isUpdatablePrimaryKeys() 
        // By default, isUpdatablePrimaryKeys() return false
        sr.setSaleId((long) (Math.random() * 999999999L));
        sr.setFiscalYear(2007);

        sr.store();

        System.out.println("The stored record is (force UPDATE of primary key):\n" + sr);

        // update primary key via query
        ctx.update(SALE)
                .set(SALE.SALE_ID, sr.getSaleId() + 1)
                .where(SALE.SALE_ID.eq(sr.getSaleId()))
                .execute();
    }

    /* Compare composed keys */
    public void compareComposedPrimaryKey() {

        var result1 = ctx.selectFrom(PRODUCTLINE)
                .where(PRODUCTLINE.PRODUCT_LINE.eq("Classic Cars")
                        .and(PRODUCTLINE.CODE.eq(599302L)))
                .fetchSingle();
        System.out.println("Result 1:\n" + result1);

        var result2 = ctx.selectFrom(PRODUCTLINE)
                .where(row(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE)
                        .eq(row("Classic Cars", 599302L))) // or, .eq("Classic Cars", 599302L))
                .fetchSingle();
        System.out.println("Result 2:\n" + result2);

        // using in() (similar, you can use notIn(), and so on)
        var result3 = ctx.selectFrom(PRODUCTLINE)
                .where(row(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE)
                        .in(row("Classic Cars", 599302L),
                                row("Trains", 123333L),
                                row("Motorcycles", 599302L)))
                .fetch();
        System.out.println("Result 3:\n" + result3);
    }

    @Transactional
    public void getSequenceInfo() {

        String name = EMPLOYEE_SEQ.getName();
        Field<Long> start = EMPLOYEE_SEQ.getStartWith();
        Field<Long> min = EMPLOYEE_SEQ.getMinvalue();
        Field<Long> max = EMPLOYEE_SEQ.getMaxvalue();
        Field<Long> inc = EMPLOYEE_SEQ.getIncrementBy();

        System.out.println("SEQUENCE: " + "\nName:" + name + "\n"
                + "Start: " + start + "\nMin: " + min + "\nMax: " + max + "\nInc:" + inc);       
        
        long nval1 = ctx.fetchValue(EMPLOYEE_SEQ.nextval());
        long nval2 = ctx.select(EMPLOYEE_SEQ.nextval()).fetchSingle().value1();
        long nval3 = ctx.select(EMPLOYEE_SEQ.nextval()).fetchSingleInto(Long.class); // or, fetchOneInto()
        
        System.out.println("Current val: " + nval1 + ", " + nval2 + ", " +nval3);
        
        Record2<Long, Long> vals1 = ctx.fetchSingle(EMPLOYEE_SEQ.nextval(), EMPLOYEE_SEQ.currval());
        Record2<Long, Long> vals2 = ctx.select(EMPLOYEE_SEQ.nextval(), EMPLOYEE_SEQ.currval()).fetchSingle();        

        System.out.println("Next val: " + vals1.get(0) + " Current val: " + vals1.get(1));
        System.out.println("Next val: " + vals2.get(0) + " Current val: " + vals2.get(1));        
        
        long cval1 = ctx.fetchValue(EMPLOYEE_SEQ.currval());
        long cval2 = ctx.select(EMPLOYEE_SEQ.currval()).fetchSingle().value1();
        long cval3 = ctx.select(EMPLOYEE_SEQ.currval()).fetchSingleInto(Long.class); // or, fetchOneInto()
        
        System.out.println("Current val: " + cval1 + ", " + cval2 + ", " +cval3);

        List<Long> next10Vals1 = ctx.fetchValues(EMPLOYEE_SEQ.nextvals(10));
        List<Long> next10Vals2 = ctx.fetch(EMPLOYEE_SEQ.nextvals(10)).into(Long.class);
        List<Record1<Long>> next10Vals3 = ctx.fetch(EMPLOYEE_SEQ.nextvals(10));
        
        System.out.println("Next 10 vals:\n" + next10Vals1);
        System.out.println("Next 10 vals:\n" + next10Vals2);        
        System.out.println("Next 10 vals:\n" + next10Vals3);
    }

    @Transactional
    public void currentSequenceVal1() {

        // Avoid: ERROR: CURRVAL of sequence "sale_seq" is not yet defined in this session
        // SALE_SEQ.nextval(); - you can call this, but an INSERT will also call NEXTVAL
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2020, 900.25, 1611L, 1, 0.0)
                .execute();

        // PAY ATTENTION TO THE FACT THAT, MEANWHILE, A CONCURRENT TRANSACTION CAN MODIFY THE CURRENT VALUE
        // SO, THERE IS NO GUARANTEE THAT THE BELOW FETCHED *cr* IS THE PRIMARY KEY OF THE PREVIOUS INSERT
        // IF YOU NEED THE PK OF THE ABOVE INSERT THEN RELY ON INSERT ... RETURNING
        /*
         var pk = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER,
                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2020, 900.25, 1611L, 1, 0.0)
                .returningResult(SALE.SALE_ID)
                .execute();
        */

        long cr = ctx.fetchValue(SALE_SEQ.currval());

        // UPDATE the SALE having as ID the fetched *cr* 
        // (it is possible that this is not the current value anymore)
        ctx.update(SALE)
                .set(SALE.FISCAL_YEAR, 2005)
                .where(SALE.SALE_ID.eq(cr)) 
                .execute();

        // DELETE the SALE having as ID the fetched *cr* 
        // (it is possible that this is not the current value anymore)        
        ctx.deleteFrom(SALE)
                .where(SALE.SALE_ID.eq(cr))
                .execute();

        // this is prone to the same issue because it results in a SELECT and a DELETE and,
        // between them a concurrent transaction can affect the current value
        ctx.deleteFrom(SALE)
                .where(SALE.SALE_ID.eq(ctx.fetchValue(SALE_SEQ.currval())))
                .execute();

        // this is not prone to the same issue because there will be a single UPDATE/DELETE, so
        // this time, you'll affect the latest current value, whichever that value is
        ctx.update(SALE)
                .set(SALE.FISCAL_YEAR, 2005)
                .where(SALE.SALE_ID.eq(SALE_SEQ.currval()))
                .execute();

        ctx.deleteFrom(SALE)
                .where(SALE.SALE_ID.eq(SALE_SEQ.currval()))
                .execute();
    }

    @Transactional
    public void currentSequenceVal2() {

        // Avoid: ERROR: CURRVAL of sequence "sale_seq" is not yet defined in this session
        // SALE_SEQ.nextval(); - you can call this, but an INSERT will also call NEXTVAL
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, 
                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2030, 900.25, 1611L, 1, 0.0)
                .execute();

        // This updates the record having the current value, which can be
        // other than the primary key of the previous INSERT (e.g., concurrent transaction performed an INSERT)
        ctx.update(SALE)
                .set(SALE.FISCAL_YEAR, 2040)
                .where(SALE.SALE_ID.eq(SALE_SEQ.currval()))
                .execute();
    }

    @Transactional
    public void nextSequenceVal() {

        // For a SEQUENCE that is auto-generated from a (BIG)SERIAL or is set as 
        // default (e.g., NOT NULL DEFAULT NEXTVAL ('sale_seq')) there is no need 
        // to call currval() or nextval(). Simply omit the PK and let the database 
        // to generate it.
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_,
                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                .values(2005, 1370L, 1282.641, 1, 0.0)
                .execute();

        // But, for SEQUENCE owned by non-auto-generated rows, you have to rely on nextval()/nextvals()
        // For instance, you can INSERT 10 employees via *EMPLOYEE_SEQ.nextval()* 
        for (int i = 0; i < 10; i++) {
            ctx.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME,
                    EMPLOYEE.EXTENSION, EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY,
                    EMPLOYEE.REPORTS_TO, EMPLOYEE.JOB_TITLE)
                    .values(EMPLOYEE_SEQ.nextval(),
                            val("Lionel"), val("Andre"), val("x8990"), val("landre@gmail.com"), val("1"),
                            val(57000), val(1143L), val("Sales Rep"))
                    .execute();
        }

        // Or, by fetching and caching 10 IDs
        List<Long> ids = ctx.fetchValues(EMPLOYEE_SEQ.nextvals(10));

        // This is also useful for Records to pre-set IDs:
        /*
         EmployeeRecord er = new EmployeeRecord(ids.get(0),
                "Lionel", "Andre", "x8990", "landre@gmail.com", "1", 
                        57000, 0, 1143L, "Sales Rep", null, null);        
        */
        
        for (int i = 0; i < ids.size(); i++) {

            ctx.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME,
                    EMPLOYEE.EXTENSION, EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY,
                    EMPLOYEE.REPORTS_TO, EMPLOYEE.JOB_TITLE)
                    .values(ids.get(i),
                            "Lionel", "Andre", "x8990", "landre@gmail.com", "1",
                            57000, 1143L, "Sales Rep")
                    .execute();
        }
    }
}