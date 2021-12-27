package com.classicmodels.repository;

import com.classicmodels.pojo.EmployeeData;
import com.classicmodels.pojo.EmployeeName;
import com.classicmodels.pojo.SaleStats;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.jooq.RecordMapper;
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
                = ctx.fetch("SELECT employee_number, first_name, last_name, salary FROM employee")
                        .map(
                                rs -> new EmployeeData(
                                        rs.getValue("employee_number", Long.class),
                                        rs.getValue("salary", Integer.class),
                                        new EmployeeName(rs.getValue("first_name", String.class),
                                                rs.getValue("last_name", String.class))
                                )
                        );

        // using into()              
        List<EmployeeData> result2 = ctx.fetch("""
                                              SELECT employee_number, salary, 
                                              first_name AS `employeeName.firstName`,
                                              last_name AS `employeeName.lastName` FROM employee""")
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
        
        SaleStats result1 = ctx.fetch("SELECT sale FROM sale")  // jOOQ fluent API ends here                                                
                .stream() // Stream fluent API starts here                   
                .collect(Collectors.teeing(summingDouble(rs -> rs.getValue("sale", Double.class)),
                        mapping(rs -> rs.getValue("sale", Double.class), toList()),
                        SaleStats::new));        
                
        SaleStats result2 = ctx.select(SALE.SALE_)
                .from(SALE)
                .fetch() // jOOQ fluent API ends here                  
                .stream() // Stream fluent API starts here                   
                .collect(Collectors.teeing(summingDouble(rs -> rs.getValue(SALE.SALE_)),
                        mapping(rs -> rs.getValue(SALE.SALE_), toList()),
                        SaleStats::new));
        
        return result2;
    }
}