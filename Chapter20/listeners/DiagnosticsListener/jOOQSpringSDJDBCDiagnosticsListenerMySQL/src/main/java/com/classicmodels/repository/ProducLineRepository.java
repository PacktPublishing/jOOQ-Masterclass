package com.classicmodels.repository;

import com.classicmodels.model.ProductLine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ProducLineRepository 
        extends CrudRepository<ProductLine, String>, ClassicModelsRepository {    
}
