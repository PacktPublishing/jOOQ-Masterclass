package com.classicmodels.pojo.java16records;

import java.util.List;

public record RecordOffice(String officeCode, String officeCity, String officeCountry, List<RecordDepartment> departments, List<RecordEmployee> employees, List<RecordManager> managers) {}