package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.OrderRecord;
import jooq.generated.tables.records.OrderdetailRecord;
import jooq.generated.tables.records.ProductRecord;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }
    
    public List<Tuple2<EmployeeRecord, List<Tuple2<CustomerRecord, 
            List<Tuple2<OrderRecord, List<Tuple2<OrderdetailRecord, ProductRecord>>>>>>>> 
        fetchEmployeeWithCustomersOrdersByOfficeCode(String officeCode) {

        return classicModelsRepository.findEmployeeWithCustomersOrdersByOfficeCode(officeCode);
    }

}
