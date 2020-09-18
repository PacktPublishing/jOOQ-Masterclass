package com.classicmodels.repository;

import java.util.List;
import jooq.generated.tables.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends 
        JpaRepository<com.classicmodels.entity.Employee, Long>, QueryRepository {
    
    /* This doesn't work! */
    /*
    @Query("SELECT e.email AS email, e.extension AS extension, "
            + "e.firstName AS firstName, e.lastName AS lastName, "
            + "e.salary AS salary FROM Employee e WHERE e.jobTitle=?1")
    public List<jooq.generated.tables.pojos.Employee> findByJobTitle1(String jobTitle);
    */
    
    /* This doesn't work! */
    /*
    @Query("SELECT new jooq.generated.tables.pojos.Employee("
            + "e.employeeNumber, e.email, e.extension, e.firstName, "
            + "e.jobTitle, e.lastName, e.salary, e.officeCode, e.reportsTo) "
            + "FROM Employee e WHERE e.jobTitle=?1")
    public List<Employee> findByJobTitle2(String jobTitle);
    */
}