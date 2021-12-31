package com.classicmodels.repository;

import java.util.List;
import org.jooq.TableRecord;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ClassicModelsRepository<R extends TableRecord<R>, P, T> {

    List<P> fetchAll();

    @Transactional
    void deleteById(T id);
}
