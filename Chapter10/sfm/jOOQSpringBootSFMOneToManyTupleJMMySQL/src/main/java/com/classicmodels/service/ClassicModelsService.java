package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import jooq.generated.tables.records.ProductRecord;
import jooq.generated.tables.records.ProductlineRecord;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public List<Tuple2<ProductlineRecord, List<ProductRecord>>> fetchProductLineWithProducts() {

        return classicModelsRepository.findProductLineWithProducts();
    }

}