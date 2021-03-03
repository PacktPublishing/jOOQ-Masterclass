package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import jooq.generated.tables.records.PaymentRecord;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({ClassicModelsController.PAYMENT_ATTR, ClassicModelsController.ALL_PAYMENT_ATTR})
public class ClassicModelsController {

    protected static final String ALL_PAYMENT_ATTR = "all";
    protected static final String PAYMENT_ATTR = "payment";

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    // load all payments for customer 103
    @GetMapping("/all")
    public String fetchAllPayment(Model model) {

        model.addAttribute(ALL_PAYMENT_ATTR,
                classicModelsService.fetchAllPayment103());

        PaymentRecord pr = new PaymentRecord();
        pr.setCustomerNumber(103L);
        
        model.addAttribute(PAYMENT_ATTR, pr);

        return "payments";
    }

    @GetMapping("/load/{nr}/{ch}")
    public String loadPayment(@PathVariable(name = "nr") Long nr,
            @PathVariable(name = "ch") String ch, Model model) {

        model.addAttribute(PAYMENT_ATTR,
                classicModelsService.loadPayment(nr, ch));

        return "payments";
    }

    @PostMapping("/store")
    public String storePayment(SessionStatus sessionStatus, 
            @ModelAttribute(PAYMENT_ATTR) PaymentRecord pr) {

        classicModelsService.storePayment(pr);

        sessionStatus.setComplete();

        return "redirect:all";
    }

    @GetMapping(value = "/")
    public String indexPage() {

        return "index";
    }

    @InitBinder
    void allowFields(WebDataBinder webDataBinder) {
        webDataBinder.setAllowedFields("checkNumber", "customerNumber",
                "invoiceAmount", "${_csrf.parameterName}");
    }
}
