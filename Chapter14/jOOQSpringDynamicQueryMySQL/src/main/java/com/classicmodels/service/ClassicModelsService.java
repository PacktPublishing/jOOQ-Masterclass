package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.math.BigDecimal;
import java.util.List;
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.ProductRecord;
import org.springframework.stereotype.Service;
import org.jooq.Record;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void callAll() {

        List<EmployeeRecord> r1 = classicModelsRepository.toggleComparisonPredicates1(true);
        System.out.println("EXAMPLE 1\n" + r1);

        List<EmployeeRecord> r2 = classicModelsRepository.toggleComparisonPredicates2(false);
        System.out.println("EXAMPLE 2\n" + r2);

        List<ProductRecord> r3 = classicModelsRepository.toggleComparisonPredicates3(50, 90);
        System.out.println("EXAMPLE 3\n" + r3);

        List<ProductRecord> r4 = classicModelsRepository.findProductsWithConditions1(BigDecimal.valueOf(35), BigDecimal.valueOf(55), null, "1:10");
        System.out.println("EXAMPLE 4\n" + r4);

        List<ProductRecord> r5 = classicModelsRepository.findProductsWithConditions2(BigDecimal.valueOf(25), BigDecimal.valueOf(65), "Highway 66 Mini Classics", null);
        System.out.println("EXAMPLE 5\n" + r5);

        List<Record> r6 = classicModelsRepository.appendTwoJoins(true, true);
        System.out.println("EXAMPLE 6\n" + r6);

        List<CustomerRecord> r7 = classicModelsRepository.unionQueries(
                new Clazz(0, 1), new Clazz(2, 2), new Clazz(3, 5), new Clazz(6, 15));
        System.out.println("EXAMPLE 7\n" + r7);

        long r8 = classicModelsRepository.insertValues("z", "t", "1:10", true);
        System.out.println("EXAMPLE 8\n" + r8);

        int r9 = classicModelsRepository.updateValues(38.25f, 0.25f);
        System.out.println("EXAMPLE 9\n" + r9);

        int r10 = classicModelsRepository.deleteValues(2004, 5640.99);
        System.out.println("EXAMPLE 10\n" + r10);
    }
}
