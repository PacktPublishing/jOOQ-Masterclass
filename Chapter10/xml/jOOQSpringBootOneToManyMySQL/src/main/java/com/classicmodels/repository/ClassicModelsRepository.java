package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleBProduct;
import com.classicmodels.pojo.SimpleBProductLine;
import com.classicmodels.pojo.SimpleUProduct;
import com.classicmodels.pojo.SimpleUProductLine;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void fetchOneToManyUnidirectional() {

        // approach 1
        var map = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(3)
                .fetchGroups(new Field[]{PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION},
                new Field[]{PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK});

        List<SimpleUProductLine> result1 = map.entrySet()
                .stream()
                .map((e) -> {
                    SimpleUProductLine productLine = e.getKey().into(SimpleUProductLine.class);
                    productLine.setProducts(e.getValue().into(SimpleUProduct.class));

                    return productLine;
                }).collect(Collectors.toList());
        System.out.println("\nExample 1.1:");
        result1.forEach(e -> {System.out.println(e + " => " + e.getProducts()); });       

        // approach 2 (map from ResultSet)
        ResultSet rs = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))                
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(3)
                .fetchResultSet();

        List<SimpleUProductLine> result2 = Collections.emptyList();
        Map<String, SimpleUProductLine> temp = new HashMap<>();

        try {
            while (rs.next()) {
                String productLineName = rs.getString("product_line");

                if (productLineName != null) {
                    temp.putIfAbsent(productLineName, 
                            new SimpleUProductLine(
                                    rs.getString("product_line"), 
                                    rs.getString("text_description")));

                    SimpleUProduct product = new SimpleUProduct(
                            rs.getString("product_name"),
                            rs.getString("product_vendor"), 
                            rs.getShort("quantity_in_stock"));
                    
                    temp.get(productLineName).getProducts().add(product);
                }                                
            }
            
            result2 = new ArrayList<>(temp.values());
            
        } catch (SQLException ex) {
            // handle exception
        }
        System.out.println("\nExample 1.2:");
        result2.forEach(e -> {System.out.println(e + " => " + e.getProducts()); });       
    }
    
    public void fetchOneToManyBidirectional() {

        // approach 1
        var map = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))                
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(3)
                .fetchGroups(new Field[]{PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION},
                new Field[]{PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK});

        List<SimpleBProductLine> result1 = map.entrySet()
                .stream()
                .map((e) -> {
                    SimpleBProductLine productLine = e.getKey().into(SimpleBProductLine.class);
                    List<SimpleBProduct> products = e.getValue().into(SimpleBProduct.class);
                    
                    productLine.setProducts(products);
                    products.forEach(p -> ((SimpleBProduct) p).setProductLine(productLine));
                    
                    return productLine;
                }).collect(Collectors.toList());
        System.out.println("\nExample 2.1:");
        result1.forEach(e -> {System.out.println(e + " => " + e.getProducts()); });
        result1.forEach(e -> {for(SimpleBProduct p : e.getProducts()) 
        {System.out.println(p + " => " + p.getProductLine());} });
        
        // approach 2 (map from ResultSet)
        ResultSet rs = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(3)
                .fetchResultSet();

        List<SimpleBProductLine> result2 = Collections.emptyList();
        Map<String, SimpleBProductLine> temp = new HashMap<>();

        try {
            while (rs.next()) {
                String productLineName = rs.getString("product_line");

                if (productLineName != null) {
                    temp.putIfAbsent(productLineName, 
                            new SimpleBProductLine(
                                    rs.getString("product_line"), 
                                    rs.getString("text_description")));

                    SimpleBProduct product = new SimpleBProduct(
                            rs.getString("product_name"),
                            rs.getString("product_vendor"), 
                            rs.getShort("quantity_in_stock"));
                    
                    temp.get(productLineName).getProducts().add(product);
                    product.setProductLine(temp.get(productLineName));
                }                                
            }
            
            result2 = new ArrayList<>(temp.values());
            
        } catch (SQLException ex) {
            // handle exception
        }
        System.out.println("\nExample 2.2:");
        result2.forEach(e -> {System.out.println(e + " => " + e.getProducts()); });
        result2.forEach(e -> {for(SimpleBProduct p : e.getProducts()) 
        {System.out.println(p + " => " + p.getProductLine());} });
    }
}