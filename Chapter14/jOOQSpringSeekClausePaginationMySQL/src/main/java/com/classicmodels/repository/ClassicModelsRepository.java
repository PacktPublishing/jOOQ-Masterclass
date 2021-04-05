package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.pojos.Orderdetail;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import jooq.generated.tables.pojos.Product;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.pojos.Employee;
import org.jooq.DSLContext;
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
                .orderBy(PRODUCT.PRODUCT_ID, PRODUCT.BUY_PRICE)
                .seek(val(productId), PRODUCT.MSRP.minus(PRODUCT.MSRP.mul(0.35))) // or, seekAfter
                .limit(size)
                .fetchInto(Product.class);

        return result;
    }
}
