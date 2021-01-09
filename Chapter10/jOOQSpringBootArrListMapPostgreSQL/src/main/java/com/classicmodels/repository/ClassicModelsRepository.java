package com.classicmodels.repository;

import static com.classicmodels.converter.YearMonthConverter.INTEGER_YEARMONTH_CONVERTER;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.pojos.Department;
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.CustomerdetailRecord;
import jooq.generated.tables.records.DepartmentRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Result;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.val;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void fetchArrayExamples() {

        Record[] result1 = ctx.select()
                .from(DEPARTMENT)
                .fetchArray();
        System.out.println("Example 1.1\n" + Arrays.toString(result1));

        String[] result2 = ctx.select(DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchArray(DEPARTMENT.NAME);
        System.out.println("Example 1.2\n" + Arrays.toString(result2));

        Integer[] result3 = ctx.select(DEPARTMENT.OFFICE_CODE)
                .from(DEPARTMENT)
                .fetchArray(DEPARTMENT.OFFICE_CODE, Integer.class);
        System.out.println("Example 1.3\n" + Arrays.toString(result3));

        YearMonth[] result4 = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .fetchArray(CUSTOMER.FIRST_BUY_DATE, INTEGER_YEARMONTH_CONVERTER);
        System.out.println("Example 1.4\n" + Arrays.toString(result4));

        Object[][] result5 = ctx.select(
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchArrays();
        System.out.println("Example 1.5\n" + Arrays.deepToString(result5));

        Object[] result6 = ctx.select(DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .where(DEPARTMENT.DEPARTMENT_ID.eq(1))
                .fetchOneArray();
        System.out.println("Example 1.6\n" + Arrays.toString(result6));

        Object[] result7 = ctx.select(DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .where(DEPARTMENT.DEPARTMENT_ID.eq(1))
                .fetchSingleArray();
        System.out.println("Example 1.7\n" + Arrays.toString(result7));

        Object[] result8 = ctx.select(DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchAnyArray();
        System.out.println("Example 1.8\n" + Arrays.toString(result8));

        String[][] result9 = ctx.select(DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetchArray(DEPARTMENT.TOPIC);
        System.out.println("Example 1.9\n" + Arrays.deepToString(result9));

        /* There is a bug in 3.14, but probably you'll not have it in the latest jOOQ version        
        Record3<Integer, String, String>[] result = ctx.select(
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchArray();        
         
        Record1<String>[] result = ctx.select(DEPARTMENT.NAME)
                .from(DEPARTMENT)                
                .fetchArray();   
        
        Record1<String[]>[] result = ctx.select(DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetchArray();        
         */
    }

    public void fetchListExamples() {

        Result<Record> result1 = ctx.select() // Result is a wrapper of List
                .from(DEPARTMENT)
                .fetch();
        System.out.println("Example 2.1\n" + result1);

        List<String> result2 = ctx.select(DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetch(DEPARTMENT.NAME); // or .fetch().getValues(DEPARTMENT.NAME)
        System.out.println("Example 2.2\n" + result2);

        List<Integer> result3 = ctx.select(DEPARTMENT.OFFICE_CODE)
                .from(DEPARTMENT)
                .fetch(DEPARTMENT.OFFICE_CODE, Integer.class); // or, .fetchInto(int.class);
        System.out.println("Example 2.3\n" + result3);

        List<YearMonth> result4 = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .fetch(CUSTOMER.FIRST_BUY_DATE, INTEGER_YEARMONTH_CONVERTER);
        System.out.println("Example 2.4\n" + result4);

        Result<Record3<Integer, String, String>> result5 = ctx.select(
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetch();
        System.out.println("Example 2.5\n" + result5);

        List<Department> result6 = ctx.selectFrom(DEPARTMENT)
                .fetchInto(Department.class); // or, .fetch().into(Department.class)                        
        System.out.println("Example 2.6\n" + result6);

        List<Department> result7 = ctx.select(
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchInto(Department.class); // or, .fetch().into(Department.class)                        
        System.out.println("Example 2.7\n" + result7);

        Result<DepartmentRecord> result8 = ctx.select( // or, List
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchInto(DEPARTMENT); // or, .fetch().into(Department.class)                        
        System.out.println("Example 2.8\n" + result8);

        List<DepartmentRecord> result9 = ctx.select(
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchInto(DepartmentRecord.class); // or, .fetch().into(Department.class)                        
        System.out.println("Example 2.9\n" + result9);

        Result<Record1<String[]>> result10 = ctx.select(DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetch();
        System.out.println("Example 2.10\n" + result10);

        List<String[]> result11 = ctx.select(DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetch(DEPARTMENT.TOPIC, String[].class);
        System.out.println("Example 2.11\n" + result11);
    }

    public void fetchMapExamples() {

        Map<Integer, DepartmentRecord> result1 = ctx.selectFrom(DEPARTMENT)
                .fetchMap(DEPARTMENT.DEPARTMENT_ID);
        System.out.println("Example 3.1\n" + prettyPrint(result1));

        Map<String, Record2<String, BigDecimal>> result2 = ctx.select(
                concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "),
                        CUSTOMER.CONTACT_LAST_NAME).as("customer_name"), CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER)
                .limit(10)
                .fetchMap(field("customer_name", String.class));
        System.out.println("Example 3.2\n" + prettyPrint(result2));

        Map<Department, DepartmentRecord> result3 = ctx.selectFrom(DEPARTMENT)
                .fetchMap(Department.class);
        System.out.println("Example 3.3\n" + prettyPrint(result3));

        Map<Record, DepartmentRecord> result4 = ctx.selectFrom(DEPARTMENT)
                .fetchMap(new Field[]{DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME});
        System.out.println("Example 3.4\n" + prettyPrint(result4));

        Map<Integer, String> result5 = ctx.select(DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchMap(DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.NAME);
        System.out.println("Example 3.5\n" + prettyPrint(result5));

        Map<String, String[]> result6 = ctx.select(DEPARTMENT.NAME, DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetchMap(DEPARTMENT.NAME, DEPARTMENT.TOPIC);
        System.out.println("Example 3.6\n" + prettyPrint(result6));

        // mapping one-to-one
        Map<Record, Record> result7 = ctx.select(CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetchMap(new Field[]{CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME},
                new Field[]{CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY});
        System.out.println("Example 3.7\n" + prettyPrint(result7));

        Map<CustomerRecord, CustomerdetailRecord> result8 = ctx.select()
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetchMap(CUSTOMER, CUSTOMERDETAIL);
        System.out.println("Example 3.8\n" + prettyPrint(result8));

        // mapping one-to-many
        Map<Record, Record> result9 = ctx.select(concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), 
                CUSTOMER.CONTACT_LAST_NAME).as("customer_name"), PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE)
                .from(CUSTOMER)
                .join(PAYMENT)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                .fetchMap(new Field[]{PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE},
                new Field[]{field("customer_name", String.class)});
        System.out.println("Example 3.9\n" + prettyPrint(result9));

        Map<Integer, BigDecimal> result10 = ctx.select(rowNumber().over().as("no"), CUSTOMER.CREDIT_LIMIT)
                .from(CUSTOMER)
                .fetchMap(field("no", Integer.class), CUSTOMER.CREDIT_LIMIT);
        System.out.println("Example 3.10\n" + prettyPrint(result10));

        Map<String, Object> result11 = ctx.select(DEPARTMENT.NAME,
                DEPARTMENT.OFFICE_CODE, DEPARTMENT.PHONE).from(DEPARTMENT)
                .where(DEPARTMENT.DEPARTMENT_ID.eq(1))
                .fetchOneMap();
        System.out.println("Example 3.11\n" + prettyPrint(result11));

        Map<String, Object> result12 = ctx.selectFrom(PRODUCT)
                .where(PRODUCT.PRODUCT_ID.eq(23L))
                .fetchSingleMap();
        System.out.println("Example 3.12\n" + prettyPrint(result12));

        Map<String, Object> result13 = ctx.selectFrom(ORDER)
                .fetchAnyMap();
        System.out.println("Example 3.13\n" + prettyPrint(result13));

        List<Map<String, Object>> result14 = ctx.selectFrom(DEPARTMENT)
                .fetchMaps();
        System.out.println("Example 3.14\n" + result14);
    }

    public static <K, V> String prettyPrint(Map<K, V> map) {

        StringBuilder sb = new StringBuilder();
        Iterator<Entry<K, V>> iter = map.entrySet().iterator();

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
