package com.classicmodels.pojo.java16records;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RecordPayment(Long customerNumber, BigDecimal invoiceAmount, LocalDateTime cachingDate, List<RecordBank> transactions) {}