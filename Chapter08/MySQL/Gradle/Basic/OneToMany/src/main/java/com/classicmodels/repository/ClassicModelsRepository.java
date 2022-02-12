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
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
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

        // approach 1, using collect()
        var result1
                = ctx.select(
                        PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                        PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCTLINE)
                        .join(PRODUCT) // with collect() you can use LEFT JOIN as well
                        .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                        .orderBy(PRODUCTLINE.PRODUCT_LINE)
                        .limit(3)
                        .fetch()
                        .collect(Collectors.groupingBy(
                                rs -> new SimpleUProductLine(rs.getValue(PRODUCTLINE.PRODUCT_LINE),
                                        rs.getValue(PRODUCTLINE.TEXT_DESCRIPTION)),
                                mapping(rs -> new SimpleUProduct(rs.getValue(PRODUCT.PRODUCT_NAME),
                                rs.getValue(PRODUCT.PRODUCT_VENDOR),
                                rs.getValue(PRODUCT.QUANTITY_IN_STOCK)),
                                        toList())))
                        .entrySet()
                        .stream()
                        .map((e) -> {
                            SimpleUProductLine productLine = e.getKey();
                            productLine.setProducts(e.getValue());

                            return productLine;
                        }).collect(Collectors.toList());

        System.out.println("\nExample 1.1:");
        result1.forEach(e -> {
            System.out.println(e + " => " + e.getProducts());
        });

        // approach 2, using fetchGroups()
        var map = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT) // for using LEFT JOIN please check this https://github.com/jOOQ/jOOQ/issues/11888
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(3)
                .fetchGroups(new Field[]{PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION},
                new Field[]{PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK});

        List<SimpleUProductLine> result2 = map.entrySet()
                .stream()
                .map((e) -> {
                    SimpleUProductLine productLine = e.getKey().into(SimpleUProductLine.class);
                    productLine.setProducts(e.getValue().into(SimpleUProduct.class));

                    return productLine;
                }).collect(Collectors.toList());

        System.out.println("\nExample 1.2:");
        result2.forEach(e -> {
            System.out.println(e + " => " + e.getProducts());
        });

        // approach 3 (map from ResultSet)
        ResultSet rs = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .limit(3)
                .fetchResultSet();

        List<SimpleUProductLine> result3 = Collections.emptyList();
        Map<String, SimpleUProductLine> temp = new HashMap<>();

        try (rs) {
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
                            rs.getInt("quantity_in_stock"));

                    temp.get(productLineName).getProducts().add(product);
                }
            }

            result3 = new ArrayList<>(temp.values());

        } catch (SQLException ex) {
            // handle exception
        }

        System.out.println("\nExample 1.3:");
        result3.forEach(e -> {
            System.out.println(e + " => " + e.getProducts());
        });
    }

    public void fetchOneToManyBidirectional() {
        
        // OF COURSE, YOU CAN USE collect() AS WELL JUST AS YOU PREVIOUSLY SAW

        // approach 1
        var map = ctx.select( // Map<Record, Result<Record>>
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
        result1.forEach(e -> {
            System.out.println(e + " => " + e.getProducts());
        });
        result1.forEach(e -> {
            for (SimpleBProduct p : e.getProducts()) {
                System.out.println(p + " => " + p.getProductLine());
            }
        });

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

        try (rs) {
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
                            rs.getInt("quantity_in_stock"));

                    temp.get(productLineName).getProducts().add(product);
                    product.setProductLine(temp.get(productLineName));
                }
            }

            result2 = new ArrayList<>(temp.values());

        } catch (SQLException ex) {
            // handle exception
        }

        System.out.println("\nExample 2.2:");
        result2.forEach(e -> {
            System.out.println(e + " => " + e.getProducts());
        });
        result2.forEach(e -> {
            for (SimpleBProduct p : e.getProducts()) {
                System.out.println(p + " => " + p.getProductLine());
            }
        });
    }
}
