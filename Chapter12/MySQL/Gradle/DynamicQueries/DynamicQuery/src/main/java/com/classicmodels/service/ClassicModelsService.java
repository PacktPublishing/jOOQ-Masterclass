package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.math.BigDecimal;
import java.util.List;
import jooq.generated.tables.records.BankTransactionRecord;
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

        List<ProductRecord> r1 = classicModelsRepository.fetchCarsOrNoCars(0, false);
        System.out.println("EXAMPLE 1\n" + r1);
                
        List<EmployeeRecord> r2 = classicModelsRepository.fetchEmployees1(true);
        System.out.println("EXAMPLE 2\n" + r2);
        
        List<BankTransactionRecord> r3 = classicModelsRepository.fetchBankTransactions("SUCCESS");
        System.out.println("EXAMPLE 3\n" + r3);

        List<EmployeeRecord> r4 = classicModelsRepository.fetchEmployees2(false);
        System.out.println("EXAMPLE 4\n" + r4);

        List<ProductRecord> r5 = classicModelsRepository.fetchProducts(50, 90);
        System.out.println("EXAMPLE 5\n" + r5);

        List<ProductRecord> r6 = classicModelsRepository.findProductsWithConditions1(BigDecimal.valueOf(35), BigDecimal.valueOf(55), null, "1:10");
        System.out.println("EXAMPLE 6\n" + r6);

        List<ProductRecord> r7 = classicModelsRepository.findProductsWithConditions2(BigDecimal.valueOf(25), BigDecimal.valueOf(65), "Highway 66 Mini Classics", null);
        System.out.println("EXAMPLE 7\n" + r7);

        List<Record> r8 = classicModelsRepository.appendTwoJoins(true, true);
        System.out.println("EXAMPLE 8\n" + r8);
        
        List<Record> r9 = classicModelsRepository.decomposeSelectAndFrom();
        System.out.println("EXAMPLE 9\n" + r9);

        List<CustomerRecord> r10 = classicModelsRepository.classifyCustomerPayments(
                new Clazz(0, 1), new Clazz(2, 2), new Clazz(3, 5), new Clazz(6, 15));
        System.out.println("EXAMPLE 10\n" + r10);

        long r11 = classicModelsRepository.insertClassicCar("1972 Alfa Romeo GTA", "Motor City Art Classics", "1:10", true);
        System.out.println("EXAMPLE 11\n" + r11);

        int r12 = classicModelsRepository.updateProduct(38.25f, 0.25f);
        System.out.println("EXAMPLE 12\n" + r12);

        int r13 = classicModelsRepository.deleteSale(2004, 5640.99);
        System.out.println("EXAMPLE 13\n" + r13);
    }
}
