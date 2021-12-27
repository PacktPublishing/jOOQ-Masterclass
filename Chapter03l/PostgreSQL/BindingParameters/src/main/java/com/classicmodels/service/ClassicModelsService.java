package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }
    
    public void callAll() {

        ////////////////////////
        /* Indexed parameters */
        ////////////////////////
        classicModelsRepository.hardCodedValuesAsIndexedParams();
        classicModelsRepository.userInputValuesAsIndexedParams(5000, "Sales Rep");
        classicModelsRepository.usingValExplicitly1();
        classicModelsRepository.usingValExplicitly2(LocalDateTime.now());
        classicModelsRepository.usingValExplicitly3();
        classicModelsRepository.usingValExplicitly4(0.15f);
        classicModelsRepository.usingValExplicitly5();
        classicModelsRepository.usingValExplicitly6(75000);
        classicModelsRepository.plainSQLHardCodedValues();
        classicModelsRepository.plainSQLUserInputValues(5000, "Sales Rep");
        classicModelsRepository.extractBindValuesIndexedParams();
        classicModelsRepository.extractBindValueIndexedParams();
        classicModelsRepository.modifyingTheBindValueIndexedParam1();
        classicModelsRepository.modifyingTheBindValueIndexedParam2();

        //////////////////////
        /* Named parameters */
        //////////////////////
        classicModelsRepository.hardCodedValuesAsNamedParams();
        classicModelsRepository.userInputValuesAsNamedParams(5000, "Sales Rep");
        classicModelsRepository.namedParameterNoInitialValueOrType();
        classicModelsRepository.namedParameterWithClassTypeNoInitialValue();
        classicModelsRepository.namedParameterWithDataTypeNoInitialValue();
        classicModelsRepository.namedParameterWithFieldTypeNoInitialValue();
        classicModelsRepository.unnamedParameterWithClassTypeNoInitialValue();
        classicModelsRepository.unnamedParameterWithDataTypeNoInitialValue();
        classicModelsRepository.unnamedParameterWithFieldTypeNoInitialValue();
        classicModelsRepository.namedParameterWithTypeAndInitialValue();
        classicModelsRepository.extractBindValuesNamedParams();
        classicModelsRepository.extractBindValueNamedParams();
        classicModelsRepository.modifyingTheBindValueNamedParam1();
        classicModelsRepository.modifyingTheBindValueNamedParam2();

        ///////////////////////
        /* Inline parameters */
        ///////////////////////
        classicModelsRepository.hardCodedValuesAsInlineParams();
        classicModelsRepository.userInputValuesAsInlineParams(5000, "Sales Rep");
        classicModelsRepository.inlineParamsViaSettings();
        classicModelsRepository.extractBindValueInlineParams();
        classicModelsRepository.modifyingTheBindValueInlineParam1();
        classicModelsRepository.modifyingTheBindValueInlineParam2();

        // All parameters
        classicModelsRepository.viaResultQueryGetSQL();
    }

}
