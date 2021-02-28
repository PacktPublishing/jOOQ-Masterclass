package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping(value = "/text", produces = MediaType.TEXT_PLAIN_VALUE)
    public String formatResultAsText() {

        return classicModelsService.formatResultAsText();
    }

    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public String formatResultAsJSON() {

        return classicModelsService.formatResultAsJSON();
    }

    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String formatResultAsXML() {

        return classicModelsService.formatResultAsXML();
    }

    @GetMapping(value = "/html", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
    public String formatResultAsHTML() {

        return classicModelsService.formatResultAsHTML();
    }

    @GetMapping(value = "/csv", produces = MediaType.TEXT_PLAIN_VALUE)
    public String formatResultAsCSV() {

        return classicModelsService.formatResultAsCSV();
    }

    @GetMapping(value = "/chart1", produces = MediaType.TEXT_PLAIN_VALUE)
    public String formatResultAs1Chart() {

        return classicModelsService.formatResultAs1Chart();
    }

    @GetMapping(value = "/chart2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String formatResultAs2Chart() {

        return classicModelsService.formatResultAs2Chart();
    }

    @GetMapping(value = "/insert", produces = MediaType.TEXT_PLAIN_VALUE)
    public String formatResultAsInserts() {

        return classicModelsService.formatResultAsInserts();
    }
}
