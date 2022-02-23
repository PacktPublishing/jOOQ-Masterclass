package com.classicmodels.repository;

import jooq.generated.Keys;
import static jooq.generated.tables.Department.DEPARTMENT;
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.CustomerdetailRecord;
import jooq.generated.tables.records.DepartmentRecord;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.OfficeRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Result<DepartmentRecord> fetchDepartments() {

        return ctx.selectFrom(DEPARTMENT)
                .fetch();
    }

    public OfficeRecord fetchOfficeOfDepartment(DepartmentRecord dr) {

        ctx.attach(dr);

        return dr.fetchParent(Keys.DEPARTMENT_OFFICE_FK);
    }

    public Result<EmployeeRecord> fetchEmployeesOfOffice(OfficeRecord or) {

        ctx.attach(or);

        return or.fetchChildren(Keys.EMPLOYEE_OFFICE_FK);
    }

    public Result<CustomerRecord> fetchCustomersOfEmployee(EmployeeRecord er) {

        ctx.attach(er);

        return er.fetchChildren(Keys.CUSTOMER_EMPLOYEE_FK);
    }

    public CustomerdetailRecord fetchCustomerdetailOfCustomer(CustomerRecord cr) {

        ctx.attach(cr);

        return cr.fetchChild(Keys.CUSTOMERDETAIL_CUSTOMER_FK);
    }
}
