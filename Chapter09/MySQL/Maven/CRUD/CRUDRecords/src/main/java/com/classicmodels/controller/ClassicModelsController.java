package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import jooq.generated.tables.records.BankTransactionRecord;
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
    protected static final String INSERT_DELETE_OR_UPDATE_BANK_TRANSACTION_ATTR = "dubt";

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    // load all bank transactions of a certain payment (333/NF959653)
    @GetMapping("/transactions")
    public String loadAllBankTransactionOfCertainPayment(SessionStatus sessionStatus, Model model) {

        sessionStatus.setComplete();

        model.addAttribute(ALL_BANK_TRANSACTION_ATTR,
                classicModelsService.loadAllBankTransactionOfCertainPayment());

        return "transactions";
    }

    // load the bank transaction to be edited and start edit the bank name    
    @GetMapping("/editbankname/{id}")
    public String loadBankTransaction(@PathVariable(name = "id") Long id, Model model) {

        model.addAttribute(BANK_TRANSACTION_ATTR, classicModelsService.loadBankTransaction(id));

        return "redirect:/editbankname";
    }

    @PostMapping("/name")
    public String editBankName(@ModelAttribute(BANK_TRANSACTION_ATTR) BankTransactionRecord btr) {

        return "redirect:editbankiban";
    }    
    
    @PostMapping("/iban")
    public String editBankIban(@ModelAttribute(BANK_TRANSACTION_ATTR) BankTransactionRecord btr) {

        return "redirect:editcardtype";
    }
    
    @PostMapping("/cardtype")
    public String editCardType(@ModelAttribute(BANK_TRANSACTION_ATTR) BankTransactionRecord btr) {

        return "redirect:editbanktransfer";
    }

    @PostMapping("/transfer")
    public String updateBankTransfer(@ModelAttribute(BANK_TRANSACTION_ATTR) BankTransactionRecord btr,
            RedirectAttributes redirectAttributes, SessionStatus sessionStatus) {

        classicModelsService.updateBankTransaction(btr);
        redirectAttributes.addFlashAttribute(INSERT_DELETE_OR_UPDATE_BANK_TRANSACTION_ATTR, btr);

        sessionStatus.setComplete();

        return "redirect:success";
    }

    // insert a new transaction                        
    @GetMapping("/newbanktransaction")
    public String newBankTransaction(Model model) {

        model.addAttribute(NEW_BANK_TRANSACTION_ATTR, new BankTransactionRecord());

        return "newtransaction";
    }

    @PostMapping("/new")
    public String newBankTransaction(@ModelAttribute BankTransactionRecord btr,
            RedirectAttributes redirectAttributes) {
       
        classicModelsService.newBankTransaction(btr);
        redirectAttributes.addFlashAttribute(INSERT_DELETE_OR_UPDATE_BANK_TRANSACTION_ATTR, btr);

        return "redirect:success";
    }

    // delete transaction        
    @GetMapping("/delete")
    public String deleteBankTransaction(SessionStatus sessionStatus, Model model,
            RedirectAttributes redirectAttributes) {

        if (model.containsAttribute(BANK_TRANSACTION_ATTR)) {

            BankTransactionRecord btr = (BankTransactionRecord) model.getAttribute(BANK_TRANSACTION_ATTR);
            classicModelsService.deleteBankTransaction(btr);
            sessionStatus.setComplete();

            redirectAttributes.addFlashAttribute(INSERT_DELETE_OR_UPDATE_BANK_TRANSACTION_ATTR, btr);

            return "redirect:/success";
        }

        return "redirect:/transactions";
    }

    @GetMapping("/cancel")
    public String cancel(SessionStatus sessionStatus) {

        sessionStatus.setComplete();

        return "redirect:transactions";
    }

    @GetMapping("/reset/{page}")
    public String reset(@PathVariable(name = "page") String page, Model model) {

        if (model.containsAttribute(BANK_TRANSACTION_ATTR)) {
            ((BankTransactionRecord) model.getAttribute(BANK_TRANSACTION_ATTR)).reset();
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
    
    @GetMapping(value = "/editcardtype")
    public String editCardTypePage() {
        return "editcardtype";
    }

    @GetMapping(value = "/editbanktransfer")
    public String editBankTransferPage() {
        return "editbanktransfer";
    }

    @InitBinder
    void allowFields(WebDataBinder webDataBinder) {
        webDataBinder.setAllowedFields("bankName", "bankIban", "uin",
                "transferAmount", "cardType", "${_csrf.parameterName}");
    }
}
