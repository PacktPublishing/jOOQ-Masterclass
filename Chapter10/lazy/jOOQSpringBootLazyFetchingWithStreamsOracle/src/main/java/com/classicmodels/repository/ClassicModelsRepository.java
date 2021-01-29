package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleSale;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.pojos.Product;
import jooq.generated.tables.pojos.Productline;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void eagerAndLazyFetchingWithStreams() {

        // eager-fetching with streams
        ctx.selectFrom(SALE)
                .fetch() // jOOQ fetches the whole result set into memory and close the database connection
                .stream() // stream over the in-memory result set (no database connection is active)
                .filter(rs -> rs.getValue(SALE.SALE_) > 5000)
                .forEach(System.out::println);

        // eager-fetching with streams (don't forget, this is a resourceful stream!)
        try ( Stream<SaleRecord> stream = ctx.selectFrom(SALE).fetch().stream()) {
            stream.filter(rs -> rs.getValue(SALE.SALE_) > 5000)
                    .forEach(System.out::println);
        }

        // lazy streaming (pay attention to not accidentally forget the fetch() method)
        ctx.selectFrom(SALE)
                .stream() // stream over the result set (the database connection remains open)
                .filter(rs -> rs.getValue(SALE.SALE_) > 5000)
                .forEach(System.out::println);

        // lazy streaming (don't forget, this is a resourceful stream!)
        try ( Stream<SaleRecord> stream = ctx.selectFrom(SALE).stream()) {
            stream.filter(rs -> rs.getValue(SALE.SALE_) > 5000)
                    .forEach(System.out::println);
        }
    }

    @Transactional(readOnly = true) // open the database connection
    public void eagerAndLazyFetchingWithStreamsAndTransactional() {

        // eager-fetching with streams
        ctx.selectFrom(SALE)
                .fetch() // jOOQ fetches the whole result set into memory via the database connection opened by @Transactional
                .stream() // stream over the in-memory result set (database connection is active)
                .filter(rs -> rs.getValue(SALE.SALE_) > 5000)
                .forEach(System.out::println);

        // lazy-fetching with streams
        ctx.selectFrom(SALE)
                .stream() // stream over the result set (the database connection remains open)
                .filter(rs -> rs.getValue(SALE.SALE_) > 5000)
                .forEach(System.out::println);
    }

    // lazy fetching with streams
    @Transactional(readOnly = true)
    public void lazyFetchingViaFetchStream() {

        ctx.fetchStream("SELECT SALE FROM SALE")
                .filter(rs -> rs.getValue("SALE", Double.class) > 5000)
                .forEach(System.out::println);

        ctx.selectFrom(SALE).fetchStream()
                .filter(rs -> rs.getValue(SALE.SALE_) > 5000)
                .forEach(System.out::println);

        SimpleSale result1 = ctx.fetchStream("SELECT SALE FROM SALE") // jOOQ fluent API ends here                                                
                .collect(Collectors.teeing( // Stream API starts here                           
                        summingDouble(rs -> rs.getValue("SALE", Double.class)),
                        mapping(rs -> rs.getValue("SALE", Double.class), toList()),
                        SimpleSale::new));
        System.out.println("Result=" + result1);

        SimpleSale result2 = ctx.select(SALE.SALE_)
                .from(SALE).fetchSize(5).fetchStream() // jOOQ fluent API ends here                                                                
                .collect(Collectors.teeing( // Stream API starts here                           
                        summingDouble(rs -> rs.getValue(SALE.SALE_)),
                        mapping(rs -> rs.getValue(SALE.SALE_), toList()),
                        SimpleSale::new));
        System.out.println("Result=" + result2);

        // if you don't need the stream pipeline then simply don't use fetchStream()
        SimpleSale result3 = ctx.select(SALE.SALE_).from(SALE).fetchSize(5) // jOOQ fluent API ends here                                                                
                .collect(Collectors.teeing( // Stream API starts here                           
                        summingDouble(rs -> rs.getValue(SALE.SALE_)),
                        mapping(rs -> rs.getValue(SALE.SALE_), toList()),
                        SimpleSale::new));
        System.out.println("Result=" + result3);
    }

    // lazy fetching groups with streams
    @Transactional(readOnly=true)
    public void lazyFetchingGroupsViaFetchStream() {

        Map<Productline, List<Product>> result = ctx.select()
                .from(PRODUCTLINE)
                .leftOuterJoin(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .fetchSize(5) // optionally, set the fetch size
                // .fetchStream() // add this only if  you want to add additional operations to the stream pipeline                 
                .collect(Collectors.groupingBy(rs -> rs.into(Productline.class),
                        Collectors.mapping(rs -> rs.into(Product.class), toList())));

        System.out.println("Result: " + prettyPrint(result));
    }

    private static <K, V> String prettyPrint(Map<K, V> map) {

        StringBuilder sb = new StringBuilder();
        Iterator<Entry<K, V>> iter = map.entrySet().iterator();

        System.out.println("Iterating map: " + iter);
        while (iter.hasNext()) {
            Entry<K, V> entry = iter.next();
            sb.append("Key:\n").append(entry.getKey()).append("\n");
            sb.append("Value:\n").append(entry.getValue()).append("\n");
            if (iter.hasNext()) {
                sb.append("\n\n");
            }
        }

        return sb.toString();
    }
}
