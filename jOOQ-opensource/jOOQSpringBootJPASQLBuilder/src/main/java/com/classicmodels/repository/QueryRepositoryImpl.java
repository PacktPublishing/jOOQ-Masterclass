package com.classicmodels.repository;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeDto;
import com.classicmodels.pojo.EmployeeDtoCntr;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.transform.Transformers;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.firstValue;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.table;
import org.springframework.stereotype.Repository;

@Repository
public class QueryRepositoryImpl implements QueryRepository {

    private final DSLContext create;
    private final EntityManager em;

    public QueryRepositoryImpl(DSLContext create, EntityManager em) {
        this.create = create;
        this.em = em;
    }

    @Override
    public List<Object[]> findEmployeesWithTotalSalesByFiscalYear() {

        org.jooq.Query query = create.select(field("EMPLOYEE.FIRST_NAME"),
                field("EMPLOYEE.LAST_NAME"), field("SALE.FISCAL_YEAR"),
                field("SALE.SALE"), field("SALE.EMPLOYEE_NUMBER"),
                sum(field("SALE.SALE").coerce(Float.class))
                        .over(partitionBy(field("SALE.FISCAL_YEAR"))).as("TOTAL_SALES"))
                .from(table("SALE"))
                .join(table("EMPLOYEE")).using(field("EMPLOYEE_NUMBER"));

        return nativeQueryToListOfObj(em, query);
    }

    @Override
    public List<EmployeeDto> findEmployeesAndLeastSalary() {

        org.jooq.Query query = create.select(field("EMPLOYEE.FIRST_NAME").as("firstName"),
                field("EMPLOYEE.LAST_NAME").as("lastName"),
                field("EMPLOYEE.SALARY").as("salary"),
                firstValue(field("EMPLOYEE.FIRST_NAME"))
                        .over().orderBy(field("EMPLOYEE.SALARY")).as("leastSalary"))
                .from(table("EMPLOYEE"));

        return nativeQueryToPojo(em, query, EmployeeDto.class);
    }

    @Override
    public List<EmployeeDtoCntr> findEmployeesAndLeastSalaryCntr() {

        org.jooq.Query query = create.select(field("EMPLOYEE.FIRST_NAME").as("firstName"),
                field("EMPLOYEE.LAST_NAME").as("lastName"),
                field("EMPLOYEE.SALARY").as("salary"),
                firstValue(field("EMPLOYEE.FIRST_NAME"))
                        .over().orderBy(field("EMPLOYEE.SALARY")).as("leastSalary"))
                .from(table("EMPLOYEE"));

        return nativeQueryToPojoCntr(em, query, "EmployeeDtoMapping");
    }

    @Override
    public List<Employee> findEmployeeInCity(String city) {

        org.jooq.Query query
                = create.select()
                        .from(table("EMPLOYEE")
                                .where(field("EMPLOYEE.OFFICE_CODE").eq(create
                                        .select(field("OFFICE.OFFICE_CODE"))
                                        .from(table("OFFICE"))
                                        .where(field("OFFICE.CITY").eq(city)))));

        return nativeQueryToEntity(em, query, Employee.class);
    }

    private static List<Object[]> nativeQueryToListOfObj(EntityManager em, org.jooq.Query query) {

        // Get the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL());

        // Get the bind values from the jOOQ query
        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }

        return result.getResultList();
    }

    private static <P> List<P> nativeQueryToPojo(
            EntityManager em, org.jooq.Query query, Class<P> type) {

        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL());

        // Extract the bind values from the jOOQ query
        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }

        // in Hibernate 6 you should use the new Transformers
        result.unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(
                        Transformers.aliasToBean(type));

        // JPA returns the right type
        return result.getResultList();
    }

    private static <P> List<P> nativeQueryToPojoCntr(
            EntityManager em, org.jooq.Query query, String mapping) {

        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL(), mapping);

        // Extract the bind values from the jOOQ query
        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }

        // JPA returns the right type
        return result.getResultList();
    }

    private static <E> List<E> nativeQueryToEntity(
            EntityManager em, org.jooq.Query query, Class<E> type) {

        // Extract the SQL statement from the jOOQ query
        javax.persistence.Query result = em.createNativeQuery(query.getSQL(), type);

        // Extract the bind values from the jOOQ query
        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }

        // JPA returns the right type
        return result.getResultList();
    }

}
