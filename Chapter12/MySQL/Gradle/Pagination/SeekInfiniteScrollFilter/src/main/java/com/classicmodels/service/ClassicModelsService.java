package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.math.BigDecimal;
import java.util.List;
import jooq.generated.tables.pojos.Orderdetail;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public List<Orderdetail> loadOrderdetailPageAsc(long orderdetailId, int size,
            BigDecimal priceEach, Integer quantityOrdered) {

        return classicModelsRepository.fetchOrderdetailPageAsc(orderdetailId, size, 
                priceEach, quantityOrdered);
    }
}
