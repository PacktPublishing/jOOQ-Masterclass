package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jooq.generated.tables.records.PaymentRecord;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClassicModelsController {

    protected static final String ALL_PAYMENT_ATTR = "all";
    protected static final String PAYMENT_ATTR = "payment";

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    // load all payments for customer 103
    @GetMapping("/payments")
    public String loadAllPayment(Model model) {

        model.addAttribute(ALL_PAYMENT_ATTR,
                classicModelsService.loadAllPayment103());

        model.addAttribute(PAYMENT_ATTR,
                new PaymentRecord(103L, null, null, BigDecimal.ONE, LocalDateTime.now(), null, null));

        return "payments";
    }

    @PostMapping("/merge")
    public String mergePayment(PaymentRecord pr) {

        classicModelsService.mergePayment(pr);

        return "redirect:payments";
    }

    @InitBinder
    void allowFields(WebDataBinder webDataBinder) {
        webDataBinder.setAllowedFields("checkNumber", "customerNumber",
                "invoiceAmount", "cachingDate", "${_csrf.parameterName}");
    }
}
