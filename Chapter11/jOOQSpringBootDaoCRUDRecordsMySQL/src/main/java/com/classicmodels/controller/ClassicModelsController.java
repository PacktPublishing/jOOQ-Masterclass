package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import jooq.generated.tables.pojos.BankTransaction;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes({ClassicModelsController.BANK_TRANSACTION_ATTR})
public class ClassicModelsController {

    protected static final String ALL_BANK_TRANSACTION_ATTR = "all";
    protected static final String BANK_TRANSACTION_ATTR = "bt";
    protected static final String NEW_BANK_TRANSACTION_ATTR = "nbt";
    protected static final String DELETE_OR_UPDATE_BANK_TRANSACTION_ATTR = "dubt";

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    // load all bank transactions of a certain payment (333/NF959653)
    @GetMapping("/all")
    public String fetchAllBankTransactionOfCertainPayment(SessionStatus sessionStatus, Model model) {

        sessionStatus.setComplete();

        model.addAttribute(ALL_BANK_TRANSACTION_ATTR,
                classicModelsService.fetchAllBankTransactionOfCertainPayment());

        return "transactions";
    }

    // load the bank transaction to be edited and start edit the bank name    
    @GetMapping("/editbankname/{id}")
    public String fetchBankTransaction(@PathVariable(name = "id") Long id, Model model) {

        model.addAttribute(BANK_TRANSACTION_ATTR, classicModelsService.fetchBankTransaction(id));

        return "redirect:/editbankname";
    }

    @PostMapping("/name")
    public String editBankName(@ModelAttribute(BANK_TRANSACTION_ATTR) BankTransaction bt) {

        return "redirect:editbankiban";
    }

    @PostMapping("/iban")
    public String editBankIban(@ModelAttribute(BANK_TRANSACTION_ATTR) BankTransaction bt) {

        return "redirect:editbanktransfer";
    }

    @PostMapping("/transfer")
    public String updateBankTransfer(@ModelAttribute(BANK_TRANSACTION_ATTR) BankTransaction bt,
            RedirectAttributes redirectAttributes, SessionStatus sessionStatus) {

        classicModelsService.updateBankTransaction(bt);
        redirectAttributes.addFlashAttribute(DELETE_OR_UPDATE_BANK_TRANSACTION_ATTR, bt);

        sessionStatus.setComplete();

        return "redirect:success";
    }

    // insert a new transaction                        
    @GetMapping("/newbanktransaction")
    public String newBankTransaction(Model model) {

        model.addAttribute(NEW_BANK_TRANSACTION_ATTR, new BankTransaction());

        return "newtransaction";
    }

    @PostMapping("/new")
    public String newBank(@ModelAttribute BankTransaction btr,
            RedirectAttributes redirectAttributes) {

        btr.setCustomerNumber(333L);
        btr.setCheckNumber("NF959653");

        classicModelsService.newBankTransaction(btr);
        redirectAttributes.addFlashAttribute(NEW_BANK_TRANSACTION_ATTR, btr);

        return "redirect:all";
    }

    // delete transaction        
    @GetMapping("/delete")
    public String deleteBankTransaction(SessionStatus sessionStatus, Model model,
            RedirectAttributes redirectAttributes) {

        BankTransaction btr = (BankTransaction) model.getAttribute(BANK_TRANSACTION_ATTR);

        if (btr != null) {
            classicModelsService.deleteBankTransaction(btr);
        }

        sessionStatus.setComplete();

        redirectAttributes.addFlashAttribute(DELETE_OR_UPDATE_BANK_TRANSACTION_ATTR, btr);

        return "redirect:/success";
    }

    @GetMapping("/cancel")
    public String cancel(SessionStatus sessionStatus) {

        sessionStatus.setComplete();

        return "redirect:all";
    }

    @GetMapping("/reset/{page}")
    public String refresh(@PathVariable(name = "page") String page, Model model) {

        BankTransaction btr = (BankTransaction) model.getAttribute(BANK_TRANSACTION_ATTR);

        if (btr != null) {
            model.addAttribute(BANK_TRANSACTION_ATTR, 
                    classicModelsService.fetchBankTransaction(btr.getTransactionId()));
        }

        return "redirect:/" + page;
    }

    @GetMapping(value = "/")
    public String indexPage(SessionStatus sessionStatus) {

        sessionStatus.setComplete();

        return "index";
    }

    @GetMapping(value = "/success")
    public String successPage() {
        return "success";
    }

    @GetMapping(value = "/editbankname")
    public String editBankNamePage() {
        return "editbankname";
    }

    @GetMapping(value = "/editbankiban")
    public String editBankIbanPage() {
        return "editbankiban";
    }

    @GetMapping(value = "/editbanktransfer")
    public String editBankTransferPage() {
        return "editbanktransfer";
    }

    @InitBinder
    void allowFields(WebDataBinder webDataBinder) {
        webDataBinder.setAllowedFields("bankName", "bankIban", "uin",
                "transferAmount", "${_csrf.parameterName}");
    }
}
