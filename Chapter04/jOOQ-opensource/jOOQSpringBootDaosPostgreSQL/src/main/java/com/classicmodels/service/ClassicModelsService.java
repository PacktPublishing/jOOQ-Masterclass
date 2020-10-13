package com.classicmodels.service;

import com.classicmodels.pojo.CustomerAndOrder;
import com.classicmodels.repository.ClassicModelsRepository;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.daos.ManagerRepository;
import jooq.generated.tables.daos.OrderRepository;
import jooq.generated.tables.pojos.JooqManager;
import jooq.generated.tables.pojos.JooqOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    /* our repository */
    private final ClassicModelsRepository classicModelsRepository;
    
    /* jOOQ generated repositories */
    private final ManagerRepository managerRepository;
    private final OrderRepository orderRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository,
            ManagerRepository managerRepository, OrderRepository orderRepository) {
        this.classicModelsRepository = classicModelsRepository;
        this.managerRepository = managerRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public JooqManager fetchManager(Long managerId) {

        return managerRepository.fetchOneByManagerId(managerId);
    }

    @Transactional(readOnly = true)
    public List<JooqOrder> fetchOrdersByRequiredDate(LocalDate startDate, LocalDate endDate) {

        return orderRepository.fetchRangeOfRequiredDate(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<CustomerAndOrder> fetchCustomersAndOrders() {

        return classicModelsRepository.findCustomersAndOrders();
    }

}