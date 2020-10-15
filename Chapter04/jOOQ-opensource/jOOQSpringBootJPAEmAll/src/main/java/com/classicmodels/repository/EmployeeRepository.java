package com.classicmodels.repository;

import com.classicmodels.entity.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository 
        extends JpaRepository<Employee, Long>, ClassicModelsRepository {
           
    public List<Employee> findByJobTitle(String jobTitle);
}