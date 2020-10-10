package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import com.classicmodels.pojo.DelayedPayment;
import com.classicmodels.pojo.Manager;
import com.classicmodels.pojo.CustomerCachingDate;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public Manager fetchManager(Long managerId) {

        return classicModelsRepository.findManager(managerId);
    }

    @Transactional(readOnly = true)
    public List<CustomerCachingDate> fetchCustomerCachingDate() {

        return classicModelsRepository.findCustomerCachingDate();
    }

    @Transactional(readOnly = true)
    public List<DelayedPayment> fetchDelayedPayments(
            LocalDate startDate, LocalDate endDate) {

        return classicModelsRepository.findDelayedPayments(startDate, endDate);
    }
}
