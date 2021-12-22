package com.classicmodels.repository;

import com.classicmodels.pojo.EmployeeNoCntr;
import java.util.List;
import static jooq.generated.tables.Employee.EMPLOYEE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.firstValue;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepositoryImpl implements ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;               
    }

    @Override
    public List<EmployeeNoCntr> findEmployeesAndLeastSalary() {        
        
        ctx.configuration().settings()
                 .withRenderNameCase​(org.jooq.conf.RenderNameCase.LOWER);
        
        List<EmployeeNoCntr> result = ctx.select(EMPLOYEE.FIRST_NAME,
                EMPLOYEE.LAST_NAME,
                EMPLOYEE.SALARY,
                firstValue(EMPLOYEE.FIRST_NAME)
                        .over().orderBy(EMPLOYEE.SALARY).as("least_salary"))
                .from(EMPLOYEE)
                .fetchInto(EmployeeNoCntr.class);

        return result;
    }
}
