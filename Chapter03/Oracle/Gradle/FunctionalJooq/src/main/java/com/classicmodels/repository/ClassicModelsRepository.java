package com.classicmodels.repository;

import com.classicmodels.pojo.EmployeeData;
import com.classicmodels.pojo.EmployeeName;
import com.classicmodels.pojo.SaleStats;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.RecordMapper;
import org.jooq.conf.FetchIntermediateResult;
import org.jooq.conf.Settings;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<EmployeeData> findAllEmployee() {

        // using map()
        List<EmployeeData> result1
                = ctx.fetch("SELECT EMPLOYEE_NUMBER, FIRST_NAME, LAST_NAME, SALARY FROM EMPLOYEE")
                        .map(
                                rs -> new EmployeeData(
                                        rs.getValue("EMPLOYEE_NUMBER", Long.class),
                                        rs.getValue("SALARY", Integer.class),
                                        new EmployeeName(rs.getValue("FIRST_NAME", String.class),
                                                rs.getValue("LAST_NAME", String.class))
                                )
                        );

        // using into()              
        List<EmployeeData> result2 = ctx.fetch("""
                                              SELECT EMPLOYEE_NUMBER, SALARY, 
                                              FIRST_NAME AS "employeeName.firstName",
                                              LAST_NAME AS "employeeName.lastName" FROM EMPLOYEE""")
                .into(EmployeeData.class);

        // using fetch().map()                
        List<EmployeeData> result3 = ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME,
                EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                .from(EMPLOYEE)
                .fetch()
                .map(
                        rs -> new EmployeeData(
                                rs.getValue(EMPLOYEE.EMPLOYEE_NUMBER),
                                rs.getValue(EMPLOYEE.SALARY),
                                new EmployeeName(rs.getValue(EMPLOYEE.FIRST_NAME),
                                        rs.getValue(EMPLOYEE.LAST_NAME))
                        )
                );

        // or, using fetch(mapper)        
        List<EmployeeData> result4 = ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME,
                EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                .from(EMPLOYEE)
                .fetch(
                        rs -> new EmployeeData(
                                rs.getValue(EMPLOYEE.EMPLOYEE_NUMBER),                                
                                rs.getValue(EMPLOYEE.SALARY),
                                new EmployeeName(rs.getValue(EMPLOYEE.FIRST_NAME),
                                                rs.getValue(EMPLOYEE.LAST_NAME))
                        )
                );
         
        // using fetchInto()        
        List<EmployeeData> result5 = ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.SALARY,
                EMPLOYEE.FIRST_NAME.as("employeeName.firstName"), 
                EMPLOYEE.LAST_NAME.as("employeeName.lastName"))
                .from(EMPLOYEE)
                .fetchInto(EmployeeData.class);
         
        return result5;
    }

    public List<Double> findAllSales() {

        // print all fetched data
        ctx.selectFrom(SALE)
                .orderBy(SALE.SALE_)
                // .fetch() - optional
                .forEach(System.out::println);

        // print 'sales' using fetch().map(mapper)
        ctx.selectFrom(SALE)
                .orderBy(SALE.SALE_)
                .fetch()
                .map(SaleRecord::getSale)
                .forEach(System.out::println);

        // print 'sales' using fetch(mapper)
        ctx.selectFrom(SALE)
                .orderBy(SALE.SALE_)
                .fetch(SaleRecord::getSale)
                .forEach(System.out::println);

        // print 'sales' using lambda expression
        ctx.selectFrom(SALE)
                .orderBy(SALE.SALE_)
                .fetch(s -> s.getSale())
                .forEach(System.out::println);

        // return 'sales' using anonymous RecordMapper
        return ctx.selectFrom(SALE)
                .orderBy(SALE.SALE_)
                .fetch(new RecordMapper<SaleRecord, Double>() {
                    @Override
                    public Double map(SaleRecord sr) {
                        return sr.getSale();
                    }
                });
    }

    public SaleStats findSalesAndTotalSale() {
        
        SaleStats result11 = ctx.fetch("SELECT SALE FROM SALE")  // jOOQ fluent API ends here (ResultSet is closed)                                                                  
                .stream() // Stream fluent API starts here (java.​util.​Collection.stream())                
                .collect( // java.​util.​stream.​Stream.collect()
                        Collectors.teeing(summingDouble(rs -> rs.getValue("SALE", Double.class)), 
                        mapping(rs -> rs.getValue("SALE", Double.class), toList()), SaleStats::new)
                );        
        
        SaleStats result12 = ctx.fetch("SELECT SALE FROM SALE") // (ResultSet is closed)                                                                                                  
                .collect( // org.​jooq.​Result.collect() which is the same as java.​util.​stream.​Stream.collect()
                        Collectors.teeing(summingDouble(rs -> rs.getValue("SALE", Double.class)),
                        mapping(rs -> rs.getValue("SALE", Double.class), toList()), SaleStats::new)
                );   
                     
        SaleStats result21 = ctx.select(SALE.SALE_)
                .from(SALE)
                .fetch() // jOOQ fluent API ends here (ResultSet is closed)                  
                .stream() // Stream fluent API starts here (java.​util.​Collection.stream())                                      
                .collect(
                        Collectors.teeing(summingDouble(rs -> rs.getValue(SALE.SALE_)),
                        mapping(rs -> rs.getValue(SALE.SALE_), toList()), SaleStats::new)
                );
        
        SaleStats result22 = ctx.select(SALE.SALE_)
                .from(SALE)
                .fetch() // ResultSet is closed
                .collect( // org.​jooq.​Result.collect() which is the same as java.​util.​stream.​Stream.collect()
                        Collectors.teeing(summingDouble(rs -> rs.getValue(SALE.SALE_)),
                        mapping(rs -> rs.getValue(SALE.SALE_), toList()),
                        SaleStats::new)
                );
        
        // try-with-resources is needed, here we stream the ResultSet which remains open
        try (Stream<Record1<Double>> stream = ctx.select(SALE.SALE_).from(SALE).stream()) {
            SaleStats result31 = stream
                    .collect( // Stream fluent API starts here 
                        Collectors.teeing(summingDouble(rs -> rs.getValue(SALE.SALE_)), 
                        mapping(rs -> rs.getValue(SALE.SALE_), toList()),
                        SaleStats::new)
                );
        }
               
        // no need for managing resources, which are handled inside of the collect() method
        SaleStats result32 = ctx.select(SALE.SALE_)
                .from(SALE)                               
                .collect( // org.​jooq.​ResultQuery.collect()
                        Collectors.teeing(summingDouble(rs -> rs.getValue(SALE.SALE_)), 
                        mapping(rs -> rs.getValue(SALE.SALE_), toList()),
                        SaleStats::new)
                );
                
        // explicitly require an intermediate Result (useful when you have a  ExecuteListener around)
        DSLContext dtx = ctx.configuration()
                .derive(new Settings().withFetchIntermediateResult(
                        FetchIntermediateResult.ALWAYS)).dsl();
        
        // try-with-resources is needed, here we stream the ResultSet which remains open
        try (Stream<Record1<Double>> stream = dtx.select(SALE.SALE_).from(SALE).stream()) {
            SaleStats result41 = stream
                    .collect( // Stream fluent API starts here 
                        Collectors.teeing(summingDouble(rs -> rs.getValue(SALE.SALE_)), 
                        mapping(rs -> rs.getValue(SALE.SALE_), toList()),
                        SaleStats::new)
                );
        }
        
        // no need for managing resources, which are handled inside of the collect() method
        SaleStats result42 = dtx.select(SALE.SALE_)
                .from(SALE)                               
                .collect( // org.​jooq.​ResultQuery.collect()
                        Collectors.teeing(summingDouble(rs -> rs.getValue(SALE.SALE_)), 
                        mapping(rs -> rs.getValue(SALE.SALE_), toList()),
                        SaleStats::new)
                );
        
        return result11;
    }
}
