package com.classicmodels.repository;

import com.classicmodels.entity.Employee;
import java.util.List;
import jooq.generated.tables.interfaces.IEmployee;
import jooq.generated.tables.pojos.JooqEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository
        extends JpaRepository<Employee, Long>, ClassicModelsRepository {
    
    /* Migrating native SQLs that doesn't return JPA entities */
    /* This query was migrated to jOOQ  */
    /* 
    @Query(value="""
                 SELECT 
                   e.first_name, 
                   e.last_name, 
                   s.fiscal_year, 
                   s.sale, 
                   s.employee_number, 
                   sum(s.sale) OVER (PARTITION BY s.fiscal_year) AS TOTAL_SALES
                 FROM 
                   sale s 
                   INNER JOIN employee e ON s.employee_number = e.employee_number;
                 """, nativeQuery = true)
    public List<Object[]> findEmployeesWithTotalSalesByFiscalYear();     
    */

    /* This query was migrated to jOOQ  */
    /*
    @Query(value="""
                 SELECT
                   first_name AS firstName, last_name AS lastName, salary AS salary, 
                   first_value(
                     first_name
                   ) OVER (
                     ORDER BY 
                       salary
                   ) AS leastSalary
                 FROM
                   employee
                 """, nativeQuery = true)
    public List<EmployeeLeastSalary> findEmployeesAndLeastSalary();
    */
            
    /* Migrating queries generated via Query Builder Mechanism that doesn't return JPA entities */
    /* This query was migrated to jOOQ  */
    //public List<EmployeeSlim> findFirst5ByOrderBySalaryDesc();
        
    /* Migrating JPQLs that doesn't return JPA entities */
    /* This query was migrated to jOOQ  */
    /*
    @Query(value="""
                 SELECT new com.classicmodels.pojo.EmployeeCntr(
                   e.firstName, e.lastName, e.salary)
                 FROM Employee e WHERE e.salary > 80000
                 """)
    public List<EmployeeCntr> findEmployeesAndLeastSalaryCntr();
    */
    
    /* Migrating native queries that return entities */
    /* This query was migrated to jOOQ  */
    /*
    @Query(value = """
                 SELECT 
                   employee_number, last_name, first_name, extension, 
                   email, office_code, salary, reports_to, job_title 
                 FROM employee
                 WHERE office_code = (
                     SELECT office_code 
                     FROM office
                     WHERE city = ?1
                   )
                 """, nativeQuery = true)
    public List<Employee> findEmployeeInCity(String city);   
    */  
    
    /* Migrating queries generated via Query Builder Mechanism that return JPA entities */
    /* This query was migrated to jOOQ  */
    /*
    public List<Employee> findFirst3BySalaryLessThanAndJobTitleOrderByFirstNameDesc(int salary, String jobTitle);    
    */
    /* Migrating JPQLs that return JPA entities */
    /* This query was migrated to jOOQ  */
    /*
    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN ?1 AND ?2")
    List<Employee> findEmployeeSalaryInRange(int start, int end);  
    */ 
    
    /* The following queries can be migrated to jOOQ as well, but let's
       keep them here for using jOOQ generated POJOs and interfaces
    */
    // uses jOOQ generated POJO, JooqEmployee
    @Query("""
           SELECT 
              new jooq.generated.tables.pojos.JooqEmployee(
                e.employeeNumber, e.lastName, e.firstName, 
                e.extension, e.email, '', e.salary, -1L, e.jobTitle)              
            FROM 
              Employee e 
            WHERE 
              e.jobTitle = ?1
           """)         
    public List<JooqEmployee> findByJobTitleCntr(String jobTitle);

    // uses jOOQ generated interfaces, IEmployee
    @Query("""
           SELECT 
             e.employeeNumber AS employeeNumber, 
             e.email as email, 
             e.extension as extension, 
             e.firstName AS firstName, 
             e.jobTitle AS jobTitle, 
             e.lastName AS lastName, 
             e.salary AS salary 
           FROM 
             Employee e 
           WHERE 
             e.jobTitle = ?1
           """)
    public List<IEmployee> findByJobTitleJpql(String jobTitle);

    // uses jOOQ generated interfaces, IEmployee
    @Query(value = """
                   SELECT 
                     e.employee_number AS employeeNumber, 
                     e.email as email, 
                     e.extension as extension, 
                     e.first_name AS firstName, 
                     e.job_title AS jobTitle, 
                     e.last_name AS lastName, 
                     e.salary AS salary, 
                     e.office_code AS officeCode, 
                     e.reports_to AS reportsTo 
                   FROM 
                     employee e 
                   WHERE 
                     e.job_title = ?1
                   """
            , nativeQuery = true)
    public List<IEmployee> findByJobTitleNative(String jobTitle);
}