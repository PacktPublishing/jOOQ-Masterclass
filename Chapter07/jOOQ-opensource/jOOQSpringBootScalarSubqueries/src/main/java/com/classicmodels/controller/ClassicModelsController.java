package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }
/*
    @GetMapping("/fetchOfficesAndEmployeesNr")
    public Object[][] fetchOfficesAndEmployeesNr() {

        return classicModelsService.fetchOfficesAndEmployeesNr();
    }

    @GetMapping("/fetchSalariesGeAvgPlus25000")
    public void fetchSalariesGeAvgPlus25000() {

        classicModelsService.fetchSalariesGeAvgPlus25000();
    }

    @GetMapping("/insertEmployee")
    public int insertEmployee() {

        return classicModelsService.insertEmployee();
    }

    @GetMapping("/insertAnotherTableDataInManager")
    public int insertAnotherTableDataInManager() {

        return classicModelsService.insertAnotherTableDataInManager();
    }

    @GetMapping("/deletePaymentsOfAtelierGraphique")
    public int deletePaymentsOfAtelierGraphique() {

        return classicModelsService.deletePaymentsOfAtelierGraphique();
    }

    @GetMapping("/createPaymentViewForCustomerSignalGiftStores")
    public Object[][] createPaymentViewForCustomerSignalGiftStores() {

        return classicModelsService.createPaymentViewForCustomerSignalGiftStores();
    }

    @GetMapping("/fetchBaseSalary")
    public Object[] fetchBaseSalary() {

        return classicModelsService.fetchBaseSalary();
    }

    @GetMapping("/fetchUnpaidPayments")
    public Object[][] fetchUnpaidPayments() {

        return classicModelsService.fetchUnpaidPayments();
    }

    @GetMapping("/minAndRoundMinInvoiceAmount")
    public Object[][] minAndRoundMinInvoiceAmount() {

        return classicModelsService.minAndRoundMinInvoiceAmount();
    }
    
    @GetMapping("/maxSalePerFiscalYearAndEmployee")
    public Object[][] maxSalePerFiscalYearAndEmployee() {

        return classicModelsService.maxSalePerFiscalYearAndEmployee();
    }    
*/
}
