package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleProduct;
import com.classicmodels.pojo.SimpleProductLine;
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

    public void fetchOneToMany() {

        // approach 1 (use the proper aliases)
        var map = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetchGroups(new Field[]{PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION},
                new Field[]{PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK});

        List<SimpleProductLine> result1 = map.entrySet()
                .stream()
                .map((e) -> {
                    SimpleProductLine productLine = e.getKey().into(SimpleProductLine.class);
                    productLine.setProducts(e.getValue().into(SimpleProduct.class));

                    return productLine;
                }).collect(Collectors.toList());

        System.out.println("Example 1\n" + result1);

        // approach 2 (map from ResultSet)
        ResultSet rs = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetchResultSet();

        List<SimpleProductLine> result2 = Collections.emptyList();
        Map<String, SimpleProductLine> temp = new HashMap<>();

        try {
            while (rs.next()) {
                String productLineName = rs.getString("product_line");

                if (productLineName != null) {
                    temp.putIfAbsent(productLineName, 
                            new SimpleProductLine(
                                    rs.getString("product_line"), 
                                    rs.getString("text_description")));

                    SimpleProduct product = new SimpleProduct(
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
        System.out.println("Example 2\n" + result2);
    }
}