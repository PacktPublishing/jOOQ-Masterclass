package com.classicmodels.pojo.java16records;

import java.util.List;

public record RecordEmployee (String employeeFirstName, String employeeLastName, Integer employeeSalary, List<RecordSale> sales) {}