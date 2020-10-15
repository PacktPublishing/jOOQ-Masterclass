package com.classicmodels.repository;

import com.classicmodels.entity.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository
        extends JpaRepository<Employee, Long>, ClassicModelsRepository {

    @Query(value = """
                 SELECT 
                   `classicmodels`.`employee`.`employee_number`, 
                   `classicmodels`.`employee`.`last_name`, 
                   `classicmodels`.`employee`.`first_name`, 
                   `classicmodels`.`employee`.`extension`, 
                   `classicmodels`.`employee`.`email`, 
                   `classicmodels`.`employee`.`office_code`, 
                   `classicmodels`.`employee`.`salary`, 
                   `classicmodels`.`employee`.`reports_to`, 
                   `classicmodels`.`employee`.`job_title` 
                 FROM 
                   `classicmodels`.`employee` 
                 WHERE
                   `classicmodels`.`employee`.`office_code` = (
                     SELECT 
                       `classicmodels`.`office`.`office_code` 
                     FROM 
                       `classicmodels`.`office` 
                     WHERE
                       `classicmodels`.`office`.`city` = ?1
                   )
                 """, nativeQuery = true)
    public List<Employee> findEmployeeInCity(String city);   
}