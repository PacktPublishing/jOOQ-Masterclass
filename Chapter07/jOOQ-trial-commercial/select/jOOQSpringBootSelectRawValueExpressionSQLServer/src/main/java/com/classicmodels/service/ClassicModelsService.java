package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import static org.jooq.impl.DSL.row;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void callAll() {
        classicModelsRepository.findProductsByVendorScaleAndProductLine();                          // EXAMPLE 1
        classicModelsRepository.findProductByIdVendorAsCarouselDieCastLegendsScaleAndProductLine(); // EXAMPLE 2
        classicModelsRepository.findProductsByVendorScaleAndProductLineIn();                        // EXAMPLE 3
        
        // List<Row3<String, String, String>>
        var list = List.of(row("Min Lin Diecast","1:10", "Motorcycles"), 
                row("Autoart Studio Design","1:12", "Classic Cars"));
        classicModelsRepository.findProductsByVendorScaleAndProductLineInCollection(list);          // EXAMPLE 4
        classicModelsRepository.findProductsByVendorScaleAndProductLineInSelect();                  // EXAMPLE 5
        classicModelsRepository.findOrdersBetweenOrderDateAndShippedDate();                         // EXAMPLE 6
        classicModelsRepository.findOverlappingDates();                                             // EXAMPLE 7        
        classicModelsRepository.findOverlappingSales();                                             // EXAMPLE 8
        classicModelsRepository.findOfficeNullCityAndCountry();                                     // EXAMPLE 9        
    }
}