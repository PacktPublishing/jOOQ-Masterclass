package com.classicmodels.repository;

import com.classicmodels.model.ProductLine;
import java.util.List;

public interface ClassicModelsRepository {
    
    public List<ProductLine> findProductLineJooq();    
    public void updateProductLineDescriptionJooq();
}
