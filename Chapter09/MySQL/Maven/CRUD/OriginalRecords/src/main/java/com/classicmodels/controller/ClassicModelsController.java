package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import jooq.generated.tables.records.ProductRecord;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({ClassicModelsController.PRODUCT_ATTR})
public class ClassicModelsController {

    protected static final String PRODUCT_ATTR = "product";

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/product/{id}")
    public String loadProduct(@PathVariable(name = "id") Long id, Model model) {

        model.addAttribute(PRODUCT_ATTR, classicModelsService.loadProduct(id));

        return "product";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute(PRODUCT_ATTR) ProductRecord product) {

        System.out.println("Product:\n" + product);

        return "redirect:index";
    }

    @GetMapping("/index")
    public String getIndexPage() {

        return "index";
    }
}
