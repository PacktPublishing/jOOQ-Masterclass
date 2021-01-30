package com.classicmodels.repository;

import java.util.concurrent.CompletableFuture;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Async
    public CompletableFuture<String> fetchManagersAsync() {

        return ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME)
                .from(MANAGER)
                .fetchAsync()
                .thenApply(rs -> rs.formatHTML())
                .toCompletableFuture();
    }
    
    @Async
    public CompletableFuture<String> fetchOfficesAsync() {

        return ctx.selectFrom(OFFICE)        
                .fetchAsync()
                .thenApply(rs -> rs.formatHTML())
                .toCompletableFuture();
    }
    
    @Async
    public CompletableFuture<String> fetchEmployeesAsync() {

        return ctx.select(EMPLOYEE.OFFICE_CODE, EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY)  
                .from(EMPLOYEE)
                .fetchAsync()
                .thenApply(rs -> rs.formatHTML())
                .toCompletableFuture();
    }
}
