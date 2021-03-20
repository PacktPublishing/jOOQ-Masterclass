package com.classicmodels.service;

import com.classicmodels.pojo.Office;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ClassicModelsServiceTest {

    @Autowired
    private ClassicModelsService classicModelsService;

    @Test
    void name() {
        System.out.println("Fetching offices from 'NA' territory:");
        List<Office> offices = classicModelsService.fetchOfficesInTerritory("NA");
        System.out.println(offices);
    }
}