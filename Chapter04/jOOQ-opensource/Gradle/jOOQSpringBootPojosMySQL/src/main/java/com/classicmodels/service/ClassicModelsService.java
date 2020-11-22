package com.classicmodels.service;

import com.classicmodels.pojo.CustomerAndOrder;
import com.classicmodels.repository.ClassicModelsRepository;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.pojos.JooqOffice;
import jooq.generated.tables.pojos.JooqOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public List<JooqOffice> fetchOfficesInTerritory(String territory) {

        return classicModelsRepository.findOfficesInTerritory(territory);
    }

    @Transactional(readOnly = true)
    public List<JooqOrder> fetchOrdersByRequiredDate(LocalDate startDate, LocalDate endDate) {

        return classicModelsRepository.findOrdersByRequiredDate(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<CustomerAndOrder> fetchCustomersAndOrders() {

        return classicModelsRepository.findCustomersAndOrders();
    }

}
