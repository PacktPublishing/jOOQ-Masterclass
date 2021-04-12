package com.classicmodels.repository;

import java.util.List;
import jooq.generated.embeddables.records.EmbeddedProductlinePkRecord;
import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.pojos.Orderdetail;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import jooq.generated.tables.pojos.Product;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.pojos.Productline;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import jooq.generated.tables.pojos.Employee;
import org.jooq.DSLContext;
import org.jooq.JSONFormat;
import static org.jooq.impl.DSL.sum;
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

    public List<Product> fetchProductsPageAsc(long productId, int size) {

        List<Product> result = ctx.selectFrom(PRODUCT)
                .orderBy(PRODUCT.PRODUCT_ID)
                .seek(productId) // or, seekAfter
                .limit(size)
                .fetchInto(Product.class);

        return result;
    }

    public List<Product> fetchProductsPageDesc(long productId, int size) {

        List<Product> result = ctx.selectFrom(PRODUCT)
                .orderBy(PRODUCT.PRODUCT_ID.desc())
                .seek(productId) // or, seekAfter
                .limit(size)
                .fetchInto(Product.class);

        return result;
    }

    public List<Employee> fetchEmployeesPageOfficeCodeAscSalaryDesc(
            String officeCode, Integer salary, int size) {

        List<Employee> result = ctx.selectFrom(EMPLOYEE)
                .orderBy(EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY.desc())
                .seek(officeCode, salary) // or, seekAfter
                .limit(size)
                .fetchInto(Employee.class);

        return result;
    }

    public List<Employee> fetchEmployeesPageOfficeCodeAscSalaryAsc(
            String officeCode, Integer salary, int size) {

        List<Employee> result = ctx.selectFrom(EMPLOYEE)
                .orderBy(EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY)
                .seek(officeCode, salary) // or, seekAfter
                .limit(size)
                .fetchInto(Employee.class);

        return result;
    }

    public List<Orderdetail> fetchOrderdetailPageOrderIdAscProductIdQuantityOrderedDesc(
            long orderId, long productId, int quantityOrdered, int size) {

        List<Orderdetail> result = ctx.selectFrom(ORDERDETAIL)
                .orderBy(ORDERDETAIL.ORDER_ID, ORDERDETAIL.PRODUCT_ID.desc(),
                        ORDERDETAIL.QUANTITY_ORDERED.desc())
                .seek(orderId, productId, quantityOrdered) // or, seekAfter
                .limit(size)
                .fetchInto(Orderdetail.class);

        return result;
    }

    public List<Product> fetchProductsBuyPriceGtMsrp(long productId, int size) {

        List<Product> result = ctx.selectFrom(PRODUCT)
                .orderBy(PRODUCT.BUY_PRICE, PRODUCT.PRODUCT_ID)
                .seek(PRODUCT.MSRP.minus(PRODUCT.MSRP.mul(0.35)), val(productId)) // or, seekAfter
                .limit(size)
                .fetchInto(Product.class);

        return result;
    }

    public List<Productline> fetchProductlineEmbeddedKey(EmbeddedProductlinePkRecord epk, int size) {

        List<Productline> result = ctx.select(PRODUCTLINE.asterisk()
                .except(PRODUCTLINE.HTML_DESCRIPTION, PRODUCTLINE.IMAGE))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCTLINE_PK) // embedded key
                .seek(epk) // or, seekAfter
                .limit(size)
                .fetchInto(Productline.class);

        return result;
    }

    public String fetchOrderdetailPageGroupBy(long orderId, int size) {

        var result = ctx.select(ORDERDETAIL.ORDER_ID, sum(ORDERDETAIL.PRICE_EACH))
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.ORDER_ID)
                .orderBy(ORDERDETAIL.ORDER_ID)
                .seek(orderId) // or, seekAfter
                .limit(size)
                .fetch()
                .formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);

        return result;
    }
}
