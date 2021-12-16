package com.classicmodels.repository;

import com.classicmodels.entity.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface EmployeeRepository extends
        JpaRepository<com.classicmodels.entity.Employee, Long>, ClassicModelsRepository {
    
    List<Employee> findByJobTitle(String jobTitle);
}