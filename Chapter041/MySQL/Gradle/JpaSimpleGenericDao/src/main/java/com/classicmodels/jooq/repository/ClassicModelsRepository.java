package com.classicmodels.jooq.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ClassicModelsRepository<T, ID> {

    List<T> fetchAll();

    @Transactional
    void deleteById(ID id);
}
