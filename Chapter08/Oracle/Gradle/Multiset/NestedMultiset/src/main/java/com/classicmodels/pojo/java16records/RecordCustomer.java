package com.classicmodels.pojo.java16records;

import java.math.BigDecimal;
import java.util.List;

public record RecordCustomer(String customerName, BigDecimal creditLimit, List<RecordPayment> payments, List<RecordCustomerDetail> details) {}