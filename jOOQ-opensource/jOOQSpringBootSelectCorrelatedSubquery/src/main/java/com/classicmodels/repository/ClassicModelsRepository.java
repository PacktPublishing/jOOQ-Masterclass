package com.classicmodels.repository;

import com.classicmodels.pojo.CustomerPojo;
import com.classicmodels.pojo.EmployeePojo;
import com.classicmodels.pojo.ProductPojo;
import java.math.BigDecimal;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.Product;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext create;

    public ClassicModelsRepository(DSLContext create) {
        this.create = create;
    }

    /* correlated subquery - example 1 */
    public List<ProductPojo> findProductsMaxBuyPriceByProductionLine() {

        Product p1 = PRODUCT.as("p1");
        Product p2 = PRODUCT.as("p2");

        Select<Record1<BigDecimal>> maxBuyPrice = select(max(p2.BUY_PRICE))
                .from(p2)
                .where(p2.PRODUCT_LINE.eq(p1.PRODUCT_LINE))
                .groupBy(p2.PRODUCT_LINE);

        List<ProductPojo> result = create.select(p1.PRODUCT_ID, p1.PRODUCT_NAME,
                p1.PRODUCT_LINE, p1.PRODUCT_VENDOR, p1.BUY_PRICE)
                .from(p1)
                .where(p1.BUY_PRICE.in(maxBuyPrice))
                .orderBy(p1.PRODUCT_LINE, p1.BUY_PRICE)
                .fetchInto(ProductPojo.class);

        return result;
    }

    /* correlated subquery - example 2 */
    public List<EmployeePojo> findEmployeesBySumSales() {

        Field<?> sumSales = select(sum(SALE.SALE_))
                .from(SALE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                .asField("sumSales");
        
        // or, using DSL.Field(Select)
        /*
        Field<?> sumSales = field(select(sum(SALE.SALE_))
                .from(SALE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)))
                .as("sumSales");
        */

        List<EmployeePojo> result = create.select(EMPLOYEE.EMPLOYEE_NUMBER,
                EMPLOYEE.FIRST_NAME, EMPLOYEE.JOB_TITLE, sumSales)
                .from(EMPLOYEE)
                .orderBy(sumSales.asc())
                .fetchInto(EmployeePojo.class);

        return result;
    }

    public List<CustomerPojo> findCustomerFullNameCityCountry() {
        
        Field<?> fullName = select(concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME))
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))                               
                .asField("fullName");                        

        // or, using DSL.Field(Select)
        /*
        Field<?> fullName = field(select(concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME))
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))).as("fullName");
        */
        
        List<CustomerPojo> result = create.select(
                CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY, fullName)
                .from(CUSTOMERDETAIL)
                .fetchInto(CustomerPojo.class);
        
        return result;
    }

}