package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import jooq.generated.tables.pojos.BankTransaction;
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
    public List<BankTransaction> loadAllBankTransactionOfCertainPayment() {

        return classicModelsService.loadAllBankTransactionOfCertainPayment();
    }

    @GetMapping(path = "/transaction/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BankTransaction loadBankTransaction(@PathVariable(name = "id") Long id) {

        return classicModelsService.loadBankTransaction(id);
    }

    @PostMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BankTransaction updateBankTransaction(@RequestBody BankTransaction bt) {

        classicModelsService.updateBankTransaction(bt);

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
