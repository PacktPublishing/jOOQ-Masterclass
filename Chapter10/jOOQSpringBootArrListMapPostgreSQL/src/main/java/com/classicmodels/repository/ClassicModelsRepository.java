package com.classicmodels.repository;

import static com.classicmodels.converter.YearMonthConverter.INTEGER_YEARMONTH_CONVERTER;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Department.DEPARTMENT;
import jooq.generated.tables.pojos.Department;
import jooq.generated.tables.records.DepartmentRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record3;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void fetchArrayExamples() {

        /*
        List<String[]> result1 = ctx.select(DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetch(DEPARTMENT.NAME, String[].class);
        System.out.println("Example 1.1\n" + result1);
         */
        org.jooq.Record[] result1 = ctx.select()
                .from(DEPARTMENT)
                .fetchArray();
        System.out.println("Example 1.1\n" + Arrays.toString(result1));

        /*
        var result1z = ctx.select(DEPARTMENT.NAME)
                .from(DEPARTMENT)                
                .fetchArray();
        //System.out.println("Example 1.1.z\n" + Arrays.toString(result1z));
         */
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

        /*
        Record3<Integer, String, String>[] result5 = ctx.select(
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchArray();
        System.out.println("Example 1.5\n" + Arrays.toString(result5));
         */
        Object[][] result6 = ctx.select(
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchArrays();
        System.out.println("Example 1.6\n" + Arrays.deepToString(result6));                

        Object[] result7 = ctx.select(
                DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .where(DEPARTMENT.DEPARTMENT_ID.eq(1))
                .fetchOneArray();
        System.out.println("Example 1.7\n" + Arrays.toString(result7));

        Object[] result8 = ctx.select(
                DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .where(DEPARTMENT.DEPARTMENT_ID.eq(1))
                .fetchSingleArray();
        System.out.println("Example 1.8\n" + Arrays.toString(result8));

        Object[] result9 = ctx.select(
                DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchAnyArray();
        System.out.println("Example 1.9\n" + Arrays.toString(result9));

        String[][] result10 = ctx.select(DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetchArray(DEPARTMENT.TOPIC);
        System.out.println("Example 1.10\n" + Arrays.deepToString(result10));

        /*
        Record1<String[]>[] result11 = ctx.select(DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetchArray();
        System.out.println("Example 1.11\n" + Arrays.toString(result11));
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
        System.out.println("Example 1.3\n" + result3);

        List<YearMonth> result4 = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .fetch(CUSTOMER.FIRST_BUY_DATE, INTEGER_YEARMONTH_CONVERTER);
        System.out.println("Example 1.4\n" + result4);

        Result<Record3<Integer, String, String>> result5 = ctx.select(
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)                
                .fetch();                        
        System.out.println("Example 1.5\n" + result5);
        
        List<Department> result6 = ctx.select(
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchInto(Department.class); // or, .fetch().into(Department.class)                        
        System.out.println("Example 1.6\n" + result6);
        
        Result<DepartmentRecord> result7 = ctx.select( // or, List
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchInto(DEPARTMENT); // or, .fetch().into(Department.class)                        
        System.out.println("Example 1.7\n" + result7);
        
        List<DepartmentRecord> result8 = ctx.select(
                DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.OFFICE_CODE, DEPARTMENT.NAME)
                .from(DEPARTMENT)
                .fetchInto(DepartmentRecord.class); // or, .fetch().into(Department.class)                        
        System.out.println("Example 1.8\n" + result8);
        
        Result<Record1<String[]>> result10 = ctx.select(DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetch();
        System.out.println("Example 1.10\n" +result10);
        
        List<String[]> result11 = ctx.select(DEPARTMENT.TOPIC)
                .from(DEPARTMENT)
                .fetch(DEPARTMENT.TOPIC, String[].class);
        System.out.println("Example 1.11\n" + result11);
    }
    
    public void fetchMapExamples() {
        
        Result<Record> result1 = ctx.select() // Result is a wrapper of List
                .from(DEPARTMENT)                
                .fetch();
        System.out.println("Example 2.1\n" + result1);
    }
}
