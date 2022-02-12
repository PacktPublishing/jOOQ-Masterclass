package com.classicmodels.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.JSONFormat;
import org.jooq.Record1;
import org.jooq.Result;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.key;
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

    public String jsonProductlineProductOrderdetail() {

        Result<Record1<JSON>> result = ctx.select(
                jsonObject("root", jsonArrayAgg(
                        jsonObject(
                                key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                                key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                                key("products").value(select(jsonArrayAgg(
                                        jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                                key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                                key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK),
                                                key("orderdetail")
                                                        .value(select(jsonArrayAgg(
                                                                jsonObject(
                                                                        key("quantityOrdered").value(ORDERDETAIL.QUANTITY_ORDERED),
                                                                        key("priceEach").value(ORDERDETAIL.PRICE_EACH)))
                                                                .orderBy(ORDERDETAIL.QUANTITY_ORDERED))
                                                                .from(ORDERDETAIL)
                                                                .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID)))))
                                        .orderBy(PRODUCT.QUANTITY_IN_STOCK))
                                        .from(PRODUCT)
                                        .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                                        .orderBy(PRODUCTLINE.PRODUCT_LINE))))))
                .from(PRODUCTLINE)                
                .fetch(); // feel free to practice fetchOne(), fetchAny(), fetchSingle() and so on

        System.out.println("\nExample (format the result set as JSON) 1.1:\n"
                + result.formatJSON());
        System.out.println("\nExample (format the result set as JSON) 1.2:\n"
                + result.formatJSON(JSONFormat.DEFAULT_FOR_RECORDS));

        // There will be a single entry in the result containing the whole JSON under the "root" element.
        // But, if you remove the "root" element then the result will contains one entry per product line.
        System.out.println("\nExample (iterate the result set) 1.3:\n");
        result.forEach(System.out::println);

        System.out.println("\nExample (extract data from result set) 1.4:\n"
                + result.get(0).component1().data()); // the whole JSON data (most probably, this you'll like to return from a REST controller
        
        System.out.println("\nExample (extract data from result set) 1.5:\n"
                + result.get(0).value1().data()); // the whole JSON data (most probably, this you'll like to return from a REST controller
                
        // create a Jackson JSON
        ObjectMapper om = new ObjectMapper();
        try {
            JsonNode jsonNode = om.readTree(result.get(0).component1().data());            
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return result.get(0).value1().data(); // check the following example to express this return fluently
    }
    
    public String jsonProductlineProductOrderdetailFluentReturn() {

        return ctx.select(
                jsonObject("root", jsonArrayAgg(
                        jsonObject(
                                key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                                key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                                key("products").value(select(jsonArrayAgg(
                                        jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                                key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                                key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK),
                                                key("orderdetail")
                                                        .value(select(jsonArrayAgg(
                                                                jsonObject(
                                                                        key("quantityOrdered").value(ORDERDETAIL.QUANTITY_ORDERED),
                                                                        key("priceEach").value(ORDERDETAIL.PRICE_EACH)))
                                                                .orderBy(ORDERDETAIL.QUANTITY_ORDERED))
                                                                .from(ORDERDETAIL)
                                                                .where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID)))))
                                        .orderBy(PRODUCT.QUANTITY_IN_STOCK))
                                        .from(PRODUCT)
                                        .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                                        .orderBy(PRODUCTLINE.PRODUCT_LINE))))))
                .from(PRODUCTLINE)                
                .fetchSingle() // there is a single JSON
                .value1()      // this is org.jooq.JSON
                .data();       // this is JSON data as String
        
                // or, replace the last three lines from above with: .fetchSingleInto(String.class)
                // or, replace the last three lines with: .fetch().formatJSON(JSONFormat.DEFAULT_FOR_RECORDS)
               
    }
}
