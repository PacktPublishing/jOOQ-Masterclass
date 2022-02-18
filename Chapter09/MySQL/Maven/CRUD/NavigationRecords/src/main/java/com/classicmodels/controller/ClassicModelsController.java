package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.DepartmentRecord;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.OfficeRecord;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClassicModelsController {

    protected static final String DEPARTMENTS_ATTR = "departments";
    protected static final String OFFICE_ATTR = "office";
    protected static final String EMPLOYEES_ATTR = "employees";
    protected static final String CUSTOMERS_ATTR = "customers";
    protected static final String CUSTOMERDETAIL_ATTR = "customerdetail";

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/departments")
    public String loadDepartments(Model model) {

        model.addAttribute(DEPARTMENTS_ATTR,
                classicModelsService.loadDepartments());

        return "departments";
    }

    @PostMapping("/office")
    public String loadOfficeOfDepartment(DepartmentRecord dr,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(OFFICE_ATTR,
                classicModelsService.loadOfficeOfDepartment(dr));

        return "redirect:office";
    }

    @PostMapping("/employees")
    public String loadEmployeesOfOffice(RedirectAttributes redirectAttributes, OfficeRecord or) {

        redirectAttributes.addFlashAttribute(EMPLOYEES_ATTR,
                classicModelsService.loadEmployeeOfOffice(or));

        return "redirect:employees";
    }

    @PostMapping("/customers")
    public String loadCustomersOfEmployee(RedirectAttributes redirectAttributes, EmployeeRecord er) {

        redirectAttributes.addFlashAttribute(CUSTOMERS_ATTR,
                classicModelsService.loadCustomersOfEmployee(er));

        return "redirect:customers";
    }

    @PostMapping("/customerdetail")
    public String loadCustomerdetailOfCustomers(RedirectAttributes redirectAttributes, CustomerRecord cr) {

        redirectAttributes.addFlashAttribute(CUSTOMERDETAIL_ATTR,
                classicModelsService.loadCustomerdetailOfCustomer(cr));

        return "redirect:customerdetail";
    }

    @GetMapping("/office")
    public String getOfficePage() {

        return "office";
    }
    
    @GetMapping("/employees")
    public String getEmployeesPage() {

        return "employees";
    }
    
    @GetMapping("/customers")
    public String getCustomersPage() {

        return "customers";
    }
    
    @GetMapping("/customerdetail")
    public String getCustomerdetailPage() {

        return "customerdetail";
    }
}
