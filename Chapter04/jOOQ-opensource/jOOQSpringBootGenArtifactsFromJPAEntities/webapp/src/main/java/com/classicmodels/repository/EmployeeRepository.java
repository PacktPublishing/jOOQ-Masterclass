package com.classicmodels.repository;

import java.util.List;
import jooq.generated.tables.interfaces.IEmployee;
import jooq.generated.tables.pojos.JooqEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface EmployeeRepository extends
        JpaRepository<com.classicmodels.entity.Employee, Long>, ClassicModelsRepository {

    @Query("SELECT new jooq.generated.tables.pojos.JooqEmployee("
            + "e.employeeNumber, e.email, e.extension, "
            + "e.firstName, e.jobTitle, e.lastName, e.salary, '', -1L) "
            + "FROM Employee e WHERE e.jobTitle=?1")
    public List<JooqEmployee> findByJobTitleCntr(String jobTitle);

    @Query("SELECT e.employeeNumber AS employeeNumber, "
            + "e.email as email, e.extension as extension,"
            + " e.firstName AS firstName, e.jobTitle AS jobTitle,"
            + "  e.lastName AS lastName, e.salary AS salary"
            + " FROM Employee e WHERE e.jobTitle=?1")
    public List<IEmployee> findByJobTitleJpql(String jobTitle);

    @Query(value = "SELECT e.employee_number AS employeeNumber, "
            + "e.email as email, e.extension as extension,"
            + " e.first_name AS firstName, e.job_title AS jobTitle,"
            + "  e.last_name AS lastName, e.salary AS salary, "
            + "e.office_code AS officeCode, e.reports_to AS reportsTo"
            + " FROM employee e WHERE e.job_title=?1", nativeQuery = true)
    public List<IEmployee> findByJobTitleNative(String jobTitle);
}