package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public void callAll() {

        /* casting examples */
        classicModelsRepository.printOfficeByCode1(1);
        classicModelsRepository.printPaymentDatesByAmount1(14571.44f);
        // 2004-12-17 12:30:15
        classicModelsRepository.printOrderStatusByOrderDate1(LocalDateTime.of(2004, 12, 17, 12, 30, 15));
        classicModelsRepository.printProductNameAndPrice1();

        /* coercing examples */
        classicModelsRepository.printOfficeByCode2(1);
        classicModelsRepository.printPaymentDatesByAmount2(14571.44f);
        // 2004-12-17 12:30:15
        classicModelsRepository.printOrderStatusByOrderDate2(LocalDateTime.of(2004, 12, 17, 12, 30, 15));
        classicModelsRepository.printProductNameAndPrice2();

        /* cast vs coerce */
        classicModelsRepository.printPaymentDatesByAmountCast(14571.44f);
        classicModelsRepository.printPaymentDatesByAmountCoerce(14571.44f);

        // 2003-04-09
        classicModelsRepository.printInvoicesPerDayCoerce(LocalDate.of(2003, 4, 9));
        classicModelsRepository.printInvoicesPerDayCast(LocalDate.of(2003, 4, 9));

        /* collation */
        classicModelsRepository.printProductsName();
    }

}