package com.classicmodels.repository;

import com.classicmodels.service.Clazz;
import java.math.BigDecimal;
import java.util.List;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.BankTransactionRecord;
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.ProductRecord;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.InsertQuery;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.jooq.Record;
import org.jooq.UpdateQuery;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<ProductRecord> fetchCarsOrNoCars(float buyPrice, boolean cars) {

        return ctx.selectFrom(PRODUCT)
                .where((buyPrice > 0f ? PRODUCT.BUY_PRICE.gt(BigDecimal.valueOf(buyPrice)) : noCondition())
                        .and(cars ? PRODUCT.PRODUCT_LINE.in("Classic Cars", "Motorcycles", "Trucks and Buses", "Vintage Cars")
                                : PRODUCT.PRODUCT_LINE.in("Plains", "Ships", "Trains")))
                .fetch();
    }

    public List<EmployeeRecord> fetchEmployees1(boolean isSaleRep) {

        return ctx.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.SALARY.compare(isSaleRep ? Comparator.IN : Comparator.NOT_IN,
                        select(EMPLOYEE.SALARY).from(EMPLOYEE).where(EMPLOYEE.SALARY.lt(65000))))
                .orderBy(EMPLOYEE.SALARY)
                .fetch();
    }

    public List<BankTransactionRecord> fetchBankTransactions(String status) {

        return ctx.selectFrom(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.TRANSFER_AMOUNT.compare(
                        status.equals("SUCCESS") || status.equals("FAILED")
                        ? Comparator.GREATER_OR_EQUAL : Comparator.LESS_OR_EQUAL, BigDecimal.valueOf(10000)))
                .fetch();
    }

    public List<EmployeeRecord> fetchEmployees2(boolean salesReps) {

        return ctx.selectFrom(EMPLOYEE)
                .where(
                        EMPLOYEE.SALARY.compare(
                                salesReps ? Comparator.LESS : Comparator.GREATER,
                                select(avg(EMPLOYEE.SALARY).coerce(Integer.class)).from(EMPLOYEE).where(
                                        EMPLOYEE.JOB_TITLE.compare(
                                                salesReps ? Comparator.IN : Comparator.NOT_IN,
                                                select(EMPLOYEE.JOB_TITLE).from(EMPLOYEE)
                                                        .where(EMPLOYEE.JOB_TITLE.eq("Sales Rep"))
                                        )
                                )
                        )
                )
                .orderBy(EMPLOYEE.SALARY)
                .fetch();
    }

    public List<ProductRecord> fetchProducts(float buyPrice, float msrp) {

        return ctx.selectFrom(PRODUCT)
                .where(PRODUCT.BUY_PRICE.compare(
                        buyPrice < 55f ? Comparator.LESS : Comparator.GREATER,
                        select(avg(PRODUCT.MSRP.minus(PRODUCT.MSRP.mul(buyPrice / 100f))))
                                .from(PRODUCT).where(PRODUCT.MSRP.coerce(Float.class).compare(
                                msrp > 100f ? Comparator.LESS : Comparator.GREATER, msrp))))
                .fetch();
    }

    public List<ProductRecord> findProductsWithConditions1(
            BigDecimal startBuyPrice, BigDecimal endBuyPrice, String productVendor, String productScale) {

        // create the SelectQuery having the fix parts like the table PRODUCT and a condition
        SelectQuery select = ctx.selectFrom(PRODUCT)
                .where(PRODUCT.QUANTITY_IN_STOCK.gt(0))
                .getQuery();

        // add the dynamic parts
        if (startBuyPrice != null && endBuyPrice != null) {
            select.addConditions(PRODUCT.BUY_PRICE.betweenSymmetric(startBuyPrice, endBuyPrice));
        }

        if (productVendor != null) {
            select.addConditions(PRODUCT.PRODUCT_VENDOR.eq(productVendor));
        }

        if (productScale != null) {
            select.addConditions(PRODUCT.PRODUCT_SCALE.eq(productScale));
        }

        return select.fetch();
    }

    public List<ProductRecord> findProductsWithConditions2(BigDecimal startBuyPrice, BigDecimal endBuyPrice,
            String productVendor, String productScale) {

        // create the fix condition
        Condition condition = PRODUCT.QUANTITY_IN_STOCK.gt(0);
        // Condition condition = trueCondition(); // or, start from a TRUE condition (there is falseCondition() as well)       

        // add the dynamic conditions
        if (startBuyPrice != null && endBuyPrice != null) {
            condition = condition.and(PRODUCT.BUY_PRICE.betweenSymmetric(startBuyPrice, endBuyPrice));
        }

        if (productVendor != null) {
            condition = condition.and(PRODUCT.PRODUCT_VENDOR.eq(productVendor));
        }

        if (productScale != null) {
            condition = condition.and(PRODUCT.PRODUCT_SCALE.eq(productScale));
        }

        // create the *select*
        SelectQuery select = ctx.selectFrom(PRODUCT)
                .where(condition)
                .getQuery();

        return select.fetch();
    }

    public List<Record> appendTwoJoins(boolean andEmp, boolean addSale) {

        SelectQuery select = ctx.select(OFFICE.CITY, OFFICE.COUNTRY).from(OFFICE).limit(10).getQuery();

        if (andEmp) {
            select.addSelect(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME);
            select.addJoin(EMPLOYEE, OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE));

            if (addSale) {
                select.addSelect(SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER);
                select.addJoin(SALE, JoinType.LEFT_OUTER_JOIN, EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER));
            }
        }

        return select.fetch();
    }

    public List<Record> decomposeSelectAndFrom() {

        SelectQuery select = ctx.select().limit(100).getQuery();

        select.addFrom(OFFICE);
        select.addSelect(OFFICE.CITY, OFFICE.COUNTRY);

        select.addFrom(EMPLOYEE);
        select.addSelect(EMPLOYEE.JOB_TITLE);

        select.addFrom(CUSTOMER);
        select.addSelect(CUSTOMER.asterisk().except(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME));

        select.addFrom(PAYMENT);
        select.addSelect(PAYMENT.fields());

        return select.fetch();
    }

    public List<CustomerRecord> classifyCustomerPayments(Clazz... clazzes) {

        SelectQuery[] sq = new SelectQuery[clazzes.length];

        for (int i = 0; i < sq.length; i++) {
            sq[i] = getQuery(); // create a query for each UNION
        }

        sq[0].addSelect(count().as("clazz_[" + clazzes[0].left() + ", " + clazzes[0].right() + "]"));
        sq[0].addHaving(count().between(clazzes[0].left(), clazzes[0].right()));
        for (int i = 1; i < sq.length; i++) {
            sq[0].addSelect(val(0).as("clazz_[" + clazzes[i].left() + ", " + clazzes[i].right() + "]"));
        }

        for (int i = 1; i < sq.length; i++) {
            for (int j = 0; j < i; j++) {
                sq[i].addSelect(val(0));
            }

            sq[i].addSelect(count());

            for (int j = i + 1; j < sq.length; j++) {
                sq[i].addSelect(val(0));
            }

            sq[i].addHaving(count().between(clazzes[i].left(), clazzes[i].right()));
            sq[0].union(sq[i]);
        }
        
        return sq[0].fetch();
    }

    private SelectQuery getQuery() {

        return ctx.select(CUSTOMER.CUSTOMER_NUMBER)
                .from(CUSTOMER)
                .join(PAYMENT)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                .groupBy(CUSTOMER.CUSTOMER_NUMBER)
                .getQuery();
    }

    @Transactional
    public long insertClassicCar(
            String productName, String productVendor, String productScale, boolean price) {

        InsertQuery iq = ctx.insertQuery(PRODUCT);
        
        iq.addValue(PRODUCT.PRODUCT_LINE, "Classic Cars");
        iq.addValue(PRODUCT.CODE, 599302);

        if (productName != null) {
            iq.addValue(PRODUCT.PRODUCT_NAME, productName);
        }

        if (productVendor != null) {
            iq.addValue(PRODUCT.PRODUCT_VENDOR, productVendor);
        }

        if (productScale != null) {
            iq.addValue(PRODUCT.PRODUCT_SCALE, productScale);
        }

        if (price) {
            iq.addValue(PRODUCT.BUY_PRICE, select(avg(PRODUCT.BUY_PRICE)).from(PRODUCT));
        }
        
        iq.setReturning(PRODUCT.getIdentity());

        iq.execute();

        return iq.getReturnedRecord()
                .getValue(PRODUCT.getIdentity().getField(), Long.class);
    }

    @Transactional
    public int updateProduct(float oldPrice, float value) {

        UpdateQuery uq = ctx.updateQuery(PRODUCT);

        uq.addValue(PRODUCT.BUY_PRICE, PRODUCT.BUY_PRICE.plus(PRODUCT.BUY_PRICE.mul(value)));
        uq.addConditions(PRODUCT.BUY_PRICE.lt(BigDecimal.valueOf(oldPrice)));

        return uq.execute();
    }

    @Transactional
    public int deleteSale(int fiscalYear, double sale) {

        DeleteQuery dq = ctx.deleteQuery(SALE);

        Condition condition = SALE.FISCAL_YEAR.compare(fiscalYear <= 2003
                ? Comparator.GREATER : Comparator.LESS, fiscalYear);

        if (sale > 5000d) {
            condition = condition.or(SALE.SALE_.gt(sale));
        }

        dq.addConditions(condition);

        return dq.execute();
    }
}
