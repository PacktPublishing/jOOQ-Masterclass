package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import jooq.generated.tables.pojos.BankTransaction;
import org.jooq.JSONFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    // load all bank transactions of a certain payment (333/NF959653)
    @GetMapping(path = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public String loadAllBankTransactionOfCertainPayment() {

        return classicModelsService.loadAllBankTransactionOfCertainPayment().formatJSON();
    }

    @GetMapping(path = "/transaction/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String loadBankTransaction(@PathVariable(name = "id") Long id) {

        return classicModelsService.loadBankTransaction(id).formatJSON(JSONFormat.DEFAULT_FOR_RESULTS);
    }

    @PostMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateBankTransaction(@RequestBody String bt) {

        System.out.println("qqqqqqqqqqq="+bt);
        
        int stored = classicModelsService.insertOrUpdateBankTransaction(bt);
        System.out.println("ddddddddd="+stored);

        return bt;
    }

    @PostMapping(path = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BankTransaction newBankTransaction(@RequestBody BankTransaction bt) {

        classicModelsService.newBankTransaction(bt);

        return bt;
    }

    @PostMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BankTransaction deleteBankTransaction(@RequestBody BankTransaction bt) {

        classicModelsService.deleteBankTransaction(bt);

        return bt;
    }
}
