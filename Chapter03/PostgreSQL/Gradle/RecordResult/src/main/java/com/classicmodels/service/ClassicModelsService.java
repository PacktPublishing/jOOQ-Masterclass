package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.jooq.Result;
import org.jooq.Record;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

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

    // fetch some records and send them to the controller
    public Result<Record> fetchCustomers() {

        return classicModelsRepository.fetchCustomers();
    }
}
