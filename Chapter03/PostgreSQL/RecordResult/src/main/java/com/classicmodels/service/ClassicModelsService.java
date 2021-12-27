package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public void callAll() {

        classicModelsRepository.nonTypesafePlainSQL();
        classicModelsRepository.nonTypesafeSelect();
        classicModelsRepository.typesafeSelectIntoTableRecord();
        classicModelsRepository.typesafeSelectIntoClass();
        classicModelsRepository.nonTypesafeSelectJoin();
        classicModelsRepository.typesafeSelectJoin();
        classicModelsRepository.typesafeSelectFrom();
        classicModelsRepository.typesafeAdHocSelect();
        classicModelsRepository.typesafeAdHocSelectIntoTableRecord();
        classicModelsRepository.typesafeAdHocSelectJoin();
        classicModelsRepository.typesafeUdtType();
        classicModelsRepository.typesafeUdtTypeIntoTableRecord();
        classicModelsRepository.beyondDegree22();
    }
}
