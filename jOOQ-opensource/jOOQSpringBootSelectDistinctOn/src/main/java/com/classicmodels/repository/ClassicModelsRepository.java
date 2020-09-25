package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.pojos.Product;
import jooq.generated.tables.pojos.Sale;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext create;

    public ClassicModelsRepository(DSLContext create) {
        this.create = create;
    }

    /* PostgreSQL DISTINCT ON */
    
    /* The following statement sorts the result set by the product's vendor and scale, 
       and then for each group of duplicates, it keeps the first row in the returned result set */
    public List<Product> findProductsByVendorScale() {

        List<Product> result = create.selectDistinct()
                .on(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                .from(PRODUCT)
                .orderBy(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                .fetchInto(Product.class);
        
        /* or, like this */
        /*
        List<Product> result = create.select()
                .distinctOn(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                .from(PRODUCT)
                .orderBy(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                .fetchInto(Product.class);
        */
        
        return result;
    }

    /* What is the employee numbers of the max sales per fiscal years */
    public List<Sale> findEmployeeNumberOfMaxSalePerFiscalYear() {
                
        List<Sale> result = create.select(
                SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR)
                .distinctOn(SALE.FISCAL_YEAR)
                .from(SALE)
                .orderBy(SALE.FISCAL_YEAR, SALE.SALE_.desc())                
                .fetchInto(Sale.class);         
        
        /* SQL alternative based on JOIN */
        /*
        SELECT
          sale.fiscal_year,
          sale.employee_number
        FROM sale
        INNER JOIN (SELECT
            fiscal_year,
            MAX(sale) AS max_sale
        FROM sale
        GROUP BY fiscal_year) s
          ON sale.fiscal_year = s.fiscal_year
          AND sale.sale = s.max_sale
        ORDER BY (fiscal_year)
        */        
        
        /*
        List<Sale> result = create.select(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER)
                .from(SALE)
                .innerJoin(select(SALE.FISCAL_YEAR.as("fy"), max(SALE.SALE_).as("ms"))
                        .from(SALE)
                        .groupBy(SALE.FISCAL_YEAR))
                .on(SALE.FISCAL_YEAR.eq(field(name("fy"), Integer.class))
                        .and(SALE.SALE_.eq(field(name("ms"), Double.class))))
                .orderBy(SALE.FISCAL_YEAR)
                .fetchInto(Sale.class);
        */
        
        return result;
    }

}
