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
    public Object[][] fetchOfficesAndEmployeesNr() {

        return classicModelsRepository.findOfficesAndEmployeesNr();
    }

    @Transactional(readOnly = true)
    public Object[][] fetchSalariesGeAvgPlus25000() {

        return classicModelsRepository.findSalariesGeAvgPlus25000();
    }

    @Transactional
    public int insertEmployee() {

        return classicModelsRepository.insertEmployee();
    }

    @Transactional
    public int insertAnotherTableDataInManager() {

        return classicModelsRepository.insertAnotherTableDataInManager();
    }

    @Transactional
    public int deletePaymentsOfAtelierGraphique() {

        return classicModelsRepository.deletePaymentsOfAtelierGraphique();
    }

    @Transactional
    public Object[][] createPaymentViewForCustomerSignalGiftStores() {

        return classicModelsRepository.createPaymentViewForCustomerSignalGiftStores();
    }

    @Transactional(readOnly = true)
    public Object[] fetchBaseSalary() {

        return classicModelsRepository.findBaseSalary();
    }

    @Transactional(readOnly = true)
    public Object[][] fetchUnpaidPayments() {

        return classicModelsRepository.findUnpaidPayments();
    }

    @Transactional(readOnly = true)
    public Object[][] minAndRoundMinInvoiceAmount() {
        
        return classicModelsRepository.minAndRoundMinInvoiceAmount();
    }
    
    @Transactional(readOnly = true)
    public Object[][] maxSalePerFiscalYearAndEmployee() {
        
        return classicModelsRepository.maxSalePerFiscalYearAndEmployee();
    }    
}