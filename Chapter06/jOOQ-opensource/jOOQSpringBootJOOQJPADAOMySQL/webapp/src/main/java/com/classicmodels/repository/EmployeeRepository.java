package com.classicmodels.repository;

import com.classicmodels.entity.Employee;
import java.util.List;
import jooq.generated.tables.pojos.Customer;
import org.jooq.exception.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface EmployeeRepository extends
        JpaRepository<com.classicmodels.entity.Employee, Long>, ClassicModelsRepository,
         jooq.generated.tables.daos.EmployeeRepository{
               
    List<com.classicmodels.entity.Employee> findByJobTitle(String jobTitle);
}