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
        classicModelsRepository.insertPaymentOnDuplicateKeyIgnore();                   // EXAMPLE 1
        classicModelsRepository.insertPaymentOnConflictDoNothing();                    // EXAMPLE 2   
        classicModelsRepository.insertPaymentOnDuplicateCheckNumberDoNothing();        // EXAMPLE 3
        classicModelsRepository.insertPaymentOnConflictOnConstraintDoNothing();        // EXAMPLE 4
        classicModelsRepository.insertPaymentOnDuplicateKeyUpdateIt();                 // EXAMPLE 5
        classicModelsRepository.insertPaymentOnConflictUpdateIt();                     // EXAMPLE 6
        classicModelsRepository.insertPaymentRecordOnDuplicateKeyUpdateIt();           // EXAMPLE 7
        classicModelsRepository.insertPaymentRecordOnConflictUpdateIt();               // EXAMPLE 8
        classicModelsRepository.updateProductNameElseInsertProduct();                  // EXAMPLE 9
        classicModelsRepository.updateCustomerFirstNameElseInsertCustomer();           // EXAMPLE 10
        classicModelsRepository.updateSaleElseInsertSale();                            // EXAMPLE 11
        classicModelsRepository.deleteSaleElseInsertSale();                            // EXAMPLE 12 
        classicModelsRepository.deleteNegativeSaleElseInsertSaleAsDoubleCommission();  // EXAMPLE 13
        classicModelsRepository.deleteSalesOfEmpoyeeSalaryLt65000ElseInsert();         // EXAMPLE 14
        classicModelsRepository.updateSaleThenDeleteViaWhenMatchedAnd();               // EXAMPLE 15 
        classicModelsRepository.updateSaleThenDeleteViaDeleteWhere();                  // EXAMPLE 16
        classicModelsRepository.updateSaleBeforeDelete();                              // EXAMPLE 17
        classicModelsRepository.deleteSaleBeforeUpdate();                              // EXAMPLE 18   
    }
}
