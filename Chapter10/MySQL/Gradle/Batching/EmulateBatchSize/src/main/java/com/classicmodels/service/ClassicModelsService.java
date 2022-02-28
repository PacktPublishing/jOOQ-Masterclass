package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;


// This code result in:
// Duplicate entry '1' for key 'sale.PRIMARY'

@Service
public class ClassicModelsService {

    private static final int BATCH_SIZE = 3;
    
    private final DSLContext ctx;
    private final List queries;
    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository, DSLContext ctx) {
        this.classicModelsRepository = classicModelsRepository;
        this.ctx = ctx;
        this.queries = List.of(
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(1L, 2000, 1370L, 1282.64, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2001, 1370L, 3938.24, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2002, 1370L, 4676.14, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2003, 1504L, 1222.64, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2004, 1504L, 1938.24, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2005, 1504L, 4446.14, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2006, 1611L, 5748.24, 1, 0.0),
                // contains a duplicate key that causes rollback
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(1L, 2007, 1611L, 2216.14, 1, 0.0)
        );
    }

    public void batchQueriesInOneTransaction() {
        
        classicModelsRepository.cleanSaleTable();
        classicModelsRepository.batchQueriesInOneTransaction();
    }
    
    public void batchQueriesInTransactionPerBatch() {
        
        classicModelsRepository.cleanSaleTable();
        
        int i = 0;
        while ((i + BATCH_SIZE) <= queries.size()) {

            classicModelsRepository.batchQueriesInTransactionPerBatch(
                    queries.subList(i, i + BATCH_SIZE));

            i = i + BATCH_SIZE;            
        }

        classicModelsRepository.batchQueriesInTransactionPerBatch(
                    queries.subList(i, queries.size()));        
    }
}
