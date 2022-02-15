package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleManager;
import com.classicmodels.pojo.SimpleOffice;
import com.classicmodels.pojo.SimpleProduct;
import com.classicmodels.pojo.SimpleProductLine;
import com.classicmodels.pojo.java16records.RecordManager;
import com.classicmodels.pojo.java16records.RecordOffice;
import com.classicmodels.pojo.java16records.RecordProduct;
import com.classicmodels.pojo.java16records.RecordProductLine;
import java.util.List;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void oneToMany() {

        // POJO
        List<SimpleProductLine> resultPojo = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                multiset(
                        select(
                                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                ).as("products").convertFrom(r -> r.map(mapping(SimpleProduct::new))))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch(mapping(SimpleProductLine::new));

        System.out.println("One-to-many (POJO):\n" + resultPojo);

        // Java 16 Record
        List<RecordProductLine> resultRecord = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                multiset(
                        select( // or selectDistinct() if you are in a case with duplicates
                                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                                .from(PRODUCT)
                                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                ).as("products").convertFrom(r -> r.map(mapping(RecordProduct::new))))
                .from(PRODUCTLINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch(mapping(RecordProductLine::new));

        System.out.println("One-to-many (Record):\n" + resultRecord);
    }

    public void manyToMany() {

        // POJO
        List<SimpleManager> resultPojo = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                multiset(
                        select(
                                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE)
                                .from(OFFICE)
                                .join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                                .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                ).as("offices").convertFrom(r -> r.map(mapping(SimpleOffice::new))))
                .from(MANAGER)
                .orderBy(MANAGER.MANAGER_ID)
                .fetch(mapping(SimpleManager::new));

        System.out.println("Many-to-many (POJO):\n" + resultPojo);

        // Record
        List<RecordManager> resultRecord = ctx.select(
                MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                multiset(
                        select(
                                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.STATE)
                                .from(OFFICE)
                                .join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                                .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID))
                ).as("offices").convertFrom(r -> r.map(mapping(RecordOffice::new))))
                .from(MANAGER)
                .orderBy(MANAGER.MANAGER_ID)
                .fetch(mapping(RecordManager::new));

        System.out.println("Many-to-many (Record):\n" + resultRecord);
    }

}
