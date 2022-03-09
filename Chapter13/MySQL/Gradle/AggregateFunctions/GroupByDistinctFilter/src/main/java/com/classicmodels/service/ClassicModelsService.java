package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void callAll() {

       classicModelsRepository.officeHavingLessThen3Employees();
       classicModelsRepository.employeeHavingLargestNumberOfSales();
       classicModelsRepository.orderdetailsQuantityOufOfRange2040();
       classicModelsRepository.avgOfSumOfSales();
       classicModelsRepository.groupByExtractedMonth();       
       classicModelsRepository.exactDivisionOrderdetailTop3Product();       
       classicModelsRepository.employeeSalary();
       classicModelsRepository.avgSumProduct();
       classicModelsRepository.pivotViaFilter();
       classicModelsRepository.filterInAggWindowFunction();       
       classicModelsRepository.filterInOrderedSetAggregateFunction();
       classicModelsRepository.minMaxBuyPriceProduct();
       classicModelsRepository.firstNSalaries();
       classicModelsRepository.medianViaGroupByAndAvgDistinct();
       classicModelsRepository.countDistinctSalePerFiscalYear();
       classicModelsRepository.sumAvgOrder();        
    }
}
