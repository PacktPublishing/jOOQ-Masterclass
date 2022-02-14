package com.classicmodels.repository;

import static com.classicmodels.converter.YearMonthConverter.YEARMONTH;
import com.classicmodels.pojo.SimpleProduct;
import com.classicmodels.pojo.SimpleProductline;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static java.util.stream.Collectors.filtering;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Customer.CUSTOMER;
import jooq.generated.tables.pojos.Department;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import jooq.generated.tables.pojos.Manager;
import jooq.generated.tables.pojos.Office;
import jooq.generated.tables.pojos.Product;
import jooq.generated.tables.pojos.Productline;
import jooq.generated.tables.records.BankTransactionRecord;
import jooq.generated.tables.records.DepartmentRecord;
import jooq.generated.tables.records.ManagerRecord;
import jooq.generated.tables.records.OfficeRecord;
import jooq.generated.tables.records.OrderRecord;
import jooq.generated.tables.records.ProductRecord;
import jooq.generated.tables.records.ProductlineRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Result;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import static org.jooq.Records.intoGroups;
import static org.jooq.Records.intoResultGroups;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.row;
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

    public void fetchGroupsExamples() {

        Map<Long, Result<OrderRecord>> result1_1 = ctx.selectFrom(ORDER)
                .fetchGroups(ORDER.CUSTOMER_NUMBER);
        System.out.println("Example 1.1\n" + prettyPrint(result1_1));
        
        // using Records utility
        Map<Long, List<OrderRecord>> result1_2 = ctx.selectFrom(ORDER)
                .collect(intoGroups(r -> r.get(ORDER.CUSTOMER_NUMBER)));
        System.out.println("Example 1.2\n" + prettyPrint(result1_2));

        Map<Long, Result<Record2<Long, BigDecimal>>> result2_1 = ctx.select(
                BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.TRANSFER_AMOUNT)
                .from(BANK_TRANSACTION)
                .fetchGroups(BANK_TRANSACTION.CUSTOMER_NUMBER);
        System.out.println("Example 2.1\n" + prettyPrint(result2_1));
        
        Map<Long, List<BigDecimal>> result2_2 = ctx.select(
                BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.TRANSFER_AMOUNT)
                .from(BANK_TRANSACTION)
                .fetchGroups(BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.TRANSFER_AMOUNT);
        System.out.println("Example 2.2\n" + prettyPrint(result2_2));

        // using Records utility
        Map<Long, List<Record2<Long, BigDecimal>>> result2_3_1 = ctx.select(
                BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.TRANSFER_AMOUNT)
                .from(BANK_TRANSACTION)
                .collect(intoGroups(r -> r.get(BANK_TRANSACTION.CUSTOMER_NUMBER)));
        System.out.println("Example 2.3.1\n" + prettyPrint(result2_3_1));
        
        Map<Long, Result<Record2<Long, BigDecimal>>> result2_3_2 = ctx.select(
                BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.TRANSFER_AMOUNT)
                .from(BANK_TRANSACTION)
                .collect(intoResultGroups(r -> r.get(BANK_TRANSACTION.CUSTOMER_NUMBER)));
        System.out.println("Example 2.3.2\n" + prettyPrint(result2_3_2));
        
        // using Records utility
        Map<Long, List<BigDecimal>> result2_4 = ctx.select(
                BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.TRANSFER_AMOUNT)
                .from(BANK_TRANSACTION)
                .collect(intoGroups(r -> r.get(BANK_TRANSACTION.CUSTOMER_NUMBER),
                        r -> r.get(BANK_TRANSACTION.TRANSFER_AMOUNT)));
        System.out.println("Example 2.4\n" + prettyPrint(result2_4));
        
        // using Records utility
        Map<Long, List<BigDecimal>> result2_5 = ctx.select(
                BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.TRANSFER_AMOUNT)
                .from(BANK_TRANSACTION)
                .collect(intoGroups());
        System.out.println("Example 2.5\n" + prettyPrint(result2_5));

        // Example 3 and 4 produces the same result
        Map<Record, Result<BankTransactionRecord>> result3 = ctx.selectFrom(BANK_TRANSACTION)
                .fetchGroups(row(BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.CHECK_NUMBER).fields());
        System.out.println("Example 3\n" + prettyPrint(result3));

        Map<Record, Result<BankTransactionRecord>> result4 = ctx.selectFrom(BANK_TRANSACTION)
                .fetchGroups(new Field[]{BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.CHECK_NUMBER});
        System.out.println("Example 4\n" + prettyPrint(result4));

        Map<Record, Result<Record>> result5 = ctx.select(
                BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.CHECK_NUMBER,
                BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.TRANSFER_AMOUNT)
                .from(BANK_TRANSACTION)
                .fetchGroups(new Field[]{BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.CHECK_NUMBER},
                new Field[]{BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.TRANSFER_AMOUNT});
        System.out.println("Example 5\n" + prettyPrint(result5));

        // let me stress it again that you should use "var"
        Map<Record, Result<Record4<String, String, String, String>>> result6
                = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                        EMPLOYEE.employee().FIRST_NAME, EMPLOYEE.employee().LAST_NAME)
                        .from(EMPLOYEE)
                        .fetchGroups(new Field[]{EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME});
        System.out.println("Example 6\n" + prettyPrint(result6));

        Map<Department, Result<DepartmentRecord>> result7 = ctx.selectFrom(DEPARTMENT)
                .fetchGroups(Department.class);
        System.out.println("Example 7\n" + prettyPrint(result7));

        Map<String, List<Office>> result8 = ctx.selectFrom(OFFICE)
                .fetchGroups(OFFICE.CITY, Office.class);
        System.out.println("Example 8\n" + prettyPrint(result8));

        var result9 = ctx.select(CUSTOMER.FIRST_BUY_DATE.coerce(YEARMONTH), CUSTOMER.CUSTOMER_NUMBER)
                .from(CUSTOMER)
                .where(CUSTOMER.FIRST_BUY_DATE.isNotNull())
                .fetchGroups(CUSTOMER.FIRST_BUY_DATE);
        System.out.println("Example 9\n" + prettyPrint(result9));
        
        Map<String, List<Office>> result10 = ctx.select(OFFICE.CITY.as("office_city"),
                OFFICE.asterisk().except(OFFICE.CITY))
                .from(OFFICE)
                .fetchGroups(field("office_city", String.class), Office.class);
        System.out.println("Example 10\n" + prettyPrint(result10));

        // mapping one-to-many
        Map<Record, Result<Record>> result11_1 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                .from(PRODUCTLINE)
                .innerJoin(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .fetchGroups(new Field[]{PRODUCTLINE.PRODUCT_LINE},
                new Field[]{PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE});
        System.out.println("Example 11.1\n" + prettyPrint(result11_1));

        // for left-join, use ResultQuery.collect(), at least until you see
        // this (https://github.com/jOOQ/jOOQ/issues/11888) resolved
        Map<ProductlineRecord, List<ProductRecord>> result11_2 = ctx.select()
                .from(PRODUCTLINE)
                .leftOuterJoin(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .collect(groupingBy(
                        r -> r.into(PRODUCTLINE),
                        filtering(
                                r -> r.get(PRODUCT.PRODUCT_ID) != null,
                                mapping(
                                        r -> r.into(PRODUCT),
                                        toList()
                                )
                        )
                ));
        System.out.println("Example 11.2\n" + prettyPrint(result11_2));

        Map<String, Result<Record3<String, String, BigDecimal>>> result12 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                .from(PRODUCTLINE)
                .innerJoin(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .fetchGroups(PRODUCTLINE.PRODUCT_LINE);
        System.out.println("Example 12\n" + prettyPrint(result12));

        Map<ProductlineRecord, Result<ProductRecord>> result13 = ctx.select()
                .from(PRODUCTLINE)
                .innerJoin(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .fetchGroups(PRODUCTLINE, PRODUCT);
        System.out.println("Example 13\n" + prettyPrint(result13));

        // take advantage of generated equals()/hashCode(), 
        //<pojosEqualsAndHashCode>true</pojosEqualsAndHashCode>                                                   
        Map<Productline, List<Product>> result14_1 = ctx.select()
                .from(PRODUCTLINE)
                .innerJoin(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .fetchGroups(Productline.class, Product.class);
        System.out.println("Example 14.1\n" + prettyPrint(result14_1));

        // for left-join, use ResultQuery.collect(), at least until you see
        // this (https://github.com/jOOQ/jOOQ/issues/11888) resolved
        Map<Productline, List<Product>> result14_2 = ctx.select()
                .from(PRODUCTLINE)
                .leftOuterJoin(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .collect(groupingBy(
                        r -> r.into(Productline.class),
                        filtering(
                                r -> r.get(PRODUCT.PRODUCT_ID) != null,
                                mapping(
                                        r -> r.into(Product.class),
                                        toList()
                                )
                        )
                ));
        System.out.println("Example 14.2\n" + prettyPrint(result14_2));

        Map<SimpleProductline, List<SimpleProduct>> result15 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE,
                PRODUCT.PRODUCT_ID, PRODUCT.BUY_PRICE)
                .from(PRODUCTLINE)
                .innerJoin(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .fetchGroups(SimpleProductline.class, SimpleProduct.class);
        System.out.println("Example 15\n" + prettyPrint(result15));

        // denormalising (flattening)
        Map<ProductlineRecord, Result<Record>> result16 = ctx.select()
                .from(PRODUCTLINE)
                .innerJoin(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .fetchGroups(PRODUCTLINE);
        System.out.println("Example 16\n" + prettyPrint(result16));

        // mapping many-to-many (you can also re-write this using crossApply() as in example 18 below)
        Map<ManagerRecord, Result<OfficeRecord>> result17 = ctx.select()
                .from(MANAGER)
                .join(select().from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("t"))
                .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                .fetchGroups(MANAGER, OFFICE);
        System.out.println("Example 17\n" + prettyPrint(result17));

        // take advantage of generated equals()/hashCode(), 
        //<pojosEqualsAndHashCode>true</pojosEqualsAndHashCode>              
        Map<Manager, List<Office>> result18 = ctx.select()
                .from(MANAGER).crossApply(select().from(OFFICE).join(OFFICE_HAS_MANAGER)
                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)))
                .fetchGroups(Manager.class, Office.class);
        System.out.println("Example 18\n" + prettyPrint(result18));

        // same thing as the above query but a little bit more verbose
        Map<Manager, List<Office>> result19 = ctx.select()
                .from(MANAGER)
                .join(select().from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("t"))
                .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                .fetchGroups(Manager.class, Office.class);
        System.out.println("Example 19\n" + prettyPrint(result19));

        Map<Record, Result<Record>> result20
                = ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME, field("city"), field("country"))
                        .from(MANAGER).crossApply(select(OFFICE.CITY.as("city"), OFFICE.COUNTRY.as("country"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)))
                        .fetchGroups(new Field[]{MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME},
                        new Field[]{field("city"), field("country")});
        System.out.println("Example 20\n" + prettyPrint(result20));

        Map<Record, Result<Record>> result21
                = ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME, field("city"), field("country"))
                        .from(MANAGER)
                        .join(select(OFFICE.CITY.as("city"), OFFICE.COUNTRY.as("country"),
                                OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID.as("managers_manager_id"))
                                .from(OFFICE).join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE)).asTable("t"))
                        .on(MANAGER.MANAGER_ID.eq(field(name("managers_manager_id"), Long.class)))
                        .fetchGroups(new Field[]{MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME},
                        new Field[]{field("city"), field("country")});
        System.out.println("Example 21\n" + prettyPrint(result21));
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
